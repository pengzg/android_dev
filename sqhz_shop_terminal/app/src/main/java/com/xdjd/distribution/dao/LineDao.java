package com.xdjd.distribution.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/2
 *     desc   : 线路DAO
 *     version: 1.0
 * </pre>
 */

public class LineDao {
    private DatabaseHelper mDatabaseHelper;
    private boolean flag =true;

    public LineDao(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /**
     * 增
     *
     * @param data
     */
    public void insert(LineBean data) {
        String sql = "insert into " + DatabaseHelper.LINE_TABLE_NAME;

        sql += "(bl_id, bl_name) values(?, ?)";

        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        sqlite.execSQL(sql, new String[]{data.getBl_id() + "",
                data.getBl_name()});
        sqlite.close();
    }

    /**
     * 添加线路
     * @param list
     */
    public boolean batchInsert(List<LineBean> list) {
        String sql = "insert into " + DatabaseHelper.LINE_TABLE_NAME;
        sql += "(bl_id, bl_name) values(?, ?)";

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            // 删除全部
            db.execSQL("delete from " + DatabaseHelper.LINE_TABLE_NAME);
            for (LineBean bean : list) {
                db.execSQL(sql,
                        new Object[]{bean.getBl_id(), bean.getBl_name()});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e){
            flag =false;
        } finally {
            db.endTransaction();    //结束事务
        }
        return flag;
    }

    public void deleteAllLine(){
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.execSQL("delete from " + DatabaseHelper.LINE_TABLE_NAME);
        db.close();
    }

    /**
     * 删
     *
     * @param id
     */
    public void delete(int id) {
        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        String sql = ("delete from " + DatabaseHelper.LINE_TABLE_NAME + " where bl_id=?");
        sqlite.execSQL(sql, new Integer[]{id});
        sqlite.close();
    }

    /**
     * 改
     *
     * @param data
     */
    public void update(LineBean data) {
        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        String sql = ("update " + DatabaseHelper.LINE_TABLE_NAME + " set bl_name=? where bl_id=?");
        sqlite.execSQL(sql,
                new String[]{data.getBl_id() + "", data.getBl_name()});
        sqlite.close();
    }

    /**
     * 查
     *
     * @param where
     * @return
     */
    public List<LineBean> query(String where) {
        SQLiteDatabase sqlite = mDatabaseHelper.getReadableDatabase();
        ArrayList<LineBean> data = null;
        data = new ArrayList<LineBean>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.LINE_TABLE_NAME + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            LineBean lineBean = new LineBean();
            lineBean.setBl_id(cursor.getString(0));
            lineBean.setBl_name(cursor.getString(1));
            data.add(lineBean);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }

    /**
     * 查
     * @return
     */
    public List<LineBean> query() {
        SQLiteDatabase sqlite = mDatabaseHelper.getReadableDatabase();
        ArrayList<LineBean> data = null;
        data = new ArrayList<LineBean>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.LINE_TABLE_NAME , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            LineBean lineBean = new LineBean();
            lineBean.setBl_id(cursor.getString(0));
            lineBean.setBl_name(cursor.getString(1));
            data.add(lineBean);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }

    /**
     * 重置
     *
     * @param datas
     */
    public void reset(List<LineBean> datas) {
        if (datas != null) {
            SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
            // 删除全部
            sqlite.execSQL("delete from " + DatabaseHelper.LINE_TABLE_NAME);
            // 重新添加
            for (LineBean data : datas) {
                insert(data);
            }
            sqlite.close();
        }
    }

    /**
     * 保存一条数据到本地(若已存在则直接覆盖)
     *
     * @param data
     */
    public void save(LineBean data) {
        List<LineBean> datas = query(" where bl_id =" + data.getBl_id());
        if (datas != null && !datas.isEmpty()) {
            update(data);
        } else {
            insert(data);
        }
    }

    //
    // /**
    // * 合并一条数据到本地(通过更新时间判断仅保留最新)
    // *
    // * @param data
    // * @return 数据是否被合并了
    // */
    // public boolean merge(NotebookData data) {
    // Cursor cursor = sqlite.rawQuery(
    // "select * from " + DatabaseHelper.NOTE_TABLE_NAME
    // + " where _id=" + data.getId(), null);
    // NotebookData localData = new NotebookData();
    // // 本循环其实只执行一次
    // for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
    // localData.setId(cursor.getInt(0));
    // localData.setIid(cursor.getInt(1));
    // localData.setUnixTime(cursor.getString(2));
    // localData.setDate(cursor.getString(3));
    // localData.setContent(cursor.getString(4));
    // localData.setColor(cursor.getInt(5));
    // }
    // // 是否需要合这条数据
    // boolean isMerge = localData.getUnixTime() < data.getUnixTime();
    // if (isMerge) {
    // save(data);
    // }
    // return isMerge;
    // }

    public void destroy() {
        mDatabaseHelper.close();
    }
}
