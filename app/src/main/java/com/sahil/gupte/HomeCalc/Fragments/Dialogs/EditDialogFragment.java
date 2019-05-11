package com.sahil.gupte.HomeCalc.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;
import com.sahil.gupte.HomeCalc.R;

import static com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils.RemoveItemDB;
import static com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils.UpdateDB;


public class EditDialogFragment extends DialogFragment
{
    private Context mContext;
    private int pos;
    private EditText date,price,notes;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setCancelable(true);
        pos = getArguments().getInt("pos");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle(getString(R.string.edit));

        date = view.findViewById(R.id.edit_date);
        price = view.findViewById(R.id.edit_price);
        notes = view.findViewById(R.id.edit_notes);

        date.setText(ShowDetailUtils.DateList.get(pos));
        price.setText(ShowDetailUtils.PriceList.get(pos));
        notes.setText(ShowDetailUtils.NotesList.get(pos));

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView save = view.findViewById(R.id.save);
        TextView delete = view.findViewById(R.id.delete);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(getString(R.string.confirm));
                builder1.setTitle(getString(R.string.areyousure));

                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveText();
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveItemDB(pos, getContext());
            }
        });
        return dialog;
    }

    private void saveText() {
        ShowDetailUtils.setTime(pos, date.getText().toString(), getContext());
        ShowDetailUtils.setPrice(pos, price.getText().toString());
        ShowDetailUtils.setNotes(pos, notes.getText().toString());
        UpdateDB(pos, getContext());
    }
}
