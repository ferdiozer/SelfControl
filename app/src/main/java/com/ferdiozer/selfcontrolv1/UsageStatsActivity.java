package com.ferdiozer.selfcontrolv1;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class UsageStatsActivity extends AppCompatActivity {

    List<ModelUsageStats>  mUsage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_stats);

        ListView listView=findViewById(R.id.lv_usageStats);

        try {   // Usage Stats İşlem Metodu
            controll();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        CustomAdapter adapter = new CustomAdapter(this,mUsage);
        listView.setAdapter(adapter);
    }

    //Kitkat için izin kontrolü
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void controll() throws PackageManager.NameNotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            kont();
        }
    }

    // USAGE STATS   // Min Api :21 (Android 5 yani lolipop için Usage Stats Yapı Fonksiyonu(kont)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void kont() throws PackageManager.NameNotFoundException {
        if (checkForPermission(UsageStatsActivity.this)) {
            UsageStatsManager usageStatsManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                usageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            long start = calendar.getTimeInMillis();
            long end = System.currentTimeMillis();
            assert usageStatsManager != null;

            //Yeni Yöntem
            long time = System.currentTimeMillis();

            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);

          //  List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end);

            for (UsageStats us : stats) {

                Drawable icon = getPackageManager().getApplicationIcon(us.getPackageName());
               // ApplicationInfo applicationInfo =getApplicationContext().getApplicationContext().getPackageManager().getApplicationInfo(us.getPackageName(),0);
               if (us.getTotalTimeInForeground()!=0){

                   PackageManager packageManager= getApplicationContext().getPackageManager();
                   String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(us.getPackageName(), PackageManager.GET_META_DATA));
                   if (appName==null || Objects.equals(appName, "")){
                       appName="Uygulama Adı";
                   }
                   mUsage.add(new ModelUsageStats(us.getPackageName(),icon, convertTime(stats.get(0).getLastTimeStamp()),convertTotalTime(us.getTotalTimeInForeground()),appName));

               }
            }

        }
        else { //uyguluma izin sayfasına yönlendir
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            }
            Toast.makeText(getApplicationContext(),"Uygulamamız İçin İzin Vermeniz Gerekiyor",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
    }

    //İzinleri kontrol et
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkForPermission(Context context) {
        try{
            AppOpsManager appOps;
            appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            assert appOps != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
            }
            return mode == MODE_ALLOWED;
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),""+ex.getMessage(),Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    // Süreyi tarih tipine dönüştür
    public String convertTime(long time){
        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat") Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
    // Süreyi Saat : Dakika : Saniye Tipine Dönüştür
    public  String convertTotalTime(long dt){
        int minutes,seconds,hours;
        String mMinutes,mSeconds,mHours;
        minutes = (int) ((dt / (1000*60)) % 60);
        if(minutes<10){
            mMinutes="0"+minutes;
        }
        else{
            mMinutes=""+minutes;
        }
        seconds = (int) (dt / 1000) % 60 ;
        if(seconds<10){
            mSeconds="0"+seconds;
        }
        else{
            mSeconds=""+seconds;
        }
        hours   = (int) ((dt / (1000*60*60)) % 24);
        if(hours<10){
            mHours="0"+hours;
        }
        else{
            mHours=""+hours;
        }
        return mHours+":"+mMinutes+":"+mSeconds;
    }
}