package com.xdjd.distribution.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xdjd.distribution.bean.SaleTypeBean;
import com.xdjd.distribution.bean.SaleTypeBean;
import com.xdjd.distribution.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/2
 *     desc   : 销售类型DAO
 *     version: 1.0
 * </pre>
 */

public class SaleTypeDao {
    private DatabaseHelper mDatabaseHelper;
    private boolean flag =true;

    public SaleTypeDao(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /**
     * 增
     *
     * @param data
     */
    public void insert(SaleTypeBean data) {
        String sql = "insert into " + DatabaseHelper.SALE_TYPE_TABLE_NAME;

        sql += "(sp_id, sp_name) values(?, ?)";

        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        sqlite.execSQL(sql, new String[]{data.getSp_code() + "",
                data.getSp_name()});
        sqlite.close();
    }

    /**
     * 添加线路
     * @param list
     */
    public boolean batchInsert(List<SaleTypeBean> list) {
        String sql = "insert into " + DatabaseHelper.SALE_TYPE_TABLE_NAME;
        sql += "(Sp_id, bl_name) values(?, ?)";

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            // 删除全部
            db.execSQL("delete from " + DatabaseHelper.SALE_TYPE_TABLE_NAME);
            for (SaleTypeBean bean : list) {
                db.execSQL(sql,
                        new Object[]{bean.getSp_code(), bean.getSp_name()});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e){
            flag =false;
        } finally {
            db.endTransaction();    //结束事务
        }
        return flag;
    }

    /**
     * 删
     *
     * @param id
     */
    public void delete(int id) {
        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        String sql = ("delete from " + DatabaseHelper.SALE_TYPE_TABLE_NAME + " where Sp_id=?");
        sqlite.execSQL(sql, new Integer[]{id});
        sqlite.close();
    }

    /**
     * 改
     *
     * @param data
     */
    public void update(SaleTypeBean data) {
        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        String sql = ("update " + DatabaseHelper.SALE_TYPE_TABLE_NAME + " set bl_name=? where Sp_id=?");
        sqlite.execSQL(sql,
                new String[]{data.getSp_code() + "", data.getSp_name()});
        sqlite.close();
    }

    /**
     * 查
     *
     * @param where
     * @return
     */
    public List<SaleTypeBean> query(String where) {
        SQLiteDatabase sqlite = mDatabaseHelper.getReadableDatabase();
        ArrayList<SaleTypeBean> data = null;
        data = new ArrayList<SaleTypeBean>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.SALE_TYPE_TABLE_NAME + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            SaleTypeBean SaleTypeBean = new SaleTypeBean();
            SaleTypeBean.setSp_code(cursor.getString(0));
            SaleTypeBean.setSp_name(cursor.getString(1));
            data.add(SaleTypeBean);
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
    public List<SaleTypeBean> query() {
        SQLiteDatabase sqlite = mDatabaseHelper.getReadableDatabase();
        ArrayList<SaleTypeBean> data = null;
        data = new ArrayList<SaleTypeBean>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.SALE_TYPE_TABLE_NAME , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            SaleTypeBean SaleTypeBean = new SaleTypeBean();
            SaleTypeBean.setSp_code(cursor.getString(0));
            SaleTypeBean.setSp_name(cursor.getString(1));
            data.add(SaleTypeBean);
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
    public void reset(List<SaleTypeBean> datas) {
        if (datas != null) {
            SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
            // 删除全部
            sqlite.execSQL("delete from " + DatabaseHelper.SALE_TYPE_TABLE_NAME);
            // 重新添加
            for (SaleTypeBean data : datas) {
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
    public void save(SaleTypeBean data) {
        List<SaleTypeBean> datas = query(" where Sp_id =" + data.getSp_code());
        if (datas != null && !datas.isEmpty()) {
            update(data);
        } else {
            insert(data);
        }
    }

    public void destroy() {
        mDatabaseHelper.close();
    }
}
