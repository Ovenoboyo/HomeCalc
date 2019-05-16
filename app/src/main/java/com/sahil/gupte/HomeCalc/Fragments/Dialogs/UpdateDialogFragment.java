package com.sahil.gupte.HomeCalc.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.sahil.gupte.HomeCalc.R;
import com.sahil.gupte.HomeCalc.Utils.UpdateUtils;


public class UpdateDialogFragment extends DialogFragment
{
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setCancelable(true);
        UpdateUtils updateUtils = new UpdateUtils();
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
        builder.setTitle(" New update available!");
        builder.setPositiveButton("Update now!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment(mContext);
                        progressDialogFragment.show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
