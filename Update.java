package com.firstapp.mazarinilaura.clockingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class Update extends ActionBarActivity {

  public static   EditText position;
  public static EditText salary;
    ImageButton updatePosition;
    ImageButton updateSalary;
    String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        position=(EditText)findViewById(R.id.positionupdate);
        salary=(EditText)findViewById(R.id.salaryupdate);
        updatePosition=(ImageButton)findViewById(R.id.updatePosition);
        updateSalary=(ImageButton)findViewById(R.id.updateSalary);
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final String angajatSelectat = prefs.getString("angajatSelectat", "spersamearga");
        pass = prefs.getString("parola", "fraiera");
        Toast.makeText(getApplicationContext(),"Se modifica datele angajatului:"+angajatSelectat,Toast.LENGTH_LONG).show();
        updateSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(salary.getText().toString()==null)
             {
                 Toast.makeText(getApplicationContext(), "Introduceti salariul", Toast.LENGTH_LONG).show();
             }
                else {
                 UserLog.date.updateSalary(angajatSelectat, salary.getText().toString());
                 Toast.makeText(getApplicationContext(), "Noul salariu al lui " + angajatSelectat + salary.getText().toString(), Toast.LENGTH_LONG).show();
             }
            }
        });
        updatePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position.getText().toString()==null)
                {
                    Toast.makeText(getApplicationContext(), "Introduceti departamentul", Toast.LENGTH_LONG).show();
                }
                else {
                    UserLog.date.updatePosition(angajatSelectat, position.getText().toString());
                    Toast.makeText(getApplicationContext(), "Noul departament din care face parte:" + angajatSelectat + " , este " + UserLog.date.getpoz(pass), Toast.LENGTH_LONG).show();
                }


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
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
}
