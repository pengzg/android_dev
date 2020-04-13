package com.xdjd.distribution.popup;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.StorehouseBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.steward.bean.SalesdocListBean;
import com.xdjd.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 仓库选择popup
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   : 选择业务员popup
 *     version: 1.0
 * </pre>
 */

public class SelectSalesmanPopup extends PopupWindow {

    private View view;
    private LinearLayout ll;
    private ListView list;
    private LineAdapter adapter;

    private EditText etSearch;
    private LinearLayout llSearch;

    TextView mTvAllTab;
    TextView mTvSalesmanTab;
    TextView mTvDistributionTab;
    View mVLine;
    RelativeLayout mLine;

    private int salesmanType = 1;

    private ItemOnListener listener;
    private SalesmanSearchListener listener1;

    private List<SalesdocListBean> listName = new ArrayList<>();
    private String salesmanId = null;

    private Context mContext;

    public void setData(List<SalesdocListBean> listName) {
        this.listName = listName;
        adapter.notifyDataSetChanged();
    }

    public void setId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public SelectSalesmanPopup(Context context, int height, ItemOnListener listener, final SalesmanSearchListener listener1) {
        super(context);
        this.listener = listener;
        this.listener1 = listener1;
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_select_salesman, null);

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

        mTvAllTab = (TextView) view.findViewById(R.id.tv_all_tab);
        mTvSalesmanTab = (TextView) view.findViewById(R.id.tv_salesman_tab);
        mTvDistributionTab = (TextView) view.findViewById(R.id.tv_distribution_tab);
        mVLine = view.findViewById(R.id.v_line);
        mLine = (RelativeLayout) view.findViewById(R.id.line);

        //计算宽度
        int width = context.getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 3;

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
                listener1.onSearch(salesmanType,etSearch.getText().toString());
            }
        });

        mTvAllTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salesmanType = 1;
                selectTab();
                listener1.onSearch(salesmanType,etSearch.getText().toString());
            }
        });

        mTvSalesmanTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salesmanType = 2;
                selectTab();
                listener1.onSearch(salesmanType,etSearch.getText().toString());
            }
        });

        mTvDistributionTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salesmanType = 3;
                selectTab();
                listener1.onSearch(salesmanType,etSearch.getText().toString());
            }
        });

        salesmanType = 1;
        selectTab();

        list = (ListView) view.findViewById(R.id.lv_salesman);
        adapter = new LineAdapter();
        list.setAdapter(adapter);
    }

    public interface SalesmanSearchListener{
        void onSearch(int salesmanType,String searchStr);
    }

    public class LineAdapter extends BaseAdapter {

        public int getCount() {
            return listName == null ? 0 : listName.size();
        }

        @Override
        public Object getItem(int i) {
            return listName.get(i);
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
                        inflate(R.layout.item_store_select, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mStorehouseTv.setText(listName.get(i).getSu_name());

            if (salesmanId != null && salesmanId.equals(listName.get(i).getSu_id())) {
                holder.mStorehouseIv.setVisibility(View.VISIBLE);
                holder.mStorehouseTv.setTextColor(UIUtils.getColor(R.color.text_blue));
            } else {
                holder.mStorehouseIv.setVisibility(View.GONE);
                holder.mStorehouseTv.setTextColor(UIUtils.getColor(R.color.text_gray));
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItem(i);
                }
            });
            return view;
        }

        class ViewHolder {
            @BindView(R.id.storehouse_tv)
            TextView mStorehouseTv;
            @BindView(R.id.storehouse_iv)
            ImageView mStorehouseIv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }



    /**
     * 切换按钮状态
     */
    private void selectTab() {
        restoreTab();
        switch (salesmanType) {
            case 1:
                mTvAllTab.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvAllTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvAllTab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                moveAnimation(0);
                alterWidth(mTvAllTab);
                break;
            case 2:
                mTvSalesmanTab.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvSalesmanTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvSalesmanTab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                moveAnimation(1);
                alterWidth(mTvSalesmanTab);
                break;
            case 3:
                mTvDistributionTab.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvDistributionTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvDistributionTab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                moveAnimation(2);
                alterWidth(mTvDistributionTab);
                break;
        }

//        list.clear();
//        adapter.notifyDataSetChanged();
//        page = 1;
//        mFlag = 1;
//        loadData();
    }

    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvAllTab.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvAllTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvAllTab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvSalesmanTab.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvSalesmanTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvSalesmanTab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvDistributionTab.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvDistributionTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvDistributionTab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    /**
     * 根据TextView的宽度修改线的宽度
     *
     * @param tv
     */
    private void alterWidth(TextView tv) {
        TextPaint paint = tv.getPaint();
        paint.setTextSize(tv.getTextSize());
        float width = paint.measureText(tv.getText().toString()); //这个方法能把文本所占宽度衡量出来.

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
        lp.width = (int) width + UIUtils.dp2px(5);
        mVLine.setLayoutParams(lp);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        mContext.getResources().getDisplayMetrics().widthPixels / 3
                                * index).setDuration(300).start();
    }

}
