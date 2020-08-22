package com.github.mqpearth.receiver;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.github.mqpearth.activity.ConfigActivity;
import com.github.mqpearth.activity.MainActivity;
import com.github.mqpearth.model.Entity;
import com.github.mqpearth.model.Sms;
import com.github.mqpearth.utils.EmailUtil;
import com.github.mqpearth.utils.SqlLiteDatabase;

import javax.mail.MessagingException;

/**
 * SMSReceiver
 *
 * @author mpqearth
 * @date 2020/08/22 14:52
 */
public class SmsReceiver extends BroadcastReceiver {


    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        Entity entity = MainActivity.readData(context);
        if (entity != null && entity.getAuto()) {
            Sms sms = extrasData(intent);
            if (sms != null) {
                StringBuilder builder = new StringBuilder("发送方:<br />");
                builder.append(sms.getNumber()).append("<br /><hr />")
                        .append("短信内容:<br />")
                        .append(sms.getContent()).append("<br />");

                ContentResolver cr = context.getContentResolver();
                ContentValues values = new ContentValues();
                values.put("read", 1);
                values.put("address", sms.getNumber());
                //1为收 2为发
                values.put("type", String.valueOf(1));
                values.put("date", System.currentTimeMillis());
                values.put("body", sms.getContent());
                cr.insert(Uri.parse("content://sms/inbox"), values);

                EmailUtil.androidSendMail(entity, "短信转发", builder.toString());
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Sms extrasData(Intent intent) {
        Sms sms = new Sms();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //通过pdus获得接收到的所有短信消息，获取短信内容
            Object[] objects = (Object[]) bundle.get("pdus");

            //构建短信对象数组
            SmsMessage[] smsMessages = new SmsMessage[objects.length];
            for (int i = 0; i < objects.length; i++) {

                //获取单条短信内容，以pdu格式存，并生成短信对象
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) objects[i], intent.getStringExtra("format"));

                //发送方的电话号码
                String number = smsMessages[i].getDisplayOriginatingAddress();

                //获取短信的内容
                String content = smsMessages[i].getDisplayMessageBody();

                sms.setNumber(number);

                sms.setContent(content);

                return sms;
            }
        }
        return null;
    }
}
