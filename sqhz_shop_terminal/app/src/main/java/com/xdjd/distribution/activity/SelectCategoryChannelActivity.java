package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.SelectCategoryChannelAdapter;
import com.xdjd.distribution.adapter.SelectGoodsPriceGradeAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.AddInfoBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SelectCategoryChannelActivity extends BaseActivity implements ItemOnListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.classify_one_lv)
    ListView mClassifyOneLv;
    @BindView(R.id.classify_two_lv)
    ListView mClassifyTwoLv;


    private SelectCategoryChannelAdapter adapterOne;
    private SelectCategoryChannelAdapter adapterTwo;

    private String type;//1类别;2渠道

    private List<AddInfoBean> list;

    private OneItemListener oneListener;

    private UserBean userBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_category_channel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        userBean = UserInfoUtils.getUser(this);

        type = getIntent().getStringExtra("type");

        mTitleBar.leftBack(this);
        if ("1".equals(type)) {//类别
            mTitleBar.setTitle("选择客户类别");
        } else if ("2".equals(type)) {//渠道
            mTitleBar.setTitle("选择客户渠道类别");
        }

        oneListener = new OneItemListener();
        adapterOne = new SelectCategoryChannelAdapter(oneListener, type);
        mClassifyOneLv.setAdapter(adapterOne);


        adapterTwo = new SelectCategoryChannelAdapter(this, type);
        mClassifyTwoLv.setAdapter(adapterTwo);

        getAddInfo();

    }

    class OneItemListener implements ItemOnListener {

        @Override
        public void onItem(int position) {
            adapterTwo.index = 0;
            adapterTwo.setData(list.get(position).getSubList());
        }
    }

    private void getAddInfo() {
        AsyncHttpUtil<AddInfoBean> httpUtil = new AsyncHttpUtil<>(SelectCategoryChannelActivity.this, AddInfoBean.class, new IUpdateUI<AddInfoBean>() {
            @Override
            public void updata(AddInfoBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {

                    if ("1".equals(type)) {//类别
                        list = jsonBean.getCategoryList();
                        if (list != null && list.size() != 0){
                            adapterOne.setData(list);
                            adapterTwo.setData(list.get(0).getSubList());
                        }else{
                            showToast("没有相关数据");
                        }
                    } else if ("2".equals(type)) {//渠道
                        list = jsonBean.getChannelList();
                        if (list != null && list.size() != 0){
                            adapterOne.setData(list);
                            adapterTwo.setData(list.get(0).getSubList());
                        }else{
                            showToast("没有相关数据");
                        }
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getAddInfo, L_RequestParams.getAddInfo(), true);
    }

    @Override
    public void onItem(int position) {
        AddInfoBean bean = list.get(adapterOne.index).getSubList().get(position);
        Intent intent = new Intent();
        intent.putExtra("addInfo", bean);
        setResult(10, intent);
        finish();
    }
}
