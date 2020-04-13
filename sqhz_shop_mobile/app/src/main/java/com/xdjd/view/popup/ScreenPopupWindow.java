package com.xdjd.view.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.ScreenBrandAdapter;
import com.xdjd.storebox.adapter.ScreenCategoryAdapter;
import com.xdjd.storebox.adapter.ScreenPriceAdapter;
import com.xdjd.storebox.bean.ScreenBean;
import com.xdjd.view.NoScrollGridView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 筛选popup
 * Created by lijipei on 2016/12/9.
 */

public class ScreenPopupWindow extends PopupWindow {

    private View view;

    private Context context;

    private LinearLayout brand_ll,price_ll,category_ll;
    private NoScrollGridView gridBrand,gridPrice,gridCategory;
    private TextView formatting_tv,sure_tv;

    /**
     * 品牌
     */
    List<ScreenBean> screenBrandList;
    /**
     * 价格
     */
    List<ScreenBean> screenPriceList;
    /**
     * 筛选
     */
    List<ScreenBean> screenCategoryList;

    /**
     * 记录商品二级分类id
     */
    private String sxCategoryId ="";
    /**
     * 品牌适配器
     */
    private ScreenBrandAdapter adapterBrand = new ScreenBrandAdapter();
    /**
     * 价格筛选adapter
     */
    private ScreenPriceAdapter adapterPrice = new ScreenPriceAdapter();
    /**
     * 三级分类adapter
     */
    private ScreenCategoryAdapter adapterCategory = new ScreenCategoryAdapter();

    private String strBrandId;//	品牌	N	多个用英文逗号分隔   4,5,6
    private String strPriceId;//	价格	N
    private String strCategoryId;//	三级分类 	N	多个用英文逗号分隔   4,5,6

    ScreenPopupLisenter listener;


