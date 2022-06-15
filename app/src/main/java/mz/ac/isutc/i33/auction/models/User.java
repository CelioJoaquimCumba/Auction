package mz.ac.isutc.i33.auction.models;

import java.util.ArrayList;

public class User {
    private String username,email,password;
    private double balance;
    private ArrayList<String> followers;
    private ArrayList<String> followings;
    private int followers_count;
    private int following_count;
    public User(){

    }


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = 10000;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.followers.add("test");
        this.followings.add("test");
        this.following_count = followings.size();
        this.followers_count = followers.size();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }
    public boolean deductBalance(Double value){
        if( balance >= value ){
            balance = balance - value;
            return true;
        }
        return false;
    }
    public ArrayList<String> getFollowers() {
        return followers;
    }
    public ArrayList<String> getFollowings() {
        return followings;
    }
    public void addFollower(String user){
        followers.add(user);
    }
    public void addBalance(double value){
        balance = balance + 1;
    }
    public void addFollower(){
        followers_count++;
    }
    public void removeFollower(){
        followers_count--;
    }
    public void addFollowing(){
        following_count++;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public void setFollowings(ArrayList<String> followings) {
        this.followings = followings;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void removeFollowing(){
        following_count--;
    }

}
