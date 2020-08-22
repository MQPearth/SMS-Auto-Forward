package com.github.mqpearth.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper
 *
 * @author mpqearth
 * @date 2020/08/21 20:52
 */
public class SqlLiteDatabase extends SQLiteOpenHelper {
    /**
     * 自定义的数据库名
     */
    private static final String DB_NAME = "AutoForwardConfig";
    /**
     * 版本号
     */
    private static final int VERSION = 1;

    public SqlLiteDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    /**
     * 该方法会自动调用，首先系统会检查该程序中是否存在数据库名为‘AutoForwardConfig’的数据库，
     * 如果存在则不会执行该方法，如果不存在则会执行该方法。
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String configTable =
                "create table config(" +
                        "id integer primary key autoincrement," +
                        "host text NOT NULL," +
                        "port integer NOT NULL," +
                        "username text NOT NULL," +
                        "password text NOT NULL," +
                        "email text NOT NULL," +
                        "auto text NOT NULL)";
        db.execSQL(configTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {


    }

    public static SQLiteDatabase getConnection(Context context) {
        SqlLiteDatabase sqlLiteDatabase = new SqlLiteDatabase(context);
        return sqlLiteDatabase.getReadableDatabase();
    }

}
