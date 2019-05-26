package com.sahil.gupte.HomeCalc.CustomViews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.sahil.gupte.HomeCalc.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomRecyclerViewInput extends RecyclerView.Adapter<CustomRecyclerViewInput.RecyclerViewHolder> {
    private int count = 1;

    public CustomRecyclerViewInput(Activity context) {
        Activity context1 = context;

    }

    @Override
    public CustomRecyclerViewInput.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);
        EditText time = view.findViewById(R.id.editText3);
        String date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        date = formatter.format(new Date().getTime());
        time.setText(date);
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
