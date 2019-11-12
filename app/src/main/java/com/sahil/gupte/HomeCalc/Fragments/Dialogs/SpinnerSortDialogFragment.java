package com.sahil.gupte.HomeCalc.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sahil.gupte.HomeCalc.CustomViews.CustomRecyclerViewSpinner;
import com.sahil.gupte.HomeCalc.CustomViews.SwipeAndDragHelper;
import com.sahil.gupte.HomeCalc.R;
import com.sahil.gupte.HomeCalc.Utils.FamilyUtils;
import com.sahil.gupte.HomeCalc.Utils.ShowDetailUtils;

import java.util.Objects;


public class SpinnerSortDialogFragment extends DialogFragment {
    private Context mContext;
    private int pos;
    private EditText date, price, notes;
    private Spinner category;
    private CustomRecyclerViewSpinner listAdapter;
    private RecyclerView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setCancelable(true);
        pos = Objects.requireNonNull(getArguments()).getInt("pos");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.sort_dialog_spinner, null);
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

        listAdapter = new CustomRecyclerViewSpinner(getContext(), ShowDetailUtils.getSpinnerNameList(), getChildFragmentManager());
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(listAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        listAdapter.setTouchHelper(touchHelper);
        list = view.findViewById(R.id.custom_list_spinner);
        list.setAdapter(listAdapter);
        touchHelper.attachToRecyclerView(list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        list.setLayoutManager(llm);

        FloatingActionButton add = view.findViewById(R.id.add);
        add.setOnClickListener(view1 -> listAdapter.addCount());

        AlertDialog dialog = builder.create();
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = Objects.requireNonNull(getContext()).getTheme();
        theme.resolveAttribute(R.attr.Primary, typedValue, true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(typedValue.data));
        dialog.show();

        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        FamilyUtils.updateSpinners(CustomRecyclerViewSpinner.spinnerNameListFinal);

        CustomRecyclerViewSpinner.spinnerNameList.clear();
        CustomRecyclerViewSpinner.spinnerNameListFinal.clear();
        super.onDismiss(dialog);
    }

    void changeItems(int pos, String text) {
        listAdapter.changeItem(pos, text);
    }
}
