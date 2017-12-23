package com.programmersk.brainpie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Accounts.db";
    private static final String TABLE_NAME = "users_data_table";

    //Table with 11 Columns
    private static final String COL_id = "ID";              //0
    private static final String COL_name = "NAME";          //1
    private static final String COL_age = "AGE";            //2
    private static final String COL_avatar = "AVATAR";      //3
    private static final String COL_dialog = "DIALOG";      //4
    private static final String COL_best_score = "BEST_SCORE";      //5
    private static final String COL_coins = "COINS";      //6
    private static final String COL_diamonds = "DIAMONDS";      //7
    private static final String COL_skips = "SKIPS";      //8

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID TEXT PRIMARY KEY, NAME TEXT, AGE TEXT, AVATAR TEXT, DIALOG TEXT, BEST_SCORE TEXT, COINS TEXT, DIAMONDS TEXT, SKIPS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return result;
    }

    public boolean InsertData(String id, String name, String age, String avatar, String show_dialog, String best_score, String coins, String diamonds, String skips){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_id, id);
        cv.put(COL_name, name);
        cv.put(COL_age, age);
        cv.put(COL_avatar, avatar);
        cv.put(COL_dialog, show_dialog);
        cv.put(COL_best_score, best_score);
        cv.put(COL_coins, coins);
        cv.put(COL_diamonds, diamonds);
        cv.put(COL_skips, skips);

        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1)
            return false;
        else
            return true;

    }

    //it takes no arguments since there is only one row to be either updated or deleted
    public boolean UpdateData(String id, String name, String age, String avatar, String show_dialog){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_id, id);
        cv.put(COL_name, name);
        cv.put(COL_age, age);
        cv.put(COL_avatar, avatar);
        cv.put(COL_dialog, show_dialog);

        int result = db.update(TABLE_NAME, cv, "ID = ?", new String[] { id } );

        //1 row affected
        if(result == 1) {
            return false;
        }
        else
            return true;
    }

    //to just update the profile_avatar
    public boolean UpdateAvatar(String id, String avatar){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_id, id);
        cv.put(COL_avatar, avatar);
        int result = db.update(TABLE_NAME, cv, "ID = ?", new String[] { id } );

        //1 row affected
        if(result == 1) {
            return false;
        }
        else
            return true;
    }

    //to just update the profile_avatar
    public boolean UpdateBestScore(String id, String best_score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_id, id);
        cv.put(COL_best_score, best_score);
        int result = db.update(TABLE_NAME, cv, "ID = ?", new String[] { id } );

        //1 row affected
        if(result == 1) {
            return false;
        }
        else
            return true;
    }

    //to just update the coins
    public boolean UpdateCoins(String id, String coins){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_id, id);
        cv.put(COL_coins, coins);
        int result = db.update(TABLE_NAME, cv, "ID = ?", new String[] { id } );

        //1 row affected
        if(result == 1) {
            return false;
        }
        else
            return true;
    }

    //to just update the diamonds
    public boolean UpdateDiamonds(String id, String diamonds){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_id, id);
        cv.put(COL_diamonds, diamonds);
        int result = db.update(TABLE_NAME, cv, "ID = ?", new String[] { id } );

        //1 row affected
        if(result == 1) {
            return false;
        }
        else
            return true;
    }

    //to just update the skips
    public boolean UpdateSkips(String id, String skips) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_id, id);
        cv.put(COL_skips, skips);
        int result = db.update(TABLE_NAME, cv, "ID = ?", new String[] { id } );

        //1 row affected
        if(result == 1) {
            return false;
        }
        else
            return true;
    }
}
