package com.sahil.gupte.HomeCalc.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.sahil.gupte.HomeCalc.CustomViews.CustomRecyclerViewSpinner;
import com.sahil.gupte.HomeCalc.R;

import java.util.Objects;


public class SpinnerEditDialogFragment extends DialogFragment {
    private Context mContext;
    private int pos;
    private EditText price;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setCancelable(true);
        pos = Objects.requireNonNull(getArguments()).getInt("pos");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.edit_dialog_spinner, null);
        SharedPreferences pref = mContext.getSharedPreferences("Theme", 0);
        final boolean dark = pref.getBoolean("dark", true);
        AlertDialog.Builder builder;
        if (dark) {
            builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Dialog_Dark));
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setView(view);
        builder.setTitle(getString(R.string.edit));

        price = view.findViewById(R.id.edit_price);

        price.setText(CustomRecyclerViewSpinner.spinnerNameList.get(pos));

        AlertDialog dialog = builder.create();
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = Objects.requireNonNull(getContext()).getTheme();
        theme.resolveAttribute(R.attr.Primary, typedValue, true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(typedValue.data));
        dialog.show();

        TextView save = view.findViewById(R.id.save);
        TextView delete = view.findViewById(R.id.delete);

        save.setOnClickListener(v -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
            builder1.setTitle(getString(R.string.confirm));
            builder1.setTitle(getString(R.string.areyousure));

            builder1.setPositiveButton("Yes", (dialog12, which) -> saveText());
            builder1.setNegativeButton("No", (dialog1, which) -> {
            });
            AlertDialog alertDialog = builder1.create();
            TypedValue typedValue1 = new TypedValue();
            Resources.Theme theme1 = Objects.requireNonNull(getContext()).getTheme();
            theme1.resolveAttribute(R.attr.Primary, typedValue1, true);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(typedValue1.data));
            alertDialog.show();
        });
        return dialog;
    }

    private void saveText() {
        if (CustomRecyclerViewSpinner.spinnerNameList.contains(price.getText().toString().trim())) {
            Toast.makeText(getContext(), "Duplicate item names not allowed", Toast.LENGTH_SHORT).show();
        } else {
            SpinnerSortDialogFragment spinnerSortDialog = (SpinnerSortDialogFragment) Objects.requireNonNull(Objects.requireNonNull(getParentFragment()).getFragmentManager()).findFragmentByTag("SpinnerSortDialogFragment");
            Objects.requireNonNull(spinnerSortDialog).changeItems(pos, price.getText().toString().trim());
            dismiss();
        }
    }
}
