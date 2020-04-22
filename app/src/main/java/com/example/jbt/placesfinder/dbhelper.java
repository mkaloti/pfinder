package com.example.jbt.placesfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by user on 02/11/2017.
 */

public class dbhelper extends SQLiteOpenHelper {


    private static final String note_TABLE_NAME = "fav";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_ADDRESS = "details";
    private static final String COL_TYPE = "url";


    public dbhelper(Context context) {//CONSTRUCTOR
        super(context, "favoritedb", null, 1);//CREATE A DATABASE FILE CALLED FAVORITEDB
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE fav ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, details TEXT,url TEXT);");//CREATE FAVORITE TABLE
        db.execSQL("CREATE TABLE history ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, details TEXT,url TEXT);");//CREATE HISTORY TABLE


    }

    @Override   //IN CASE OF A NEW VERSION DEVELOPED
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addName(places p) { //ADD A ROW TO THE TABLE
        ContentValues values = new ContentValues();
        values.put("name", p.getTitle());           //ADD THE NAME TO THE VALUES
        values.put("details", p.getDetails());      //ADD THE ADDRESS TO THE VALUES
        values.put("url", p.getImage());

        SQLiteDatabase db = getWritableDatabase();
        db.insert("fav", null, values);   //INSERT THE VALUES TO THE FAVORITE TABLE
        db.close();
    }

    public void addhistory(places p) { //ADD A ROW TO THE HISTORY TABLE TO RETRIEVE IT WHEN THERE IS NO INTERNET
        ContentValues values = new ContentValues();
        values.put("name", p.getTitle());
        values.put("details", p.getDetails());
        values.put("url", p.getImage());

        SQLiteDatabase db = getWritableDatabase();
        db.insert("history", null, values);   //INSERT THE VALUES TO THE HISTORY TABLE
        db.close();
    }


    public boolean isExisthistory(String name) {   //A FUNCTION TO CHECK IF ROW EXIST
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM history WHERE NAME= '" + name + "'", null);  //GET THE NAME TITLE
        boolean exist = (c.getCount() > 0);      //IF THE COUNT MORE THAN 0 IT MEANS THAT THE NAME EXIST
        c.close();
        db.close();
        return exist;                  //RETURN TRUE IF EXIST

    }

    public boolean isExisfav(String name) {   //A FUNCTION TO CHECK IF ROW EXIST
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM fav WHERE NAME= '" + name + "'", null);  //GET THE NAME TITLE
        boolean exist = (c.getCount() > 0);      //IF THE COUNT MORE THAN 0 IT MEANS THAT THE NAME EXIST
        c.close();
        db.close();
        return exist;                  //RETURN TRUE IF EXIST

    }

    public void removeallplaces() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("fav", null, null);

        db.close();

    }

    public void removehistory() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("history", null, null);

        db.close();

    }

    public ArrayList<places> getAllNames() { // A FUNCTION TO RETRIEVE ALL THE FAVORITE LIST

        ArrayList<places> names = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query("fav", null, null, null, null, null, null, null);

        //Cursor running until the last item
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));                  //get ID number
            String name = c.getString(c.getColumnIndex("name"));        //get String name
            String details = c.getString(c.getColumnIndex("details"));//get String ADDRESS
            String url = c.getString(c.getColumnIndex("url"));
            names.add(new places(id, name, details, url));                     //Every loop, pull one

        }
        return names;//return all THE list

    }


    public ArrayList<places> gethistory() {// A FUNCTION TO RETRIEVE ALL THE HISTORY LIST

        ArrayList<places> names = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query("history", null, null, null, null, null, null, null);

        //Cursor running until the last item
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));                  //get ID number
            String name = c.getString(c.getColumnIndex("name"));        //get String name
            String details = c.getString(c.getColumnIndex("details"));//get String lastName
            String url = c.getString(c.getColumnIndex("url"));
            names.add(new places(id, name, details, url));                     //Every loop, pull one

        }
        return names;//return all my list

    }


}
