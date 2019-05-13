package com.sahil.gupte.HomeCalc.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sahil.gupte.HomeCalc.CustomViews.CustomListViewInput;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.SwitchDialogFragment;
import com.sahil.gupte.HomeCalc.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Home extends Fragment {

    private View view;
    private ListView list;
    private CustomListViewInput listAdapter;
    private Button AddNew, Submit;
    private DatabaseReference priceNode;
    private DatabaseReference notesNode;
    private DatabaseReference spinnerNode;
    private DatabaseReference userNode;
    private DatabaseReference timeNode;
    private EditText price, notes;
    private Spinner spinner;
    private final ArrayList<Integer> pricelist = new ArrayList<>();
    private final ArrayList<String> noteslist = new ArrayList<>();
    private final ArrayList<Integer> spinnerlist = new ArrayList<>();

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        final SwitchDialogFragment sdf = new SwitchDialogFragment();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            listAdapter = new CustomListViewInput(getActivity());
            list = view.findViewById(R.id.custom_list);
            list.setAdapter(listAdapter);
            AddNew = view.findViewById(R.id.button);
            Submit = view.findViewById(R.id.submit);

        } else {
            ((ViewGroup)view.getParent()).removeView(view);
        }

        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("Family", 0);
        String family = pref.getString("familyID", "LostData");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference firstNode = rootRef.child(family);
        userNode = firstNode.child(user.getDisplayName());

        AddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.addItem();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    price = list.getChildAt(i).findViewById(R.id.editText);
                    notes = list.getChildAt(i).findViewById(R.id.editText2);
                    spinner = list.getChildAt(i).findViewById(R.id.spinner);
                    String ed_text = notes.getText().toString().trim();
                    String ed_textp = price.getText().toString().trim();
                    if (TextUtils.isEmpty(ed_text)|| (TextUtils.isEmpty(ed_textp))) {
                        return;
                    }
                    pricelist.add(Integer.parseInt(price.getText().toString()));
                    noteslist.add(notes.getText().toString());
                    spinnerlist.add(spinner.getSelectedItemPosition());
                }

                for (int i = 0; i < listAdapter.getCount(); i++) {
                    spinnerNode = userNode.child("spinner");
                    priceNode = userNode.child("price");
                    notesNode = userNode.child("notes");
                    timeNode = userNode.child(("timestamp"));
                    spinnerNode.push().setValue(spinnerlist.get(i));
                    priceNode.push().setValue(pricelist.get(i));
                    notesNode.push().setValue(noteslist.get(i));
                    timeNode.push().setValue(String.valueOf(getTimeStartOfDay()));

                }
                showProgressDialog(ft, sdf);
            }
        });
        return view;
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
}
