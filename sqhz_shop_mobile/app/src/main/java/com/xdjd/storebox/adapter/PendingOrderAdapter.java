package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.PendingOrderBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/1/10.
 */

public class PendingOrderAdapter extends BaseAdapter {
    private List<PendingOrderBean> list;
    private pendingOrderListener listener;
    private int page;
    public void setData(List<PendingOrderBean> list){
        this.list = list;
    }
    public PendingOrderAdapter(pendingOrderListener listener,int page) {
        this.listener = listener;
        this.page = page;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pending_order, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(page == 1){
            if (i == 0) {
                viewHolder.gapLine.setVisibility(View.GONE);
            }else{
                viewHolder.gapLine.setVisibility(View.VISIBLE);
            }
        }else{
            viewHolder.gapLine.setVisibility(View.VISIBLE );
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.item(list.get(i).getOrderId());
            }
        });
        viewHolder.shopName.setText(list.get(i).getShopName());//店铺名称
        viewHolder.orderNum.setText(list.get(i).getOrderCode()) ;//订单编号
        viewHolder.orderTime.setText(list.get(i).getAddtime());//下单时间
        viewHolder.receiveName.setText(list.get(i).getReceiverName());//收货人姓名
        viewHolder.receiveMobile.setText(list.get(i).getMobile());//收货人电话
        viewHolder.receiveAddress.setText(list.get(i).getAddress());//收货人地址
        viewHolder.orderStatus.setText(list.get(i).getOrderStatus_nameref());//订单状态
        viewHolder.goodsNum.setText("共"+list.get(i).getGoodsNum()+"件商品");//商品件数
        viewHolder.orderTotal.setText(list.get(i).getTotalAmount()+"元");//订单金额
        return view;
    }


    public interface pendingOrderListener {
        public void item(String  i);
    }

    static class ViewHolder {
        @BindView(R.id.gap_line)
        View gapLine;
        @BindView(R.id.end_line)
        View endLine;
        @BindView(R.id.shop_name)
        TextView shopName;
        @BindView(R.id.order_num)
        TextView orderNum;
        @BindView(R.id.order_time)
        TextView orderTime;
        @BindView(R.id.receive_name)
        TextView receiveName;
        @BindView(R.id.receive_mobile)
        TextView receiveMobile;
        @BindView(R.id.receive_address)
        TextView receiveAddress;
        @BindView(R.id.order_status)
        TextView orderStatus;
        @BindView(R.id.goods_num)
        TextView goodsNum;
        @BindView(R.id.order_total)
        TextView orderTotal;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
