package com.sahil.gupte.HomeCalc.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sahil.gupte.HomeCalc.MainActivity;
import com.sahil.gupte.HomeCalc.R;

import java.util.Objects;


public class SortDialogFragment extends DialogFragment
{
    private static final String TAG = "SortDialogFragment";
    private Context mContext;
    private Spinner row1, column1, column2, column3;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setCancelable(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.sort_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle(getString(R.string.edit));

        row1 = view.findViewById(R.id.row1);
        column1 = view.findViewById(R.id.column1);
        column2 = view.findViewById(R.id.column2);
        column3 = view.findViewById(R.id.column3);

        SharedPreferences pref = mContext.getSharedPreferences("SpinnerSort", 0);
        int row1p = pref.getInt("row1", 0);
        int column1p = pref.getInt("column1", 1);
        int column2p = pref.getInt("column2", 0);
        int column3p = pref.getInt("column3", 1);

        row1.setSelection(row1p);
        column1.setSelection(column1p);
        column2.setSelection(column2p);
        column3.setSelection(column3p);

        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView save = view.findViewById(R.id.save);
        TextView cancel = view.findViewById(R.id.cancel);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (row1.getSelectedItemPosition() != column1.getSelectedItemPosition() && column2.getSelectedItemPosition() != column3.getSelectedItemPosition()) {
                    saveSort();
                    dialog.dismiss();
                    ((MainActivity) Objects.requireNonNull(getActivity())).displaySelectedScreen(R.id.nav_details);
                } else {
                    Toast.makeText(mContext, "All items must be different", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private void saveSort() {
        Log.d(TAG, "saveSort: here");
        int row1Item = row1.getSelectedItemPosition();
        int column1Item = column1.getSelectedItemPosition();
        int column2Item = column2.getSelectedItemPosition();
        int column3Item = column3.getSelectedItemPosition();

        SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("SpinnerSort", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("row1", row1Item);
        editor.putInt("column1", column1Item);
        editor.putInt("column2", column2Item);
        editor.putInt("column3", column3Item);
        editor.commit();

    }
}
