package com.ferdiozer.selfcontrolv1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<ModelUsageStats> list;
    private Activity act;
    CustomAdapter(Activity activity, List<ModelUsageStats> mlist){
       layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       list=mlist;
       act=activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       View rowView;
        ImageView iv;
        TextView txt_packet_name,txt_last_date,txt_total_time;
        Button btn;
       rowView =layoutInflater.inflate(R.layout.row_usage_stats,null);
        iv= rowView.findViewById(R.id.iv);
        txt_packet_name=rowView.findViewById(R.id.txt_packet_name);
        txt_last_date  =rowView.findViewById(R.id.txt_last_date);
        txt_total_time =rowView.findViewById(R.id.txt_total_time);
        btn=rowView.findViewById(R.id.ivv);
        final ModelUsageStats us =list.get(i);

        txt_packet_name.setText(us.getPacketName());
        iv.setImageDrawable(us.getIcon());
      //  txt_last_date.setText("Son Giriş : "+us.getLastTime());
        txt_last_date.setText(us.getAppName());
        txt_total_time.setText("Kullanım Süresi: "+us.getTotalTime());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + us.getPacketName()));
                act.startActivity(intent);

            }
        });

        return rowView;
    }
}
