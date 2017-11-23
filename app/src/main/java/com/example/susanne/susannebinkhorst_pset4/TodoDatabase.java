package com.example.susanne.susannebinkhorst_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Susanne on 20-11-2017.
 */

public class TodoDatabase extends SQLiteOpenHelper {

    private static String TABLE_NAME = "todos";
    private static TodoDatabase instance;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, title TEXT, completed BOOLEAN)";
        sqLiteDatabase.execSQL(create);

        sqLiteDatabase.execSQL("insert into todos (title, completed) values ('Do Laundry', 0)");
        sqLiteDatabase.execSQL("insert into todos (title, completed) values ('Do Dishes', 0)");
        sqLiteDatabase.execSQL("insert into todos (title, completed) values ('Clean Kitchen', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private TodoDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    public static TodoDatabase getInstance(Context context){
        if (instance == null){
            instance = new TodoDatabase(context);
        }
            return instance;
    }

    public boolean insert(String title, Boolean completed) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("completed", completed);

        Log.d("TodoDatabase", "insert: adding " + title + "to " + TABLE_NAME);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else{
            return true;
        }
    }

    public Cursor selectAll(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from "+ TABLE_NAME,null);
        return c;
    }

    public void update(long id, boolean status){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Integer completed;

        if (status){
            completed = 0;
        }
        else{
            completed = 1;
        }

        String sql = "UPDATE " + TABLE_NAME + " SET completed = '" + completed + "' WHERE _id = '" + id + "';";
        sqLiteDatabase.execSQL(sql);
    }

    public void delete(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE _id = '" + id + "';" ;
        sqLiteDatabase.execSQL(sql);
    }
}

