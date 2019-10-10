package com.sahil.gupte.HomeCalc.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.EditDialogFragment;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
public class ShowDetailUtils {

    private static final String TAG = "ShowDetsilUtils";
    private final Context mContext;
    public static final ArrayList<String> SpinnerList = new ArrayList<>();
    public static final ArrayList<String> NotesList = new ArrayList<>();
    public static final ArrayList<String> PriceList = new ArrayList<>();
    public static final ArrayList<String> TimeList = new ArrayList<>();
    public static final ArrayList<String> DateList = new ArrayList<>();
    public static final ArrayList<String> CurrencyList = new ArrayList<>();

    private static String[] SpinnerNameList;

    private static final ArrayList<String> SpinnerKeyList = new ArrayList<>();
    private static final ArrayList<String> NotesKeyList = new ArrayList<>();
    private static final ArrayList<String> PriceKeyList = new ArrayList<>();
    private static final ArrayList<String> TimeKeyList = new ArrayList<>();
    private static final ArrayList<String> CurrencyKeyList = new ArrayList<>();

    private final String[] monthArray = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    final boolean[] expandLayout = new boolean[1];

    public static float familyTotal = 0;

    private FragmentManager fm;

    private static MainActivity mainActivity;

    private int row1;
    private int column2;
    private int column3;
    private int colorFromTheme;

    private float totalamt = 0;
    private float grandTotal = 0;

    //Passed by ShowDetails Fragment
    public ShowDetailUtils(Context context) {
        mContext = context;
    }

    //Passed by EditDetails Fragment
    public ShowDetailUtils(Context context, FragmentManager fragmentManager, MainActivity activity) {
        mContext = context;
        fm = fragmentManager;
        mainActivity = activity;
    }

