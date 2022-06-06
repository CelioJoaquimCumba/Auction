package mz.ac.isutc.i33.auction.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mz.ac.isutc.i33.auction.Bid.Bid;
import mz.ac.isutc.i33.auction.Bid.Bid_post;
import mz.ac.isutc.i33.auction.BidListAdapter;
import mz.ac.isutc.i33.auction.R;

public class HomeFragment extends Fragment {
    ListView listView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.
                inflate(R.layout.home_page,
                        container,false);
        listView = rootView.findViewById(R.id.list_view);
        ArrayList<Bid_post> bid_posts = new ArrayList<>();
        Bid bid = new Bid("celio","12","1");
        ArrayList<Bid> bids = new ArrayList<>();
        bids.add(bid);
        bid_posts.add(new Bid_post("Celio",bids));
        bid_posts.add(new Bid_post("Claudio",bids));



        BidListAdapter adapter = new BidListAdapter(
                getContext(),
                R.layout.bid_adapter,bid_posts);
        listView.setAdapter(adapter);
        return rootView;
    }
}
