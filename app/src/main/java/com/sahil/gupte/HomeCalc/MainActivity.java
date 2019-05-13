package com.sahil.gupte.HomeCalc;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.HomeCalc.Auth.LoginActivity;
import com.sahil.gupte.HomeCalc.Fragments.AccountDetails;
import com.sahil.gupte.HomeCalc.Fragments.EditDetails;
import com.sahil.gupte.HomeCalc.Fragments.FamilyDetails;
import com.sahil.gupte.HomeCalc.Fragments.FamilyUID;
import com.sahil.gupte.HomeCalc.Fragments.Home;
import com.sahil.gupte.HomeCalc.Fragments.ShowDetails;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;
import com.sahil.gupte.HomeCalc.Utils.UpdateUtils;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 886 ;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Storage Permission is required to install updates", Toast.LENGTH_LONG).show();
            } else {
                // No explanation needed; request the permission

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {


            auth = FirebaseAuth.getInstance();
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

            authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        // user auth state is changed - user is null
                        // launch login activity
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            };

            Calendar calendar = Calendar.getInstance();
            SharedPreferences prefF = getSharedPreferences("Family", 0);
            String family = prefF.getString("familyID", "LostData");
            final ShowDetailUtils showDetailUtils = new ShowDetailUtils(getApplicationContext());

            if (isFirstDayofMonth(calendar)) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                final AlertDialog alertDialog = dialogBuilder.create();
                LayoutInflater inflater = this.getLayoutInflater();
                alertDialog.setContentView(inflater.inflate(R.layout.progress_dialog, null));
                alertDialog.show();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference userNode = database.getReference(family).child(user.getDisplayName());

                userNode.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        showDetailUtils.ClearDB(dataSnapshot);
                        alertDialog.dismiss();

                        userNode.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView Username = headerView.findViewById(R.id.username_text);
            TextView Email = headerView.findViewById(R.id.email_text);
            Username.setText(user.getDisplayName());
            Email.setText(user.getEmail());
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
            displaySelectedScreen(R.id.nav_home);
            UpdateUtils updateUtils = new UpdateUtils();

            updateUtils.runUpdater(getApplicationContext(), getSupportFragmentManager());
        }

    }

    //sign out method
    private void signOut() {
        auth.signOut();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                navigationView.setCheckedItem(R.id.nav_home);
                fragment = new Home();
                break;

            case R.id.nav_details:
                navigationView.setCheckedItem(R.id.nav_details);
                fragment = new ShowDetails();
                break;

            case R.id.nav_edit:
                navigationView.setCheckedItem(R.id.nav_edit);
                fragment = new EditDetails();
                break;

            case R.id.nav_logout:
                navigationView.setCheckedItem(R.id.nav_logout);
                signOut();
                break;

            case R.id.nav_family:
                navigationView.setCheckedItem(R.id.nav_family);
                fragment = new FamilyUID();
                break;

            case R.id.nav_family_view:
                navigationView.setCheckedItem(R.id.nav_family_view);
                fragment = new FamilyDetails();
                break;

            case R.id.nav_account:
                navigationView.setCheckedItem(R.id.nav_account);
                fragment = new AccountDetails();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private boolean isFirstDayofMonth(Calendar calendar){
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar cannot be null.");
        }

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth <= 15;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle b)
    {
        super.onSaveInstanceState(b);
    }

}
