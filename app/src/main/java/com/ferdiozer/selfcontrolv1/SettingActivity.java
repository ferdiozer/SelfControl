package com.ferdiozer.selfcontrolv1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
EditText et_rule;
Button btn_ok;
String get_text;
Switch switch_notif;
int notif=0;
int rule_time=0;
TimeRuleRepo rule;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        rule = new TimeRuleRepo(this);
        et_rule = findViewById(R.id.et_rule);
        btn_ok = findViewById(R.id.btn_ok);
        switch_notif = findViewById(R.id.switch_notif);
       // Toast.makeText(getApplicationContext(),""+rule.get_last_id() , Toast.LENGTH_SHORT).show();
        //Kayıtlı Kural VArsa
        if (rule.check_value()){
            //Kayıtlı kural id  si
             int _last_id = rule.get_last_id();


            TimeRuleRepo rule_get = new TimeRuleRepo(); // model kullanımı için

            rule_get=rule.getById(_last_id);

            int _is_notif =rule_get.getIs_notif();
            //Bildirim Alma Açıksa
            if (_is_notif==1){
                switch_notif.setChecked(true);
                notif=1;
            }
            else{ // Bildirim kapalıysa
                switch_notif.setChecked(false);
                notif=0;
            }

            int _get_total_time=rule_get.getTotal_time()/60;//Db deki saniye cinsinden kayıtlı verileri dk cinsiye gönüştür.

            et_rule.setText(""+_get_total_time);

        }
        //Kayıtlı Kurak Yoksa
        else{

        }

        switch_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    notif =1;
                }
                else {
                    notif=0;
                }
              //  Toast.makeText(getApplicationContext(),""+notif , Toast.LENGTH_SHORT).show();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Kullanıcıdan Alınan kullanım sınırı
                get_text = et_rule.getText().toString();

                    try {
                    rule_time =  Integer.parseInt(get_text)*60;

                        if (rule.check_value()){
                            int _last_id = rule.get_last_id();
                            rule.update(_last_id,rule_time,notif);
                        }
                        else {
                            rule.insert(rule_time,notif);

                        }

                        // Toast.makeText(getApplicationContext(),"Başarılı" , Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                catch (NumberFormatException ex){
                    Toast.makeText(getApplicationContext(),"Girdiğiniz Değeri Kontrol Ediniz", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}