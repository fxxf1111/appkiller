package com.trance.appkiller;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
            Toast.makeText(this, "No Root!", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("killAllApp, --------onCreate killAllApp------------");
            killAllApp(MainActivity.this);
        }
    }

    private void killAllApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        Pattern pattern = null;
        String list = MyApp.getList();
        if (list != null && list.length() > 0) {
            pattern = Pattern.compile(list);
        } else {
            Log.d("白名单", "白名单为空");
        }
        for (PackageInfo packageInfo : packages) {
            // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                if (pattern != null && pattern.matcher(packageInfo.packageName).find()) {
                    Log.d("白名单", "白名单内 跳过");
                    continue;
                }
                eu.chainfire.libsuperuser.Shell.SU.run("am force-stop " + packageInfo.packageName.toLowerCase());
            }
        }

        Toast.makeText(this, "killAllApp finish!", Toast.LENGTH_SHORT).show();
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}