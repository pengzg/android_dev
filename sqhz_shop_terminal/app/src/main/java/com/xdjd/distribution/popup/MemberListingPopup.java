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
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.steward.bean.SalesdocListBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   : 会员选择列表popup
 *     version: 1.0
 * </pre>
 */

public class MemberListingPopup extends PopupWindow {

    private View view;
    private LinearLayout ll;
    private PullToRefreshScrollView pullScroll;
    private NoScrollListView list;
    private LineAdapter adapter;

    private EditText etSearch;
    private LinearLayout llSearch;

    private ItemOnListener listener;
    private MemberSearchListener listener1;

    private List<SalesdocListBean> listName = new ArrayList<>();
    private String memberId = null;

    private Context mContext;

    private int page = 1;

    public void setData(List<SalesdocListBean> listName) {
        this.listName = listName;
        adapter.notifyDataSetChanged();
    }

    public void setId(String memberId) {
        this.memberId = memberId;
    }

    public MemberListingPopup(Context context, int height, ItemOnListener listener, final MemberSearchListener listener1) {
        super(context);
        this.listener = listener;
        this.listener1 = listener1;
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_member_listing, null);

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
            }
        });

        pullScroll = (PullToRefreshScrollView) view.findViewById(R.id.pull_scroll);
        pullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        pullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
        list = (NoScrollListView) view.findViewById(R.id.lv_no_scroll);
        adapter = new LineAdapter();
        list.setAdapter(adapter);
    }

    public interface MemberSearchListener{
        void onSearch(String searchStr);
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
                        inflate(R.layout.item_member_listing, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mTvName.setText(listName.get(i).getSu_name());

            if (memberId != null && memberId.equals(listName.get(i).getSu_id())) {
                holder.mIvCheck.setVisibility(View.VISIBLE);
                holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_blue));
            } else {
                holder.mIvCheck.setVisibility(View.GONE);
                holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_gray));
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
            @BindView(R.id.tv_name)
            TextView mTvName;
            @BindView(R.id.iv_check)
            ImageView mIvCheck;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
