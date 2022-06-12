package mz.ac.isutc.i33.auction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import mz.ac.isutc.i33.auction.models.Bid.Bid;
import mz.ac.isutc.i33.auction.models.Bid.Bid_post;

public class BidListAdapter extends ArrayAdapter<Bid_post> {
    private static final String TAG = "PersonListAdapter";

    private Context context;
    int resource;
    DatabaseReference reference;

    public BidListAdapter(Context context, int resource, ArrayList<Bid_post> objects, DatabaseReference reference) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.reference = reference;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bid_post bid_post = getItem(position);
        String username = bid_post.getOwner();
        String title = bid_post.getTitle();
        String description = bid_post.getDescription();
        String highest_bidder = bid_post.getHighest_bidder();
        String highest_bid = bid_post.getHighest_bid();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvUsername = (TextView) convertView.findViewById(R.id.username_bid);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.title_bid);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.description_bid) ;
        TextView tvHighestBidder = (TextView) convertView.findViewById(R.id.highest_bidder);
        TextView tvHighestBid = (TextView) convertView.findViewById(R.id.highest_bid);
        Button button = (Button) convertView.findViewById(R.id.bid_button_bid);
        EditText bid_proposal = convertView.findViewById(R.id.bid_proposal_bid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bid_proposal_text = bid_proposal.getText().toString();
                if (!bid_proposal_text.trim().equals("") && Double.parseDouble(bid_proposal_text)>Double.parseDouble(highest_bid) ){
                    bid_post.addBid(new Bid(MainActivity.username,Double.parseDouble(bid_proposal_text),bid_post.getId()));
                    reference.child(bid_post.getId()).setValue(bid_post);
                }else{
                    Toast.makeText(getContext(), "Proposta de leilao concluida sem sucesso! insira um valor maior que o apostado.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvUsername.setText(username);
        tvTitle.setText(title);
        tvDescription.setText(description);
        tvHighestBid.setText(highest_bid);
        tvHighestBidder.setText(highest_bidder);




        return convertView;

    }

}
