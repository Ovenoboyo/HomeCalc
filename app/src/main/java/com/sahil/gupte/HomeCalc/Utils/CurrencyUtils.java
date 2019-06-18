package com.sahil.gupte.HomeCalc.Utils;

import android.os.AsyncTask;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class CurrencyUtils extends AsyncTask<Void, Void, Float> {

    public static String defaultCurrency;
    private String from, to;
    private float value;

    private static final String ACCESS_KEY = "WM1PJ9WL556FF12K"; //free
    private static final String BASE_URL = "https://www.alphavantage.co/";
    private static final String QUERY_URL = "query?function=CURRENCY_EXCHANGE_RATE&";
    private static final HashMap<String, Double> CurrencyCache = new HashMap<>();

    private static CloseableHttpClient httpClient = HttpClients.createDefault();


    public CurrencyUtils(String from, String to, float value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }
    private static float sendConvertRequest(String from, String to, float value){

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
                return (float) (value * rate);
            } else {
                throw new Exception();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (org.apache.hc.core5.http.ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    protected Float doInBackground(Void... voids) {
        return sendConvertRequest(from, to, value);
    }

    @Override
    protected void onPostExecute(Float finalValue) {
        super.onPostExecute(finalValue);
    }
}
