package com.sahil.gupte.HomeCalc.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.WriterException;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.FamilyHintDialogFragment;
import com.sahil.gupte.HomeCalc.R;

import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;


public class FamilyUID extends Fragment {

    private static final String TAG = "FamilyUID";

    public FamilyUID() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_family, container, false);
        TextView Familyid = view.findViewById(R.id.familyText);
        ImageView clipboard = view.findViewById(R.id.clipboard);
        ImageView qrCode = view.findViewById(R.id.qrCode);

        SharedPreferences prefD = Objects.requireNonNull(getContext()).getSharedPreferences("family_hint_dialog", 0);
        if(!prefD.getBoolean("hint", false)) {
            ShowHintDialogFragment();
        }

        WindowManager manager = (WindowManager) Objects.requireNonNull(getContext()).getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        SharedPreferences prefF = getContext().getSharedPreferences("Family", 0);
        final String family = prefF.getString("familyID", "Unknown");

        QRGEncoder qrgEncoder = new QRGEncoder(family, null, QRGContents.Type.TEXT, smallerDimension);

        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }

        Familyid.setText(family);

        clipboard.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) Objects.requireNonNull(getContext()).getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("FamilyUID", family);
            clipboardManager.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied ID to clipboard", Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        MenuItem sort = menu.findItem(R.id.sort);
        sort.setVisible(false);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.hint) {
            ShowHintDialogFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowHintDialogFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("fragment", 1);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FamilyHintDialogFragment familyHintDialogFragment = new FamilyHintDialogFragment();
        familyHintDialogFragment.setArguments(bundle);
        familyHintDialogFragment.show(ft, "dialog");
    }
}
