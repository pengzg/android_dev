package com.xdjd.storebox.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xdjd.storebox.bean.RecordsBean;
import com.xdjd.storebox.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RecordsDao {

    DatabaseHelper mDatabaseHelper;

    SQLiteDatabase recordsDb;

    public RecordsDao(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /**
     * 增
     *
     * @param data
     */
    public void insert(RecordsBean data) {
        String sql = "insert into " + DatabaseHelper.ACCOUNT_RECORD_NAME;

        sql += "(account, password) values(?, ?)";

        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        sqlite.execSQL(sql, new String[]{data.getAccount(),
                data.getPassword()});
        sqlite.close();
    }

    /**
     * 删
     *
     * @param id
     */
    public void delete(int id) {
        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        String sql = ("delete from " + DatabaseHelper.ACCOUNT_RECORD_NAME + " where id=?");
        sqlite.execSQL(sql, new Integer[]{id});
        sqlite.close();
    }

    //添加搜索记录
    public void addRecords(String record) {

        if (!isHasRecord(record)) {
            recordsDb = mDatabaseHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", record);
            //添加
            recordsDb.insert("records", null, values);
            //关闭
            recordsDb.close();
        }
    }

    //判断是否含有该搜索记录
    public boolean isHasRecord(String record) {
        boolean isHasRecord = false;
        recordsDb = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query(mDatabaseHelper.ACCOUNT_RECORD_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (record.equals(cursor.getString(cursor.getColumnIndexOrThrow("account")))) {
                isHasRecord = true;
            }
        }
        //关闭数据库
        recordsDb.close();
        return isHasRecord;
    }

    //获取全部搜索记录
    public List<RecordsBean> getRecordsList() {
        SQLiteDatabase sqlite = mDatabaseHelper.getReadableDatabase();
        ArrayList<RecordsBean> data = null;
        data = new ArrayList<RecordsBean>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.ACCOUNT_RECORD_NAME , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            RecordsBean recordsBean = new RecordsBean();
            recordsBean.setId(cursor.getInt(0));
            recordsBean.setAccount(cursor.getString(1));
            recordsBean.setPassword(cursor.getString(2));
            data.add(recordsBean);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        /*List<String> recordsList = new ArrayList<>();
        recordsDb = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query("records", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            recordsList.add(name);
        }
        //关闭数据库
        recordsDb.close();*/
        return data;
    }

    //模糊查询
    public List<String> querySimlarRecord(String record){
        String queryStr = "select * from records where name like '%" + record + "%' order by name ";
        List<String> similarRecords = new ArrayList<>();
        Cursor cursor= mDatabaseHelper.getReadableDatabase().rawQuery(queryStr,null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            similarRecords.add(name);
        }
        return similarRecords;
    }

    //清空搜索记录
    public void deleteAllRecords() {
        recordsDb = mDatabaseHelper.getWritableDatabase();
        recordsDb.execSQL("delete from records");

        recordsDb.close();
    }

    public void destroy() {
        mDatabaseHelper.close();
    }
}
