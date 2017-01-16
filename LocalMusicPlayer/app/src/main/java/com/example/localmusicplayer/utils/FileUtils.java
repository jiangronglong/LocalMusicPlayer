package com.example.localmusicplayer.utils;

import android.os.Environment;

import com.example.localmusicplayer.classes.Mp3Info;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiang rong long on 2017/1/13.
 */

public class FileUtils {
    private String SDCardRoot;//sd卡路径

    public FileUtils() {
        // 获得外部存储设备的目录
        SDCardRoot = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator;
    }
    //在sd卡上创建文件
    public File CreateFileInSDCard(String filename, String dir)throws Exception{

           File  file = new File(SDCardRoot + dir + File.separator + filename);
          file.createNewFile();
           return file;
    }
    //在sd卡上创建目录
    public File CreateDirInSDCard(String dir){
        File dirFile = new File(SDCardRoot + dir + File.separator);
        dirFile.mkdir();
        return dirFile;
    }
    //判断文件是否在sd卡上存在
    public boolean isFileExist(String filename, String path) {
        File file = new File(SDCardRoot + path + File.separator + filename);
        return file.exists();
    }
    //重点
    public File writeToSDCardInput(String path, String filename,InputStream inStream) {
        File file = null;
        OutputStream outStrem = null;
        try {
            CreateDirInSDCard(path); // 创建目录
            file = CreateFileInSDCard(filename, path); // 创建文件
            outStrem = new FileOutputStream(file); // 得到文件输出流
            byte[] buffer = new byte[4 * 1024]; // 定义一个缓冲区
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outStrem.write(buffer, 0, len);
            }
            outStrem.flush(); // 确保数据写入到磁盘当中
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outStrem.close(); // 关闭输出流
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

//重点
    public List<Mp3Info> getMp3Infos(String path) {
        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
        File file = new File(SDCardRoot +  File.separator + path);
        File[] files = file.listFiles();
        FileUtils fileUtils = new FileUtils();
        for(int i = 0; i < files.length; i++) {
            if(files[i].getName().endsWith("mp3")) {
                Mp3Info mp3Info = new Mp3Info();
                mp3Info.setTitle(files[i].getName());
                mp3Info.setSize(files[i].length());
                String temp[] = mp3Info.getTitle().split("\\.");
                String lrcName = temp[0] + ".lrc";
                if(fileUtils.isFileExist(lrcName, "/mp3")){
                    mp3Info.setLrcTitle(lrcName);
                }
                mp3Infos.add(mp3Info);
            }
        }
        return mp3Infos;
    }


}
