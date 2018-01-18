package com.ferdiozer.selfcontrolv1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_READ_SMS=1;
    private static final int REQUEST_READ_CALL=1;
    Button btn_app,btn_count,btn_setting,btn_time,btn_sms,btn_call;
    ScreenRepo sr;
    myHelper helper = new myHelper();
    int permissionCheck;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i=new Intent(MainActivity.this,UpdateService.class);
        startService(i);

        btn_app= findViewById(R.id.btn_app);
        btn_count =findViewById(R.id.btn_count);
        btn_time = findViewById(R.id.btn_time);
        btn_setting=findViewById(R.id.btn_setting);
        btn_sms=findViewById(R.id.sms_log);
        btn_call=findViewById(R.id.call_log);
        //Veritabanı Tıklanma olayları
        sr=new ScreenRepo(this);

        //Günlük Ekran Açma Sayısı
        btn_count.setText(""+sr.getScreenCountToday());

        //Uygulama Anasayfasına Yönlendirme Butonu
        btn_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UsageStatsActivity.class);
                startActivity(intent);
            }
        });

        //Ayarlar Butonu
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_sms.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        totaltimeOperation();

    }
    // Toplam kullanım süresini hesapla
    private void totaltimeOperation()
    {
        List<ModelScreen> list;
        String od,cd;
        list = sr.getScreenListForTime();
        long diff_total=0;
        //UsageStats us : stats
        for (ModelScreen ms: list) {
           od =  ms.getOpen_date();
           cd = ms.getClose_date();

        diff_total   +=  helper.diffDate(od,cd);
        }

        btn_time.setText(helper.convertTotalTime(diff_total));
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==btn_sms.getId()){
            permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_SMS);
            if(permissionCheck==0){
                Intent intent = new Intent(MainActivity.this, SmsActivity.class);
                startActivity(intent);
            }
            else{
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_SMS)) {
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_SMS},
                                REQUEST_READ_SMS);
                    }
                }
            }
        }
        if(view.getId()==btn_call.getId()){
            permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_CALL_LOG);
            if(permissionCheck==0){
                Intent intent = new Intent(MainActivity.this, CallActivity.class);
                startActivity(intent);
            }
            else{
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_CALL_LOG)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_CALL_LOG)) {
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_CALL_LOG},
                                REQUEST_READ_CALL);
                    }
                }
            }
        }
    }
}