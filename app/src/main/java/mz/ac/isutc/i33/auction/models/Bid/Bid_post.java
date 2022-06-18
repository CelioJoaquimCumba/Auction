package mz.ac.isutc.i33.auction.models.Bid;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Bid_post {
    String id;
    String owner;
    String title;
    String description;
    String startingBid;
    String endDate;
    Date createdDate;

    String endTime;
    ArrayList<Bid> bids;
    String highest_bidder;
    String highest_bid;
    String imageUri;
    boolean show;




    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sort (){
        bids.sort(Comparator.comparing(Bid::getBid));


    }


    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Bid_post(String id, String owner, String title, String description
            , String startingBid,String endDate, String endTime
            , ArrayList<Bid> bids,String imageUri
                    ) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.createdDate = new Date();
        this.description = description;
        this.startingBid = startingBid;
        this.endDate = endDate;
        this.endTime = endTime;
        this.bids = bids;
        this.imageUri = imageUri;
        this.show = true;
        sort();
        this.highest_bidder = bids.get( bids.size()-1 ).username;
        this.highest_bid = bids.get( bids.size() -1 ).bid.toString();



    }
    public Bid_post(){
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public String getImageUri() {
        return imageUri;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getStartingBid() {
        return startingBid;
    }
    public void setStartingBid(String startingBid) {
        this.startingBid = startingBid;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setHighest_bidder(String highest_bidder) {
        this.highest_bidder = highest_bidder;
    }
    public void setHighest_bid(String highest_bid) {
        this.highest_bid = highest_bid;
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
