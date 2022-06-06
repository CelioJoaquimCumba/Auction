package mz.ac.isutc.i33.auction.Bid;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class Bid_post {
    String title;
    String owner;
    ArrayList<Bid> bids;
    String highest_bidder;
    String highest_bid;
    String description;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sort (){
        bids.sort(Comparator.comparing(Bid::getBid));


    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    public Bid_post(String title,String owner, ArrayList<Bid> bids, String description) {
        this.title = title;
        this.owner = owner;
        this.bids = bids;
        sort();
        this.highest_bidder = bids.get( bids.size()-1 ).username;
        this.highest_bid = bids.get( bids.size() -1 ).bid.toString();
        this.description = description;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getHighest_bidder(){
        return bids.get( bids.size() -1 ).username;
    }
    public String getHighest_bid(){
        return bids.get( bids.size() -1 ).getBid().toString();
    }

    public void addBid( Bid bid ){
        if( bids.get(0).getBid()<bid.getBid() ){
            this.bids.add(bid);
        }

    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }
}
