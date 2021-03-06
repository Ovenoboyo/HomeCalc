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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.HintDialogFragment;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.SpinnerSortDialogFragment;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;
import com.sahil.gupte.HomeCalc.Utils.FamilyUtils;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDetails extends Fragment {

    public EditDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_details, container, false);
        final LinearLayout linear = view.findViewById(R.id.linear);

        SharedPreferences prefD = Objects.requireNonNull(getContext()).getSharedPreferences("hint_dialog", 0);
        if(!prefD.getBoolean("hint", false)) {
            ShowHintDialogFragment();
        }

        FragmentManager fm = getChildFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        final ShowDetailUtils showDetailUtils = new ShowDetailUtils(getContext(), fm, ((MainActivity)getActivity()));

        Fragment prev = Objects.requireNonNull(getFragmentManager()).findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        final RelativeLayout progress = view.findViewById(R.id.progressLayout);
        progress.setVisibility(View.VISIBLE);

        String family = FamilyUtils.FID;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userNode = database.getReference(Objects.requireNonNull(family)).child(Objects.requireNonNull(Objects.requireNonNull(user).getDisplayName()));

        SharedPreferences pref = getContext().getSharedPreferences("SpinnerSort", 0);
        final int row1 = pref.getInt("row1", 0);

        userNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                showDetailUtils.getData(dataSnapshot);
                if (getActivity() != null) {
                    Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if (f instanceof EditDetails) {
                        if (row1 == 0) {
                            showDetailUtils.addTextViews(linear, ShowDetailUtils.SpinnerList, true, false);
                        } else if (row1 == 1) {
                            showDetailUtils.addTextViews(linear, ShowDetailUtils.TimeList, true, false);
                        }
                    }
                }
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu( Menu menu,  MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
         inflater.inflate(R.menu.main, menu);
        MenuItem hint = menu.findItem(R.id.hint);
        hint.setVisible(true);

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

        if(id == R.id.hint) {
            ShowHintDialogFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowSortDialogFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("fragment", 0);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SpinnerSortDialogFragment sortDialogFragment = new SpinnerSortDialogFragment();
        sortDialogFragment.setArguments(bundle);
        sortDialogFragment.show(ft, "SpinnerSortDialogFragment");
        ft.addToBackStack("SpinnerSortDialogFragment");
    }

    private void ShowHintDialogFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        HintDialogFragment hintDialogFragment = new HintDialogFragment();
        hintDialogFragment.show(ft, "dialog");
    }


}
