package com.example.android.broadcastreceiverexample;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.broadcastreceiverexample.MyFilters.CUSTOM_INTENT_FILTER;

public class MainActivity extends AppCompatActivity implements MyBroadcastReceiver.BroadcastListener {
    private static final String KEY_RANDOM_ITEMS_LIST = "random_items";
    @BindView(R.id.textView)
    public TextView textView;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter airplaneFilter;
    private IntentFilter addFilter;
    private RandomAdapter adapter;
    private List<RandomItem> data = new ArrayList<>();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        broadcastReceiver = new MyBroadcastReceiver(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        airplaneFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        airplaneFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        addFilter = new IntentFilter(CUSTOM_INTENT_FILTER);
        addFilter.addAction(MyFilters.ACTION_ADDED_ITEM);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, addFilter);
        this.registerReceiver(broadcastReceiver, airplaneFilter);
        if (savedInstanceState != null) {
            data = Parcels.unwrap(savedInstanceState.getParcelable(KEY_RANDOM_ITEMS_LIST));
        } else {
            createDummyData();
        }
        adapter = new RandomAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    private void createDummyData() {
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
    }

    private int getRandomNumber() {
        return random.nextInt(999) + 1;
    }

    private int getRandomColor() {
        return Color.rgb(random.nextInt(254) + 1, random.nextInt(254) + 1, random.nextInt(254) + 1);
    }

    @Override
    public void airplaneModeChanged() {
        textView.setText(R.string.airplane_changed);
    }

    @Override
    public void addedItem() {
        textView.setText(R.string.add_item);
    }

    @OnClick({R.id.register_button, R.id.unregister_button})
    public void onClick(View v) {
        switch (v.getId()) {
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

    @OnClick(R.id.add_button)
    void addNewRandomItem() {
        data.add(new RandomItem(getRandomNumber(), getRandomColor()));
        adapter.notifyDataSetChanged();
        localBroadcastManager.sendBroadcast(new Intent(MyFilters.ACTION_ADDED_ITEM));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RANDOM_ITEMS_LIST, Parcels.wrap(data));
    }
}
