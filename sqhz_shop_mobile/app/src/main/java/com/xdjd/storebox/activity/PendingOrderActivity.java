package com.xdjd.storebox.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.fragment.CheckPendingFragment;
import com.xdjd.storebox.fragment.PendingOrderFragment;
import com.xdjd.storebox.fragment.WaitDeliveyFragment;
import com.xdjd.storebox.fragment.WaitSendFragment;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/1/10.
 */

public class PendingOrderActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_0)
    TextView mTv0;
    @BindView(R.id.tv_1)
    TextView mTv1;
    @BindView(R.id.tv_2)
    TextView mTv2;
    @BindView(R.id.tv_3)
    TextView mTv3;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_del)
    LinearLayout mSearchDel;
    @BindView(R.id.search_ll)
    LinearLayout searchLl;
    @BindView(R.id.search_btn)
    Button searchBtn;
    @BindView(R.id.search_main_ll)
    LinearLayout mSearchMainLl;
    @BindView(R.id.first_line)
    View firstLine;


    private TextView[] tvs;
    private int mIndex;
    private int index = 0;
    private int index_search = 0;
    private MyFragmentAdapter adapter;
    private Fragment[] fragment = new Fragment[]{new PendingOrderFragment(),new CheckPendingFragment(),
                    new WaitSendFragment(),new WaitDeliveyFragment()};

    private String searchKey = " ";//搜索关键字


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);
        ButterKnife.bind(this);
        firstLine.setVisibility(View.GONE);
        initView();

    }

    private void initView() {
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("待处理订单");
        mTitleBar.setRightText("搜索");
        mTitleBar.setRightTextColor(R.color.text_212121);
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSearchMainLl.getVisibility() == View.GONE) {
                    mSearchMainLl.setVisibility(View.VISIBLE);
                    firstLine.setVisibility(View.VISIBLE);
                    mTitleBar.setRightText("取消");
                } else {
                    firstLine.setVisibility(View.GONE);
                    mSearchMainLl.setVisibility(View.GONE);
                    mTitleBar.setRightText("搜索");
                    searchKey = "";
                    switch(index_search) {
                        case 0:((PendingOrderFragment) fragment[index_search]).setData(searchKey);break;
                        case 1:((CheckPendingFragment) fragment[index_search]).setData(searchKey);break;
                        case 2:((WaitSendFragment) fragment[index_search]).setData(searchKey);break;
                        case 3:((WaitDeliveyFragment) fragment[index_search]).setData(searchKey);break;
                        default :break;
                    }
                }
            }
        });
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    mSearchDel.setVisibility(View.VISIBLE);
                } else {
                    mSearchDel.setVisibility(View.GONE);
                }
            }
        });
        tvs = new TextView[]{mTv0, mTv1, mTv2, mTv3};
        tvs[mIndex].setSelected(true);
        mIndex = getIntent().getIntExtra("index", 0);
        /*mViewPager.setAdapter(adapter = new MyFragmentAdapter(
                getSupportFragmentManager(), new Fragment[]{
                new PendingOrderFragment(),//全部待处理订单
                new CheckPendingFragment(),//待审核
                new WaitSendFragment(),//待发货
                new WaitDeliveyFragment()}));//待配送*/
        mViewPager.setAdapter(adapter = new MyFragmentAdapter(
                getSupportFragmentManager(),fragment));//待配送
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int position;

            @Override
            public void onPageSelected(int position) {
                moveAnimation(position);
                this.position = position;
                for (int i = 0; i < tvs.length; i++) {
                    if (i == position) {
                        tvs[i].setSelected(true);
                    } else {
                        tvs[i].setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tvs[mIndex].setSelected(true);
        mViewPager.setCurrentItem(mIndex);
        index_search = mIndex;
    }

    @OnClick({R.id.tv_0, R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.search_del, R.id.search_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_0:
                index = 0;
                break;
            case R.id.tv_1:
                index = 1;
                break;
            case R.id.tv_2:
                index = 2;
                break;
            case R.id.tv_3:
                index = 3;
                break;
            case R.id.search_del:
                mSearchEdit.setText("");
                break;
            case R.id.search_btn:
                if (mSearchEdit.getText().length() == 0 || "".equals(mSearchEdit.getText())) {
                    showToast("请输入搜索条件");
                } else {
                    searchKey = mSearchEdit.getText().toString();
                    Log.e("搜索内容",searchKey);
                    switch(index_search) {
                        case 0:((PendingOrderFragment) fragment[index_search]).setData(searchKey);break;
                        case 1:((CheckPendingFragment) fragment[index_search]).setData(searchKey);break;
                        case 2:((WaitSendFragment) fragment[index_search]).setData(searchKey);break;
                        case 3:((WaitDeliveyFragment) fragment[index_search]).setData(searchKey);break;
                        default :break;
                    }
                }
                break;
        }
        mViewPager.setCurrentItem(index);
        index_search = index;
    }
        private class MyFragmentAdapter extends FragmentPagerAdapter {
            private Fragment[] fragments;
        public MyFragmentAdapter(FragmentManager fm, Fragment[] frag) {

            super(fm);
            this.fragments = frag;
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return fragments[position];
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragments.length;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 4
                                * index).setDuration(300).start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 4;
        }
    }
}
