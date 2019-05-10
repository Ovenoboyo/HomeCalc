package com.sahil.gupte.HomeCalc.Auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;

import java.util.UUID;

public class PostSignupActivity extends AppCompatActivity {

    private EditText inputID;
    private ProgressBar progressBar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postsignup);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Family", 0);
        final SharedPreferences.Editor editor = pref.edit();


        Button btnSignUp = findViewById(R.id.sign_up_button);
        Button btnGenerate = findViewById(R.id.generate_button);
        inputID = findViewById(R.id.family_id);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(PostSignupActivity.this, MainActivity.class));
            finish();
        }


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String familyID = inputID.getText().toString().trim();

                if (TextUtils.isEmpty(familyID)) {
                    Toast.makeText(getApplicationContext(), "Enter Some ID", Toast.LENGTH_SHORT).show();
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
}
