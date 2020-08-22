package com.github.mqpearth.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.github.mqpearth.model.Entity;
import com.github.mqpearth.utils.SqlLiteDatabase;

/**
 * 首页
 *
 * @author mpqearth
 */
public class MainActivity extends AppCompatActivity {


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init(savedInstanceState);
        final Intent intent = new Intent(MainActivity.this, ConfigActivity.class);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    startActivity(intent);

                    finish();
                } catch (InterruptedException e) {
                    Log.e("thread", e.getMessage());
                }
            }
        }.start();

        Entity entity = readData(this);
        Entity.runtimeEntity = entity;
        if (entity != null) {
            intent.putExtra("host", entity.getHost());
            intent.putExtra("port", entity.getPort());
            intent.putExtra("username", entity.getUsername());
            intent.putExtra("password", entity.getPassword());
            intent.putExtra("email", entity.getEmail());
            intent.putExtra("auto", entity.getAuto().toString());
        }

    }

    @SuppressLint({"InlinedApi", "ShortAlarm"})
    private void init(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.mainColor));
        }
    }

    public static Entity readData(Context context) {
        Entity entity = null;
        SQLiteDatabase db = SqlLiteDatabase.getConnection(context);
        Cursor cursor = db.query("config",
                new String[]{"*"}, "id = ?", new String[]{"1"}, null, null, null);
        while (cursor.moveToNext()) {
            String host = cursor.getString(1);
            Integer port = cursor.getInt(2);
            String username = cursor.getString(3);
            String password = cursor.getString(4);
            String email = cursor.getString(5);
            String auto = cursor.getString(6);
            entity = new Entity(host, port, username, password, email, Boolean.parseBoolean(auto));
        }

        return entity;
    }
}
