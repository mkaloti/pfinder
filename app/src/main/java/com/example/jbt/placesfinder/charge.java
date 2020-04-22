package com.example.jbt.placesfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


//A BROADCAST RECEIVER TO TOAST A CHARGING BEGINS WHEN A CHARGER PLUGGED IN

public class charge extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "charging begins...", Toast.LENGTH_SHORT).show();
    }
}
