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


public class SecurityQuestion extends ActionBarActivity {

    ImageButton log;
    EditText answer;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);
        log=(ImageButton) findViewById(R.id.ok);
        answer=(EditText)findViewById(R.id.textQuestion);
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        pass = prefs.getString("parola", "fraiera");
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (UserLog.date.securityExist(answer.getText().toString())) {

                        if (UserLog.date.getPosition() == "HR") {

                            Intent intent = new Intent(SecurityQuestion.this, ClockInOut.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SecurityQuestion.this, HrOperation.class);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(SecurityQuestion.this, UserLog.class);
                        startActivity(intent);
                    }


            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_security_question, menu);
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
