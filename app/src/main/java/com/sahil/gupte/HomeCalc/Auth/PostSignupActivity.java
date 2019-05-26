package com.sahil.gupte.HomeCalc.Auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.FamilyHintDialogFragment;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;
import com.sahil.gupte.HomeCalc.Utils.ThemeUtils;

import java.util.Objects;
import java.util.UUID;

public class PostSignupActivity extends AppCompatActivity {

    private static final String TAG = "PostSignupActivity";
    private EditText inputID;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this, getApplicationContext());
        setContentView(R.layout.activity_postsignup);

        ShowHintDialogFragment();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Family", 0);
        final SharedPreferences.Editor editor = pref.edit();


        Button btnSignUp = findViewById(R.id.sign_up_button);
        Button btnGenerate = findViewById(R.id.generate_button);
        inputID = findViewById(R.id.family_id);
        progressBar = findViewById(R.id.progressBar);

        String prevID = pref.getString("familyID", "");
        inputID.setText(prevID);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null) {
            startActivity(new Intent(PostSignupActivity.this, LoginActivity.class));
        }


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String familyID = inputID.getText().toString().trim();

                if (TextUtils.isEmpty(familyID)) {
                    Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    UUID uuid = UUID.fromString(familyID);
                } catch (IllegalArgumentException e) {
                    Log.d(TAG, "onClick: "+e);
                    Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                editor.putString("familyID", familyID);
                editor.apply();
                startActivity(new Intent(PostSignupActivity.this, MainActivity.class));
                finish();

            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("familyID", UUID.randomUUID().toString());
                editor.apply();
                startActivity(new Intent(PostSignupActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void ShowHintDialogFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("fragment", 1);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FamilyHintDialogFragment familyHintDialogFragment = new FamilyHintDialogFragment();
        familyHintDialogFragment.setArguments(bundle);
        familyHintDialogFragment.show(ft, "dialog");
    }
}
