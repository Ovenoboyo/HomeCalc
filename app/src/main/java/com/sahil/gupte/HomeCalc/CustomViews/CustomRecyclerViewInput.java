package com.sahil.gupte.HomeCalc.CustomViews;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.sahil.gupte.HomeCalc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CustomRecyclerViewInput extends RecyclerView.Adapter<CustomRecyclerViewInput.RecyclerViewHolder> {
    private int count = 1;
    private Activity context1;
    public HashMap<Integer, RecyclerView.ViewHolder> holderHashMap = new HashMap<>();

    public CustomRecyclerViewInput(Activity context) {
        context1 = context;

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
        String date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        date = formatter.format(new Date().getTime());
        holder.time.setText(date);

        SharedPreferences pref = context1.getSharedPreferences("Theme", 0);
        final boolean dark = pref.getBoolean("dark", true);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                holder.time.setText(sdf.format(myCalendar.getTime()));
            }

        };

        holder.time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(dark) {
                    new DatePickerDialog(context1, R.style.Dialog_Dark, dateClick, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                } else {
                    new DatePickerDialog(context1, dateClick, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
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

        EditText time;

        RecyclerViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.editText3);

        }
    }
}
