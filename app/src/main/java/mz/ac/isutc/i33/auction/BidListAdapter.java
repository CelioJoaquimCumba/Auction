package mz.ac.isutc.i33.auction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import mz.ac.isutc.i33.auction.Bid.Bid_post;

public class BidListAdapter extends ArrayAdapter<Bid_post> {
    private static final String TAG = "PersonListAdapter";

    private Context context;
    int resource;

    public BidListAdapter(Context context, int resource, ArrayList<Bid_post> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String username = getItem(position).getOwner();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvUsername = (TextView) convertView.findViewById(R.id.username_bid);


        tvUsername.setText(username);


        return convertView;

    }
}
