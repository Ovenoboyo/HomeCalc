package com.sahil.gupte.HomeCalc.Fragments.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.sahil.gupte.HomeCalc.R;
import com.sahil.gupte.HomeCalc.Utils.UpdateUtils;

public class ProgressDialogFragment extends Dialog {

    public ProgressDialogFragment(Context context) {
        super(context);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_progressbar);
        setCancelable(true);

        UpdateUtils updateUtils = new UpdateUtils();

        ProgressBar progressBar = findViewById(R.id.progressBar);

        updateUtils.UpdateNow(getContext(), progressBar, this);

    }
}
