package com.sahil.gupte.HomeCalc.CustomViews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.sahil.gupte.HomeCalc.R;

public class CustomListViewInput extends ArrayAdapter<String> {
    private final Activity context;
    private int count = 1;
    private Spinner spinner;
    private EditText Price;
    private EditText notes;

    public CustomListViewInput(Activity context) {
        super(context, R.layout.custom_list);
        this.context=context;

    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        if (view == null) {
            view = inflater.inflate(R.layout.custom_list, null, true);
            spinner = view.findViewById(R.id.spinner);
            Price = view.findViewById(R.id.editText);
            notes = view.findViewById(R.id.editText2);
        }

        return view;
    }


    public void addItem(){
        count++;
        notifyDataSetChanged();
    }
}
