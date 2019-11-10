package com.sahil.gupte.HomeCalc.Utils;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.sahil.gupte.HomeCalc.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class FamilyUtils {

    public static String FID;

    public static String getFamilyID(DataSnapshot dataSnapshot, String UID) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (Objects.equals(ds.getKey(), UID)) {
                FID = ds.getValue().toString();
            }
        }
        return FID;
    }

    public static boolean getFamilyAdminStatus(DataSnapshot dataSnapshot, String UID) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (Objects.equals(ds.getKey(), FID)) {
                for (DataSnapshot ds1 : ds.getChildren()) {
                    if (Objects.equals(ds1.getKey(), UID)) {
                        if (Boolean.parseBoolean(Objects.requireNonNull(ds1.getValue()).toString())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<String> getSpinnerList(DataSnapshot dataSnapshot, Context mContext) {
        ArrayList<String> spinnerNameList = new ArrayList<>();
        spinnerNameList.add(0, "Select an option");
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            spinnerNameList.add(Integer.parseInt(Objects.requireNonNull(ds.getKey())), Objects.requireNonNull(ds.getValue()).toString());
        }

        if (spinnerNameList.isEmpty()) {
            Collections.addAll(spinnerNameList, mContext.getResources().getStringArray(R.array.category));
        }
        return spinnerNameList;
    }
}
