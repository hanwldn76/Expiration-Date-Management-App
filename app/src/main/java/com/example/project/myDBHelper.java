package com.example.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context){
        super(context, "infoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE infoTBL(iName CHAR(50) PRIMARY KEY, iDate CHAR(20), iCategory INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS infoTBL");
        onCreate(db);
    }
}