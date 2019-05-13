package com.sahil.gupte.HomeCalc.Utils;

import android.os.Environment;

class CheckForSDCard {
    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        return Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED);
    }
}
