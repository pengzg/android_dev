package com.xdjd.distribution.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.EquipmentBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   : 设备选择列表popup
 *     version: 1.0
 * </pre>
 */

public class EquipmentListingPopup extends PopupWindow {

    private View view;
    private LinearLayout ll;
    private PullToRefreshScrollView pullScroll;
    private NoScrollListView list;
    private EquipmentAdapter adapter;

    private EditText etSearch;
    private LinearLayout llSearch;

    private ItemOnListener listener;
    private FacilitySearchListener listener1;

    public List<EquipmentBean> listEquipment = new ArrayList<>();

    private Context mContext;

    private int page = 1;
    private int mFlag = 0;

    private ClientBean clientBean;

    public void setClientBean(ClientBean clientBean) {
        this.clientBean = clientBean;
    }

    public void setData(List<EquipmentBean> listEquipment) {
        this.listEquipment = listEquipment;
        adapter.notifyDataSetChanged();
    }

    public EquipmentListingPopup(Context context, int height, ItemOnListener listener, final FacilitySearchListener listener1) {
        super(context);
        this.listener = listener;
        this.listener1 = listener1;
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_equipment_listing, null);

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

        ll = (LinearLayout) view.findViewById(R.id.pop_layout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        lp.height = height / 2 + UIUtils.dp2px(45);
        ll.setLayoutParams(lp);

        etSearch = (EditText) view.findViewById(R.id.et_search);
        llSearch = (LinearLayout) view.findViewById(R.id.ll_search);
        pullScroll = (PullToRefreshScrollView) view.findViewById(R.id.pull_scroll);
        list = (NoScrollListView) view.findViewById(R.id.lv_no_scroll);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener1.onSearch(etSearch.getText().toString());
                page = 1;
                mFlag = 1;
                listEquipment.clear();
                adapter.notifyDataSetInvalidated();
                getEquipmentList();
            }
        });

        initRefresh(pullScroll);
        pullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        pullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mFlag = 1;
                listEquipment.clear();
                adapter.notifyDataSetInvalidated();
                getEquipmentList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                getEquipmentList();
            }
        });

        adapter = new EquipmentAdapter();
        list.setAdapter(adapter);
    }

    public void showPopup() {
        page = 1;
        mFlag = 1;
        listEquipment.clear();
        adapter.notifyDataSetInvalidated();
        getEquipmentList();
    }

    private void getEquipmentList() {
        AsyncHttpUtil<EquipmentBean> httpUtil = new AsyncHttpUtil<>((Activity) mContext, EquipmentBean.class, new IUpdateUI<EquipmentBean>() {
            @Override
            public void updata(EquipmentBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listEquipment.addAll(jsonBean.getListData());
                        adapter.notifyDataSetInvalidated();
                    } else {
                        if (mFlag == 2) {
                            page--;
                            UIUtils.Toast(UIUtils.getString(R.string.on_pull_remind));
                        }
                    }
                } else {
                    UIUtils.Toast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                UIUtils.Toast(s.getDetail());
            }

            @Override
            public void finish() {
                pullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getEquipmentList, L_RequestParams.getEquipmentList(etSearch.getText().toString(),
                String.valueOf(page), clientBean.getCc_id()), true);
    }

    public interface FacilitySearchListener {
        void onSearch(String searchStr);
    }

    public class EquipmentAdapter extends BaseAdapter {

        public int getCount() {
            return listEquipment == null ? 0 : listEquipment.size();
        }

        @Override
        public Object getItem(int i) {
            return listEquipment.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.item_select_facility_listing, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mTvName.setText(listEquipment.get(i).getMe_num());
            holder.mTvType.setText("设备类型:"+listEquipment.get(i).getMe_type_nameref());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItem(i);
                }
            });
            return view;
        }

        class ViewHolder {
            @BindView(R.id.tv_name)
            TextView mTvName;
            @BindView(R.id.tv_type)
            TextView mTvType;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    /**
     * 下拉刷新和上拉加载的提示信息
     */
    public void initRefresh(PullToRefreshBase my_scroll) {
        ILoadingLayout startLabels = my_scroll.getLoadingLayoutProxy(true,
                false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = my_scroll.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
    }

}
