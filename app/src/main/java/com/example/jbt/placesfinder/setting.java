package com.example.jbt.placesfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class setting extends AppCompatActivity {

    RadioButton  miles, kilo;
    Button btn, go;
    dbhelper hbd;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        miles = (RadioButton) findViewById(R.id.miles);

        sp = getSharedPreferences("DATA", MODE_PRIVATE);

        kilo = (RadioButton) findViewById(R.id.kilo);
        btn = (Button) findViewById(R.id.fav);
        go = (Button) findViewById(R.id.go);
        kilo.setChecked(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hbd = new dbhelper(setting.this);
                hbd.removeallplaces();//DELETE FAVORITE TABLE DATA
                Toast.makeText(setting.this, "Favorites deleted!", Toast.LENGTH_SHORT).show();

            }
        });

        miles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kilo.setChecked(false);
                Intent i = new Intent(setting.this, MainActivity.class);
                i.putExtra("flag", true);
                startActivity(i);
                sp.edit().putBoolean("miles", true).commit();//SET MILE TRUE IN THE SHARED PREFERENCE IN CASE MILES SELECTED
                sp.edit().putBoolean("kilo", false).commit();//SET KILO FALSE IN THE SHARED PREFERENCE IN CASE MILES SELECTED




            }
        });

        kilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miles.setChecked(false);
                Intent i2 = new Intent(setting.this, MainActivity.class);
                i2.putExtra("flag", false);
                startActivity(i2);
                sp.edit().putBoolean("miles", false).commit();//SET MILE FALSE IN THE SHARED PREFERENCE IN CASE MILES SELECTED
                sp.edit().putBoolean("kilo", true).commit();//SET KILO TRUE IN THE SHARED PREFERENCE IN CASE MILES SELECTED

            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//GO TO FAVORITE
                Intent i = new Intent(setting.this, favorite.class);
                startActivity(i);
            }
        });
    }


}
