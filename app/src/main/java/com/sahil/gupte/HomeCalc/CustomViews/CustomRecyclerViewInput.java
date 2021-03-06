package com.sahil.gupte.HomeCalc.CustomViews;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;

import com.sahil.gupte.HomeCalc.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CustomRecyclerViewInput extends RecyclerView.Adapter<CustomRecyclerViewInput.RecyclerViewHolder> {
    private int count = 1;
    private final Activity context1;
    public final HashMap<Integer, RecyclerView.ViewHolder> holderHashMap = new HashMap<>();
    private final List<String> spinnerNameList;

    public CustomRecyclerViewInput(Activity context, ArrayList<String> spinnerNameList) {
        context1 = context;
        Log.d("test", "CustomRecyclerViewInput: "+spinnerNameList);
        this.spinnerNameList = spinnerNameList;

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerViewHolder holder) {
        holderHashMap.put(holder.getAdapterPosition(),holder);
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        holderHashMap.remove(holder.getAdapterPosition());
        super.onViewAttachedToWindow(holder);

    }
    @Override
    public CustomRecyclerViewInput.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerViewInput.RecyclerViewHolder holder, int position) {
        if (spinnerNameList != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context1, android.R.layout.simple_spinner_item, spinnerNameList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);
        }

        String date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        date = formatter.format(new Date().getTime());
        holder.time.setText(date);

        SharedPreferences pref = context1.getSharedPreferences("Theme", 0);
        final boolean dark = pref.getBoolean("dark", true);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateClick = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            holder.time.setText(sdf.format(myCalendar.getTime()));
        };

        holder.time.setOnClickListener(v -> {
            if(dark) {
                new DatePickerDialog(context1, R.style.Dialog_Dark, dateClick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            } else {
                new DatePickerDialog(context1, dateClick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



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

        final EditText time;
        final Spinner spinner;

        RecyclerViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.editText3);
            spinner = view.findViewById(R.id.spinner);

        }
    }
}