    public ScreenPopupWindow(Context context,ScreenPopupLisenter listener) {
        super(context);
        this.context = context;
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = LayoutInflater.from(context).
                inflate(R.layout.pw_screen_layout, null);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 实例化一个ColorDrawable颜色为半透明
        //        ColorDrawable dw = new ColorDrawable(0x30000000);
        this.setBackgroundDrawable(new BitmapDrawable());

        AutoUtils.auto(view);
        //        LinearLayout linearLayout = (LinearLayout) mViewPwScreen.
        //                findViewById(R.id.screen_pw_ll);
        //        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
        //                linearLayout.getLayoutParams();
        //        //设置显示的宽度为屏幕的3/5
        //        layoutParams.width = (metric.widthPixels / 5) * 3;
        //        layoutParams.height = metric.heightPixels - purchaseFragment.params.height;
        //        linearLayout.setLayoutParams(layoutParams);

        this.setContentView(view);
        //mLvLeft = (ListView) mViewPwScreen.findViewById(R.id.paixu_listview);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    translateAnimOut(view, ScreenPopupWindow.this);
                    return;
                }
            }
        });

        brand_ll = (LinearLayout) view.findViewById(R.id.brand_ll);
        price_ll = (LinearLayout) view.findViewById(R.id.price_ll);
        category_ll = (LinearLayout) view.findViewById(R.id.category_ll);

        gridBrand = (NoScrollGridView) view.findViewById(R.id.brand_gridview);
        gridPrice = (NoScrollGridView) view.findViewById(R.id.price_gridview);
        gridCategory = (NoScrollGridView) view.findViewById(R.id.category_gridview);

        gridBrand.setAdapter(adapterBrand);
        gridPrice.setAdapter(adapterPrice);
        gridCategory.setAdapter(adapterCategory);

        formatting_tv = (TextView) view.findViewById(R.id.formatting_tv);
        sure_tv = (TextView) view.findViewById(R.id.sure_tv);

        gridPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (screenPriceList.get(i).getType() == 0){
                    screenPriceList.get(i).setType(1);
                }else{
                    screenPriceList.get(i).setType(0);
                }

                for (int k=0;k<screenPriceList.size();k++){
                    if (k==i){
                        continue;
                    }
                    screenPriceList.get(k).setType(0);
                }
                adapterPrice.notifyDataSetChanged();
            }
        });

        formatting_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i= 0;i<screenBrandList.size();i++){
                    screenBrandList.get(i).setType(0);
                }
                for (int i= 0;i<screenPriceList.size();i++){
                    screenPriceList.get(i).setType(0);
                }
                for (int i= 0;i<screenCategoryList.size();i++){
                    screenCategoryList.get(i).setType(0);
                }

                adapterBrand.notifyDataSetChanged();
                adapterPrice.notifyDataSetChanged();
                adapterCategory.notifyDataSetChanged();
            }
        });

        sure_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jointSxStr();
            }
        });
    }

    /**
     * PopupWindow显示的动画
     */
    private void translateAnimIn(View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim_in);
        view.startAnimation(animation);
    }

    /**
     * PopupWindow消失的动画
     */
    private void translateAnimOut(View view, final PopupWindow pw) {
        //setTab(PublicFinal.ALLDEFAULT);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim_out);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jointSxStr();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 显示筛选popup
     */
    public void showPwScreen(View v,List<ScreenBean> scBList,List<ScreenBean> scPList,
                              List<ScreenBean> scCList) {
        this.screenBrandList = scBList;
        this.screenPriceList = scPList;
        this.screenCategoryList = scCList;

        if (null == screenBrandList || screenBrandList.size()==0){
            brand_ll.setVisibility(View.GONE);
        }else{
            brand_ll.setVisibility(View.VISIBLE);
            adapterBrand.setData(screenBrandList);
        }
        if (null == screenPriceList || screenPriceList.size()==0){
            price_ll.setVisibility(View.GONE);
        }else{
            price_ll.setVisibility(View.VISIBLE);
            adapterPrice.setData(screenPriceList);
        }
        if (null == screenCategoryList || screenCategoryList.size()==0){
            category_ll.setVisibility(View.GONE);
        }else{
            category_ll.setVisibility(View.VISIBLE);
            adapterCategory.setData(screenCategoryList);
        }

        this.showAtLocation(v,
                Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
        translateAnimIn(view);
    }

    /**
     * 拼接筛选参数
     */
    private void jointSxStr(){
        StringBuilder bIds = new StringBuilder();
        for (int i = 0; i < screenBrandList.size(); i++){
            if (screenBrandList.get(i).getType() == 1) {
                bIds.append(screenBrandList.get(i).getBrandId()
                        + ",");
            }
        }
        if (bIds.length() > 0) {
            bIds.deleteCharAt(bIds.length() - 1);
        }

        StringBuilder pIds = new StringBuilder();
        for (int i = 0; i < screenPriceList.size(); i++) {
            if (screenPriceList.get(i).getType() == 1) {
                pIds.append(screenPriceList.get(i).getBd_code()
                        + ",");
            }
        }
        if (pIds.length() > 0) {
            pIds.deleteCharAt(pIds.length() - 1);
        }

        StringBuilder cIds = new StringBuilder();
        for (int i = 0; i < screenCategoryList.size(); i++) {
            if (screenCategoryList.get(i).getType() == 1) {
                cIds.append(screenCategoryList.get(i).getGcId()
                        + ",");
            }
        }
        if (cIds.length() > 0) {
            cIds.deleteCharAt(cIds.length() - 1);
        }

        strBrandId = bIds.toString();
        strPriceId = pIds.toString();
        strCategoryId = cIds.toString();

        listener.screenType(strBrandId,strPriceId,strCategoryId);
        dismiss();
    }

    /**
     * 筛选选择回调监听
     */
    public interface ScreenPopupLisenter{
        /**
         *
         * @param strBrandId 品牌
         * @param strPriceId 价格
         * @param strCategoryId 分类
         */
        void screenType(String strBrandId,String strPriceId,String strCategoryId);
    }
}
