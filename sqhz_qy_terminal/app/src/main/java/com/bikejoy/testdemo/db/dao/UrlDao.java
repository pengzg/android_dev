package com.bikejoy.testdemo.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bikejoy.utils.LogUtils;
import com.bikejoy.testdemo.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/2
 *     desc   : 域名DAO
 *     version: 1.0
 * </pre>
 */

public class UrlDao {
    private DatabaseHelper mDatabaseHelper;
    private boolean flag =true;

    public UrlDao(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /**
     * 新增url数据
     *
     * @param data
     */
    public void insert(UrlBean data) {
        String sql = "insert into " + DatabaseHelper.URL_TABLE_NAME;

        sql += " (domain_name, ip, is_select) values(?,?,?) ";
        String sqlUpdate = DatabaseHelper.URL_TABLE_NAME + " set is_select = '0' where 1=1";

        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        sqlite.beginTransaction();  //开始事务
        try {
            // 修改全部,设置为0
            sqlite.execSQL("update " + sqlUpdate);
            sqlite.execSQL(sql, new String[]{data.getDomain_name() + "",
                    data.getIp(),"1"});
            sqlite.setTransactionSuccessful();  //设置事务成功完成
            LogUtils.e("tag","成功");
        } catch (Exception e){
            LogUtils.e("tag",e.toString());
            flag =false;
        } finally {
            sqlite.endTransaction();    //结束事务
        }
    }

    /**
     * 更新URL选择
     * @param data
     */
    public void update(UrlBean data){
        SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
        String sqlUpdate = DatabaseHelper.URL_TABLE_NAME + " set is_select = '0' where 1=1";
        String sql = ("update " + DatabaseHelper.URL_TABLE_NAME + " set is_select = ? where id=?");

        sqlite.beginTransaction();  //开始事务
        try {
            // 修改全部,设置为0
            sqlite.execSQL("update " + sqlUpdate);
            sqlite.execSQL(sql, new String[]{"1",
                    data.getId()+"","1"});
            sqlite.setTransactionSuccessful();  //设置事务成功完成
            LogUtils.e("tag","成功");
        } catch (Exception e){
            LogUtils.e("tag",e.toString());
            flag =false;
        } finally {
            sqlite.endTransaction();    //结束事务
        }
    }

    /**
     * 添加域名
     * @param list
     */
    /*public boolean batchInsert(List<UrlBean> list) {
        String sql = "insert into " + DatabaseHelper.URL_TABLE_NAME;
        sql += "(domain_name, ip,is_select) values(?, ?,?)";

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            // 删除全部
            db.execSQL("delete from " + DatabaseHelper.URL_TABLE_NAME);
            for (UrlBean bean : list) {
                db.execSQL(sql,
                        new Object[]{bean.getDomain_name(), bean.getIp(),"0"});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e){
            flag =false;
        } finally {
            db.endTransaction();    //结束事务
        }
        return flag;
    }*/

    /**
     * 查默认的url
     *
     * @return
     */
    public UrlBean queryBean() {
        SQLiteDatabase sqlite = mDatabaseHelper.getReadableDatabase();
        UrlBean data = null;
        data = new UrlBean();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.URL_TABLE_NAME + " where id = '1'", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            UrlBean UrlBean = new UrlBean();

            UrlBean.setDomain_name(cursor.getString(1));
            UrlBean.setIp(cursor.getString(2));
            UrlBean.setIs_select(cursor.getString(3));
            data = UrlBean;
            break;
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }

    /**
     * 查所有url地址列表
     * @return
     */
    public List<UrlBean> queryList() {
        SQLiteDatabase sqlite = mDatabaseHelper.getReadableDatabase();
        ArrayList<UrlBean> data = null;
        data = new ArrayList<UrlBean>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.URL_TABLE_NAME , null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            UrlBean UrlBean = new UrlBean();
            UrlBean.setDomain_name(cursor.getString(1));
            UrlBean.setIp(cursor.getString(2));
            UrlBean.setIs_select(cursor.getString(3));
            data.add(UrlBean);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }

    /**
     * 删
     */
    public void delete(){
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            // 删除全部
            db.execSQL("delete from " + DatabaseHelper.URL_TABLE_NAME);
            db.setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e){
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * 重置
     *
     * @param datas
     */
    public void reset(List<UrlBean> datas) {
        if (datas != null) {
            SQLiteDatabase sqlite = mDatabaseHelper.getWritableDatabase();
            // 删除全部
            sqlite.execSQL("delete from " + DatabaseHelper.URL_TABLE_NAME);
            // 重新添加
            for (UrlBean data : datas) {
                insert(data);
            }
            sqlite.close();
        }
    }

    public void destroy() {
        mDatabaseHelper.close();
    }
}
