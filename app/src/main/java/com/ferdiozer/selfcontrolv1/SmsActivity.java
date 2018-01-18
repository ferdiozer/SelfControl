package com.ferdiozer.selfcontrolv1;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SmsActivity extends AppCompatActivity {
    Uri uri;
    int smsCounter=0,smsIn=0,smsOut=0;
    Cursor cursor;
    Button btnSms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ListView listemiz=findViewById(R.id.listView1);
        ArrayAdapter<String> veriAdaptoru= new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, fetchInbox());
        listemiz.setAdapter(veriAdaptoru);
        btnSms=findViewById(R.id.btn_count_sms);
        btnSms.setText("Toplam sms sayısı : "+smsCounter+"\nGelen sms sayısı : "+smsIn+"\nGiden sms sayısı : "+smsOut);
    }
    public ArrayList<String> fetchInbox(){
        ArrayList<String> sms= new ArrayList<>();
        smsCounter=0;
        try{
            uri=Uri.parse("content://sms/");
            cursor=getContentResolver().query(uri,new String[]{"_id","address","date","body","type"},null,null,"date ASC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String address = cursor.getString(1);
                    String body = cursor.getString(3);
                    Date date = new Date(Long.parseLong(cursor.getString(2)));
                    String sdate=convertDate(date);
                    int z = Integer.parseInt(cursor.getString(4));
                    String typ = "";
                    if (z == 1) {
                        typ = "Gelen";
                        smsIn+=1;
                    }
                    if (z == 2) {
                        typ = "Gönderilen";
                        smsOut+=1;
                    }
                    smsCounter+=1;
                    sms.add("\nTel no => " + address + "\n\nSms => " + body + "\n\nTarih => " + sdate + "\n\nTip => " + typ+"\n");
                }
                while (cursor.moveToNext());
            }
            return sms;
        }
        catch (Exception ex){
            return null;
        }

    }
    String convertDate(Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return dateformat.format(date);
    }
}
