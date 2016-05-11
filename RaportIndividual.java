package com.firstapp.mazarinilaura.clockingapp;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RaportIndividual extends ExpandableListActivity {

    HashMap child;
    ArrayList<String> lista;
    String nume;
        @SuppressWarnings("unchecked")
        public void onCreate(Bundle savedInstanceState) {
            try{
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_raport_individual);

                SimpleExpandableListAdapter expListAdapter =
                        new SimpleExpandableListAdapter(
                                this,
                                createGroupList(),              // Creating group List.
                                R.layout.group_row,             // Group item layout XML.
                                new String[] { "Group Item" },  // the key of group item.
                                new int[] { R.id.row_name },    // ID of each group item.-Data under the key goes into this TextView.
                                createChildList(),              // childData describes second-level entries.
                                R.layout.child_row,             // Layout for sub-level entries(second level).
                                new String[] {"Sub Item"},      // Keys in childData maps to display.
                                new int[] { R.id.grp_child}     // Data under the keys above go into these TextViews.
                        );
                setListAdapter( expListAdapter );       // setting the adapter in the list.

            }catch(Exception e){
                System.out.println("Errrr +++ " + e.getMessage());
            }
        }

        /* Creating the Hashmap for the row */
        @SuppressWarnings("unchecked")
        private List createGroupList() {
            ArrayList result = new ArrayList();

            lista=UserLog.date.getNames();
            for( int i = 0 ; i < lista.size() ; i++ ) { // 15 groups........
                HashMap m = new HashMap();
                m.put( "Group Item","" + lista.get(i) ); // the key and it's value.
                result.add(m);
            }
            return (List)result;
        }

        /* creatin the HashMap for the children */
        @SuppressWarnings("unchecked")
        private List createChildList() {

            ArrayList result = new ArrayList();
            for( int i = 0 ; i < UserLog.date.getNames().size() ; i++ ) {
                ArrayList secList = new ArrayList();
                for( int n = 0 ; n < 2 ; n++ ) {
                     child = new HashMap();
                    if(n==0)
                    {child.put( "Sub Item", "Afiseaza ore"  );}
                    if(n==1)
                    {child.put( "Sub Item", "Modifica date angajat"  );}
                    secList.add( child );
                }
                result.add( secList );
            }
            return result;
        }
        public void  onContentChanged  () {
            System.out.println("onContentChanged");
            super.onContentChanged();
        }
        /* This function is called on each child click */
        public boolean onChildClick( ExpandableListView parent, View v, int groupPosition,int childPosition,long id) {
            System.out.println("Inside onChildClick at groupPosition = " + groupPosition +" Child clicked at position " + childPosition);
            if(childPosition==0){
            Toast.makeText(getApplicationContext(),UserLog.date.afiseazaOreLucrate(lista.get(groupPosition)),Toast.LENGTH_LONG).show();}
            else if(childPosition==1) {
                Intent nou=new Intent(RaportIndividual.this,Update.class);
                nume=lista.get(groupPosition);
                final SharedPreferences prefs = this.getSharedPreferences( "com.example.app", Context.MODE_PRIVATE);
                prefs.edit().putString("angajatSelectat", nume).apply();
                startActivity(nou);

            }
            return true;
        }

        /* This function is called on expansion of the group */
        public void  onGroupExpand  (int groupPosition) {
            try{
                System.out.println("Group exapanding Listener => groupPosition = " + groupPosition);
            }catch(Exception e){
                System.out.println(" groupPosition Errrr +++ " + e.getMessage());
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_raport_individual, menu);
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
