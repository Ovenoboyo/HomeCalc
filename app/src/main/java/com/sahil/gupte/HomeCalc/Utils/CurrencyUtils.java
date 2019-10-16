package com.sahil.gupte.HomeCalc.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CurrencyUtils {

    public static String defaultCurrency;

    private Context context;

    public CurrencyUtils(Context context) {
        this.context = context;
    }

    public float getCurrencyData(String to, String from, float value) {

        float rate = 0;
        SharedPreferences pref = context.getSharedPreferences("currency", 0);
        if (!checkCurrencyExists(to+from, pref)) {
            try {
                rate = new CurrencyHttpTask(to, from).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            SharedPreferences.Editor editor = pref.edit();

            ArrayList<String> list = new ArrayList<>();
            list.add(to+from);
            list.add(String.valueOf(rate));
            list.add(String.valueOf(cal.getTimeInMillis()));

            Gson gson = new Gson();
            String json = gson.toJson(list);

            editor.putString(to+from, json);
            editor.apply();
        } else {
            rate = getRate(to+from , pref);
            Log.d("test", "getCurrencyData: "+rate);
        }
        return (rate*value);
    }

    private boolean checkCurrencyExists(String tofrom, SharedPreferences currencySharedPrefs) {
        if (currencySharedPrefs.getString(tofrom, null) != null) {
            Date date = new Date();
            ArrayList<String> list;

            String json = currencySharedPrefs.getString(tofrom, null);
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {}.getType();
            list = gson.fromJson(json, type);

            return date.getTime() < Float.parseFloat(list != null ? list.get(2) : "" + 0);
        } else {
            return false;
        }
    }

    private float getRate(String tofrom, SharedPreferences currencySharedPrefs) {
        ArrayList<String> list;

        String json = currencySharedPrefs.getString(tofrom, null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        list = gson.fromJson(json, type);
        if (list != null) {
            return Float.parseFloat(list.get(1));
        } else {
            return 0;
        }
    }
}
