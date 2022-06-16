package mz.ac.isutc.i33.auction.fragments;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mz.ac.isutc.i33.auction.models.Bid.Bid;
import mz.ac.isutc.i33.auction.models.Bid.Bid_post;
import mz.ac.isutc.i33.auction.BidListAdapter;
import mz.ac.isutc.i33.auction.R;
import mz.ac.isutc.i33.auction.models.User;

public class HomeFragment extends Fragment {
    ListView listView;
    User winner;
    User owner;
    FirebaseDatabase database;
    DatabaseReference reference, reference_users;
    String username;
    private int limit=5,start=0,end=limit;
    ArrayList<Bid_post> bid_posts;
    BidListAdapter adapter;
    ProgressBar progressBar;
    boolean load = true;
    TextView empty_text;
    User user;

    public HomeFragment(String username) {
        this.username = username;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.
                inflate(R.layout.home_page,
                        container,false);

        database = FirebaseDatabase.getInstance("https://auction-a4883-default-rtdb.firebaseio.com/");
        reference = database.getReference("bidPosts");
        reference_users = database.getReference("users");

        listView = rootView.findViewById(R.id.list_view_home);
        progressBar = rootView.findViewById(R.id.progressBar_home);

        empty_text = rootView.findViewById(R.id.text_empty_home);
        bid_posts = new ArrayList<>();


//        ArrayList<Bid> bids = new ArrayList<>();
//        bids.add(new Bid("Celio",12.0,"1"));
//        bids.add(new Bid(username,22.0,"1"));
//        bids.add(new Bid("Tomas",10.0,"1"));
//        bids.add(new Bid("Cumba",21.0,"1"));
//        bid_posts.add(new Bid_post("claudio","Absolutely nothing","Claudio","40","12/12/2022","12:30",bids,""));


        returnUser();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int counts[] = {firstVisibleItem, visibleItemCount, totalItemCount};

                if( load &&  firstVisibleItem + visibleItemCount == totalItemCount ){
                    Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                    start = end;
                    end = start + limit;
                    returnData(end);
                    load = false;
                    //TODO
                }
                if ( !load && firstVisibleItem + visibleItemCount != totalItemCount ){
                    load = true;
                    Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
                }
            }
        });







        return rootView;
    }

    private void returnData(int end){
        progressBar.setVisibility(View.VISIBLE);
        Query databaseQuery = reference
                .limitToFirst(end);

        databaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bid_posts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bid_post bid_post = snapshot.getValue(Bid_post.class);
                    bid_posts.add(bid_post);
                    adapter.notifyDataSetChanged();
                    if (!dateIsValid(bid_post.getEndTime(), bid_post.getEndDate())) {

                        deleteBid(snapshot);

                    }
                }
                progressBar.setVisibility(View.GONE);
                if( bid_posts.isEmpty() ){
                    empty_text.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
                progressBar.setVisibility(View.GONE);

            }
        });
    }
    private boolean dateIsValid(String time, String date) {
        Date date_now = new Date();
        Date date_comparing;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            date_comparing = formatter.parse(date + " " + time);
        } catch (Exception e) {
            return false;
        }
        if (date_comparing.compareTo(date_now) <= 0) {
            return false;
        }
        return true;
    }
    private void returnUser(){
//        reference_users.equalTo(username,"username");
        reference_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user_database = (User) snapshot.getValue(User.class);
                    if(user_database.getUsername().trim().equals(username.trim())){
                        user = user_database;
                        returnData(limit);
                        adapter = new BidListAdapter(
                                getContext(),
                                R.layout.bid_adapter,bid_posts,
                                reference,reference_users, user);
                        listView.setAdapter(adapter);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void deleteBid(DataSnapshot snapshot) {
        Bid_post bid_post = snapshot.getValue(Bid_post.class);
        snapshot.getRef().setValue(null);
        //NOTIFICAO E ENVIAR EMAIL
        String notification = bid_post.getTitle() + "has expired" ;
        owner.addNotifications(notification);
        returnWinner(bid_post.getHighest_bidder());
        owner.addBalance(Double.parseDouble(bid_post.getHighest_bid()));
        winner.deductBalance(Double.parseDouble(bid_post.getHighest_bid()));
        reference_users.child(owner.getUsername()).setValue(owner);
        reference_users.child(winner.getUsername()).setValue(winner);
    }

    private void returnOwner(String value){
        reference_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user_database = (User) snapshot.getValue(User.class);
                    if(user_database.getUsername().trim().equals(value.trim())){
                        owner = snapshot.getValue(User.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void returnWinner(String value){

        reference_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user_database = (User) snapshot.getValue(User.class);
                    if(user_database.getUsername().trim().equals(value.trim())){
                        winner = snapshot.getValue(User.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
