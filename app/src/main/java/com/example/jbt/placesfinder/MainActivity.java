package com.example.jbt.placesfinder;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

import android.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    CustomAdapter adapter;
    ProgressDialog progressDialog;
    double lat;
    double lng;
    EditText et;
    Location myLocation;
    Button btn;
    dbhelper hdb;
SharedPreferences sp;
    @Override
    public boolean onContextItemSelected(MenuItem item) {//FOR THE ITEM SELECTION ON THE MENU

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


        switch (item.getItemId()) {

            case R.id.fav://IN CASE FAVORITE CLICKED


                places p = (places) list.getItemAtPosition(info.position);//GET THE ITEM POSITION
                hdb = new dbhelper(this);
                if (!hdb.isExisfav(p.getTitle())) {
                    hdb.addName(new places(p.getImage(), p.getTitle(), p.getDetails(), p.getDistance()));//ADD THE PLACE OBJECT TO THE FAVORITE TABLE
                    Toast.makeText(MainActivity.this, "Place saved to favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Places already in favorite", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.share://SHARE THE PLACE NAME
                places pla = (places) list.getItemAtPosition(info.position);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, " " + pla.getTitle() + "");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override// TO CREATE MENU WITH LIST OPTIONS HEADER
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()) {


            case R.id.list:
                getMenuInflater().inflate(R.menu.sub_menu, menu);
                menu.setHeaderTitle("List Options:");
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//CREATE MENU FOR THE SETTING
        getMenuInflater().inflate(R.menu.menu_bar , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.dfav){//GO TO SETTING ACTIVITY

            Intent i=new Intent(MainActivity.this,setting.class);
            startActivity(i);


        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sp = getSharedPreferences("DATA", MODE_PRIVATE);//CREATE A DATA TO STORE SHAREDPREFERENCE
            hdb = new dbhelper(this);
            ImageView gps;
            list = (ListView) findViewById(R.id.list);
            registerForContextMenu(list);
            et = (EditText) findViewById(R.id.editText2);
            btn = (Button) findViewById(R.id.btn);
            gps = (ImageView) findViewById(R.id.gps);
            adapter = new CustomAdapter(this, R.layout.list_item);
            list.setOnItemClickListener(this);

            gps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        try
                        {
                            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                            myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);// GET THE CURRENT LOCATION
                        }
                        catch (SecurityException e)
                        {

                        }

                        if (myLocation != null) {
                            lat = myLocation.getLatitude();//GET THE LATITUDE OF THE CURRENT LOCATION

                            lng = myLocation.getLongitude();//GET THE LONGITUDE OF THE CURRENT LOCATION

                            //JSON API
                            new place().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=500&key=AIzaSyDA4LMKpEe3pek6XaCNADWsNveW23IH0-Q");


                        } else {

                            lat = 0.0;
                            lng = 0.0;

                            if (hdb.gethistory() != null) {
                                adapter.addAll(hdb.gethistory());
                                list.setAdapter(adapter);
                                Toast.makeText(MainActivity.this, "NO GPS FOUND!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "NO GPS FOUND!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    } catch (SecurityException e) {


                    }

                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//THE SEARCH BY TEXT BUTTON


                    if (myLocation != null) {
                        new place().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=500&keyword=" + et.getText().toString() + "&key=AIzaSyDA4LMKpEe3pek6XaCNADWsNveW23IH0-Q");

                    } else {
                       adapter.clear();

                        if (hdb.gethistory() != null) {
                            adapter.addAll(hdb.gethistory());
                            list.setAdapter(adapter);
                            Toast.makeText(MainActivity.this, "NO GPS FOUND!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "NO GPS FOUND!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });

        }




    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (getResources().getBoolean(R.bool.isTablet)) {// IN CASE OF TABLET CREATE A FRAGMENT
            places plc = (places) adapterView.getItemAtPosition(i);
            sp.edit().putFloat("lat",(float)plc.getLat()).commit();//SAVE THE LATITUDE
            sp.edit().putFloat("lng",(float)plc.getLng()).commit();//SAVE THE LONGITUDE
            sp.edit().putString("title",plc.getTitle()).commit();
            sp.edit().putString("desc",plc.getDetails()).commit();

            mapfragment fr=new mapfragment();//CREATE THE MAP FRAGMENT
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.add(R.id.maplayout,fr);
            ft.commit();


        }
        else {
            places p = (places) adapterView.getItemAtPosition(i);

            Intent in = new Intent(MainActivity.this, MapsActivity.class);
            in.putExtra("lat", p.getLat());//IN CASE ITEM ON THE LIST IS CLICKED STORE THE LATITUDE INTO LAT VARIABLE TO USE IT TN THE MAPS ACTIVITY
            in.putExtra("lng", p.getLng());//IN CASE ITEM ON THE LIST IS CLICKED STORE THE LONGITUDE INTO LNG VARIABLE TO USE IT TN THE MAPS ACTIVITY
            startActivity(in);
        }
    }

    public class place extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            float distanceInMeters = 0;
            try {

                hdb.removehistory();
                adapter.clear();

                JSONObject o = new JSONObject(s);

                JSONArray a = o.getJSONArray("results");
                //GET AND STORE THE JSON PARAMETERS INTO VARIABLES
                for (int i = 0; i < a.length(); i++) {
                    JSONObject c = a.getJSONObject(i);
                    String title = c.getString("name");
                    String photo;
                    String details = c.getString("vicinity");
                    Double lati = c.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    Double lngi = c.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    //GET AND STORE THE LOCATION LATITUDE/LONGITUDE FOR THE PLACE FROM THE JSON OBJECT
                    Location loc1 = new Location("");
                    loc1.setLatitude(lati);
                    loc1.setLongitude(lngi);
                    //GET AND STORE THE CURRENT LOCATION LATITUDE/LONGITUDE
                    Location loc2 = new Location("");
                    loc2.setLatitude(lat);
                    loc2.setLongitude(lng);

                    if (getIntent().getBooleanExtra("flag", false) == true) {
                        distanceInMeters = loc1.distanceTo(loc2) * 0.000621f;//SET THE DISTANCE IN MILES
                    } else {
                        distanceInMeters = loc1.distanceTo(loc2) / 1000;//SET THE DISTANCE IN KILOMETERS
                    }

                    try {
                        photo = c.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                        places p = new places(title, details, distanceInMeters, lati, lngi, "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photo + "&key=AIzaSyDA4LMKpEe3pek6XaCNADWsNveW23IH0-Q");
                        adapter.add(p);

                        if (!hdb.isExisthistory(title)) {

                            hdb.addhistory(p);
                        }
                    } catch (Exception e) {
                        // in case of no image reference set a static image url
                        places p = new places(title, details, distanceInMeters, lati, lngi, "https://openclipart.org/image/2400px/svg_to_png/218125/3d-Earth-Globe.png");

                        adapter.add(p);
                        if (!hdb.isExisthistory(title)) {

                            hdb.addhistory(p);

                        }
                    }

                }
                list.setAdapter(adapter);


            } catch (Exception e) {
                Log.e("whatE", e.toString());

            }
        }

//DO IN BACKGROUND METHOD TO GET AND PUT JSON INTO STRING BUILDER
        @Override
        protected String doInBackground(String... params) {
            URL url;
            StringBuilder builder = new StringBuilder();

            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while (line != null) {
                    builder.append(line + "\n");
                    line = reader.readLine();
                }
            } catch (Exception e) {
            }


            return builder.toString();
        }
    }
}