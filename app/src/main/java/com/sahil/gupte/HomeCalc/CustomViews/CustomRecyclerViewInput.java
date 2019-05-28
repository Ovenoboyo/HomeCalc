package com.sahil.gupte.HomeCalc.CustomViews;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomRecyclerViewInput extends RecyclerView.Adapter<CustomRecyclerViewInput.RecyclerViewHolder> {
    private int count = 1;
    Activity context1;
    EditText time;

    public CustomRecyclerViewInput(Activity context) {
        context1 = context;

    }

    @Override
    public CustomRecyclerViewInput.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);
        time = view.findViewById(R.id.editText3);
        String date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        date = formatter.format(new Date().getTime());
        time.setText(date);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };

        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(context1, dateClick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return new RecyclerViewHolder(view);
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        time.setText(sdf.format(myCalendar.getTime()));
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
