package com.sahil.gupte.HomeCalc.CustomViews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.sahil.gupte.HomeCalc.R;

public class CustomRecyclerViewInput extends RecyclerView.Adapter<CustomRecyclerViewInput.RecyclerViewHolder> {
    private int count = 1;

    public CustomRecyclerViewInput(Activity context) {
        Activity context1 = context;

    }

    @Override
    public CustomRecyclerViewInput.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewInput.RecyclerViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addItem() {
        count++;
    }

    public void removeItem() {
        if (count > 1) {
            count--;
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
