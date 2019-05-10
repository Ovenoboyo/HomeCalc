package com.sahil.gupte.HomeCalc.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDetails extends Fragment {

    public EditDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_details, container, false);
        final LinearLayout linear = view.findViewById(R.id.linear);

        FragmentManager fm = getChildFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        final ShowDetailUtils showDetailUtils = new ShowDetailUtils(getContext(), fm, ((MainActivity)getActivity()));

        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        final RelativeLayout progress = view.findViewById(R.id.progressLayout);
        progress.setVisibility(View.VISIBLE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userNode = database.getReference(user.getUid());

        Query query = userNode.orderByChild("spinner");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showDetailUtils.getData(dataSnapshot);
                if (getActivity() != null) {
                    Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if (f instanceof EditDetails) {
                        showDetailUtils.addTextViews(true, linear, showDetailUtils.SpinnerList);
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



}
