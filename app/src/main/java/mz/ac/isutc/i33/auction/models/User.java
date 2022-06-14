package mz.ac.isutc.i33.auction.models;

import java.util.ArrayList;

public class User {
    private String username,email,password;
    private double balance;
    private ArrayList<String> followers;




    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        balance = 10000;
        followers = new ArrayList<>();
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
    public void addFollower(String user){
        followers.add(user);
    }
    public void addBalance(double value){
        balance = balance + 1;
    }
}
