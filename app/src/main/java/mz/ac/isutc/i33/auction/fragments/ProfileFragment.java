package mz.ac.isutc.i33.auction.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mz.ac.isutc.i33.auction.BidListAdapter;
import mz.ac.isutc.i33.auction.R;
import mz.ac.isutc.i33.auction.models.Bid.Bid_post;


public class ProfileFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Bid_post> bid_posts;
    BidListAdapter adapter;
    ProgressBar progressBar;
    ListView listView;
    String username;
    TextView username_TV;
    public ProfileFragment(String username) {
        this.username = username;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.
                inflate(R.layout.fragment_profile,
                        container,false);

        database = FirebaseDatabase.getInstance("https://auction-a4883-default-rtdb.firebaseio.com/");
        reference = database.getReference("bidPosts");

        listView = rootView.findViewById(R.id.list_view_profile);
        progressBar = rootView.findViewById(R.id.progressBar_profile);
        username_TV = rootView.findViewById(R.id.username_profile);
        username_TV.setText(username);


        bid_posts = new ArrayList<>();
//        ArrayList<Bid> bids = new ArrayList<>();
//        bids.add(new Bid("Celio",12.0,"1"));
//        bids.add(new Bid(username,22.0,"1"));
//        bids.add(new Bid("Tomas",10.0,"1"));
//        bids.add(new Bid("Cumba",21.0,"1"));
//        bid_posts.add(new Bid_post("claudio","Absolutely nothing","Claudio","40","12/12/2022","12:30",bids,""));

        adapter = new BidListAdapter(
                getContext(),
                R.layout.bid_adapter,bid_posts,
                reference);
        listView.setAdapter(adapter);
        returnData();
        return rootView;
    }
    private void returnData(){
        progressBar.setVisibility(View.VISIBLE);
        Query databaseQuery = reference;

        databaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bid_posts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Bid_post bid_post = snapshot.getValue(Bid_post.class);
                    if ( bid_post.getOwner().trim().equals(username.trim()) ){
                        bid_posts.add( bid_post );
                    }

                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}