package mz.ac.isutc.i33.auction.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import mz.ac.isutc.i33.auction.models.Bid.Bid;
import mz.ac.isutc.i33.auction.models.Bid.Bid_post;
import mz.ac.isutc.i33.auction.BidListAdapter;
import mz.ac.isutc.i33.auction.R;

public class HomeFragment extends Fragment {
    ListView listView;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.
                inflate(R.layout.home_page,
                        container,false);
        listView = rootView.findViewById(R.id.list_view);
        ArrayList<Bid_post> bid_posts = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        bids.add(new Bid("Celio",12.0,"1"));
        bids.add(new Bid("Claudio",22.0,"1"));bids.add(new Bid("Celio",12.0,"1"));
        bids.add(new Bid("Tomas",10.0,"1"));
        bids.add(new Bid("Cumba",24.0,"1"));

        bid_posts.add(new Bid_post("celio","Badass background","Celio","30","12/12/2022","12:30",bids,""));
        bid_posts.add(new Bid_post("claudio","Absolutely nothing","Claudio","40","12/12/2022","12:30",bids,""));



        BidListAdapter adapter = new BidListAdapter(
                getContext(),
                R.layout.bid_adapter,bid_posts);
        listView.setAdapter(adapter);
        return rootView;
    }
}
