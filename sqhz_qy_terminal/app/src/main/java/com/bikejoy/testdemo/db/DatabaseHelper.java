package com.bikejoy.testdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/2
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    /** 数据库名字 */
    public static final String DATABASE_NAME = "salesman.db";
    /** 数据库版本 */
    private static final int DATABASE_VERSION = 2;

    /** url-IP域名表 */
    public static final String URL_TABLE_NAME = "url";
    public static final String CREATE_URL_TABLE = "create table "
            + URL_TABLE_NAME
            + " (id integer primary key autoincrement, domain_name varchar(50),ip varchar(20),is_select varchar(2))";

    /** 登录账号记录表 */
    public static final String ACCOUNT_RECORD_NAME = "record";
    public static final String ACCOUNT_RECORD_TABLE = "create table "
            + ACCOUNT_RECORD_NAME
            +" (id integer primary key autoincrement, account varchar(40),password varchar(40),login_time varchar(50))";


    /** 域名、ip列表 */
    public static final String DOMAIN_IP_NAME = "domain";
    public static final String DOMAIN_IP_TABLE = "create table "
            + DOMAIN_IP_NAME
            +" (id integer primary key autoincrement,domain_type varchar(2), url varchar(100),add_time varchar(50))";


    public DatabaseHelper(Context context) {
        //context :上下文   ， name：数据库文件的名称    CursorFactory：用来创建cursor对象，默认为null
        //version:数据库的版本号，从1开始，如果发生改变，onUpgrade方法将会调用,4.0之后只能升不能将
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //oncreate方法是数据库第一次创建的时候会被调用;  特别适合做表结构的初始化,需要执行sql语句；SQLiteDatabase db可以用来执行sql语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_URL_TABLE);
        db.execSQL(ACCOUNT_RECORD_TABLE);
        db.execSQL(DOMAIN_IP_TABLE);
    }

    //onUpgrade数据库版本号发生改变时才会执行； 特别适合做表结构的修改
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion){
            case 2://创建账号记录表
                db.execSQL(DOMAIN_IP_TABLE);
                break;
            default:
                break;
        }
    }

}
