package com.bikejoy.testdemo.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseActivity;
import com.bikejoy.testdemo.event.UpdateMessageNumEvent;
import com.bikejoy.testdemo.popup.SelectShopPopup;
import com.bikejoy.testdemo.service.DownloadService;
import com.bikejoy.utils.permissions.PermissionUtils;
import com.bikejoy.view.NoScrollViewPager;

import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity  {

    @BindView(R.id.radioGroup1)
    RadioGroup mRadioGroup1;
    @BindView(R.id.main_viewpage)
    NoScrollViewPager mMainViewpage;

    /**
     * Fragment的集合
     */
    public List<Fragment> mFragments;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.title)
    TextView mTitle;

    RelativeLayout mRlMessage;
    @BindView(R.id.tv_message_num)
    TextView mTvMessageNum;
    @BindView(R.id.main_tab_stock)
    RadioButton mMainTabStock;
    @BindView(R.id.main_tab_home)
    RadioButton mMainTabHome;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        //        mRlMessage.setVisibility(View.VISIBLE);


        //获取权限
        PermissionUtils.requstPermission(this, 10, null);
        //repertory




    }







    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String message = intent.getStringExtra("message");
        if (message != null && message.length() > 0) {
            ((RadioButton) mRadioGroup1.getChildAt(4)).setChecked(true);
        }
    }





    private long exitTime;


    public LinearLayout getMainId() {
        return mLlMain;
    }


















//    private void setBottomNavigationItem(int space, int imgLen) {
//        float contentLen = 36;
//        Class barClass = mBottomNavBar.getClass();
//        Field[] fields = barClass.getDeclaredFields();
//        for (int i = 0; i < fields.length; i++) {
//            Field field = fields[i];
//            field.setAccessible(true);
//            if (field.getName().equals("mTabContainer")) {
//                try { //反射得到 mTabContainer
//                    LinearLayout mTabContainer = (LinearLayout) field.get(mBottomNavBar);
//                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
//                        //获取到容器内的各个 Tab
//                        View view = mTabContainer.getChildAt(j);
//                        //获取到Tab内的各个显示控件
//                        // 获取到Tab内的文字控件
//                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
//                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
//                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (Math.sqrt(2) * (contentLen - imgLen - space)));
//                        //获取到Tab内的图像控件
//                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
//                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
//                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) UIUtils.dp2px(imgLen), (int) UIUtils.dp2px(imgLen));
//                        params.gravity = Gravity.CENTER;
//                        iconView.setLayoutParams(params);
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public void onEventMainThread(UpdateMessageNumEvent event) {
        if (!TextUtils.isEmpty(event.getNum()) && !"0".equals(event.getNum())) {
            mTvMessageNum.setVisibility(View.VISIBLE);
            mTvMessageNum.setText(event.getNum());
        } else {
            mTvMessageNum.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
