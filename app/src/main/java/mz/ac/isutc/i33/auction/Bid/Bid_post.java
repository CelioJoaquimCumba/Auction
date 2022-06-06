package mz.ac.isutc.i33.auction.Bid;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Bid_post {
    String owner;
    ArrayList<Bid> bids;

    public Bid_post(String owner, ArrayList<Bid> bids) {
        this.owner = owner;
        this.bids = bids;
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
