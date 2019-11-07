package com.sahil.gupte.HomeCalc.Utils;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.util.HashMap;

class CurrencyHttpTask extends AsyncTask<Void, Void, Float> {

    private final String from;
    private final String to;

    private static final String ACCESS_KEY = "WM1PJ9WL556FF12K"; //free
    private static final String BASE_URL = "https://www.alphavantage.co/";
    private static final String QUERY_URL = "query?function=CURRENCY_EXCHANGE_RATE&";
    private static final HashMap<String, Double> CurrencyCache = new HashMap<>();

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();


    public CurrencyHttpTask(String from, String to) {
        this.from = from;
        this.to = to;
    }
    private static float sendConvertRequest(String from, String to){
        Log.d("test", "sendConvertRequest: Request sent");
        HttpGet get = new HttpGet(BASE_URL + QUERY_URL + "from_currency=" + from + "&to_currency=" + to + "&apikey=" + ACCESS_KEY);
        try {
            CloseableHttpResponse response =  httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));

            Double rate;
            if (CurrencyCache.get(from+to) != null) {
                rate = CurrencyCache.get(from+to);
            } else {
                rate = Double.parseDouble(jsonObject.getJSONObject("Realtime Currency Exchange Rate").getString("5. Exchange Rate"));
                CurrencyCache.put(from+to, rate);
            }

            response.close();

            if (rate != null) {
                return rate.floatValue();
            } else {
                throw new Exception();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    protected Float doInBackground(Void... voids) {
        return sendConvertRequest(from, to);
    }

    @Override
    protected void onPostExecute(Float finalValue) {
        super.onPostExecute(finalValue);
    }
}
