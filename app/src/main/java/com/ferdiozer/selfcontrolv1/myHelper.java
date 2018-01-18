package com.ferdiozer.selfcontrolv1;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class myHelper {
    //DB eklenen son id
    static int last_id;
    static  boolean screenOff;

    // Dünkü tarih
    String getLastDay()
    {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.DATE,-1);
       // c.set(Calendar.HOUR_OF_DAY, 24);

        return dateformat.format(c.getTime());
    }


    // Şuanki zaman Bilgisini Döndür
     String getNowDate()
    {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      //  c.set(Calendar.HOUR_OF_DAY, 24);

        return dateformat.format(c.getTime());
    }
    // Bu günün başlangıcı
    String getStartNowDate()
    {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        //  c.set(Calendar.HOUR_OF_DAY, 24);

        return dateformat.format(c.getTime());
    }



    // iki tarih arasındaki farkı saniye cinsinden döndür
    long diffDate(String o_date,String c_date)
    {
        //  Calendar c = Calendar.getInstance();
        long result=0;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            Date date_open = formatter.parse(o_date);
            Date date_close = formatter.parse(c_date);
            result= date_close.getTime()-date_open.getTime();

            //  c.setTime(date_open);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Zamanı SS:DD:Saniye Türüne Dönüştür
    String convertTotalTime(long dt){
        int minutes=500,seconds=500,hours=500;
        minutes = (int) ((dt / (1000*60)) % 60);

        seconds = (int) (dt / 1000) % 60 ;

        hours   = (int) ((dt / (1000*60*60)) % 24);

        return hours+":"+minutes+":"+seconds;
    }
}
