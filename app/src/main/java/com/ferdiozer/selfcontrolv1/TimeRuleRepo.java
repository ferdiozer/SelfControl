package com.ferdiozer.selfcontrolv1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TimeRuleRepo {

    private DBHelper dbHelper;
    private myHelper h;

    TimeRuleRepo(Context context){
        dbHelper = new DBHelper(context);
    }
    // Model için
    TimeRuleRepo(){

    }
    // Tablo ismi (label)
    static final String TABLE = "time_rule";

    // Kolon isimleri   (label)
    private static final String KEY_ID = "id";
    private static final String KEY_total_time = "total_time";
    private static final String KEY_is_notif = "is_notif";

    static String createTable() {
        return "CREATE TABLE " + TimeRuleRepo.TABLE + "("
                + TimeRuleRepo.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TimeRuleRepo.KEY_total_time + " INTEGER, "
                + TimeRuleRepo.KEY_is_notif + " INTEGER )";
    }

    //Veritabanında Veri Var Mı ?
     boolean check_value(){
        boolean result =false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                TimeRuleRepo.KEY_ID + "," +
                TimeRuleRepo.KEY_total_time + "," +
                TimeRuleRepo.KEY_is_notif +
                " FROM " + TimeRuleRepo.TABLE;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
      int  total_value_row =  cursor.getCount();
      if (total_value_row>0){
          result=true;
      }
        return result;
    }
    //ekleme
    boolean insert(int time,int notif) {

        //veri yazma işlemi için aç
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TimeRuleRepo.KEY_total_time,time);
        values.put(TimeRuleRepo.KEY_is_notif,notif);

        // Satır ekleme işlemi ve eklenen id yi geri döndür
        long insert_id = db.insert(TimeRuleRepo.TABLE, null, values);
        db.close(); // Veritabanı Bağlantısını kapat
        return insert_id > 0;
    }
    //Güncelleme
    boolean update(int id,int time,int notif) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Veritabanı Aç
        ContentValues values = new ContentValues(); //Değerler Değişkeni
        values.put(TimeRuleRepo.KEY_total_time, time); //kapanma Tarihi değişkenini ata
        values.put(TimeRuleRepo.KEY_is_notif,notif);
        int is_update = db.update(TimeRuleRepo.TABLE, values, TimeRuleRepo.KEY_ID + "="+id, null );//id eşitse güncelle
        db.close(); //Veritabanı Kapat
        return is_update > 0;
    }
    int get_last_id(){
      int last_id=0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  MAX(" +
                TimeRuleRepo.KEY_ID +
               ") FROM " + TimeRuleRepo.TABLE;
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst()){
            last_id=(int)c.getLong(0);
        }
       // c.close();
       // db.close();
        return last_id;
    }
    // id ye göre getir
    TimeRuleRepo getById(int Id){
        int _id=0,_time=0,_notif=0;
        TimeRuleRepo rule=null;
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  " +
                    TimeRuleRepo.KEY_ID + "," +
                    TimeRuleRepo.KEY_total_time +","+
                    TimeRuleRepo.KEY_is_notif +
                    " FROM " + TimeRuleRepo.TABLE
                    + " WHERE " +
                    TimeRuleRepo.KEY_ID + "=?";

            Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );


            if (cursor.moveToFirst()) {
                do {
                    _id= cursor.getInt(0);
                    _time=cursor.getInt(1);
                    _notif =cursor.getInt(2);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            //TimeRuleRepo(int id, int total_time, int is_notif)
            rule = new TimeRuleRepo(_id,_time,_notif);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return rule;
    }

    //Property
    private int id;
    private  int total_time;
    private  int is_notif;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    int getIs_notif() {
        return is_notif;
    }

    public void setIs_notif(int is_notif) {
        this.is_notif = is_notif;
    }

    private TimeRuleRepo(int id, int total_time, int is_notif) {
        this.id = id;
        this.total_time = total_time;
        this.is_notif = is_notif;
    }
}
