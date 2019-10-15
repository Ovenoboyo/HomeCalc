package com.sahil.gupte.HomeCalc.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.SortDialogFragment;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;
import com.sahil.gupte.HomeCalc.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
class ShowDetails extends Fragment {

    public ShowDetails() {
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

        String family = ShowDetailUtils.FID;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("test", "onCreateView: "+user);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference familyNode = database.getReference(Objects.requireNonNull(family));
        final DatabaseReference userNode = familyNode.child(user.getDisplayName());

        SharedPreferences pref = getContext().getSharedPreferences("SpinnerSort", 0);
        final int row1 = pref.getInt("row1", 0);

        Query query = userNode.orderByChild("spinner");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showDetailUtils.getData(dataSnapshot);
                if (getActivity() != null) {
                    Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if (f instanceof ShowDetails) {
                        if (row1 == 0) {
                            showDetailUtils.addTextViews(linear, ShowDetailUtils.SpinnerList, false, false);
                        } else if (row1 == 1) {
                            showDetailUtils.addTextViews(linear, ShowDetailUtils.DateList, false, false);
                        }
                    }
                }
                progress.setVisibility(View.GONE);
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
        Bundle bundle = new Bundle();
        bundle.putInt("fragment", 0);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SortDialogFragment sortDialogFragment = new SortDialogFragment();
        sortDialogFragment.setArguments(bundle);
        sortDialogFragment.show(ft, "dialog");
    }
}
