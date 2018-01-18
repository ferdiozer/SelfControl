package com.ferdiozer.selfcontrolv1;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CallActivity extends AppCompatActivity {
    Uri uri;
    int callCounter=0,callIn=0,callOut=0,callMissed=0;
    Cursor cursor;
    Button btnCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        ListView listemiz=findViewById(R.id.listViewCall);
        ArrayAdapter<String> veriAdaptoru= new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, fetchCalllog());
        listemiz.setAdapter(veriAdaptoru);
        btnCall=findViewById(R.id.btn_count_call);
        btnCall.setText("Toplam arama sayısı : "+callCounter+"\nGelen arama sayısı : "+callIn+"\nGiden arama sayısı : "+callOut+"\nCevapsız arama sayısı : "+callMissed);
    }
    public ArrayList<String> fetchCalllog(){
        ArrayList<String> calllog= new ArrayList<>();
        try{
            uri=Uri.parse("content://call_log/calls");
            cursor=getContentResolver().query(uri,new String[]{CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.DATE,CallLog.Calls.DURATION, CallLog.Calls.TYPE,CallLog.Calls.CACHED_NAME},null,null,"Calls._ID DESC");
            if (cursor != null && cursor.moveToFirst()) {
            do{
                String no=cursor.getString(1);
                int duration=Integer.parseInt(cursor.getString(3));
                String cachedName=cursor.getString(5);
                if (cachedName==null){
                    cachedName=" ";
                }
                Date date = new Date(Long.parseLong(cursor.getString(2)));
                String sdate=convertDate(date);
                int z=Integer.parseInt(cursor.getString(4));
                String typ="";
                if(z==1){
                    typ="Gelen";
                    callIn+=1;
                }
                if(z==2){
                    typ="Giden";
                    callOut+=1;
                }
                if(z==3){
                    typ="Kaçırılan";
                    callMissed+=1;
                }
                callCounter+=1;
                calllog.add("\nAd => "+cachedName+"\n\nTel no => "+no+"\n\nSüre => "+duration+" saniye\n\nTarih => "+sdate+"\n\nTip => "+typ+"\n");
            }
            while (cursor.moveToNext());}
            return calllog;
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
