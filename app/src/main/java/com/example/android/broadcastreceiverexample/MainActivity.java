package com.example.android.broadcastreceiverexample;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.broadcastreceiverexample.MyFilters.CUSTOM_INTENT_FILTER;

public class MainActivity extends AppCompatActivity implements MyBroadcastReceiver.BroadcastListener {
    @BindView(R.id.textView) public TextView textView;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter airplaneFilter;
    private IntentFilter addFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        broadcastReceiver = new MyBroadcastReceiver(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        airplaneFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        airplaneFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        addFilter = new IntentFilter(CUSTOM_INTENT_FILTER);
        addFilter.addAction(MyFilters.ACTION_ADDED_ITEM);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, addFilter);
        this.registerReceiver(broadcastReceiver, airplaneFilter);
    }

    @Override
    public void airplaneModeChanged() {
        textView.setText(R.string.airplane_changed);
    }

    @Override
    public void addedItem() {
        textView.setText(R.string.add_item);
    }

    @OnClick({R.id.add_button, R.id.register_button, R.id.unregister_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                localBroadcastManager.sendBroadcast(new Intent(MyFilters.ACTION_ADDED_ITEM));
                break;
            case R.id.register_button:
                localBroadcastManager.registerReceiver(broadcastReceiver, addFilter);
                this.registerReceiver(broadcastReceiver, airplaneFilter);
                textView.setText(R.string.registered);
                break;
            case R.id.unregister_button:
                try {
                    localBroadcastManager.unregisterReceiver(broadcastReceiver);
                    this.unregisterReceiver(broadcastReceiver);
                    textView.setText(R.string.unregistered);
                } catch (IllegalArgumentException e) {
                    textView.setText(R.string.error_unregister);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
