package com.example.android.broadcastreceiverexample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomAdapter extends RecyclerView.Adapter<RandomAdapter.RandomViewHolder> {
    private List<RandomItem> data = new ArrayList<>();

    public RandomAdapter(List<RandomItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RandomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new RandomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.random_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RandomViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.number_tv) TextView numberTV;
        RandomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(RandomItem data){
            numberTV.setText(String.valueOf(data.getNumber()));
            numberTV.setTextColor(data.getColor());
        }
    }
}
