package com.example.jbt.placesfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//A BROADCAST RECEIVER TO TOAST A CHARGING ENDS WHEN A CHARGER UNPLUGGED IN

public class uncharge extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "charging finished...", Toast.LENGTH_SHORT).show();

    }
}
