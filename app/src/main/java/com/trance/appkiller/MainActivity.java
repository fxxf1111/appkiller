package com.trance.appkiller;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0f);
        System.out.println("killAllApp, --------onCreate------------");
        if (!Shell.SU.available()) {
            Toast.makeText(this,"No Root!",Toast.LENGTH_SHORT).show();
        }else {
            System.out.println("killAllApp, --------onCreate killAllApp------------");
            killAllApp(this);
        }
    }


    private void killAllApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                if (packageInfo.packageName.contains("launcher")||packageInfo.packageName.contains("SMS")||packageInfo.packageName.contains("clock")||packageInfo.packageName.contains("trime")||packageInfo.packageName.contains("tencent.mm")||packageInfo.packageName.contains("appkiller")||packageInfo.packageName.contains("tts")){
                    continue;
                }
                List<String> result = eu.chainfire.libsuperuser.Shell.SU.run("am force-stop " + packageInfo.packageName.toLowerCase());
            }
        }
        Toast.makeText(this,"killAllApp finish!",Toast.LENGTH_SHORT).show();
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}