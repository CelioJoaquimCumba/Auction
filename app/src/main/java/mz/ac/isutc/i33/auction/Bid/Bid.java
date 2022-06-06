package mz.ac.isutc.i33.auction.Bid;

public class Bid {
    String username;
    Double bid;
    String product_id;

    public Bid(String username, Double bid, String product_id) {
        this.username = username;
        this.bid = bid;
        this.product_id = product_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}