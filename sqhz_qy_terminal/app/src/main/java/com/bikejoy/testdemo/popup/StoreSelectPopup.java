package com.bikejoy.testdemo.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.view.EaseTitleBar;
import com.bikejoy.testdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freestyle_hong on 2017/9/22.
 * 查询选择popup
 */

public class StoreSelectPopup extends PopupWindow implements StorehouseListAdapter.StorehouseListListener{
    private EaseTitleBar mTitleBar;
    private Context context;
    private OnStoreSearchListener listener;
    private View view;

    private ListView lvList;

    private List<StoreListBean> list;
    private StorehouseListAdapter adapter;

    private int page = 1;
    private int mFlag = 0;

    private PopupWindow pw;

    public void showPopup(View view, PopupWindow pw) {
        this.pw = pw;
        queryStoreList();
    }

    public StoreSelectPopup(final Context context, final OnStoreSearchListener listener) {
        this.context = context;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pw_storer_select_list, null);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x50000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        this.setTouchable(true);
        this.setOutsideTouchable(true);

        list = new ArrayList<>();

        mTitleBar = view.findViewById(R.id.title_bar);
        lvList = (ListView) view.findViewById(R.id.lv_list);

        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTitleBar.setTitle("请选择仓库");

        adapter = new StorehouseListAdapter(this);
        lvList.setAdapter(adapter);
    }

    @Override
    public void onItemStore(int i) {
        listener.onItem(list.get(i));
    }

    public interface OnStoreSearchListener {
        void onItem(StoreListBean bean);
    }

    /**
     * 获取仓库列表
     */
    private void queryStoreList() {
        AsyncHttpUtil<StoreListBean> httpUtil = new AsyncHttpUtil<>((Activity) context, StoreListBean.class, new IUpdateUI<StoreListBean>() {
            @Override
            public void updata(StoreListBean bean) {
                if ("200".equals(bean.getCode())) {
                    if (null != bean.getData() && bean.getData().size() > 0) {
                        list = bean.getData();
                        adapter.setData(list);
                        pw.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                    } else {
                        UIUtils.Toast("没有柜子位置信息");
                    }
                } else {
                    UIUtils.Toast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                UIUtils.Toast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.queryMapStoreList, L_RequestParams.queryMapStoreList("1"), true);
    }

}
