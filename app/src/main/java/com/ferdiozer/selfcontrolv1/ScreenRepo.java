package com.ferdiozer.selfcontrolv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScreenRepo {
    private DBHelper dbHelper;
    private myHelper h;

    ScreenRepo(Context context){
        dbHelper = new DBHelper(context);
        h=new myHelper();
    }

    //Güncelleme
    void update() {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Veritabanı Aç
        ContentValues values = new ContentValues(); //Değerler Değişkeni
        values.put(ModelScreen.KEY_close_date, h.getNowDate()); //kapanma Tarihi değişkenini ata
        db.update(ModelScreen.TABLE, values, ModelScreen.KEY_ID + "="+myHelper.last_id, null );//id eşitse güncelle
        db.close(); //Veritabanı Kapat
    }


    //ekleme
    int insert() {

        //veri yazma işlemi için aç
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ModelScreen.KEY_open_date,h.getNowDate());
        values.put(ModelScreen.KEY_close_date,h.getNowDate());

        // Satır ekleme işlemi ve eklenen id yi geri döndür
        long screen_Id = db.insert(ModelScreen.TABLE, null, values);
        myHelper.last_id=(int) screen_Id;
        db.close(); // Veritabanı Bağlantısını kapat
        return (int) screen_Id;
    }
//test
    public ArrayList<HashMap<String, String>> getScreenList() {
        //Aç
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                ModelScreen.KEY_ID + "," +
                ModelScreen.KEY_open_date + "," +
                ModelScreen.KEY_close_date +
                " FROM " + ModelScreen.TABLE;

        //ModelScreen ms = new ModelScreen();
        ArrayList<HashMap<String, String>> sList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Tüm satırları dönüp listeye ekle

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> screen = new HashMap<String, String>();
                screen.put("id", cursor.getString(cursor.getColumnIndex(ModelScreen.KEY_ID)));
                screen.put("open_date", cursor.getString(cursor.getColumnIndex(ModelScreen.KEY_open_date)));
                screen.put("close_date", cursor.getString(cursor.getColumnIndex(ModelScreen.KEY_close_date)));
                sList.add(screen);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sList;

    }

    // id ye göre getir  TEST
    String getScreenById(int Id){
        try{
            StringBuilder result;
            result = new StringBuilder();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  " +
                    ModelScreen.KEY_open_date + "," +
                    ModelScreen.KEY_close_date + ""+
                    " FROM " + ModelScreen.TABLE
                    + " WHERE " +
                    ModelScreen.KEY_ID + "=?";

            Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

            if (cursor.moveToFirst()) {


                int id;
                do {
                    String o = cursor.getString(cursor.getColumnIndex(ModelScreen.KEY_open_date));
                    String c = cursor.getString(cursor.getColumnIndex(ModelScreen.KEY_close_date));

                    result.append(o).append(" ").append(c).append(" ");
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return result.toString();
        }
        catch (Exception ex){
            return ex.getMessage();
        }

    }



    // Bugün toplam tıklanma saısı
    int getScreenCountToday(){
        int total=0;
            myHelper h = new myHelper();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  " +
                    ModelScreen.KEY_open_date + "," +
                    ModelScreen.KEY_close_date + ""+
                    " FROM " + ModelScreen.TABLE
                    + " WHERE " +
                    ModelScreen.KEY_open_date + ">=?";
            Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(h.getStartNowDate()) } );
           total =  cursor.getCount();
            cursor.close();
            db.close();
            return total;

        }

    // Bugünün tüm Listesi
    List<ModelScreen> getScreenListForTime(){
        List<ModelScreen> list = new ArrayList<ModelScreen>();
        try{

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  " +
                    ModelScreen.KEY_open_date + "," +
                    ModelScreen.KEY_close_date + ""+
                    " FROM " + ModelScreen.TABLE
                    + " WHERE " +
                    ModelScreen.KEY_open_date + ">=?";

            Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(h.getStartNowDate()) } );

            if (cursor.moveToFirst()) {

                int id;
                String open_date,close_date;
                do {
                    //id = cursor.getInt(cursor.getColumnIndex(ModelScreen.KEY_ID));
                    open_date = cursor.getString(cursor.getColumnIndex(ModelScreen.KEY_open_date));
                    close_date = cursor.getString(cursor.getColumnIndex(ModelScreen.KEY_close_date));

                    list.add(new ModelScreen(0,open_date,close_date));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return list;

        }
        catch (Exception ex){ // Hata
            return  null;
        }

    }

}
