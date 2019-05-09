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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.ProgressDialogFragment;
import com.sahil.gupte.HomeCalc.Fragments.Utils.ShowDetailUtils;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDetails extends Fragment {

    String TAG = "ShowDetails";


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
        LinearLayout linear0 = view.findViewById(R.id.linear0);
        LinearLayout linear1 = view.findViewById(R.id.linear1);
        LinearLayout linear2 = view.findViewById(R.id.linear2);
        LinearLayout linear3 = view.findViewById(R.id.linear3);

        FragmentManager fm = getChildFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        final ShowDetailUtils showDetailUtils = new ShowDetailUtils(getContext(), linear0, linear1, linear2, linear3, fm, ((MainActivity)getActivity()));

        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        final ProgressDialogFragment pdf = new ProgressDialogFragment();

        showProgressDialog(ft, pdf);

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
                        showDetailUtils.addTextViews(ft, true);
                    }
                }
                hideProgressDialog(ft, pdf);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


    private void showProgressDialog(FragmentTransaction ft, ProgressDialogFragment pdf) {
        pdf.show(ft, "dialog");
    }

    private void hideProgressDialog(FragmentTransaction ft, ProgressDialogFragment pdf) {
        pdf.dismiss();
    }


}
