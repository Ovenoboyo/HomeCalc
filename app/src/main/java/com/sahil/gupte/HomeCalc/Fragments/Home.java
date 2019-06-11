package com.sahil.gupte.HomeCalc.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sahil.gupte.HomeCalc.CustomViews.CustomRecyclerViewInput;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.SwitchDialogFragment;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Home extends Fragment {

    private View view;
    private RecyclerView list;
    private CustomRecyclerViewInput listAdapter;
    private Button AddNew, RemoveNew, Submit;
    private DatabaseReference priceNode;
    private DatabaseReference notesNode;
    private DatabaseReference spinnerNode;
    private DatabaseReference userNode;
    private DatabaseReference timeNode;
    private DatabaseReference rootRef;
    private EditText price, notes, time;
    private Spinner spinner;
    private final ArrayList<Integer> pricelist = new ArrayList<>();
    private final ArrayList<String> noteslist = new ArrayList<>();
    private final ArrayList<Integer> spinnerlist = new ArrayList<>();
    private final ArrayList<String> timelist = new ArrayList<>();

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    public boolean onBackPressed() {
        new AlertDialog.Builder(getContext())
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        getActivity().finish();
                    }
                }).create().show();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        final SwitchDialogFragment sdf = new SwitchDialogFragment();
        Fragment prev = Objects.requireNonNull(getFragmentManager()).findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            view = inflater.inflate(R.layout.fragment_home, container, false);
            listAdapter = new CustomRecyclerViewInput(getActivity());
            list = view.findViewById(R.id.custom_list);
            list.setAdapter(listAdapter);
            AddNew = view.findViewById(R.id.button);
            RemoveNew = view.findViewById(R.id.button2);
            Submit = view.findViewById(R.id.submit);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(RecyclerView.VERTICAL);
            list.setLayoutManager(llm);

        FirebaseMessaging.getInstance().subscribeToTopic("user_"+ Objects.requireNonNull(user.getDisplayName()).replaceAll("\\s", "_"));

        final SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("Family", 0);
        String family = pref.getString("familyID", "LostData");

        rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference firstNode = rootRef.child(Objects.requireNonNull(family));
        userNode = firstNode.child(user.getDisplayName());

        AddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.notifyItemInserted(listAdapter.getItemCount()+1);
                listAdapter.addItem();
            }
        });

        RemoveNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.notifyItemRemoved(listAdapter.getItemCount()+1);
                listAdapter.removeItem();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listAdapter.getItemCount(); i++) {

                    RecyclerView.ViewHolder holder = list.findViewHolderForAdapterPosition(i);
                    if(holder == null) {
                        holder = listAdapter.holderHashMap.get(i);
                    }
                    price = holder.itemView.findViewById(R.id.editText);
                    notes = holder.itemView.findViewById(R.id.editText2);
                    spinner = holder.itemView.findViewById(R.id.spinner);
                    time = holder.itemView.findViewById(R.id.editText3);
                    if (matchString(time.getText().toString())) {
                        if(DateCheck(time.getText().toString())) {
                            if (price.getText().toString().trim().isEmpty()) {
                                showToast("Price can not be empty", getContext());
                                return;
                            } else if ((!TextUtils.isEmpty(price.getText().toString()))) {
                                if(spinner.getSelectedItemPosition() != 0) {
                                    pricelist.add(Integer.parseInt(price.getText().toString()));
                                    noteslist.add(notes.getText().toString());
                                    spinnerlist.add(spinner.getSelectedItemPosition());
                                    timelist.add(time.getText().toString());
                                } else {
                                    showToast("Select an option from the dropdown menu", getContext());
                                    return;
                                }
                            }
                        } else {
                            return;
                        }
                    } else {
                        showToast("Invalid date", getContext());
                        return;
                    }
                }

                if (!pricelist.isEmpty()) {

                    for (int i = 0; i < pricelist.size(); i++) {
                        spinnerNode = userNode.child("spinner");
                        priceNode = userNode.child("price");
                        notesNode = userNode.child("notes");
                        timeNode = userNode.child(("timestamp"));
                        spinnerNode.push().setValue(spinnerlist.get(i));
                        priceNode.push().setValue(pricelist.get(i));
                        notesNode.push().setValue(noteslist.get(i));
                        timeNode.push().setValue(dateToTimestamp(timelist.get(i)));

                    }

                    firstNode.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                String username = childDataSnapshot.getKey();
                                if (username != user.getDisplayName()) {
                                    sendNotificationToUser(Objects.requireNonNull(username).replaceAll("\\s", "_"), user.getDisplayName()+" added new items to his list!");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                showProgressDialog(ft, sdf);
            }
        });
        return view;
    }

    void showToast(String text, Context mContext) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

    private void showProgressDialog(FragmentTransaction ft, SwitchDialogFragment pdf) {
        pdf.show(ft, "dialog");
    }

    private long getTimeStartOfDay() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private String dateToTimestamp(String value) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date date = formatter.parse(value);
            String newDate = String.valueOf(date.getTime());
            Log.d("test", "dateToTimestamp: "+newDate);
            return newDate;
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Invalid Date format. Try DD/MM/YYYY", Toast.LENGTH_LONG).show();
        }
        return String.valueOf(getTimeStartOfDay());
    }

    private void sendNotificationToUser(String user, final String message) {
        final DatabaseReference notifications = rootRef.child("notificationRequests");

        Map notification = new HashMap<>();
        notification.put("username", user);
        notification.put("message", message);

        notifications.push().setValue(notification);
    }

    private boolean DateCheck(String date) {
        Calendar pastCal = Calendar.getInstance();
        Calendar futureCal = Calendar.getInstance();
        pastCal.add(Calendar.MONTH, -2);
        long past = pastCal.getTimeInMillis();
        long future = futureCal.getTimeInMillis();

        if(Long.parseLong(dateToTimestamp(date)) <= past) {
            showToast("Date can not be more than 2 months in the past", getContext());
            return false;
        } else if (Long.parseLong(dateToTimestamp(date)) > future) {
            showToast("Date can not be in future", getContext());
            return false;
        } else {
            return true;
        }
    }

    private boolean matchString (String str) {
        Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        Matcher m = p.matcher(str);
        if (m.find()) {
            if(m.group() == str) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
}
