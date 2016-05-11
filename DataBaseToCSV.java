package com.firstapp.mazarinilaura.clockingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by MazariniLaura on 6/20/2015.
 */
public class DataBaseToCSV
{
    Context context;
    public DataBaseToCSV(Context context) {
        this.context=context;
    }


    public void exportDataBaseIntoCSV(){


        DataBase db = new DataBase(context);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");

        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "fisiercsv.csv");

        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase sql_db = db.getReadableDatabase();
            Cursor curCSV = sql_db.rawQuery("SELECT * FROM "+DataBase.DATABASE_TABLE,null);
            csvWrite.writeNext(curCSV.getColumnNames());

            while(curCSV.moveToNext())
            {
                //Which column you want to export you can add over here...
                String arrStr[] ={curCSV.getString(1)};
                //,curCSV.getString(1),curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),curCSV.getString(5),curCSV.getString(6),curCSV.getString(7),curCSV.getString(8),curCSV.getString(9)};
                csvWrite.writeNext(arrStr);
            }

            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("Error:", sqlEx.getMessage(), sqlEx);
        }
    }

    public String[] run() {

        String csvFile =Environment.getExternalStorageDirectory()+ "/fisiercsv.csv";
        String[] country =null;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                country = line.split(cvsSplitBy);

                return country;


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return country;
    }

}
