package com.ferdiozer.selfcontrolv1;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;


public class UpdateService extends Service {
 //   ScreenReceiver screenReceiver;
    TimeRuleRepo rule;
    myHelper helper;
  //  Timer timer;
    @Override
    public void onCreate() {
        super.onCreate();

        rule = new TimeRuleRepo(this);
        helper = new myHelper();

        triggerReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(screenReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // screenFunc(intent);
        screenFunc();
    /*
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();
            }
        }, 0, 900000);
        */
        return START_STICKY;
    }

    public void screenFunc(){   //screenFunc(Intent intent)

        try{
            int last_id;

          //  boolean screenOn = intent.getBooleanExtra("screen_state", false);
//!screenOn
            if (myHelper.screenOff) {
                if (rule.check_value()){
                    //Kayıtlı kural id  si
                    last_id = rule.get_last_id(); //id sini getir
                    //Yeni nesne örneği oluştur (çekeceğimiz  değerleri aktarmak için

                    TimeRuleRepo rule_get=rule.getById(last_id); // DB den gelen kayıtlı kural yani bildirim

                    int _is_notif = rule_get.getIs_notif(); // Bildirim değeri

                    if (_is_notif == 1) { // Bildirim Açık ise
                        int _total_time = rule_get.getTotal_time();  //gelen zaman

                        ScreenRepo sr = new ScreenRepo(this);
                        long diff_total=0;  // Günlük Toplam Kullanım süresi (saniye)
                        List<ModelScreen> list;
                        String od,cd;
                        list = sr.getScreenListForTime();

                        //UsageStats us : stats
                        for (ModelScreen ms: list) {
                            od =  ms.getOpen_date();
                            cd = ms.getClose_date();

                            diff_total   +=   helper.diffDate(od,cd);   ///   8582000     Kural : 120

                        }
                        diff_total=diff_total/1000; // Milisaniye türünden olan değeri  Saniye ye çeviriyoruz

                        //Belirtilen Zamandan Fazla telefonu kullandıysa
                        if (diff_total>_total_time){
                            Toast.makeText(this, "Kullanım Zamanını Aştınız",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
        catch(Exception ex){
            Log.i("MSG","HATA: "+ex.getMessage());
           // Toast.makeText(this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }


    }
    //Broadcast Receiver Tetikleme(add Filter)
    private void triggerReceiver()
    {
        ScreenReceiver rc = new ScreenReceiver();
        IntentFilter lockFilter = new IntentFilter();
        lockFilter.addAction(Intent.ACTION_SCREEN_ON);
        lockFilter.addAction(Intent.ACTION_SCREEN_OFF);
       // lockFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(rc, lockFilter);
    }
}
