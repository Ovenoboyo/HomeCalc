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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.zxing.WriterException;
import com.sahil.gupte.HomeCalc.R;

import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;
import static java.security.AccessController.getContext;


public class FamilyUID extends Fragment {

    private static final String TAG = "FamilyUID";

    public FamilyUID() {
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
        View view = inflater.inflate(R.layout.fragment_family, container, false);
        TextView Familyid = view.findViewById(R.id.familyText);
        ImageView clipboard = view.findViewById(R.id.clipboard);
        ImageView qrCode = view.findViewById(R.id.qrCode);

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

        clipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) Objects.requireNonNull(getContext()).getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("FamilyUID", family);
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied ID to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
