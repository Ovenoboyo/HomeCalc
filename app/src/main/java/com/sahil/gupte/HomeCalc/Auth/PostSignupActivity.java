package com.sahil.gupte.HomeCalc.Auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.HomeCalc.BuildConfig;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.FamilyHintDialogFragment;
import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;
import com.sahil.gupte.HomeCalc.Utils.ThemeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PostSignupActivity extends AppCompatActivity {

    private static final String TAG = "PostSignupActivity";
    private static final String LOG_TAG = "PostSignupActivity";
    private String uid;
    private EditText inputID;
    private ProgressBar progressBar;
    private BarcodeDetector detector;
    private Uri imageUri;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private static final int PHOTO_REQUEST = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this, getApplicationContext());
        setContentView(R.layout.activity_postsignup);

        RelativeLayout coverView = findViewById(R.id.cover_view);
        LinearLayout mainView = findViewById(R.id.content_view);

        Bundle b = getIntent().getExtras();

        assert b != null;
        uid = b.getString("UID");

        if (uid == null) {
            startActivity(new Intent(PostSignupActivity.this, LoginActivity.class));
            finish();
        }


        final String[] prevID = new String[1];
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference firstNode = rootRef.child("Users");

        firstNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prevID[0] = ShowDetailUtils.getFamilyID(dataSnapshot, uid);
                if (b.getBoolean("login", false)) {
                    if (prevID[0] != null) {
                        startActivity(new Intent(PostSignupActivity.this, MainActivity.class));
                        finish();
                        return;
                    }
                }

                mainView.setVisibility(View.VISIBLE);
                coverView.setVisibility(View.GONE);
                ShowHintDialogFragment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button btnSignUp = findViewById(R.id.sign_up_button);
        Button btnGenerate = findViewById(R.id.generate_button);
        inputID = findViewById(R.id.family_id);
        progressBar = findViewById(R.id.progressBar);

        Button button = findViewById(R.id.scan_button);
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
            inputID.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
        }
        button.setOnClickListener(view -> ActivityCompat.requestPermissions(PostSignupActivity.this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION));

        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        if (!detector.isOperational()) {
            inputID.setText("Could not set up the detector!");
            return;
        }

        inputID.setText(prevID[0]);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null) {
            startActivity(new Intent(PostSignupActivity.this, LoginActivity.class));
        }


        btnSignUp.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            String familyID = inputID.getText().toString().trim();

            if (TextUtils.isEmpty(familyID)) {
                Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                UUID.fromString(familyID);
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "onClick: "+e);
                Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, Object> taskMap = new HashMap<>();
            taskMap.put(uid, familyID);

            firstNode.updateChildren(taskMap);

            progressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(PostSignupActivity.this, MainActivity.class));
            finish();

        });

        btnGenerate.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            inputID.setText(UUID.randomUUID().toString());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else {
                Toast.makeText(PostSignupActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {
                Bitmap bitmap = decodeBitmapUri(this, imageUri);
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Barcode> barcodes = detector.detect(frame);
                    for (int index = 0; index < barcodes.size(); index++) {
                        Barcode code = barcodes.valueAt(index);
                        inputID.setText(code.displayValue);
                    }
                    if (barcodes.size() == 0) {
                        inputID.setText("Scan Failed. Try turning up the brightness");
                    }
                } else {
                    inputID.setText("Could not set up the detector!");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(PostSignupActivity.this,
                BuildConfig.APPLICATION_ID + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, inputID.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
}
