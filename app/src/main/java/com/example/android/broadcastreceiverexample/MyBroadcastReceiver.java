package com.example.android.broadcastreceiverexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MY_BROADCAST_RECEIVER";
    private BroadcastListener listener;

    public MyBroadcastReceiver(BroadcastListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (Objects.requireNonNull(intent.getAction())) {
            case Intent.ACTION_AIRPLANE_MODE_CHANGED:
                Log.d(TAG, "onReceive: AIRPLANE MODE CHANGED");
                listener.airplaneModeChanged();
                break;
            case MyFilters.ACTION_ADDED_ITEM:
                Log.d(TAG, "onReceive: ADDED ITEM");
                listener.addedItem();
                break;
            case MyFilters.ACTION_REMOVED_ITEM:
                Log.d(TAG, "onReceive: REMOVED ITEM");
                listener.removedItem();
                break;
        }
    }

    interface BroadcastListener{
        void airplaneModeChanged();
        void addedItem();
        void removedItem();
    }
}
