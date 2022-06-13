package mz.ac.isutc.i33.auction.controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import mz.ac.isutc.i33.auction.MainActivity;
import mz.ac.isutc.i33.auction.R;

public class InternetController {
    private static InternetController INSTANCE;

    public static InternetController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InternetController();
        }
        return INSTANCE;
    }
    public boolean checkConnection(Object system_service){
        ConnectivityManager connectivityManager = (ConnectivityManager) system_service;
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    public void alertDisconnection(View view, Object system_service){
        if (!checkConnection(system_service)) Snackbar.make(view, "Offline", Snackbar.LENGTH_LONG).show();
    }
}
