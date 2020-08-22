package com.github.mqpearth.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;

import com.github.mqpearth.model.Sms;
import com.github.mqpearth.receiver.SmsReceiver;

import java.util.Date;

/**
 * @author mpqearth
 */
public class SmsActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        String defaultSmsApp = null;
        String currentPn = getPackageName();
        //获取当前程序包名
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this);
            //获取手机当前设置的默认短信应用的包名}
            if (!defaultSmsApp.equals(currentPn)) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, currentPn);
                startActivity(intent);
            }

        }


    }
}