package com.example.android.broadcastreceiverexample;

import org.parceler.Parcel;

@Parcel
class RandomItem {
    int number;
    int color;


    public RandomItem() {
    }

    public RandomItem(int number, int color) {
        this.number = number;
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public int getColor() {
        return color;
    }


}
