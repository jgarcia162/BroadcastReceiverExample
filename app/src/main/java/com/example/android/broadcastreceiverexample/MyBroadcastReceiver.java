package com.example.android.broadcastreceiverexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private BroadcastListener listener;

    public MyBroadcastReceiver(BroadcastListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)){
            listener.airplaneModeChanged();
        }else if(Objects.requireNonNull(intent.getAction()).equals(MyFilters.ACTION_ADDED_ITEM)){
            listener.addedItem();
        }
    }



    interface BroadcastListener{
        void airplaneModeChanged();
        void addedItem();
    }
}
