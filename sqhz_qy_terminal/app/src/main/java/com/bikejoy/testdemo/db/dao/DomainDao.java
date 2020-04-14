package com.bikejoy.testdemo.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bikejoy.testdemo.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/6/7
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DomainDao {

    DatabaseHelper mDatabaseHelper;
    SQLiteDatabase recordsDb;
    /**
     * 域名
     */
    public static final String TYPE_DOMAIN = "1";
    /**
     * ip
     */
    public static final String TYPE_IP = "2";

    public DomainDao(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }


    /**
     * 增
     *
     * @param data
     */
    public void insert(DomainBean data) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            //删除
            db.delete(DatabaseHelper.DOMAIN_IP_NAME,"domain_type=? and url=?",
                    new String[]{data.getDomain_type(),data.getUrl()});

            String sql = "insert into " + DatabaseHelper.DOMAIN_IP_NAME;
            sql += "(domain_type, url, add_time) values(?, ?, ?)";

            db.execSQL(sql, new String[]{data.getDomain_type(),
                    data.getUrl(), data.getAdd_time()});

            db.setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e) {
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * 删
     *
     * @param id
     */
    public void delete(int id) {
        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        String sql = ("delete from " + DatabaseHelper.DOMAIN_IP_NAME + " where id=?");
        sqlite.execSQL(sql, new Integer[]{id});
        sqlite.close();
    }

    public boolean isHasDomain(DomainBean data) {
        boolean isHasRecord = false;
        recordsDb = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query(mDatabaseHelper.DOMAIN_IP_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (data.getDomain_type().equals(cursor.getString(cursor.getColumnIndexOrThrow("domain_type"))) &&
                    data.getUrl().equals(cursor.getString(cursor.getColumnIndexOrThrow("url")))) {
                isHasRecord = true;
            }
        }
        //关闭数据库
        recordsDb.close();
        return isHasRecord;
    }

    //获取全部搜索记录
    public List<DomainBean> getDomainList(String type) {
        SQLiteDatabase sqlite = mDatabaseHelper.getReadableDatabase();
        ArrayList<DomainBean> data = new ArrayList<>();
        //        Cursor cursor = sqlite.rawQuery("SELECT * FROM "
        //                + DatabaseHelper.DOMAIN_IP_NAME + " ORDER BY add_time DESC", null);
        Cursor cursor = sqlite.query(DatabaseHelper.DOMAIN_IP_NAME, new String[]{"id", "domain_type", "url"},
                "domain_type=?", new String[]{type}, null, null, "add_time DESC");

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            DomainBean recordsBean = new DomainBean();
            recordsBean.setId(cursor.getInt(0));
            recordsBean.setDomain_type(cursor.getString(1));
            recordsBean.setUrl(cursor.getString(2));
            data.add(recordsBean);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        return data;
    }

    public void destroy() {
        mDatabaseHelper.close();
    }
}
