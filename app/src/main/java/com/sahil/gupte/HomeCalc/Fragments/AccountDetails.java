package com.sahil.gupte.HomeCalc.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sahil.gupte.HomeCalc.Auth.PostSignupActivity;
import com.sahil.gupte.HomeCalc.Auth.ResetPasswordActivity;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;

import java.util.Objects;


public class AccountDetails extends Fragment {

    public AccountDetails() {
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
        Log.d("FamilyUID", "onCreateView: here");
        // Inflate the layout for this fragment

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_account_details, container, false);
        TextView email = view.findViewById(R.id.account_email);
        Button change_pass = view.findViewById(R.id.change_password);
        Button show_uid = view.findViewById(R.id.family_uid);
        Button change_uid = view.findViewById(R.id.change_family_uid);

        email.setText(Objects.requireNonNull(user).getEmail());

        show_uid.setOnClickListener(v -> ((MainActivity) Objects.requireNonNull(getActivity())).displaySelectedScreen(R.id.nav_family));

        change_pass.setOnClickListener(v -> startActivity(new Intent(getActivity(), ResetPasswordActivity.class)));

        change_uid.setOnClickListener(v -> startActivity(new Intent(getActivity(), PostSignupActivity.class)));


        return view;
    }
}
