package com.sahil.gupte.HomeCalc.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;
import com.sahil.gupte.HomeCalc.Utils.CurrencyUtils;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;

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
    private Spinner category;

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
        final boolean dark = pref.getBoolean("dark", true);
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
        category = view.findViewById(R.id.spinner);

        date.setText(ShowDetailUtils.DateList.get(pos));
        price.setText(ShowDetailUtils.PriceList.get(pos));
        notes.setText(ShowDetailUtils.NotesList.get(pos));
        category.setSelection(Integer.valueOf(ShowDetailUtils.SpinnerList.get(pos)));

        String ogDateString = date.getText().toString();
        String ogTimestamp = dateToTimestamp(ogDateString);
        Date ogDate = new Date(Long.parseLong(ogTimestamp));
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(ogDate);
        final DatePickerDialog.OnDateSetListener dateClick = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar);
        };

        date.setOnClickListener(v -> {
            if(dark) {
                new DatePickerDialog(mContext, R.style.Dialog_Dark, dateClick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            } else {
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

        save.setOnClickListener(v -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
            builder1.setTitle(getString(R.string.confirm));
            builder1.setTitle(getString(R.string.areyousure));

            builder1.setPositiveButton("Yes", (dialog12, which) -> saveText());
            builder1.setNegativeButton("No", (dialog1, which) -> {
            });
            AlertDialog alertDialog = builder1.create();
            TypedValue typedValue1 = new TypedValue();
            Resources.Theme theme1 = Objects.requireNonNull(getContext()).getTheme();
            theme1.resolveAttribute(R.attr.Primary, typedValue1, true);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(typedValue1.data));
            alertDialog.show();
        });

        delete.setOnClickListener(v -> {
            RemoveItemDB(pos, Objects.requireNonNull(getContext()));
            ((MainActivity) Objects.requireNonNull(getActivity())).displaySelectedScreen(R.id.nav_edit);
        });
        return dialog;
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean matchString (String str) {
        Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        Matcher m = p.matcher(str);
        if (m.find()) {
            Log.d("test", "matchString: " + m.group(0));
            return Objects.equals(m.group(), str);
        } else {
            return false;
        }
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
                cal.setTimeInMillis(Objects.requireNonNull(date1).getTime());
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
                    ShowDetailUtils.setSpinner(pos, String.valueOf(category.getSelectedItemPosition()));
                    ShowDetailUtils.setCurrency(pos, CurrencyUtils.defaultCurrency);
                    UpdateDB(pos, Objects.requireNonNull(getContext()));
                }
            }
        } else {
            Toast.makeText(mContext, "Invalid Date format. Try DD/MM/YYYY", Toast.LENGTH_LONG).show();
        }
    }

    private String dateToTimestamp(String value) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date date = formatter.parse(value);
            String newDate = String.valueOf(Objects.requireNonNull(date).getTime());
            Log.d("test", "dateToTimestamp: "+newDate);
            return newDate;
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Invalid Date format. Try DD/MM/YYYY", Toast.LENGTH_LONG).show();
        }
        return "";
    }
}
