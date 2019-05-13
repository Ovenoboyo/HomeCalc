package com.sahil.gupte.HomeCalc.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ProgressBar;

import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.ProgressDialogFragment;
import com.sahil.gupte.HomeCalc.Fragments.Dialogs.UpdateDialogFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class UpdateUtils {

    private static final String TAG = "UpdateUtils" ;
    private static String URL, fileName;
    private static final String downloadDirectory = "HomeCalc";


    public UpdateUtils() {
    }

    public void runUpdater(Context mContext, final FragmentManager fm) {
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(mContext)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("https://raw.githubusercontent.com/Ovenoboyo/HomeCalc/master/app/update.json")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        if (isUpdateAvailable) {
                            fileName = "HomeCalc-" + update.getLatestVersion() + ".apk";
                            URL = update.getUrlToDownload().toString() + "/download/" + update.getLatestVersion() + "/HomeCalc-" + update.getLatestVersion() + ".apk";
                            Log.d(TAG, "onSuccess: " + URL);
                            Log.d("Is update available?", Boolean.toString(true));
                            UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
                            updateDialogFragment.show(fm, "dialog");
                        }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();
    }

    public void UpdateNow(Context mContext, ProgressBar progressBar, ProgressDialogFragment progressDialogFragment) {
        if (URL != null && fileName != null) {
            new DownloadingTask(mContext, progressBar, progressDialogFragment).execute();
        }
    }

    private static class DownloadingTask extends AsyncTask<Void, Integer, Void> {

        File apkStorage = null;
        File outputFile = null;

        final ThreadLocal<Context> mContext = new ThreadLocal<>();

        final ThreadLocal<ProgressBar> mProgressBar = new ThreadLocal<>();

        final ProgressDialogFragment progressDialogFragment;

        DownloadingTask(Context context, ProgressBar progressBar, ProgressDialogFragment mProgressDialogFragment) {
            mProgressBar.set(progressBar);
            mContext.set(context);
            progressDialogFragment = mProgressDialogFragment;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            Objects.requireNonNull(mProgressBar.get()).setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    Log.d(TAG, "onPostExecute: Download Completed");
                    progressDialogFragment.dismiss();
                    OpenFile(mContext.get());
                } else {
                    Log.d(TAG, "onPostExecute: Download Failed");//If download failed change button text
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "onPostExecute: download again");
                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                Log.d(TAG, "onPostExecute: Download Failed");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onPostExecute: Download again");
                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        void OpenFile(Context mContext) {

            apkStorage = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + downloadDirectory);
            File fileLocation = new File(apkStorage, fileName);
            Intent downloadIntent;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".FileProvider", fileLocation);
                downloadIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                downloadIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                downloadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                downloadIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                downloadIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                downloadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                downloadIntent.setDataAndType(Uri.fromFile(fileLocation), "application/vnd.android.package-archive");
            }
            mContext.getApplicationContext().startActivity(downloadIntent);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(URL);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + downloadDirectory);
                } else
                    Log.d(TAG, "doInBackground: No SD Card");

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, fileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                int fileLength = c.getContentLength();

                byte[] buffer = new byte[1024];//Set buffer type
                int len;
                int total = 0;

                while ((len = is.read(buffer)) != -1) {
                    if (isCancelled()) {
                        is.close();
                        return null;
                    }
                    total += len;

                    if (fileLength > 0) { // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    }

                    fos.write(buffer, 0, len);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }

}
