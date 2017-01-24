package com.example.localmusicplayer.utils;

/**
 * Created by jiang rong long on 2017/1/24.
 */

public class ConstantUtils {

    public static String formatSecondTime(int millisecond) {
        if (millisecond == 0) {
            return "00:00";
        }
        millisecond = millisecond / 1000;
        int m = millisecond / 60 % 60;
        int s = millisecond % 60;
        return (m > 9 ? m : "0" + m) + ":" + (s > 9 ? s : "0" + s);
    }

    public static String getSuffix(String str) {
        int i = str.lastIndexOf('.');
        if (i != -1) {
            return str.substring(i + 1).toUpperCase();
        }
        return str;
    }

    /**
     * 格式化文件大小 Byte->MB
     * */
    public static String formatByteToMB(long l){
        float mb=l/1024f/1024f;
        return String.format("%.2f",mb);
    }
}