    public void getData(DataSnapshot dataSnapshot) {
        clearLists();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().compareTo("notes") == 0) {
                for (DataSnapshot dsN : ds.getChildren()) {
                    NotesList.add(dsN.getValue().toString());
                    NotesKeyList.add(dsN.getKey().toString());
                }
            } else if (ds.getKey().compareTo("price") == 0) {
                for (DataSnapshot dsN : ds.getChildren()) {
                    PriceList.add(dsN.getValue().toString());
                    PriceKeyList.add(dsN.getKey().toString());
                }
            } else if (ds.getKey().compareTo("spinner") == 0) {
                for (DataSnapshot dsN : ds.getChildren()) {
                    SpinnerList.add(dsN.getValue().toString());
                    SpinnerKeyList.add(dsN.getKey().toString());
                }
            } else if (ds.getKey().compareTo("timestamp") == 0) {
                for (DataSnapshot dsN : ds.getChildren()) {
                    TimeList.add(dsN.getValue().toString());
                    TimeKeyList.add(dsN.getKey().toString());
                }
            } else if (ds.getKey().compareTo("currency") == 0) {
                for (DataSnapshot dsN : ds.getChildren()) {
                    CurrencyList.add(dsN.getValue().toString());
                    CurrencyKeyList.add(dsN.getKey().toString());
                }
            }
        }
        setDateList(DateList, TimeList);
        sortCurrencies(CurrencyList);

        SpinnerNameList = mContext.getResources().getStringArray(R.array.category);

        SharedPreferences pref = mContext.getSharedPreferences("SpinnerSort", 0);
        row1 = pref.getInt("row1", 0);
        column2 = pref.getInt("column2", 0);
        column3 = pref.getInt("column3", 1);

        if (NotesList == null && SpinnerList == null && PriceList == null && NotesList == null) {
            Toast.makeText(mContext, "Could not get data (Database could be empty)", Toast.LENGTH_LONG).show();
        }

        TypedValue tV = new TypedValue();
        Resources.Theme theme = mContext.getTheme();
        boolean success = theme.resolveAttribute(R.attr.PrimaryText, tV, true);
        if (success) {
            colorFromTheme = tV.data;
        } else {
            colorFromTheme = Color.BLACK;
        }
    }

    public void getCollectiveData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().compareTo("notes") == 0) {
                for (DataSnapshot dsN : ds.getChildren()) {
                    NotesList.add(dsN.getValue().toString());
                    NotesKeyList.add(dsN.getKey().toString());
                }
            } else if (ds.getKey().compareTo("price") == 0) {
                for (DataSnapshot dsP : ds.getChildren()) {
                    PriceList.add(dsP.getValue().toString());
                    PriceKeyList.add(dsP.getKey().toString());
                }
            } else if (ds.getKey().compareTo("spinner") == 0) {
                for (DataSnapshot dsS : ds.getChildren()) {
                    SpinnerList.add(dsS.getValue().toString());
                    SpinnerKeyList.add(dsS.getKey().toString());
                }
            } else if (ds.getKey().compareTo("timestamp") == 0) {
                for (DataSnapshot dsT : ds.getChildren()) {
                    TimeList.add(dsT.getValue().toString());
                    TimeKeyList.add(dsT.getKey().toString());
                }
            } else if (ds.getKey().compareTo("currency") == 0) {
                for (DataSnapshot dsN : ds.getChildren()) {
                    CurrencyList.add(dsN.getValue().toString());
                    CurrencyKeyList.add(dsN.getKey().toString());
                }
            }
        }

        setDateList(DateList, TimeList);
        sortCurrencies(CurrencyList);


        SpinnerNameList = mContext.getResources().getStringArray(R.array.category);

        SharedPreferences pref = mContext.getSharedPreferences("SpinnerSort", 0);
        row1 = pref.getInt("row1", 0);
        column2 = pref.getInt("column2", 0);
        column3 = pref.getInt("column3", 1);

        TypedValue tV = new TypedValue();
        Resources.Theme theme = mContext.getTheme();
        boolean success = theme.resolveAttribute(R.attr.PrimaryText, tV, true);
        if (success) {
            colorFromTheme = tV.data;
        } else {
            colorFromTheme = Color.BLACK;
        }
    }

    private void sortCurrencies(ArrayList<String> currencyList) {
        for (int i = 0; i < currencyList.size(); i++) {
            String currency = currencyList.get(i);
            if (!currency.equals(CurrencyUtils.defaultCurrency)) {
                Float finalValue = Float.valueOf(PriceList.get(i));
                try {
                    finalValue = new CurrencyUtils(currency, CurrencyUtils.defaultCurrency, Float.valueOf(PriceList.get(i))).execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setPrice(i, String.valueOf(finalValue));
            } else {
            }
        }
    }

    private void setDateList(ArrayList<String> dateList, ArrayList<String> List) {
        dateList.clear();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        for (int i = 0; i < List.size(); i++) {
            dateList.add(i, formatter.format(new Date(Long.parseLong(List.get(i)))));
        }
    }

    public void familyView(LinearLayout linearLayout, ArrayList<String> List, DataSnapshot dataSnapshot, boolean collective) {


        if (collective) {
            if (TimeList != null && SpinnerList != null && PriceList != null && NotesList != null) {
                addTextViews(linearLayout, List, false, true);
            } else {
                Toast.makeText(mContext, "Could not get data (Database could be empty)", Toast.LENGTH_LONG).show();
            }
        } else {

            LinearLayout linearLayoutUser1 = new LinearLayout(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayoutUser1.setLayoutParams(lp);
            linearLayoutUser1.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(linearLayoutUser1);


            LinearLayout linearLayoutWButton = new LinearLayout(mContext);
            linearLayoutWButton.setLayoutParams(lp);
            linearLayoutWButton.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutWButton.setPadding(0, 25, 0, 25);
            linearLayoutWButton.setBackgroundResource(R.drawable.text_border);

            ViewGroup.LayoutParams lp11 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.15f);
            ViewGroup.LayoutParams lp21 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.85f);

            TextView textViewUser = new TextView(mContext);
            textViewUser.setText(dataSnapshot.getKey());
            textViewUser.setTextSize(32);
            textViewUser.setTextColor(colorFromTheme);
            textViewUser.setTypeface(null, Typeface.BOLD);
            textViewUser.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewUser.setLayoutParams(lp11);
            linearLayoutWButton.addView(textViewUser);

            final ImageView expand = new ImageView(mContext);
            expand.setImageResource(R.drawable.ic_arrow_drop_down);
            expand.setLayoutParams(lp21);
            linearLayoutWButton.addView(expand);

            linearLayoutUser1.addView(linearLayoutWButton);


            final LinearLayout linearLayoutUser = new LinearLayout(mContext);
            linearLayoutUser.setLayoutParams(lp);
            linearLayoutUser.setOrientation(LinearLayout.VERTICAL);
            linearLayoutUser1.addView(linearLayoutUser);

            final boolean[] expandLayout = {true};
            expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!expandLayout[0]) {
                        linearLayoutUser.setVisibility(View.GONE);
                        expand.setImageResource(R.drawable.ic_arrow_drop_down);
                    } else {
                        linearLayoutUser.setVisibility(View.VISIBLE);
                        expand.setImageResource(R.drawable.ic_arrow_drop_up);
                    }
                    expandLayout[0] = !expandLayout[0];
                }
            });

            addTextViews(linearLayoutUser, List, false, true);

            linearLayoutUser.setVisibility(View.GONE);
        }
    }

    private void addTextViewsMethod(LinearLayout linearLayout, ArrayList<String> List, boolean edit, boolean family, ArrayList<String> monthList, ArrayList<String> newList, ArrayList<String> newListC, int i, int k) {
        if (k != -1) {
            String element = monthArray[Integer.valueOf(newList.get(i).substring(3, 5))];
            Log.d(TAG, "addTextViewsMethod: "+element+", "+monthList.get(k));
            if (!element.equals(monthList.get(k))) {
                return;
            }
        }
        final LinearLayout linearLayoutOuter1 = new LinearLayout(mContext);
        ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutOuter1.setLayoutParams(lp);
        linearLayoutOuter1.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(linearLayoutOuter1);

        LinearLayout linearLayoutWButton = new LinearLayout(mContext);
        linearLayoutWButton.setLayoutParams(lp);
        linearLayoutWButton.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.15f);
        ViewGroup.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.85f);

        final LinearLayout linearLayoutOuter = new LinearLayout(mContext);
        linearLayoutOuter.setLayoutParams(lp);
        linearLayoutOuter.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(linearLayoutOuter);

        TextView textViewOuter = new TextView(mContext);
        if (row1 == 1) {

            textViewOuter.setText(newList.get(i));
            linearLayoutWButton.addView(textViewOuter);
            linearLayoutOuter1.addView(linearLayoutWButton);

        } else {
            textViewOuter.setText(SpinnerNameList[Integer.valueOf(newList.get(i))]);
            linearLayoutWButton.addView(textViewOuter);
            linearLayoutOuter1.addView(linearLayoutWButton);
        }

        textViewOuter.setTextColor(colorFromTheme);
        textViewOuter.setTextSize(28);
        textViewOuter.setTypeface(null, Typeface.BOLD_ITALIC);
        textViewOuter.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewOuter.setPadding(0, 25, 0, 25);
        textViewOuter.setLayoutParams(lp1);


        final ImageView expand = new ImageView(mContext);
        expand.setImageResource(R.drawable.ic_arrow_drop_up);
        expand.setLayoutParams(lp2);

        linearLayoutWButton.addView(expand);

        if (family) {
            expandLayout[0] = false;
        } else {
            expandLayout[0] = true;
        }
        if (!expandLayout[0]) {
            linearLayoutOuter.setVisibility(View.GONE);
            expand.setImageResource(R.drawable.ic_arrow_drop_down);
        }
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expandLayout[0]) {
                    linearLayoutOuter.setVisibility(View.GONE);
                    expand.setImageResource(R.drawable.ic_arrow_drop_down);
                } else {
                    linearLayoutOuter.setVisibility(View.VISIBLE);
                    expand.setImageResource(R.drawable.ic_arrow_drop_up);
                }
                expandLayout[0] = !expandLayout[0];
            }
        });

        //Layout for titles
        LinearLayout linearLayoutTitles = new LinearLayout(mContext);
        linearLayoutTitles.setLayoutParams(lp);
        linearLayoutTitles.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutOuter.addView(linearLayoutTitles);

        //Add titles
        if (column3 == 0 && column2 == 1) {
            if (row1 == 0) {
                addTitles(linearLayoutTitles, "Date");
            } else {
                addTitles(linearLayoutTitles, "Category");
            }
            addTitles(linearLayoutTitles, "Notes");
            addTitles(linearLayoutTitles, "Price");
        } else {
            if (row1 == 1) {
                addTitles(linearLayoutTitles, "Category");
            } else {
                addTitles(linearLayoutTitles, "Date");
            }
            addTitles(linearLayoutTitles, "Price");
            addTitles(linearLayoutTitles, "Notes");
        }

        for (int j = 0; j < NotesList.size(); j++) {

            if (row1 == 1) {
                if (List.get(j).equals(newListC.get(i))) {

                    totalamt = totalamt + Float.valueOf(PriceList.get(j));

                    LinearLayout linearLayout1 = new LinearLayout(mContext);
                    linearLayout1.setLayoutParams(lp);
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayoutOuter.addView(linearLayout1);

                    if (column3 == 0 && column2 == 1) {
                        if (row1 == 0) {
                            addDataFields(j, linearLayout1, "date", edit);
                        } else {
                            addDataFields(j, linearLayout1, "spinner", edit);
                        }
                        addDataFields(j, linearLayout1, "notes", edit);
                        addDataFields(j, linearLayout1, "price", edit);
                    } else {
                        if (row1 == 0) {
                            addDataFields(j, linearLayout1, "date", edit);
                        } else {
                            addDataFields(j, linearLayout1, "spinner", edit);
                        }
                        addDataFields(j, linearLayout1, "price", edit);
                        addDataFields(j, linearLayout1, "notes", edit);
                    }
                }
            } else {
                if (Integer.valueOf(List.get(j)).equals(Integer.valueOf(newList.get(i)))) {

                    totalamt = totalamt + Float.valueOf(PriceList.get(j));

                    LinearLayout linearLayout1 = new LinearLayout(mContext);
                    linearLayout1.setLayoutParams(lp);
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayoutOuter.addView(linearLayout1);

                    if (column3 == 0 && column2 == 1) {
                        if (row1 == 0) {
                            addDataFields(j, linearLayout1, "date", edit);
                        } else {
                            addDataFields(j, linearLayout1, "spinner", edit);
                        }
                        addDataFields(j, linearLayout1, "notes", edit);
                        addDataFields(j, linearLayout1, "price", edit);
                    } else {
                        if (row1 == 1) {
                            addDataFields(j, linearLayout1, "spinner", edit);
                        } else {
                            addDataFields(j, linearLayout1, "date", edit);
                        }
                        addDataFields(j, linearLayout1, "price", edit);
                        addDataFields(j, linearLayout1, "notes", edit);
                    }
                }
            }
        }
        LinearLayout totalLinearLayout = new LinearLayout(mContext);
        totalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        totalLinearLayout.setLayoutParams(lp);

        if (column2 == 0 && column3 == 1) {
            addTotal(totalamt, totalLinearLayout, "totalText", true);
            addTotal(totalamt, totalLinearLayout, "totalAmt", true);
            addTotal(totalamt, totalLinearLayout, "view", true);
        } else {
            addTotal(totalamt, totalLinearLayout, "totalText", true);
            addTotal(totalamt, totalLinearLayout, "view", true);
            addTotal(totalamt, totalLinearLayout, "totalAmt", true);
        }
        grandTotal = grandTotal + totalamt;
        totalamt = 0;
        linearLayoutOuter.addView(totalLinearLayout);
    }


    public void addTextViews(LinearLayout linearLayout, ArrayList<String> List, boolean edit, boolean family) {

        //new list will contain unique elements
        ArrayList<String> newList = new ArrayList<>();
        ArrayList<String> monthList = new ArrayList<>();
        for (String element : List) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        Collections.sort(newList, Collections.reverseOrder());
        ArrayList<String> newListC = newList;

        if (row1 == 1) {
            ArrayList<String> DateListC = new ArrayList<>();
            setDateList(DateListC, newList);
            newList = DateListC;

            for (String element : newList) {
                String monthElement = element.substring(3, 5);
                String month = monthArray[Integer.valueOf(monthElement)];
                if (!monthList.contains(month)) {
                    monthList.add(month);
                }
            }
        }


        if (row1 == 1) {
            float monthTotal = 0;
            for (int k = 0; k < monthList.size(); k++) {

                ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ViewGroup.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.15f);
                ViewGroup.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.85f);

                final LinearLayout linearLayoutMonthButton = new LinearLayout(mContext);
                linearLayoutMonthButton.setLayoutParams(lp);
                linearLayoutMonthButton.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutMonthButton.setBackgroundResource(R.drawable.text_border);
                linearLayout.addView(linearLayoutMonthButton);

                final LinearLayout linearLayoutMonth = new LinearLayout(mContext);
                linearLayoutMonth.setLayoutParams(lp);
                linearLayoutMonth.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(linearLayoutMonth);

                TextView textViewMonth = new TextView(mContext);
                textViewMonth.setText(monthList.get(k));
                textViewMonth.setTextColor(colorFromTheme);
                textViewMonth.setTextSize(28);
                textViewMonth.setTypeface(null, Typeface.BOLD_ITALIC);
                textViewMonth.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textViewMonth.setPadding(0, 25, 0, 25);
                textViewMonth.setLayoutParams(lp1);

                linearLayoutMonthButton.addView(textViewMonth);

                final ImageView expand = new ImageView(mContext);
                expand.setImageResource(R.drawable.ic_arrow_drop_up);
                expand.setLayoutParams(lp2);

                linearLayoutMonthButton.addView(expand);

                if (family) {
                    expandLayout[0] = false;
                } else {
                    expandLayout[0] = true;
                }
                if (!expandLayout[0]) {
                    linearLayoutMonth.setVisibility(View.GONE);
                    expand.setImageResource(R.drawable.ic_arrow_drop_down);
                }
                expand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!expandLayout[0]) {
                            linearLayoutMonth.setVisibility(View.GONE);
                            expand.setImageResource(R.drawable.ic_arrow_drop_down);
                        } else {
                            linearLayoutMonth.setVisibility(View.VISIBLE);
                            expand.setImageResource(R.drawable.ic_arrow_drop_up);
                        }
                        expandLayout[0] = !expandLayout[0];
                    }
                });

                for (int i = 0; i < newList.size(); i++) {
                    addTextViewsMethod(linearLayoutMonth, List, edit, family, monthList, newList, newListC, i, k);
                }

                LinearLayout grandtotalLinear = new LinearLayout(mContext);
                grandtotalLinear.setBackgroundResource(R.drawable.text_border);
                familyTotal =familyTotal +grandTotal;

                monthTotal = grandTotal - monthTotal;
                Log.d(TAG, "addTextViews: "+grandTotal+", "+monthTotal);

                if(column2 ==0&&column3 ==1)

                {
                    addTotal(monthTotal, grandtotalLinear, "grandTotal", false);
                    addTotal(monthTotal, grandtotalLinear, "totalAmt", false);
                    addTotal(monthTotal, grandtotalLinear, "view", false);
                } else

                {
                    addTotal(monthTotal, grandtotalLinear, "grandTotal", false);
                    addTotal(monthTotal, grandtotalLinear, "view", false);
                    addTotal(monthTotal, grandtotalLinear, "totalAmt", false);
                }
                linearLayoutMonth.addView(grandtotalLinear);
            }
        } else {
            for (int i = 0; i < newList.size(); i++) {
                addTextViewsMethod(linearLayout, List, edit, family, monthList, newList, newListC, i, -1);
            }
            LinearLayout grandtotalLinear = new LinearLayout(mContext);
            grandtotalLinear.setBackgroundResource(R.drawable.text_border);
            familyTotal =familyTotal +grandTotal;

            if(column2 ==0&&column3 ==1)

            {
                addTotal(grandTotal, grandtotalLinear, "grandTotal", false);
                addTotal(grandTotal, grandtotalLinear, "totalAmt", false);
                addTotal(grandTotal, grandtotalLinear, "view", false);
            } else

            {
                addTotal(grandTotal, grandtotalLinear, "grandTotal", false);
                addTotal(grandTotal, grandtotalLinear, "view", false);
                addTotal(grandTotal, grandtotalLinear, "totalAmt", false);
            }
            linearLayout.addView(grandtotalLinear);
        }

    }

    public void addTotal (float totalamt, LinearLayout totalLinearLayout, String type, boolean bg) {
        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        TextView data = new TextView(mContext);

        switch (type) {

            case "grandTotal":
                data.setText("Grand Total:");
                data.setTextSize(24);
                break;

            case "totalText":
                data.setText("Total:");
                data.setTextSize(20);
                break;

            case "view":
                break;

            case "totalAmt":
                if (CurrencyUtils.defaultCurrency.equals("INR")) {
                    data.setText("₹" + totalamt);
                } else if (CurrencyUtils.defaultCurrency.equals("USD")) {
                    data.setText("$" + totalamt);
                } else if (CurrencyUtils.defaultCurrency.equals("SGD")) {
                    data.setText("$" + totalamt);
                } else if (CurrencyUtils.defaultCurrency.equals("GBP")) {
                    data.setText("£" + totalamt);
                } else if (CurrencyUtils.defaultCurrency.equals("EUR")) {
                    data.setText("€" + totalamt);
                }

                data.setTextSize(20);
                break;

            case "familyTotal":
                data.setText("Family Total:");
                data.setTextSize(24);
                break;
        }

        if (bg) {
            data.setBackgroundResource(R.drawable.text_border);
        }
        data.setLayoutParams(lpt);
        data.setPadding(0, 50, 0, 50);
        data.setGravity(Gravity.CENTER);
        data.setTypeface(null, Typeface.BOLD);
        data.setTextColor(colorFromTheme);
        totalLinearLayout.addView(data);

    }

    private void addTitles (LinearLayout linearLayoutTitles, String title) {

        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        TextView column = new TextView(mContext);
        column.setText(title);
        column.setTextSize(24);
        column.setTextColor(colorFromTheme);
        column.setBackgroundResource(R.drawable.text_border);
        column.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        column.setTypeface(null, Typeface.ITALIC);
        column.setLayoutParams(lpt);
        column.setPadding(0,50,0, 50);
        column.setGravity(Gravity.CENTER);
        linearLayoutTitles.addView(column);

    }

    private void addDataFields(final int j, LinearLayout linearLayout1, String type, boolean edit) {

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        TextView data = new TextView(mContext);

        switch (type) {
            case "date":
                data.setText(DateList.get(j));
                break;

            case "price":
                if (CurrencyUtils.defaultCurrency.equals("INR")) {
                    data.setText("₹" + PriceList.get(j));
                } else if (CurrencyUtils.defaultCurrency.equals("USD")) {
                    data.setText("$" + PriceList.get(j));
                } else if (CurrencyUtils.defaultCurrency.equals("SGD")) {
                    data.setText("$" + PriceList.get(j));
                } else if (CurrencyUtils.defaultCurrency.equals("GBP")) {
                    data.setText("£" + PriceList.get(j));
                } else if (CurrencyUtils.defaultCurrency.equals("EUR")) {
                    data.setText("€" + PriceList.get(j));
                }

                break;

            case "notes":
                data.setText(NotesList.get(j));
                break;

            case "spinner":
                data.setText(SpinnerNameList[Integer.valueOf(SpinnerList.get(j))]);
        }
        data.setTextSize(16);
        data.setTextColor(colorFromTheme);
        data.setBackgroundResource(R.drawable.text_border);
        data.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        data.setLayoutParams(lp1);
        data.setPadding(5, 50, 5, 50);
        data.setGravity(Gravity.CENTER);
        linearLayout1.addView(data);
        if(edit) {

            linearLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowDialogFragment(j);
                }
            });
        }
    }

    public static void UpdateDB(int pos, Context mContext) {
        String notesKey = NotesKeyList.get(pos);
        String priceKey = PriceKeyList.get(pos);
        String timeKey = TimeKeyList.get(pos);
        String spinnerKey = SpinnerKeyList.get(pos);
        String currencyKey = CurrencyKeyList.get(pos);

        SharedPreferences prefF = mContext.getSharedPreferences("Family", 0);
        String family = prefF.getString("familyID", "LostData");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userNode = database.getReference(family).child(user.getDisplayName());

        userNode.child("notes").child(notesKey).setValue(NotesList.get(pos));
        userNode.child("price").child(priceKey).setValue(PriceList.get(pos));
        userNode.child("timestamp").child(timeKey).setValue(TimeList.get(pos));
        userNode.child("spinner").child(spinnerKey).setValue(SpinnerList.get(pos));
        userNode.child("currency").child(currencyKey).setValue(CurrencyList.get(pos));

        mainActivity.displaySelectedScreen(R.id.nav_edit);

    }

    public static void RemoveItemDB(int pos, Context mContext) {
        String notesKey = NotesKeyList.get(pos);
        String priceKey = PriceKeyList.get(pos);
        String timeKey = TimeKeyList.get(pos);
        String spinnerKey = SpinnerKeyList.get(pos);
        String currencyKey = CurrencyKeyList.get(pos);

        SharedPreferences prefF = mContext.getSharedPreferences("Family", 0);
        String family = prefF.getString("familyID", "LostData");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userNode = database.getReference(family).child(user.getDisplayName());

        userNode.child("notes").child(notesKey).removeValue();
        userNode.child("price").child(priceKey).removeValue();
        userNode.child("timestamp").child(timeKey).removeValue();
        userNode.child("spinner").child(spinnerKey).removeValue();
        userNode.child("currency").child(currencyKey).removeValue();

    }

    public void ClearDB(DataSnapshot dataSnapshot) {
        getData(dataSnapshot);
        Calendar cal = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.add(Calendar.MONTH, -2);
        today.set(Calendar.DAY_OF_MONTH, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i<NotesList.size(); i++) {
            cal.setTimeInMillis(Long.parseLong(TimeList.get(i)));
            cal.set(Calendar.DAY_OF_MONTH, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            if (cal.getTimeInMillis() <= today.getTimeInMillis()) {
                RemoveItemDB(i, mContext);
            }
        }
    }

    private void ShowDialogFragment(int i) {
        Bundle args = new Bundle();
        args.putInt("pos", i);
        FragmentTransaction ft = fm.beginTransaction();
        EditDialogFragment editDialogFragment = new EditDialogFragment();
        editDialogFragment.setArguments(args);
        editDialogFragment.show(ft, "dialog");
    }


    public static void setPrice(int i, String value) {
        PriceList.set(i, value);
    }

    public static void setSpinner(int i, String value) {
        SpinnerList.set(i, value);
    }

    public static void setNotes(int i, String value) {
        NotesList.set(i, value);
    }

    public static void setCurrency(int i, String value) {
        CurrencyList.set(i, value);
    }

    public static void setTime(int i, String value, Context mContext) {
        DateList.set(i, value);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
        Date date = formatter.parse(value);
        TimeList.set(i, String.valueOf(date.getTime()));
        } catch (ParseException e) {
            Log.d(TAG, "setTime: Parse Exception");
            Toast.makeText(mContext, "Invalid Date format. Try DD/MM/YYYY", Toast.LENGTH_LONG).show();
        }
    }

    public static void clearLists() {
        NotesList.clear();
        PriceList.clear();
        SpinnerList.clear();
        TimeList.clear();
        CurrencyList.clear();
        NotesKeyList.clear();
        PriceKeyList.clear();
        SpinnerKeyList.clear();
        TimeKeyList.clear();
        CurrencyKeyList.clear();
    }
}
