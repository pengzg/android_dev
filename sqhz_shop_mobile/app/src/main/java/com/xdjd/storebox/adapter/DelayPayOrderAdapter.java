package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.OrderListBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/12.
 */

public class DelayPayOrderAdapter extends BaseAdapter {
    private DelayPayOrderListener  delayPayOrderListener ;

    public DelayPayOrderAdapter (DelayPayOrderListener delayPayOrderListener ) {
        this.delayPayOrderListener  = delayPayOrderListener;
    }

    private List<OrderListBean> list;

    public void setData(List<OrderListBean> list) {
        this.list = list;
        notifyDataSetChanged();
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
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
       ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_list, viewGroup, false);
            viewHolder = new ViewHolder(view);//加载后在new!!
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delayPayOrderListener.item(i);
            }
        });
//        viewHolder.orderNum.setText(list.get(i).getOrderCode());
//        viewHolder.systemTime.setText("2016-12-09 17:11");//时间加上
//        viewHolder.orderStatus.setText(list.get(i).getOrderStatus_nameref());
//        viewHolder.orderTotal.setText("共" + list.get(i).getGoodsNum() + "件商品");//总数量
//        viewHolder.orderTotalPay.setText(list.get(i).getTotalAmount());
//        if(list.get(i).getItemList().size() != 0){
//            switch(list.get(i).getItemList().size()){
//                case 1:
//                    viewHolder .image2.setVisibility(View.GONE);
//                    viewHolder .image3.setVisibility(View.GONE);
//                    viewHolder .image4.setVisibility(View.GONE);
//                    viewHolder .image5.setVisibility(View.GONE);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(0).getGoodsImg()).into(viewHolder.image1);break;
//
//                case 2:;
//                    viewHolder .image3.setVisibility(View.GONE);
//                    viewHolder .image4.setVisibility(View.GONE);
//                    viewHolder .image5.setVisibility(View.GONE);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(0).getGoodsImg()).into(viewHolder.image1);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(1).getGoodsImg()).into(viewHolder.image2);break;
//                case 3:
//                    viewHolder .image4.setVisibility(View.GONE);
//                    viewHolder .image5.setVisibility(View.GONE);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(0).getGoodsImg()).into(viewHolder.image1);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(1).getGoodsImg()).into(viewHolder.image2);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(2).getGoodsImg()).into(viewHolder.image3);break;
//                case 4:
//                    viewHolder .image5.setVisibility(View.GONE);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(0).getGoodsImg()).into(viewHolder.image1);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(1).getGoodsImg()).into(viewHolder.image2);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(2).getGoodsImg()).into(viewHolder.image3);
//                    Glide.with(viewGroup.getContext()).load(
//                            list.get(i).getItemList().get(3).getGoodsImg()).into(viewHolder.image4);break;
//                default:break;
//
//            }
//        }
//        if(list.get(i).getItemList().size() > 4){
//            Glide.with(viewGroup.getContext()).load(
//                    list.get(i).getItemList().get(0).getGoodsImg()).into(viewHolder.image1);
//            Glide.with(viewGroup.getContext()).load(
//                    list.get(i).getItemList().get(1).getGoodsImg()).into(viewHolder.image2);
//            Glide.with(viewGroup.getContext()).load(
//                    list.get(i).getItemList().get(2).getGoodsImg()).into(viewHolder.image3);
//            Glide.with(viewGroup.getContext()).load(
//                    list.get(i).getItemList().get(3).getGoodsImg()).into(viewHolder.image4);
//        }
        return view;
    }


    public interface DelayPayOrderListener {
        void item(int i);
    }




    static class ViewHolder {
        @BindView(R.id.order_num)
        TextView orderNum;
       /* @BindView(R.id.system_time)
        TextView systemTime;*/
        @BindView(R.id.order_status)
        TextView orderStatus;
        @BindView(R.id.order_total)
        TextView orderTotal;
        @BindView(R.id.order_total_pay)
        TextView orderTotalPay;

        @BindView(R.id.image1)
        ImageView image1;
        @BindView(R.id.image2)
        ImageView image2;
        @BindView(R.id.image3)
        ImageView image3;
        @BindView(R.id.image4)
        ImageView image4;
        @BindView(R.id.image5)
        ImageView image5;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
