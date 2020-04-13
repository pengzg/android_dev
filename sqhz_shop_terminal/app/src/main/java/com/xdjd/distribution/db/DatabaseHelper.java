package com.xdjd.distribution.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xdjd.utils.LogUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.view.calendarview.StringUtil;

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
    public static final String DATABASE_NAME = "distribution.db";

    private static final int DATABASE_VERSION = 3;

    /** 线路表 */
    public static final String LINE_TABLE_NAME = "line";
    public static final String CREATE_LINE_TABLE = "create table "
            + LINE_TABLE_NAME
            + " (bl_id varchar(20) primary key, bl_name varchar(50))";

    /** 销售类型表 */
    public static final String SALE_TYPE_TABLE_NAME = "sale_type";
    public static final String CREATE_SALE_TYPE_TABLE = "create table "
            + SALE_TYPE_TABLE_NAME
            + " (sp_id varchar(20) primary key, sp_name varchar(50))";

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

    public DatabaseHelper(Context context) {
        //context :上下文   ， name：数据库文件的名称    CursorFactory：用来创建cursor对象，默认为null
        //version:数据库的版本号，从1开始，如果发生改变，onUpgrade方法将会调用,4.0之后只能升不能将
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //oncreate方法是数据库第一次创建的时候会被调用;  特别适合做表结构的初始化,需要执行sql语句；SQLiteDatabase db可以用来执行sql语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LINE_TABLE);
        db.execSQL(CREATE_SALE_TYPE_TABLE);
        db.execSQL(CREATE_URL_TABLE);
        if (DATABASE_VERSION >= 2){//如果是直接安装的,则在创建包结构中添加账号记录表
            db.execSQL(ACCOUNT_RECORD_TABLE);
        }
    }

    //onUpgrade数据库版本号发生改变时才会执行； 特别适合做表结构的修改
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion){
            case 2://创建账号记录表
                db.execSQL(ACCOUNT_RECORD_TABLE);
                break;
            case 3://登录账号记录中添加登录时间字段
                db.execSQL("ALTER TABLE "+ACCOUNT_RECORD_NAME+" ADD  login_time varchar(50)");
                break;
            default:
                break;
        }
    }

}
