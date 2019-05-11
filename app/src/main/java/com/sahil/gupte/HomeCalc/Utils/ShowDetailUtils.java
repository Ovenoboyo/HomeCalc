package com.sahil.gupte.HomeCalc.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ShowDetailUtils {

    private static final String TAG = "ShowDetsilUtils";
    private Context mContext;
    public static final ArrayList<String> SpinnerList = new ArrayList<>();
    public static final ArrayList<String> NotesList = new ArrayList<>();
    public static final ArrayList<String> PriceList = new ArrayList<>();
    public static final ArrayList<String> TimeList = new ArrayList<>();
    public static final ArrayList<String> DateList = new ArrayList<>();
    public static final ArrayList<String> UserList = new ArrayList<>();
    public static final ArrayList<String> PriceTotal = new ArrayList<>();

    public static String[] SpinnerNameList;

    private static final ArrayList<String> SpinnerKeyList = new ArrayList<>();
    private static final ArrayList<String> NotesKeyList = new ArrayList<>();
    private static final ArrayList<String> PriceKeyList = new ArrayList<>();
    private static final ArrayList<String> TimeKeyList = new ArrayList<>();
    private static final ArrayList<String> UserKeyList = new ArrayList<>();

    public static int familyTotal = 0;

    FragmentManager fm;

    private static MainActivity mainActivity;

    private int row1, column1, column2, column3;

    //Passed by ShowDetails Fragment
    public ShowDetailUtils(Context context){
        mContext = context;
    }

    //Passed by EditDetails Fragment
    public ShowDetailUtils(Context context, FragmentManager fragmentManager, MainActivity activity) {
        mContext = context;
        fm = fragmentManager;
        mainActivity = activity;
    }

    public void getUserList(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            Map<String, Object> map = (Map<String, Object>) postSnapshot.getValue();
            Set<Map.Entry<String, Object>> Entry = map.entrySet();
            ArrayList<Map.Entry<String, Object>> ListOfEntry = new ArrayList<Map.Entry<String,Object>>(Entry);
            putDataInList(ListOfEntry, UserList);
            putKeyInList(ListOfEntry, UserKeyList);

        }
    }

    public void getData(DataSnapshot dataSnapshot) {
        NotesList.clear();
        PriceList.clear();
        SpinnerList.clear();
        TimeList.clear();
        NotesKeyList.clear();
        PriceKeyList.clear();
        SpinnerKeyList.clear();
        TimeKeyList.clear();
        Map<String, Object> mapTime = (Map<String, Object>) dataSnapshot.child("timestamp").getValue();
        Map<String, Object> mapSpinner = (Map<String, Object>) dataSnapshot.child("spinner").getValue();
        Map<String, Object> mapPrice = (Map<String, Object>) dataSnapshot.child("price").getValue();
        Map<String, Object> mapNotes = (Map<String, Object>) dataSnapshot.child("notes").getValue();

        SpinnerNameList = mContext.getResources().getStringArray(R.array.category);

        SharedPreferences pref = mContext.getSharedPreferences("SpinnerSort", 0);
        row1 = pref.getInt("row1", 0);
        column1 = pref.getInt("column1", 0);
        column2 = pref.getInt("column2", 0);
        column3 = pref.getInt("column3", 0);

        if (mapTime != null && mapSpinner != null && mapPrice != null && mapNotes != null) {
            sortMap(mapTime, mapSpinner, mapPrice, mapNotes);
        } else {
            Toast.makeText(mContext, "Could not get data (Database could be empty)", Toast.LENGTH_LONG).show();
        }
    }

    private void sortMap(Map<String, Object> mapTime, Map<String, Object> mapSpinner, Map<String, Object> mapPrice, Map<String, Object> mapNotes) {

        Set<Map.Entry<String, Object>> EntryTime = mapTime.entrySet();
        ArrayList<Map.Entry<String, Object>> ListOfEntryTime = new ArrayList<Map.Entry<String,Object>>(EntryTime);

        Set<Map.Entry<String, Object>> EntrySpinner = mapSpinner.entrySet();
        ArrayList<Map.Entry<String, Object>> ListOfEntrySpinner = new ArrayList<Map.Entry<String,Object>>(EntrySpinner);

        Set<Map.Entry<String, Object>> EntryPrice = mapPrice.entrySet();
        ArrayList<Map.Entry<String, Object>> ListOfEntryPrice = new ArrayList<Map.Entry<String,Object>>(EntryPrice);

        Set<Map.Entry<String, Object>> EntryNotes = mapNotes.entrySet();
        ArrayList<Map.Entry<String, Object>> ListOfEntryNotes = new ArrayList<Map.Entry<String,Object>>(EntryNotes);

        sortList(ListOfEntryNotes);
        sortList(ListOfEntryPrice);
        sortList(ListOfEntrySpinner);
        sortList(ListOfEntryTime);

        putDataInList(ListOfEntryNotes, NotesList);
        putDataInList(ListOfEntryTime, TimeList);
        putDataInList(ListOfEntryPrice, PriceList);
        putDataInList(ListOfEntrySpinner, SpinnerList);

        putKeyInList(ListOfEntryNotes, NotesKeyList);
        putKeyInList(ListOfEntryTime, TimeKeyList);
        putKeyInList(ListOfEntryPrice, PriceKeyList);
        putKeyInList(ListOfEntrySpinner, SpinnerKeyList);

        setDateList();

    }


    private void sortList(ArrayList<Map.Entry<String, Object>> ListOfEntry) {
        Collections.sort(ListOfEntry, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
    }

    private void putDataInList(ArrayList<Map.Entry<String, Object>> ListOfEntry, ArrayList<String> list) {
        for (Map.Entry<String, Object> entry : ListOfEntry) {
            list.add(entry.getValue().toString());
        }
    }

    private void putKeyInList(ArrayList<Map.Entry<String, Object>> ListOfEntry, ArrayList<String> list) {
        for (Map.Entry<String, Object> entry : ListOfEntry) {
            list.add(entry.getKey());
        }
    }

    private void setDateList() {
        DateList.clear();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        for (int i = 0; i < TimeList.size(); i++) {
            DateList.add(i, formatter.format(new Date(Long.parseLong(TimeList.get(i)))));
        }
    }

    public void familyView (LinearLayout linearLayout, ArrayList<String> List, DataSnapshot dataSnapshot){
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

        addTextViews(false, linearLayoutUser, List);

        linearLayoutUser.setVisibility(View.GONE);
    }

    public void addTextViews(boolean edit, LinearLayout linearLayout, ArrayList<String> List) {

        int totalamt = 0, grandTotal = 0;

        //new list will contain unique elements
        ArrayList<String> newList = new ArrayList<>();
        for (String element : List) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        for(int i = 0; i<newList.size(); i++) {

            final LinearLayout linearLayoutOuter1 = new LinearLayout(mContext);
            ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayoutOuter1.setLayoutParams(lp);
            linearLayoutOuter1.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(linearLayoutOuter1);

            LinearLayout linearLayoutWButton = new LinearLayout(mContext);
            linearLayoutWButton.setLayoutParams(lp);
            linearLayoutWButton.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutWButton.setBackgroundResource(R.drawable.text_border);

            ViewGroup.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.15f);
            ViewGroup.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.85f);

            TextView textViewOuter = new TextView(mContext);
            if (row1 == 1 && !edit) {
                textViewOuter.setText(newList.get(i));
            } else {
                textViewOuter.setText(SpinnerNameList[Integer.valueOf(newList.get(i))]);
            }

            textViewOuter.setTextSize(28);
            textViewOuter.setTypeface(null, Typeface.BOLD_ITALIC);
            textViewOuter.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewOuter.setPadding(0, 25, 0, 25);
            textViewOuter.setLayoutParams(lp1);

            linearLayoutWButton.addView(textViewOuter);

            final ImageView expand = new ImageView(mContext);
            expand.setImageResource(R.drawable.ic_arrow_drop_up);
            expand.setLayoutParams(lp2);

            linearLayoutWButton.addView(expand);

            linearLayoutOuter1.addView(linearLayoutWButton);

            final LinearLayout linearLayoutOuter = new LinearLayout(mContext);
            linearLayoutOuter.setLayoutParams(lp);
            linearLayoutOuter.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(linearLayoutOuter);

            final boolean[] expandLayout = {false};
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
            if (column3 == 0 && column2 == 1 && !edit) {
                if (row1 == 0) {
                    addTitles(linearLayoutTitles, "Date");
                } else {
                    addTitles(linearLayoutTitles, "Category");
                }
                addTitles(linearLayoutTitles, "Notes");
                addTitles(linearLayoutTitles, "Price");
            } else {
                if (row1 == 1 && !edit) {
                    addTitles(linearLayoutTitles, "Category");
                } else {
                    addTitles(linearLayoutTitles, "Date");
                }
                addTitles(linearLayoutTitles, "Price");
                addTitles(linearLayoutTitles, "Notes");
            }

            for (int j = 0; j<NotesList.size(); j++) {

                if (row1 == 1 && !edit) {
                    if (List.get(j).equals(newList.get(i))) {

                        totalamt = totalamt + Integer.valueOf(PriceList.get(j));

                        LinearLayout linearLayout1 = new LinearLayout(mContext);
                        linearLayout1.setLayoutParams(lp);
                        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayoutOuter.addView(linearLayout1);

                        if (column3 == 0 && column2 == 1 && !edit) {
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
                    if (Integer.valueOf(List.get(j)) == Integer.valueOf(newList.get(i))) {

                        totalamt = totalamt + Integer.valueOf(PriceList.get(j));

                        LinearLayout linearLayout1 = new LinearLayout(mContext);
                        linearLayout1.setLayoutParams(lp);
                        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayoutOuter.addView(linearLayout1);

                        if (column3 == 0 && column2 == 1 && !edit) {
                            if (row1 == 0) {
                                addDataFields(j, linearLayout1, "date", edit);
                            } else {
                                addDataFields(j, linearLayout1, "spinner", edit);
                            }
                            addDataFields(j, linearLayout1, "notes", edit);
                            addDataFields(j, linearLayout1, "price", edit);
                        } else {
                            if (row1 == 1 && !edit) {
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
            grandTotal = grandTotal +totalamt;
            familyTotal = familyTotal + grandTotal;
            totalamt = 0;
            linearLayoutOuter.addView(totalLinearLayout);
        }

        LinearLayout grandtotalLinear = new LinearLayout(mContext);
        grandtotalLinear.setBackgroundResource(R.drawable.text_border);


        if (column2 == 0 && column3 == 1) {
            addTotal(grandTotal, grandtotalLinear, "grandTotal", false);
            addTotal(grandTotal, grandtotalLinear, "totalAmt", false);
            addTotal(grandTotal, grandtotalLinear, "view", false);
        } else {
            addTotal(grandTotal, grandtotalLinear, "grandTotal", false);
            addTotal(grandTotal, grandtotalLinear, "view", false);
            addTotal(grandTotal, grandtotalLinear, "totalAmt", false);
        }

        linearLayout.addView(grandtotalLinear);
    }

    public void addTotal (int totalamt, LinearLayout totalLinearLayout, String type, boolean bg) {
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
                data.setText("₹"+totalamt);
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
        totalLinearLayout.addView(data);

    }

    private void addTitles (LinearLayout linearLayoutTitles, String title) {

        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        TextView column = new TextView(mContext);
        column.setText(title);
        column.setTextSize(24);
        column.setBackgroundResource(R.drawable.text_border);
        column.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        column.setTypeface(null, Typeface.ITALIC);
        column.setLayoutParams(lpt);
        column.setPadding(0,50,0, 50);
        column.setGravity(Gravity.CENTER);
        linearLayoutTitles.addView(column);

    }

    private void addDataFields(final int j, LinearLayout linearLayout1, String type, boolean edit) {

        ViewGroup.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        TextView data = new TextView(mContext);

        switch (type) {
            case "date":
                data.setText(DateList.get(j));
                break;

            case "price":
                data.setText("₹" + PriceList.get(j));
                break;

            case "notes":
                data.setText(NotesList.get(j));
                break;

            case "spinner":
                data.setText(SpinnerNameList[Integer.valueOf(SpinnerList.get(j))]);
        }
        data.setTextSize(16);
        data.setBackgroundResource(R.drawable.text_border);
        data.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        data.setLayoutParams(lp1);
        data.setPadding(0, 50, 0, 50);
        data.setGravity(Gravity.CENTER);
        linearLayout1.addView(data);

        if (edit) {
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

        SharedPreferences prefF = mContext.getSharedPreferences("Family", 0);
        String family = prefF.getString("familyID", "LostData");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userNode = database.getReference(family).child(user.getDisplayName());

        userNode.child("notes").child(notesKey).setValue(NotesList.get(pos));
        userNode.child("price").child(priceKey).setValue(PriceList.get(pos));
        userNode.child("timestamp").child(timeKey).setValue(TimeList.get(pos));

        mainActivity.displaySelectedScreen(R.id.nav_edit);

    }

    public static void RemoveItemDB(int pos, Context mContext) {
        String notesKey = NotesKeyList.get(pos);
        String priceKey = PriceKeyList.get(pos);
        String timeKey = TimeKeyList.get(pos);
        String spinnerKey = SpinnerKeyList.get(pos);

        SharedPreferences prefF = mContext.getSharedPreferences("Family", 0);
        String family = prefF.getString("familyID", "LostData");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userNode = database.getReference(family).child(user.getDisplayName());

        userNode.child("notes").child(notesKey).removeValue();
        userNode.child("price").child(priceKey).removeValue();
        userNode.child("timestamp").child(timeKey).removeValue();
        userNode.child("spinner").child(spinnerKey).removeValue();

        mainActivity.displaySelectedScreen(R.id.nav_edit);

    }

    private void ShowDialogFragment(int i) {
        Bundle args = new Bundle();
        args.putInt("pos", i);
        FragmentTransaction ft = fm.beginTransaction();
        EditDialogFragment editDialogFragment = new EditDialogFragment();
        editDialogFragment.setArguments(args);
        editDialogFragment.show(ft, "dialog");
    }


    public static void setSpinner(int i, String value) {
        SpinnerList.set(i, value);
    }


    public static void setPrice(int i, String value) {
        PriceList.set(i, value);
    }

    public static void setNotes(int i, String value) {
        NotesList.set(i, value);
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
}
