package com.sahil.gupte.HomeCalc.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.SortDialogFragment;
import com.sahil.gupte.HomeCalc.R;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;

import static com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils.familyTotal;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyDetails extends Fragment {

    public FamilyDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_details, container, false);
        final LinearLayout linear = view.findViewById(R.id.linear);

        final ShowDetailUtils showDetailUtils = new ShowDetailUtils(getContext());

        final RelativeLayout progress = view.findViewById(R.id.progressLayout);
        progress.setVisibility(View.VISIBLE);

        SharedPreferences prefF = getContext().getSharedPreferences("Family", 0);
        String family = prefF.getString("familyID", "LostData");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference familyNode = database.getReference(family);

        SharedPreferences pref = getContext().getSharedPreferences("SpinnerSort", 0);
        final int row1 = pref.getInt("row1", 0);
        final int column2 = pref.getInt("column2", 0);
        final int column3 = pref.getInt("column3", 0);


        familyNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    showDetailUtils.getData(childDataSnapshot);
                    if (getActivity() != null) {
                        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
                        if (f instanceof FamilyDetails) {
                            familyTotal = 0;
                            if (row1 == 0) {
                                showDetailUtils.familyView(linear, ShowDetailUtils.SpinnerList, childDataSnapshot);
                            } else if (row1 == 1) {
                                showDetailUtils.familyView(linear, ShowDetailUtils.DateList, childDataSnapshot);
                            }
                        }
                    }
                }


                if (getActivity() != null) {
                    Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if (f instanceof FamilyDetails) {
                        LinearLayout familytotalLinear = new LinearLayout(getContext());
                        familytotalLinear.setBackgroundResource(R.drawable.text_border);

                        if (column2 == 0 && column3 == 1) {
                            showDetailUtils.addTotal(familyTotal, familytotalLinear, "familyTotal", false);
                            showDetailUtils.addTotal(familyTotal, familytotalLinear, "totalAmt", false);
                            showDetailUtils.addTotal(familyTotal, familytotalLinear, "view", false);
                        } else {
                            showDetailUtils.addTotal(familyTotal, familytotalLinear, "familyTotal", false);
                            showDetailUtils.addTotal(familyTotal, familytotalLinear, "view", false);
                            showDetailUtils.addTotal(familyTotal, familytotalLinear, "totalAmt", false);
                        }
                        linear.addView(familytotalLinear);
                        progress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sort) {
            ShowSortDialogFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowSortDialogFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SortDialogFragment sortDialogFragment = new SortDialogFragment();
        sortDialogFragment.show(ft, "dialog");
    }

}
