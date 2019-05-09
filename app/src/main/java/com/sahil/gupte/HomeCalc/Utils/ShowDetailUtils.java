package com.sahil.gupte.HomeCalc.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class ShowDetailUtils {

    private static final String TAG = "ShowDetsilUtils";
    Context mContext;
    public static final ArrayList<String> SpinnerList = new ArrayList<>();
    public static final ArrayList<String> NotesList = new ArrayList<>();
    public static final ArrayList<String> PriceList = new ArrayList<>();
    public static final ArrayList<String> TimeList = new ArrayList<>();

    private static final ArrayList<String> SpinnerKeyList = new ArrayList<>();
    private static final ArrayList<String> NotesKeyList = new ArrayList<>();
    private static final ArrayList<String> PriceKeyList = new ArrayList<>();
    private static final ArrayList<String> TimeKeyList = new ArrayList<>();

    LinearLayout linear0, linear1, linear2, linear3;
    FragmentManager fm;

    private static final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference userNode = database.getReference(user.getUid());
    private static MainActivity mainActivity;

    public ShowDetailUtils(Context context, LinearLayout linear0, LinearLayout linear1, LinearLayout linear2, LinearLayout linear3){
        mContext = context;
        this.linear0 = linear0;
        this.linear1 = linear1;
        this.linear2 = linear2;
        this.linear3 = linear3;
    }

    public ShowDetailUtils(Context context, LinearLayout linear0, LinearLayout linear1, LinearLayout linear2, LinearLayout linear3, FragmentManager fragmentManager, MainActivity activity) {
        mContext = context;
        this.linear0 = linear0;
        this.linear1 = linear1;
        this.linear2 = linear2;
        this.linear3 = linear3;
        fm = fragmentManager;
        mainActivity = activity;
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

        compareList(ListOfEntryNotes);
        compareList(ListOfEntryPrice);
        compareList(ListOfEntrySpinner);
        compareList(ListOfEntryTime);

        putDataInList(ListOfEntryNotes, NotesList);
        putDataInList(ListOfEntryTime, TimeList);
        putDataInList(ListOfEntryPrice, PriceList);
        putDataInList(ListOfEntrySpinner, SpinnerList);

        putKeyInList(ListOfEntryNotes, NotesKeyList);
        putKeyInList(ListOfEntryTime, TimeKeyList);
        putKeyInList(ListOfEntryPrice, PriceKeyList);
        putKeyInList(ListOfEntrySpinner, SpinnerKeyList);

        Log.d(TAG, "sortMap: "+NotesKeyList.get(0)+", "+SpinnerKeyList.get(0));

    }


    private void compareList(ArrayList<Map.Entry<String, Object>> ListOfEntry) {
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

    public void addTextViews(boolean edit) {
        linear0.setVisibility(View.GONE);
        linear1.setVisibility(View.GONE);
        linear2.setVisibility(View.GONE);
        linear3.setVisibility(View.GONE);
        for (int i = 0; i < NotesList.size(); i++) {
            switch (Integer.valueOf(SpinnerList.get(i))) {

                case 0:
                    if (edit) {
                        setTextLayoutEdit(linear0, i);
                    } else {
                        setTextLayout(linear0, i);
                    }
                    break;

                case 1:
                    if (edit) {
                        setTextLayoutEdit(linear1, i);
                    } else {
                        setTextLayout(linear1, i);
                    }
                    break;

                case 2:
                    if (edit) {
                        setTextLayoutEdit(linear2, i);
                    } else {
                        setTextLayout(linear2, i);
                    }
                    break;

                case 3:
                    if (edit) {
                        setTextLayoutEdit(linear3, i);
                    } else {
                        setTextLayout(linear3, i);
                    }
                    break;
            }
        }
    }


    private void setTextLayout (LinearLayout linear, int i) {
        TextView price, notes,time;

        LinearLayout linearLayout = new LinearLayout(mContext);
        ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        time = new TextView(mContext);
        time.setText(TimeList.get(i));
        time.setTextSize(16);
        time.setBackgroundResource(R.drawable.text_border);
        time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        time.setLayoutParams(lpt);
        time.setPadding(0,50,0, 50);
        time.setGravity(Gravity.CENTER);
        linearLayout.addView(time);

        linear.setVisibility(View.VISIBLE);
        price = new TextView((mContext));
        price.setText("" + PriceList.get(i)+"₹");
        price.setTextSize(16);
        price.setPadding(0,50,0, 50);
        price.setBackgroundResource(R.drawable.text_border);
        price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        price.setGravity(Gravity.CENTER);
        price.setLayoutParams(lpt);
        linearLayout.addView(price);

        notes = new TextView(mContext);
        notes.setText("" + NotesList.get(i));
        notes.setTextSize(16);
        notes.setBackgroundResource(R.drawable.text_border);
        notes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        notes.setGravity(Gravity.CENTER);
        notes.setPadding(0,50,0, 50);
        notes.setLayoutParams(lpt);
        linearLayout.addView(notes);

        linear.addView(linearLayout);
    }

    private void setTextLayoutEdit (LinearLayout linear, final int i) {
        TextView price, notes,time;

        LinearLayout linearLayout = new LinearLayout(mContext);
        ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        time = new TextView(mContext);
        time.setText(TimeList.get(i));
        time.setTextSize(16);
        time.setBackgroundResource(R.drawable.text_border);
        time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        time.setGravity(Gravity.CENTER);
        time.setPadding(0,50,0, 50);
        time.setLayoutParams(lpt);
        linearLayout.addView(time);

        linear.setVisibility(View.VISIBLE);
        price = new TextView((mContext));
        price.setText("" + PriceList.get(i)+"₹");
        price.setTextSize(16);
        price.setBackgroundResource(R.drawable.text_border);
        price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        price.setGravity(Gravity.CENTER);
        price.setPadding(0,50,0, 50);
        price.setLayoutParams(lpt);
        linearLayout.addView(price);

        notes = new TextView(mContext);
        notes.setText("" + NotesList.get(i));
        notes.setTextSize(16);
        notes.setBackgroundResource(R.drawable.text_border);
        notes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        notes.setGravity(Gravity.CENTER);
        notes.setPadding(0,50,0, 50);
        notes.setLayoutParams(lpt);
        linearLayout.addView(notes);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogFragment(i);
            }
        });

        linear.addView(linearLayout);
    }

    public static void UpdateDB(int pos) {
        String notesKey = NotesKeyList.get(pos);
        String priceKey = PriceKeyList.get(pos);
        String timeKey = TimeKeyList.get(pos);



        Log.d(TAG, "UpdateDB: pos: "+pos+", "+NotesKeyList.get(0)+", "+NotesKeyList.get(1));

        userNode.child("notes").child(notesKey).setValue(NotesList.get(pos));
        userNode.child("price").child(priceKey).setValue(PriceList.get(pos));
        userNode.child("timestamp").child(timeKey).setValue(TimeList.get(pos));


        mainActivity.displaySelectedScreen(R.id.nav_edit);

    }

    public static void RemoveItemDB(int pos) {
        String notesKey = NotesKeyList.get(pos);
        String priceKey = PriceKeyList.get(pos);
        String timeKey = TimeKeyList.get(pos);
        String spinnerKey = SpinnerKeyList.get(pos);

        Log.d(TAG, "RemoveItemDB: ");

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

    public static void setTime(int i, String value) {
        TimeList.set(i, value);
    }
}
