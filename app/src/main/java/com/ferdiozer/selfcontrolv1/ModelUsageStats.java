package com.ferdiozer.selfcontrolv1;

import android.graphics.drawable.Drawable;

public class ModelUsageStats {
    private String packetName;
    private Drawable icon;
    private String LastTime;
    private String totalTime;
    private String appName;

    public ModelUsageStats(String packetName) {
        this.packetName = packetName;
    }

    ModelUsageStats(String packetName, Drawable icon,String lastTime,String totalTime,String appName) {
        this.packetName = packetName;
        this.icon = icon;
        this.LastTime=lastTime;
        this.totalTime=totalTime;
        this.appName=appName;
    }



    String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getLastTime() {
        return LastTime;
    }

    public void setLastTime(String lastTime) {
        LastTime = lastTime;
    }

    String getPacketName() {
        return packetName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
