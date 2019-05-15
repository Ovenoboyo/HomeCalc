package com.sahil.gupte.HomeCalc.CustomViews;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sahil.gupte.HomeCalc.R;

public class CustomRecyclerViewInput extends RecyclerView.Adapter<CustomRecyclerViewInput.RecyclerViewHolder> {
    private final Activity context;
    private int count = 1;

    public CustomRecyclerViewInput(Activity context) {
        this.context = context;

    }

    @Override
    public CustomRecyclerViewInput.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
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

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
