package com.sahil.gupte.HomeCalc.Utils;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

class CsvWriter {

    static void writeToCSV(ArrayList<String[]> dataList) {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String date = formatter.format(today.getTime());
        String filePath = baseDir + File.separator + "HomeCalc" + File.separator+ "Old Data";
        String file = filePath + File.separator + "Old-"+date+".csv";
        File FilePath = new File(filePath);
        File f = new File(file);
        if (!FilePath.exists()) {
            FilePath.mkdir();
        }
        CSVWriter writer;
        FileWriter mFileWriter;

        if(f.exists() && !f.isDirectory())
        {
            try {
                mFileWriter = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            writer = new CSVWriter(mFileWriter);
        } else {
            try {
                writer = new CSVWriter(new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        writer.writeNext(new String[] {"HomeCalc", "Old Data, 2 months before "+ date});
        writer.writeNext(null);
        writer.writeNext(new String[]{"Date", "Category", "Price", "Desc"});
        for (int i = 0; i < dataList.size(); i++) {
            String[] data = dataList.get(i);
            writer.writeNext(data);
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
