package mz.ac.isutc.i33.auction.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import mz.ac.isutc.i33.auction.models.User;

public class SearchFragment extends Fragment implements  SearchView.OnQueryTextListener{
    ListView listView;
    Spinner spinner;
    SearchView searchView;
    User user;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference reference_bid_posts, reference_users;
    ArrayList<Bid_post> bid_posts;
    BidListAdapter adapter;
    TextView empty_text;
    private String username;

    public SearchFragment(String username){
        this.username = username;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.
                inflate(R.layout.search_page,
                        container,false);

        database = FirebaseDatabase.getInstance("https://auction-a4883-default-rtdb.firebaseio.com/");
        reference_bid_posts = database.getReference("bidPosts");
        reference_users = database.getReference("users");
        progressBar = rootView.findViewById(R.id.progressBar_profile);

        listView = rootView.findViewById(R.id.list_view_search);
        spinner = rootView.findViewById(R.id.spinner_search);
        progressBar = rootView.findViewById(R.id.progressBar_search);

        empty_text = rootView.findViewById(R.id.text_empty_search);
        bid_posts = new ArrayList<>();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        searchView = rootView.findViewById(R.id.searchView_search);
        searchView.setOnQueryTextListener(this);

        returnUser();

        return rootView;
    }

    private void returnData(){
        progressBar.setVisibility(View.VISIBLE);
        Query databaseQuery = reference_bid_posts;

        databaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bid_posts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Bid_post bid_post = snapshot.getValue(Bid_post.class);
                    String search_Value = searchView.getQuery().toString();
                    switch (spinner.getSelectedItem().toString()){
                        case "owner":
                            if ( bid_post.getOwner().trim().contains(search_Value) ){
                                bid_posts.add( bid_post );
                                break;
                            }
                        case "title":
                            if ( bid_post.getTitle().trim().contains(search_Value) ){
                                bid_posts.add( bid_post );
                                break;
                            }
                        case "description":
                            if ( bid_post.getDescription().trim().contains(search_Value) ){
                                bid_posts.add( bid_post );
                                break;
                            }
                        default:
                            break;
                    }

                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if( bid_posts.isEmpty() ){
                    empty_text.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void returnUser(){
//        reference_users.equalTo(username,"username");
        reference_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user_database = (User) snapshot.getValue(User.class);
                    String bc = "";
                    User a = user_database;
                    String b = username.trim();
                    if(user_database.getUsername().trim().equals(username.trim())){
                        user = user_database;
                        adapter = new BidListAdapter(
                                getContext(),
                                R.layout.bid_adapter,bid_posts,
                                reference_bid_posts,reference_users, user);
                        listView.setAdapter(adapter);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        bid_posts.clear();
        returnData();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
}
