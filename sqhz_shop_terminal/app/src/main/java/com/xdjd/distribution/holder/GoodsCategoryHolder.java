package com.xdjd.distribution.holder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.GoodsCategotyAdapter;
import com.xdjd.distribution.base.BaseHolder;
import com.xdjd.distribution.base.GoodsCategoryBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsCategoryHolder extends BaseHolder implements ExpandableListView.OnGroupExpandListener, ExpandableListView.OnChildClickListener{

    @BindView(R.id.elv_goods_category)
    AnimatedExpandableListView mElvGoodsCategory;

    private Context mContext;

    private ClientBean clientBean;
    private UserBean userBean;

    /**
     * 商品类别adapter
     */
    private GoodsCategotyAdapter adapterGoodsCategory;

    /**
     * 一二级分类集合
     */
    private List<GoodsCategoryBean> listCategory = new ArrayList<>();

    /**
     * 订单状态
     */
    private int indexOrder;

    /**
     * 仓库id
     */
    private String storehouseId;

    public GoodsCategoryHolder(Context mContext) {
        this.mContext = mContext;

        clientBean = UserInfoUtils.getClientInfo(mContext);
        userBean = UserInfoUtils.getUser(mContext);

        adapterGoodsCategory = new GoodsCategotyAdapter();
        mElvGoodsCategory.setAdapter(adapterGoodsCategory);
        mElvGoodsCategory.setOnGroupExpandListener(this);
        mElvGoodsCategory.setOnChildClickListener(this);

        mElvGoodsCategory.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                //设置扩展动画
                if (mElvGoodsCategory.isGroupExpanded(groupPosition)) {
                    mElvGoodsCategory.collapseGroupWithAnimation(groupPosition);
                } else {
                    mElvGoodsCategory.expandGroupWithAnimation(groupPosition);
                }

                return true;
            }
        });
    }

    public void setIndexOrder(int indexOrder) {
        this.indexOrder = indexOrder;
    }

    public void setStorehouseId(String storehouseId) {
        this.storehouseId = storehouseId;
    }

    @Override
    protected View initView() {
        View view = View.inflate(UIUtils.getContext(),R.layout.holder_goods_category, null);
        return view;
    }

    @Override
    protected void refreshUI(Object data) {
        listCategory.clear();
        adapterGoodsCategory.notifyDataSetInvalidated();
        queryGsCategory();
    }

    /**
     * 获取商品分类
     */
    public void queryGsCategory() {
        AsyncHttpUtil<GoodsCategoryBean> httpUtil = new AsyncHttpUtil<>((Activity) mContext, GoodsCategoryBean.class, new IUpdateUI<GoodsCategoryBean>() {
            @Override
            public void updata(GoodsCategoryBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getListData() != null && jsonBean.getListData().size() > 0) {
                        listCategory = jsonBean.getListData();

                        adapterGoodsCategory.setData(listCategory);
                    } else {
                        listCategory = new ArrayList<>();
                    }
                    GoodsCategoryBean cateTwo = new GoodsCategoryBean();
                    cateTwo.setGc_id("00");
                    cateTwo.setGc_name("查询结果");
                    List<GoodsCategoryBean> listCateTwo = new ArrayList<>();
                    listCateTwo.add(cateTwo);

                    GoodsCategoryBean cateOne = new GoodsCategoryBean();
                    cateOne.setGc_name("扫码/查询");
                    cateOne.setGc_id("00");
                    cateOne.setSecondCategoryList(listCateTwo);
                    listCategory.add(0, cateOne);

                    if (listCategory != null && listCategory.size() > 0) {
                        onGroupExpand(0);
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

            }
        });
        httpUtil.post(M_Url.queryGsCategory, L_RequestParams.
                queryGsCategory(userBean.getUserId(), clientBean.getCc_id(), String.valueOf(indexOrder), "1", storehouseId), true);
    }


    /**
     * 保证listview只展开一项
     *
     * @param groupPosition
     */
    @Override
    public void onGroupExpand(int groupPosition) {
        /*if (addGoods()) {
            adapterGoodsCategory.index = groupPosition;
            adapterGoodsCategory.index2 = 0;
        } else {
            return;
        }

        for (int i = 0; i < listCategory.size(); i++) {
            if (i != groupPosition) {
                mElvGoodsCategory.collapseGroup(i);
            }
        }

//        listGoods.clear();
//        adapterGoods.notifyDataSetChanged();
        if (listCategory.get(groupPosition).getSecondCategoryList() != null &&
                listCategory.get(groupPosition).getSecondCategoryList().size() > 0) {
            onChildClick(mElvGoodsCategory, mElvGoodsCategory.getChildAt(groupPosition),
                    groupPosition, 0, adapterGoodsCategory.getChildId(groupPosition, 0));
        }*/
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

       /* if (addGoods()) {
            adapterGoodsCategory.index2 = i1;
            adapterGoodsCategory.notifyDataSetChanged();
        } else {
            return false;
        }

        mFlag = 1;
        page = 1;
        listGoods.clear();
        adapterGoods.notifyDataSetChanged();

        List<GoodsCategoryBean> secondCategoryList =
                listCategory.get(i).getSecondCategoryList();
        if (!"00".equals(secondCategoryList.get(i1).getGc_id())) {
            categoryCode = secondCategoryList.get(i1).getGc_code();
            //00代表扫码/查询
            getGoodsList("");
        } else {
            categoryCode = "";
        }*/

        return false;
    }
}
