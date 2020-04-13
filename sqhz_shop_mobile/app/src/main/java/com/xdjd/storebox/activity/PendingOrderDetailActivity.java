package com.xdjd.storebox.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.ConfirmOrderAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.ButtonListBean;
import com.xdjd.storebox.bean.OrderDetailBean;
import com.xdjd.storebox.bean.OrderGoodsDetailBean;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/1/10.
 */

public class PendingOrderDetailActivity extends BaseActivity implements ConfirmOrderAdapter.GpidListener {
    @BindView(R.id.order_detail_pullscroll)
    ScrollView orderDetailPullscroll;
    @BindView(R.id.datail_listview)
    NoScrollListView datailListview;
    @BindView(R.id.order_code)
    TextView orderCode;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.order_status)
    TextView orderStatus;
    @BindView(R.id.pay_type)
    TextView payType;
    @BindView(R.id.send_shop)
    TextView sendShop;
    @BindView(R.id.send_phone)
    TextView sendPhone;
    @BindView(R.id.get_num)
    TextView getNum;
    @BindView(R.id.get_account)
    TextView getAccount;
    @BindView(R.id.send_num)
    TextView sendNum;
    @BindView(R.id.send_account)
    TextView sendAccount;
    @BindView(R.id.note)
    TextView note;
    /*@BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;*/
    @BindView(R.id.more_goodNum)
    TextView moreGoodNum;
    @BindView(R.id.show_more)
    LinearLayout showMore;
    @BindView(R.id.show_more_down)
    View showMoreDown;
    @BindView(R.id.image_show)
    ImageView imageShow;
    @BindView(R.id.order_status_image)
    ImageView orderStatusImage;
    @BindView(R.id.order_total)
    TextView orderTotal;
    @BindView(R.id.receive_name)
    TextView receiveName;
    @BindView(R.id.receive_phone)
    TextView receivePhone;
    @BindView(R.id.receive_address)
    TextView receiveAddress;
    @BindView(R.id.phoneService)
    LinearLayout phoneService;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;

    private int[] image_status = new int[]{R.drawable.status1, R.drawable.status2, R.drawable.status3, R.drawable.status4,
            R.drawable.status5, R.drawable.status7, R.drawable.status8, R.drawable.status9, R.drawable.status10, R.drawable.status11,
            R.drawable.status12, R.drawable.status13, R.drawable.status14};


    private VaryViewHelper mVaryViewHelper = null;
    private List<OrderGoodsDetailBean> detailBeanList = new ArrayList<OrderGoodsDetailBean>();
    private List<OrderGoodsDetailBean> detailBeanList3 = new ArrayList<>();
    private List<ButtonListBean> buttonListBeen = new ArrayList<ButtonListBean>();
    public OrderDetailBean orderDetailBean = new OrderDetailBean();
    ;
    private ConfirmOrderAdapter adapter;
    private String OrderId;
    private String vesion;
    private int flag = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order_datail);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("订单详情");
        mVaryViewHelper = new VaryViewHelper(datailListview);
        /*btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);*/
        showMore.setVisibility(View.GONE);
        adapter = new ConfirmOrderAdapter(this);

        OrderId = getIntent().getStringExtra("orderId");

        GetOrderDetail(UserInfoUtils.getId(PendingOrderDetailActivity.this), OrderId);
    }

    /*订单详情接口*/
    private void GetOrderDetail(String uid, String orderId) {
        AsyncHttpUtil<OrderDetailBean> httpUtil = new AsyncHttpUtil<>(this, OrderDetailBean.class, new IUpdateUI<OrderDetailBean>() {
            @Override
            public void updata(OrderDetailBean bean) {
                if (bean.getRepCode().equals("00")) {
                    orderCode.setText(bean.getOm_ordercode().toString());//订单编号
                    //orderTime.setText(bean.getAddTime());//下单时间
                    orderStatus.setText(bean.getOm_stats_nameref());//订单状态
                    //payType.setText(bean.getPayType_nameref());//支付方式
                    //sendShop.setText(bean.getUserShopName());//收货店铺名
                    //sendPhone.setText(bean.getMobile());//店铺电话
                    getNum.setText(bean.getOm_goods_num() + "/");//要货数量
                    getAccount.setText("¥" + bean.getOm_goods_amount());//要货金额
                    sendNum.setText(bean.getOm_delivery_num() + "/");//发货数量
                    sendAccount.setText("¥" + bean.getOm_delivery_amount());//发货金额
                    note.setText(bean.getOm_remarks());//备注
                    receiveName.setText(bean.getOm_customer_name());//收货人姓名
                    receivePhone.setText(bean.getOm_mobile());//收货人手机号
                    receiveAddress.setText(bean.getOm_address());//收货人地址
                    //orderTotal.setText(bean.getGoodsAmount());//订单金额
                    //vesion = bean.getVersion();
                    switch (bean.getOm_stats()) {
                        case "1":
                            orderStatusImage.setImageResource(image_status[3]);
                            break;//取消
                        case "2":
                            orderStatusImage.setImageResource(image_status[0]);
                            break; //待付款
                        case "3":
                            orderStatusImage.setImageResource(image_status[1]);
                            break; //审核中
                        case "4":
                            orderStatusImage.setImageResource(image_status[7]);
                            break; //待发货
                        case "5":
                            orderStatusImage.setImageResource(image_status[2]);
                            break; //已发货
                        case "6":
                            orderStatusImage.setImageResource(image_status[4]);
                            break; //退货中
                        case "7":
                            orderStatusImage.setImageResource(image_status[8]);
                            break; //订单完成
                        case "8":
                            orderStatusImage.setImageResource(image_status[6]);
                            break; //退货完成
                        case "9":
                            orderStatusImage.setImageResource(image_status[9]);
                            break; //拒收
                        default:
                            break;
                    }
                   // Log.e("showBtn", Integer.toString(bean.getButtonList().size()));
                    /*if (bean.getButtonList().size() != 0) {
                        if(btnFlag.equals("1")){
                            btn1.setVisibility(View.GONE);
                            btn2.setVisibility(View.GONE);
                        }else{
                            buttonListBeen.addAll(bean.getButtonList());
                            if (bean.getButtonList().size() == 1) {
                                btn1.setVisibility(View.GONE);
                                btn2.setVisibility(View.VISIBLE);
                                //One_btn();
                            } else {
                                btn1.setVisibility(View.VISIBLE);
                                btn2.setVisibility(View.VISIBLE);
                                //Two_btn();
                            }
                        }

                    } else {
                        btn1.setVisibility(View.GONE);
                        btn2.setVisibility(View.GONE);
                    }*/
                    mVaryViewHelper.showDataView();
                    detailBeanList.clear();
                    detailBeanList.addAll(bean.getListData());
                    if (detailBeanList.size() < 4) {
                        adapter.setData(detailBeanList);
                        datailListview.setAdapter(adapter);
                        showMore.setVisibility(View.GONE);
                        showMoreDown.setVisibility(View.GONE);
                    } else {
                        detailBeanList3.add(detailBeanList.get(0));
                        detailBeanList3.add(detailBeanList.get(1));
                        detailBeanList3.add(detailBeanList.get(2));
                        //ordeAdapter = new OrderDetailAdapter();
                        adapter.setData(detailBeanList3);
                        datailListview.setAdapter(adapter);
                        showMore.setVisibility(View.VISIBLE);
                        showMoreDown.setVisibility(View.VISIBLE);
                        moreGoodNum.setText("展开剩余商品");
                    }
                    if (detailBeanList == null || detailBeanList.size() == 0) {
                        Log.e("tag", "空地址");
                        mVaryViewHelper.showEmptyView();
                    }

                    //之前adapter.setData(detailBeanList);
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.GetOrderDetail, L_RequestParams.GetOrderDetail(uid, orderId), true);
    }

    @Override
    public void getGpid(String gpid,String ggpid) {

    }

    @OnClick({R.id.show_more,R.id.phoneService})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.show_more:
                if (flag == 1) {
                    imageShow.setSelected(true);
                    adapter.setData(detailBeanList);
                    datailListview.setAdapter(adapter);//展开全部
                    moreGoodNum.setText("收起");
                    flag = 2;
                } else {
                    imageShow.setSelected(false);
                    adapter.setData(detailBeanList3);
                    datailListview.setAdapter(adapter);//显示3
                    moreGoodNum.setText("展开剩余商品");
                    flag = 1;
                };break;
            case R.id.phoneService:
                DialogUtil.showCustomDialog(this, "客服电话",sendPhone.getText().toString(), "拨打",
                        "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sendPhone.getText().toString()));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void no() {
                            }
                        });break;
        }
    }
}
