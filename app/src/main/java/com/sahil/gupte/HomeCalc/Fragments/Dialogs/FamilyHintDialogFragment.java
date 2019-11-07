package com.sahil.gupte.HomeCalc.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.sahil.gupte.HomeCalc.R;

import java.util.Objects;


public class FamilyHintDialogFragment extends DialogFragment
{
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setCancelable(true);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        SharedPreferences pref = mContext.getSharedPreferences("Theme", 0);
        boolean dark = pref.getBoolean("dark", true);
        AlertDialog.Builder builder;
        if (dark) {
            builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Dialog_Dark));
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.family_hint_dialog, null))
                .setTitle("Family UID")
                .setCancelable(false)
                .setPositiveButton("Understood",
                        (dialog, which) -> {
                            SharedPreferences prefD = Objects.requireNonNull(getContext()).getSharedPreferences("family_hint_dialog", 0);
                            SharedPreferences.Editor editor = prefD.edit();
                            editor.putBoolean("hint", true);
                            editor.commit();
                            dialog.dismiss();
                        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }
}
