package com.firstapp.mazarinilaura.clockingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class UserLog extends ActionBarActivity implements View.OnClickListener{

    EditText password;
    ImageButton enter;
    ImageButton hr;
    ImageButton e;
    public static DataBase date;
    public static DataBaseToCSV csv;
    public DataBaseToExcel ex;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log);
        password=(EditText)findViewById(R.id.textpassword);
        enter=(ImageButton)findViewById(R.id.enter);
        hr=(ImageButton)findViewById(R.id.hr);
    //   hr.setVisibility(View.INVISIBLE);
        e=(ImageButton)findViewById(R.id.out);
        enter.setOnClickListener(this);
        date=new DataBase(this);
        date.open();
        csv=new DataBaseToCSV(date.myContext);
        csv.exportDataBaseIntoCSV();
        csv.run();


       hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(UserLog.this, HRManager.class);
                startActivity(intent);


            }
        });


       e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

      date.close();
              finish();
             System.exit(1);
            }
        });

    }




   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        //pa e ce am in casuta



        switch (v.getId())
        {
            case R.id.enter:
            {

                    String parola = password.getEditableText().toString();
                if(parola.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Introduceti parola in campul destinat",Toast.LENGTH_LONG).show();
                }
                else {
                    Bundle basket = new Bundle();
                    basket.putString("parola", parola);
                    Intent intent = new Intent(UserLog.this, VerifyFace.class);
                    intent.putExtras(basket);
                    startActivity(intent);
                    final SharedPreferences prefs = this.getSharedPreferences(
                            "com.example.app", Context.MODE_PRIVATE);
                    prefs.edit().putString("parola", parola).apply();
                }
                }

                break;
            default:

                break;


        }

    }
}
