package com.sahil.gupte.HomeCalc.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.SortDialogFragment;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;
import com.sahil.gupte.HomeCalc.Utils.FamilyUtils;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;

import java.util.Objects;

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

        String family = FamilyUtils.FID;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference familyNode = database.getReference(Objects.requireNonNull(family));

        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("SpinnerSort", 0);
        final boolean collective = pref.getBoolean("collective", false);
        final int row1 = pref.getInt("row1", 0);
        final int column2 = pref.getInt("column2", 0);
        final int column3 = pref.getInt("column3", 0);

        familyNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getActivity() != null) {
                    Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if (f instanceof FamilyDetails) {
                        familyTotal = 0;
                        ShowDetailUtils.clearLists();
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            if(collective) {
                                showDetailUtils.getCollectiveData(childDataSnapshot);
                            } else {
                                showDetailUtils.getData(childDataSnapshot);
                                if (row1 == 0) {
                                    showDetailUtils.familyView(linear, ShowDetailUtils.SpinnerList, childDataSnapshot, false);
                                } else if (row1 == 1) {
                                    showDetailUtils.familyView(linear, ShowDetailUtils.TimeList, childDataSnapshot, false);
                                }
                            }
                        }
                        if(collective) {
                            if (row1 == 0) {
                                showDetailUtils.familyView(linear, ShowDetailUtils.SpinnerList, dataSnapshot, true);
                            } else if (row1 == 1) {
                                showDetailUtils.familyView(linear, ShowDetailUtils.TimeList, dataSnapshot, true);
                            }
                        }
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
    public void onCreateOptionsMenu( Menu menu,  MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("SpinnerSort", 0);
        boolean collective = pref.getBoolean("collective", false);
        inflater.inflate(R.menu.main, menu);
        MenuItem collectiveM = menu.findItem(R.id.collective);
        if(collective) {
            collectiveM.setTitle(getString(R.string.action_separate));
        } else {
            collectiveM.setTitle(getString(R.string.action_collective));
        }
        collectiveM.setVisible(true);
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
        if (id == R.id.collective) {
            SwitchView();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowSortDialogFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("fragment", 1);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SortDialogFragment sortDialogFragment = new SortDialogFragment();
        sortDialogFragment.setArguments(bundle);
        sortDialogFragment.show(ft, "dialog");
    }

    private void SwitchView() {
        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("SpinnerSort", 0);
        boolean collective = pref.getBoolean("collective", false);
        collective = !collective;
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("collective", collective);
        editor.apply();
        ((MainActivity) Objects.requireNonNull(getActivity())).displaySelectedScreen(R.id.nav_family_view);

    }

}
