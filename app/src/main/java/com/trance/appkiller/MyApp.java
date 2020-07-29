package com.trance.appkiller;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class MyApp extends Application {
    //要申请的权限列表
    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();

    static String whiteList = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("白名单", "MyApp onCreat" );
        checkPermission();
    }

    public static String getList(){
       return whiteList;
    }

    //申请权限方法
    private void checkPermission() {
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission[i]);
            }
        }
        if (mPermissionList.isEmpty()) {
            //这里表示所有权限都授予了
            if (!Shell.SU.available()) {
                Toast.makeText(this, "No Root!", Toast.LENGTH_SHORT).show();
            } else {
                whiteList = getWhiteList();
            }
        }
    }
    private String getWhiteList(){
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/AppData/AppKiller/whiteList.txt");
            Log.d("白名单", "file= " + file);
            if (file.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line = null;
                StringBuilder totalLine = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    totalLine.append(line);
                }
                bufferedReader.close();
                whiteList = totalLine.toString();
                Log.d("白名单", "配置文件内容= " + whiteList);
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()){
                    boolean issuccess1 = parentFile.mkdirs();//targetSdkVersion 28，如果大于28无法创建
                    Log.d("白名单", "白名单文件不存在,创建白名单文件夹 issuccess= " + issuccess1+" , parentFile="+parentFile);
                }
                if (parentFile.exists()){
                    boolean issuccess = file.createNewFile();
                    Log.d("白名单", "-------白名单文件不存在,创建白名单文件 issuccess = " + issuccess);
                }
            }
        } catch (Exception e) {
            Log.d("白名单", "读取白名单出错=" + e);
        }
        return whiteList;
    }
}
