package com.example.jbt.placesfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class favorite extends AppCompatActivity {
    ListView list;
    CustomAdapter adapter;
    dbhelper hdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//CREATE THE FAVORITE ACTIVITY
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);
        hdb = new dbhelper(this);
        adapter = new CustomAdapter(this, R.layout.list_item);
        list = (ListView) findViewById(R.id.listy);
        adapter.clear();
        adapter.addAll(hdb.getAllNames());    //get all the FAVORITES objects and add it to the customer adapter
        list.setAdapter(adapter);


        if (hdb.getAllNames() == null) {//PRINT NO GPS IF THERE IS NOTHING ON THE LIST
            Toast.makeText(favorite.this, "NO GPS FOUND", Toast.LENGTH_SHORT).show();
        }


    }
}
