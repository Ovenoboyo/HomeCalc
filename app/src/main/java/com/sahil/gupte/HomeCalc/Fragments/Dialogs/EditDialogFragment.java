package com.sahil.gupte.HomeCalc.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;
import com.sahil.gupte.HomeCalc.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils.RemoveItemDB;
import static com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils.UpdateDB;


public class EditDialogFragment extends DialogFragment {
    private Context mContext;
    private int pos;
    private EditText date, price, notes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setCancelable(true);
        pos = Objects.requireNonNull(getArguments()).getInt("pos");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.edit_dialog, null);
        SharedPreferences pref = mContext.getSharedPreferences("Theme", 0);
        boolean dark = pref.getBoolean("dark", true);
        AlertDialog.Builder builder;
        if (dark) {
            builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Dialog_Dark));
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setView(view);
        builder.setTitle(getString(R.string.edit));

        date = view.findViewById(R.id.edit_date);
        price = view.findViewById(R.id.edit_price);
        notes = view.findViewById(R.id.edit_notes);

        date.setText(ShowDetailUtils.DateList.get(pos));
        price.setText(ShowDetailUtils.PriceList.get(pos));
        notes.setText(ShowDetailUtils.NotesList.get(pos));

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

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext, dateClick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        AlertDialog dialog = builder.create();
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = Objects.requireNonNull(getContext()).getTheme();
        theme.resolveAttribute(R.attr.Primary, typedValue, true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(typedValue.data));
        dialog.show();

        TextView save = view.findViewById(R.id.save);
        TextView delete = view.findViewById(R.id.delete);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(getString(R.string.confirm));
                builder1.setTitle(getString(R.string.areyousure));

                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveText();
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = builder1.create();
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = Objects.requireNonNull(getContext()).getTheme();
                theme.resolveAttribute(R.attr.Primary, typedValue, true);
                Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(typedValue.data));
                alertDialog.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveItemDB(pos, Objects.requireNonNull(getContext()));
                ((MainActivity) Objects.requireNonNull(getActivity())).displaySelectedScreen(R.id.nav_edit);
            }
        });
        return dialog;
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean matchString (String str) {
        Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        Matcher m = p.matcher(str);
        if (m.find()) {
            Log.d("test", "matchString: " + m.group(0));
            if(m.group() == str) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private void saveText() {

        if (matchString(date.getText().toString())) {

            Date date1 = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            try {
                date1 = formatter.parse(date.getText().toString());
            } catch (ParseException e) {
                Toast.makeText(mContext, "Invalid Date format. Try DD/MM/YYYY", Toast.LENGTH_LONG).show();
            }

            if (date != null) {
                Calendar today = Calendar.getInstance();
                today.add(Calendar.MONTH, -2);
                today.set(Calendar.DAY_OF_MONTH, 0);
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date1.getTime());
                cal.set(Calendar.DAY_OF_MONTH, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                if (cal.getTimeInMillis() <= today.getTimeInMillis()) {
                    Toast.makeText(mContext, "New time cannot be less than or equal to 2 months in the past", Toast.LENGTH_LONG).show();
                } else {
                    ShowDetailUtils.setTime(pos, date.getText().toString(), getContext());
                    ShowDetailUtils.setPrice(pos, price.getText().toString());
                    ShowDetailUtils.setNotes(pos, notes.getText().toString());
                    UpdateDB(pos, Objects.requireNonNull(getContext()));
                }
            }
        } else {
            Toast.makeText(mContext, "Invalid Date format. Try DD/MM/YYYY", Toast.LENGTH_LONG).show();
        }
    }
}
