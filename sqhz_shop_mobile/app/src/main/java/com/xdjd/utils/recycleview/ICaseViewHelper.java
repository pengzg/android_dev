package com.xdjd.utils.recycleview;

import android.content.Context;
import android.view.View;

/**
 * 作者： corer   时间： 2015/4/23.
 * 功能：切换页面的接口
 * 修改：
 */
public interface ICaseViewHelper {

    /**
     * <p>获取上下文</p>
     * @return
     */
    public abstract Context getContext();

    /**
     * <p>获取显示数据的View</p>
     * @return
     */
    public abstract View getDataView();
    /**
     * <p>获取当前正在显示的View</p>
     * @return
     */
    public abstract View getCurrentView();
    /**
     * <p>切换View</p>
     * @param view 需要显示的View
     */
    public abstract void showCaseLayout(View view);
    /**
     * <p>切换View</p>
     * @param layoutId 需要显示布局id
     */
    public abstract void showCaseLayout(int layoutId);
    /**
     * <p>恢复显示数据的View</p>
     */
    public abstract void restoreLayout();

    /**
     * <p>实例化布局</p>
     * @param layoutId 需要实例化的布局id
     * @return
     */
    public abstract View inflate(int layoutId);
}
