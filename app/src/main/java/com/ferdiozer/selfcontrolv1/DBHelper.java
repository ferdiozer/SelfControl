package com.ferdiozer.selfcontrolv1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
//Temel Veritabanı işlemleri
    //version numarası
    private static final int DATABASE_VERSION = 4;

    // Database İsmi
    private static final String DATABASE_NAME = "self_control_database.db";

    DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Kolonlar ve tablo bilgisi. Temel İşmeller. TABLU OLUŞTUR

        String CREATE_TABLE_SCREEN = "CREATE TABLE " + ModelScreen.TABLE  + "("
                + ModelScreen.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + ModelScreen.KEY_open_date + " TEXT, "
                + ModelScreen.KEY_close_date + " TEXT )";

        db.execSQL(CREATE_TABLE_SCREEN);    // Ekran olayları Tablosu
        db.execSQL(TimeRuleRepo.createTable()); // Zaman Kuralı Tablosu

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eğer Tablo varsa Sil
        db.execSQL("DROP TABLE IF EXISTS " + ModelScreen.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TimeRuleRepo.TABLE);  // Zaman kuralı tablo

        // ve yeniden oluştur
        onCreate(db);

    }
}
