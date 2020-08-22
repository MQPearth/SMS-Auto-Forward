package com.github.mqpearth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mqpearth.utils.EmailUtil;
import com.github.mqpearth.utils.SqlLiteDatabase;
import com.github.mqpearth.model.Entity;

import java.util.HashMap;
import java.util.Map;


/**
 * @author mpqearth
 */
public class ConfigActivity extends AppCompatActivity {

    private static final Map<Integer, OnClickListener> CLICK_LISTENER_MAP =
            new HashMap<>(2);

    @SuppressLint("DefaultLocale")
    private void initListener() {
        CLICK_LISTENER_MAP.put(R.id.saveButton, l -> {
            try {
                Entity entity = readInputText();

                Entity data = MainActivity.readData(this);
                String sql = "";
                if (data == null) {
                    sql = String.format("insert into config values(1, '%s', %d, '%s', '%s', '%s', '%s')",
                            entity.getHost(), entity.getPort(), entity.getUsername(), entity.getPassword(), entity.getEmail(), entity.getAuto().toString());
                } else {
                    sql = String.format("update config set host = '%s', port = %d, username = '%s', password = '%s', email = '%s', auto = '%s' where id = 1",
                            entity.getHost(), entity.getPort(), entity.getUsername(), entity.getPassword(), entity.getEmail(), entity.getAuto().toString());
                }

                SQLiteDatabase db = SqlLiteDatabase.getConnection(this);
                db.execSQL(sql);
                Entity.runtimeEntity = entity;
                message(this, "保存成功");
            } catch (Exception e) {
                Log.e("input", "读取输入失败");
                message(this, "保存失败:" + e.getMessage());
            }
        });


        CLICK_LISTENER_MAP.put(R.id.checkButton, l -> {
            try {

                boolean result = EmailUtil.androidSendMail(readInputText(), "配置测试", "短信自动转发测试");
                if (result) {
                    message(this, "有效配置");
                } else {
                    throw new Exception();
                }

            } catch (Exception e) {
                e.printStackTrace();
                message(this, "无效配置");
            }
        });
    }


    @SuppressLint({"SdCardPath", "MissingSuperCall", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        init(savedInstanceState);

        render(getIntent());

        eventRegister();

    }

    /**
     * 事件注册
     */
    private void eventRegister() {
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(CLICK_LISTENER_MAP.get(R.id.saveButton));


        Button checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(CLICK_LISTENER_MAP.get(R.id.checkButton));
    }

    /**
     * 数据渲染
     *
     * @param intent 数据传递
     */
    private void render(Intent intent) {
        String host = intent.getStringExtra("host");
        if (host != null && !"".equals(host)) {
            int port = intent.getIntExtra("port", -1);
            String username = intent.getStringExtra("username");
            String password = intent.getStringExtra("password");
            String email = intent.getStringExtra("email");
            boolean auto = Boolean.parseBoolean(intent.getStringExtra("auto"));

            setText(R.id.textHost, host);
            if (port > 0) {
                setText(R.id.textPort, port + "");
            }
            setText(R.id.textUsername, username);
            setText(R.id.textPassword, password);
            setText(R.id.textEmail, email);
            @SuppressLint("UseSwitchCompatOrMaterialCode")
            Switch aSwitch = (Switch) findViewById(R.id.switch1);
            aSwitch.setChecked(auto);
        }
    }

    public static void message(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(message)
                .create().show();
    }

    @SuppressLint("InlinedApi")
    private void init(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.firstColor));
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 2);
        }

        initListener();
    }


    private Entity readInputText() throws Exception {
        Entity entity = new Entity();

        entity.setHost(getViewText(R.id.textHost));

        String viewText = getViewText(R.id.textPort);
        entity.setPort(Integer.parseInt("".equals(viewText) ? "-1" : viewText));

        entity.setUsername(getViewText(R.id.textUsername));

        entity.setPassword(getViewText(R.id.textPassword));

        entity.setEmail(getViewText(R.id.textEmail));

        entity.setAuto(((Switch) findViewById(R.id.switch1)).isChecked());
        return entity;
    }

    private String getViewText(Integer id) {

        View view = findViewById(id);
        if (view instanceof TextView) {
            CharSequence text = ((TextView) view).getText();
            if (text == null) {
                return "";
            }
            return text.toString();
        } else {
            return "";
        }
    }

    private void setText(Integer id, String text) {

        View view = findViewById(id);
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setText(text);
        }
    }


}