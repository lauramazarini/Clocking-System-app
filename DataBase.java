package com.firstapp.mazarinilaura.clockingapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by MazariniLaura on 4/23/2015.
 */



public class DataBase {


    public static final String ID="id";
    public static final String NAME="name";
    public static final String SALARY="salary";
    public static final String POSITION="position";
    public static final String CNP="cnp";
    public static final String PASSWORD="password";
    public static final String HOURS="hours";
    public static final String CLOCKIN="clockIn";
    public static final String CLOCKOUT="clockOut";
    public static final String POZA="numepozaangajat";
    public static final String DAY="day1";
    public static final String SECURITY="security";
    public static  String DATABASE_HOURS="HOURS4";


    private static final String DATABASE_NAME="CLOCKINGAPPLICTION4";
    public static  String DATABASE_TABLE="EMPLOYEESSS";
    private static final int DATABASE_VERSION=1;

    private dbHelper myHelper;
    public Context myContext;
    public static SQLiteDatabase myDataBase;
    public  SQLiteDatabase getReadableDatabase() {
        return myDataBase;
    }
    private static class dbHelper extends SQLiteOpenHelper {
        public dbHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " ("+
                            ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "+
                            NAME + " TEXT NOT NULL , " +
                            SALARY + " TEXT NOT NULL , " +
                            POSITION + " TEXT NOT NULL , " +
                            CNP + " TEXT NOT NULL , " +
                            CLOCKIN +" DATE , " +
                            CLOCKOUT +" DATE , " +
                            HOURS + " DATE , " +
                            POZA + " TEXT NOT NULL , " +
                            PASSWORD + " TEXT NOT NULL , " +
                            SECURITY + " TEXT NOT NULL);"
            );
            db.execSQL("CREATE TABLE " + DATABASE_HOURS + " ("+
                            PASSWORD + " TEXT NOT NULL , "+
                            DAY +" DATE , " +
                            HOURS+ " TEXT);"


            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXITS " + DATABASE_TABLE);
            onCreate(db);
        }
    }
    public DataBase(Context context)
    {
        myContext=context;
    }
    public DataBase open()
    {
        myHelper=new dbHelper(myContext);
        myDataBase=myHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        myHelper.close();
    }
    public long createEmployee(String n,String s,String p, String ss,String pic,String pass, String sec)
    {
        ContentValues c=new ContentValues();
        c.put(NAME,n);
        c.put(SALARY,s);
        c.put(POSITION,p);
        c.put(CNP,ss);
        c.put(POZA,pic);
        c.put(PASSWORD,pass);
        c.put(SECURITY,sec);
        return  myDataBase.insert(DATABASE_TABLE,null,c);
    }
    public long createNewEntryHourAndDay(String pass)
    {
        ContentValues c=new ContentValues();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String d=df.format(date);
        c.put(PASSWORD,pass);
        c.put(DAY,d);
        c.put(HOURS,this.getHoursWorked(pass));
        return  myDataBase.insert(DATABASE_HOURS,null,c);

    }


    public ArrayList<String> getDataTable2(String pass)
    {
        String[] columns=new String[]{PASSWORD,DAY,HOURS};
        Cursor c=myDataBase.query(DATABASE_HOURS,columns,PASSWORD+ "="+pass,null,null,null,null);
        ArrayList<String> result=new ArrayList<>();
        int i=c.getColumnIndex(DAY);
        int j=c.getColumnIndex(HOURS);
        String n="";

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {

            result.add(c.getString(i)+" "+c.getString(j));

        }
        return  result;
    }

    //calculeaza orele fiecarui angajat in parte
    public ArrayList<String> calculateHours(String pass)
    {
        String[] columns=new String[]{PASSWORD,DAY,HOURS};
        Cursor c=myDataBase.query(DATABASE_HOURS,columns,PASSWORD+ "="+pass,null,null,null,null);
        ArrayList<String> result=new ArrayList<>();
        int i=c.getColumnIndex(DAY);
        int j=c.getColumnIndex(HOURS);
        String n="";

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {

            result.add(c.getString(j));

        }
        return  result;
    }
    public boolean passAndPicExist(String pass,String pic)
    {

        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        String n="";
        String m="";

        int pa=c.getColumnIndex(PASSWORD);
        int pi=c.getColumnIndex(POZA);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            n=c.getString(pa);
            m=c.getString(pi);
            if(n.equals(pass)&&m.equals(pic))
            {   return true;}
        }
        return false;

    }
    public boolean passExist(String pass)
    {

        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        String n="";


        int pa=c.getColumnIndex(PASSWORD);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            n=c.getString(pa);
           ;
            if(n.equals(pass))
            {   return true;}
        }
        return false;

    }
    public boolean picExist(String pic)
    {

        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD, SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        String n="";
        String m="";


        int pi=c.getColumnIndex(POZA);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {

            m=c.getString(pi);
            if(m.equals(pic))
            {   return true;}
        }
        return false;

    }
    public boolean securityExist(String sec)
    {

        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD, SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,null,null,null,null,null);

        String m="";


        int pi=c.getColumnIndex(SECURITY);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {

            m=c.getString(pi);
            if(m.equals(sec))
            {   return true;}
        }
        return false;

    }
    public String afiseazaOreLucrate(String pass)
    {

        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        String n="";
        int pa=c.getColumnIndex(NAME);
        int x=c.getColumnIndex(HOURS);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            n=c.getString(pa);
            if(n.equals(pass))
            {   return c.getString(x);}
        }
        return "";
    }
    public ArrayList<String> getNames()
    {
        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        ArrayList<String> result=new ArrayList<>();
        int i=c.getColumnIndex(NAME);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result.add(c.getString(i));
        }
        return  result;
    }
    public String getPosition()
    {
        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        String result="";
        int d=c.getColumnIndex(POSITION);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result=result+c.getString(d);
        }
        return  result;
    }

    public String getCIHour(String pass)
    {
        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,PASSWORD+ "="+pass,null,null,null,null);
        if(c!=null)
        {
            c.moveToFirst();
            String hour=c.getString(5);
            return hour;
        }
        return null;
    }
    public String getpoz(String pass)
    {
        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,PASSWORD+ "="+pass,null,null,null,null);
        if(c!=null)
        {
            c.moveToFirst();
            String hour=c.getString(3);
            return hour;
        }
        return null;
    }
    public String getCOHour(String pass)
    {
        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,PASSWORD+ "="+pass,null,null,null,null);
        if(c!=null)
        {
            c.moveToFirst();
            String hour=c.getString(6);
            return hour;
        }
        return null;
    }
    public String getHoursWorked(String pass)
    {
        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,PASSWORD+ "="+pass,null,null,null,null);
        if(c!=null)
        {
            c.moveToFirst();
            String hour=c.getString(7);
            return hour;
        }
        return null;
    }
    public void updateClockIn(String pass)
    {
        ContentValues cv=new ContentValues();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        Date date=new Date();
        String d=sdf.format(date);
        cv.put(CLOCKIN,d);
        myDataBase.update(DATABASE_TABLE,cv,PASSWORD+"="+pass,null);

    }
    public void stergeClockIn(String pass)
    {
        ContentValues cv=new ContentValues();
        cv.put(CLOCKIN,"00:00");
        myDataBase.update(DATABASE_TABLE, cv, PASSWORD + "=" + pass, null);
    }

    public String vezidacaezeroclockin(String pass)
    {
        String[] columns=new String[]{ID,NAME,SALARY,POSITION,CNP,CLOCKIN,CLOCKOUT,HOURS,POZA,PASSWORD,SECURITY};
        Cursor c=myDataBase.query(DATABASE_TABLE,columns,PASSWORD+ "="+pass,null,null,null,null);
        if(c!=null)
        {
            c.moveToFirst();
            String hour=c.getString(5);
            return hour;
        }
        return null;
    }

    public void updateClockOut(String pass)
    {
        ContentValues cv=new ContentValues();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        Date date=new Date();
        String d=sdf.format(date);
        cv.put(CLOCKOUT, d);
        myDataBase.update(DATABASE_TABLE,cv,PASSWORD+"="+pass,null);

    }


    public void updatePosition(String name,String position)
    {
        ContentValues cv=new ContentValues();
        cv.put(POSITION, position);
        myDataBase.update(DATABASE_TABLE, cv, NAME+"='"+name+ "'", null);
    }


    public void updateSalary(String name,String salary)
    {
        ContentValues cv=new ContentValues();
        cv.put(SALARY, salary);
        myDataBase.update(DATABASE_TABLE, cv, NAME+"='"+name+ "'", null);

    }

    public void updateHours(String pass) {

        ContentValues cv=new ContentValues();
        String hours="";
        int h=0;
        int ho=Integer.parseInt(this.getCOHour(pass).substring(0,2));
        int hi=Integer.parseInt(this.getCIHour(pass).substring(0,2));
        int mo=Integer.parseInt(this.getCOHour(pass).substring(3,5));
        int mi=Integer.parseInt(this.getCIHour(pass).substring(3,5));
        int i=0;
        if(hi>ho) {
            h = 24 - hi + ho;


            if (mi > mo) {
                i = 60 - mi;
                i = i + mo;
                h = ho - hi;

                if (h < 10 && i < 10) {
                    hours = "0" + h + ":" + "0" + i;
                } else if (h < 10 && i > 10) {
                    hours = "0" + h + ":" + i;
                } else if (h > 10 && i < 10) {
                    hours = h + ":" + "0" + i;
                } else {
                    hours = h + ":" + i;
                }
            }
            else

            {
                i=mo-mi;
                if (h < 10 && i < 10) {
                    hours = "0" + h + ":" + "0" + i;
                } else if (h < 10 && i > 10) {
                    hours = "0" + h + ":" + i;
                } else if (h > 10 && i < 10) {
                    hours = h + ":" + "0" + i;
                } else {
                    hours = h + ":" + i;
                }
            }
        }
        else
        {
            h = ho - hi;
            i = mo - mi;
            if (h < 10 && i < 10) {
                hours = "0" + h + ":" + "0" + i;
            } else if (h < 10 && i > 10) {
                hours = "0" + h + ":" + i;
            } else if (h > 10 && i < 10) {
                hours = h + ":" + "0" + i;
            } else {
                hours = h + ":" + i;
            }
        }

        String x="ana";
        cv.put(HOURS,hours);
        cv.put(CLOCKIN,x);
        myDataBase.update(DATABASE_TABLE,cv,PASSWORD+"="+pass,null);
    }

    public void deleteDataBase()
    {
        myContext.deleteDatabase(DATABASE_TABLE);
    }
}
