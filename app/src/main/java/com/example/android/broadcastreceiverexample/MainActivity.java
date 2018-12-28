package com.example.android.broadcastreceiverexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.broadcastreceiverexample.MyFilters.CUSTOM_INTENT_FILTER;

public class MainActivity extends AppCompatActivity implements MyBroadcastReceiver.BroadcastListener, RandomAdapter.RandomListener {
    private static final String KEY_RANDOM_ITEMS_LIST = "random_items";
    private static final String KEY_TEXTVIEW_TEXT = "textview_text";
    @BindView(R.id.textView)
    public TextView textView;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter airplaneFilter;
    private IntentFilter customFilter;
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

        customFilter = new IntentFilter(CUSTOM_INTENT_FILTER);
        customFilter.addAction(MyFilters.ACTION_ADDED_ITEM);
        customFilter.addAction(MyFilters.ACTION_REMOVED_ITEM);

        localBroadcastManager.registerReceiver(broadcastReceiver, customFilter);
        this.registerReceiver(broadcastReceiver, airplaneFilter);
        if (savedInstanceState != null) {
            data = Parcels.unwrap(savedInstanceState.getParcelable(KEY_RANDOM_ITEMS_LIST));
            textView.setText(savedInstanceState.getString(KEY_TEXTVIEW_TEXT));
        } else {
            createDummyData();
        }
        adapter = new RandomAdapter(data);
        adapter.attachListener(this);
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

    @Override
    public void removedItem() {
        textView.setText(R.string.removed_item);
    }

    @OnClick(R.id.unregister_button)
    void unregisterMyReceiver() {
        try {
            localBroadcastManager.unregisterReceiver(broadcastReceiver);
            this.unregisterReceiver(broadcastReceiver);
            textView.setText(R.string.unregistered);
        } catch (IllegalArgumentException e) {
            textView.setText(R.string.error_unregister);
        }
    }

    @OnClick(R.id.register_button)
    void registerMyReceiver() {
        localBroadcastManager.registerReceiver(broadcastReceiver, customFilter);
        this.registerReceiver(broadcastReceiver, airplaneFilter);
        textView.setText(R.string.registered);
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
        outState.putString(KEY_TEXTVIEW_TEXT, textView.getText().toString());
    }

    @Override
    public void itemDeleted() {
        vibrate();
        localBroadcastManager.sendBroadcast(new Intent(MyFilters.ACTION_REMOVED_ITEM));
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Objects.requireNonNull(vibrator).vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            Objects.requireNonNull(vibrator).vibrate(200);
        }
    }
}
