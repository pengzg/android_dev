package com.xdjd.storebox.listener;

import android.widget.RelativeLayout;

import com.xdjd.storebox.bean.GoodsBean;

/**
 * item点击事件公共回调接口
 * Created by lijipei on 2017/3/27.
 */

public interface ItemOnListener {
    public void onItem(int position);
    void editGoodsCartNumNew(int i, RelativeLayout rl, GoodsBean bean);
}
