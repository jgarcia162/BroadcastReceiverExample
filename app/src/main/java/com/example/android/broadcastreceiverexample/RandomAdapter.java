package com.example.android.broadcastreceiverexample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RandomAdapter extends RecyclerView.Adapter<RandomAdapter.RandomViewHolder> {
    private List<RandomItem> data;

    RandomAdapter(List<RandomItem> data) {
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

    class RandomViewHolder extends RecyclerView.ViewHolder {
        TextView numberTV;

        RandomViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTV = itemView.findViewById(R.id.number_tv);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeItem();
                    return false;
                }
            });
        }

        void bind(RandomItem data) {
            numberTV.setText(String.valueOf(data.getNumber()));
            numberTV.setTextColor(data.getColor());
        }

        void removeItem() {
            data.remove(getAdapterPosition());
            notifyDataSetChanged();
            LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(new Intent(MyFilters.ACTION_REMOVED_ITEM));
        }
    }
}
