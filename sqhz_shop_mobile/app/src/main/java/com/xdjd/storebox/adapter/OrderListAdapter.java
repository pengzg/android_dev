package com.xdjd.storebox.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.OrderListBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2016/12/1.
 */

public class OrderListAdapter extends BaseAdapter {
    private AllOrderListener allOrderListener;
    private int page;

    public OrderListAdapter(AllOrderListener allOrderListener,int page) {
        this.allOrderListener = allOrderListener;
        this.page  = page;
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
                allOrderListener.item(i);
            }
        });
        viewHolder.orderNum.setText(list.get(i).getOrderCode());
        viewHolder.orderDate.setText(list.get(i).getOrderDate());
        viewHolder.orderStatus.setText(list.get(i).getOrderStatusName());
        viewHolder.orderTotal.setText("共" + list.get(i).getGoodsNum() + "件商品");//总数量
        viewHolder.orderTotalPay.setText(list.get(i).getTotalAmount());//结算金额
        if (list.get(i).getGoodsCover().size() <= 4 && list.get(i).getGoodsCover().size() > 0) {
            viewHolder.More.setVisibility(View.GONE);
            switch (list.get(i).getGoodsCover().size()) {
                case 1:
                    viewHolder.More.setVisibility(View.GONE);
                    viewHolder.image1.setVisibility(View.VISIBLE);
                    viewHolder.image2.setVisibility(View.GONE);
                    viewHolder.image3.setVisibility(View.GONE);
                    viewHolder.image4.setVisibility(View.GONE);
                    viewHolder.image5.setVisibility(View.GONE);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(0)).into(viewHolder.image1);
                    break;

                case 2:
                    viewHolder.image1.setVisibility(View.VISIBLE);
                    viewHolder.image2.setVisibility(View.VISIBLE);
                    viewHolder.image3.setVisibility(View.GONE);
                    viewHolder.image4.setVisibility(View.GONE);
                    viewHolder.image5.setVisibility(View.GONE);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(0)).into(viewHolder.image1);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(1)).into(viewHolder.image2);
                    break;
                case 3:
                    viewHolder.image1.setVisibility(View.VISIBLE);
                    viewHolder.image2.setVisibility(View.VISIBLE);
                    viewHolder.image3.setVisibility(View.VISIBLE);
                    viewHolder.image4.setVisibility(View.GONE);
                    viewHolder.image5.setVisibility(View.GONE);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(0)).into(viewHolder.image1);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(1)).into(viewHolder.image2);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(2)).into(viewHolder.image3);
                    break;
                case 4:
                    viewHolder.image1.setVisibility(View.VISIBLE);
                    viewHolder.image2.setVisibility(View.VISIBLE);
                    viewHolder.image3.setVisibility(View.VISIBLE);
                    viewHolder.image4.setVisibility(View.VISIBLE);
                    viewHolder.image5.setVisibility(View.GONE);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(0)).into(viewHolder.image1);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(1)).into(viewHolder.image2);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(2)).into(viewHolder.image3);
                    Glide.with(viewGroup.getContext()).load(
                            list.get(i).getGoodsCover().get(3)).into(viewHolder.image4);
                    break;
                default:
                    break;

            }
        } else if(list.get(i).getGoodsCover().size() > 4){
            viewHolder.More.setVisibility(View.VISIBLE);
            viewHolder.image1.setVisibility(View.VISIBLE);
            viewHolder.image2.setVisibility(View.VISIBLE);
            viewHolder.image3.setVisibility(View.VISIBLE);
            viewHolder.image4.setVisibility(View.VISIBLE);
            Glide.with(viewGroup.getContext()).load(
                    list.get(i).getGoodsCover().get(0)).into(viewHolder.image1);
            Glide.with(viewGroup.getContext()).load(
                    list.get(i).getGoodsCover().get(1)).into(viewHolder.image2);
            Glide.with(viewGroup.getContext()).load(
                    list.get(i).getGoodsCover().get(2)).into(viewHolder.image3);
            Glide.with(viewGroup.getContext()).load(
                    list.get(i).getGoodsCover().get(3)).into(viewHolder.image4);
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
        return view;
    }


    public interface AllOrderListener {
        void item(int i);
    }




    static class ViewHolder {
        @BindView(R.id.order_num)
        TextView orderNum;
       @BindView(R.id.order_date)
        TextView orderDate;
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
        @BindView(R.id.more)
        LinearLayout More;
        @BindView(R.id.gap_line)
        View gapLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
