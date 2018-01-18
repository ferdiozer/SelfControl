package com.ferdiozer.selfcontrolv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final ScreenRepo sr = new ScreenRepo(context);

        boolean screenOff;


        //Ekran Kapandığında
        if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_OFF)) {

            sr.update();
            screenOff = true;
            Intent i = new Intent(context, UpdateService.class);
            i.putExtra("screen_state", screenOff);
            context.startService(i);

            //Ekran Açıldığında
        } else if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON)) {
            //Ekranı Açma zamanını DB kaydet
            myHelper.last_id =  sr.insert();
            myHelper.screenOff=true;

            screenOff = false;
           // Toast.makeText(context, "Ekran Açıldı",Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, UpdateService.class);
            i.putExtra("screen_state", screenOff);
            context.startService(i);

            ///////
            Intent inte=new Intent(context,UpdateService.class);
            inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(inte);
        }


    }
}
