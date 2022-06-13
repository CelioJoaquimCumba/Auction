package mz.ac.isutc.i33.auction.fragments;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mz.ac.isutc.i33.auction.models.Bid.Bid;
import mz.ac.isutc.i33.auction.models.Bid.Bid_post;
import mz.ac.isutc.i33.auction.BidListAdapter;
import mz.ac.isutc.i33.auction.R;

public class HomeFragment extends Fragment {
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference reference;
    String username;

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
        listView = rootView.findViewById(R.id.list_view);

        ArrayList<Bid_post> bid_posts = new ArrayList<>();
//        ArrayList<Bid> bids = new ArrayList<>();
//        bids.add(new Bid("Celio",12.0,"1"));
//        bids.add(new Bid(username,22.0,"1"));
//        bids.add(new Bid("Tomas",10.0,"1"));
//        bids.add(new Bid("Cumba",21.0,"1"));
//        bid_posts.add(new Bid_post("claudio","Absolutely nothing","Claudio","40","12/12/2022","12:30",bids,""));
        database = FirebaseDatabase.getInstance("https://auction-a4883-default-rtdb.firebaseio.com/");
        reference = database.getReference("bidPosts");
        BidListAdapter adapter = new BidListAdapter(
                getContext(),
                R.layout.bid_adapter,bid_posts,
                reference);
        listView.setAdapter(adapter);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bid_posts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Bid_post bid_post = snapshot.getValue(Bid_post.class);
                    bid_posts.add( bid_post );
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        return rootView;
    }
}
