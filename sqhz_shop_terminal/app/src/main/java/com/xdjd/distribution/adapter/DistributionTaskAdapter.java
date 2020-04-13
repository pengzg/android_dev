package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.CustomerTaskBean;
import com.xdjd.utils.UIUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DistributionTaskAdapter extends BaseAdapter {

    private DistributionTaskListener listener;
    public int index = 0;

    private int isDelivery;// 是否配送	Y	1 是  2未配送

    List<CustomerTaskBean> list;

    public void setData(List<CustomerTaskBean> list) {
        this.list = list;
        index = 0;
        notifyDataSetChanged();
    }

    public DistributionTaskAdapter(DistributionTaskListener listener, int isDelivery) {
        this.listener = listener;
        this.isDelivery = isDelivery;
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
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_distribution_task, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvShopName.setText(list.get(i).getCc_name());
        holder.mTvDeliverydate.setText(list.get(i).getDelivery_date());
        holder.mTvName.setText(list.get(i).getCc_contacts_name());
        holder.mTvPhone.setText(list.get(i).getCc_contacts_mobile());

        if ("".equals(list.get(i).getCc_contacts_name()) && "".equals(list.get(i).getCc_contacts_mobile())) {
            holder.mLlCustomer.setVisibility(View.GONE);
        } else {
            holder.mLlCustomer.setVisibility(View.VISIBLE);
        }

        if ("".equals(list.get(i).getCc_address())) {
            holder.mTvAddress.setVisibility(View.GONE);
        } else {
            holder.mTvAddress.setVisibility(View.VISIBLE);
            holder.mTvAddress.setText(list.get(i).getCc_address());
        }
        holder.mTvSalesidName.setText(list.get(i).getCc_salesmanid_nameref());

        if (index == i) {
            holder.mLlMain.setBackgroundColor(UIUtils.getColor(R.color.color_blue));

            holder.mTvShopName.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvDistance.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvSalesidName.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvAddress.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvDeliverydate.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvPhone.setTextColor(UIUtils.getColor(R.color.white));
        } else {
            holder.mLlMain.setBackgroundColor(UIUtils.getColor(R.color.color_f9f9f9));

            holder.mTvShopName.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvDistance.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvSalesidName.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvAddress.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvDeliverydate.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvPhone.setTextColor(UIUtils.getColor(R.color.text_gray));
        }

        if (isDelivery == 1) {
            holder.mTvDistance.setVisibility(View.GONE);
        } else {
            holder.mTvDistance.setVisibility(View.VISIBLE);
            if ("Y".equals(list.get(i).getCc_islocation()) && (!"".equals(list.get(i).getDistance()) && list.get(i).getDistance() != null)) {
                int distance = Integer.parseInt(list.get(i).getDistance());
                if (distance < 1000) {
                    holder.mTvDistance.setText("距离:" + distance + "米");
                } else if (distance >= 1000) {
                    holder.mTvDistance.setText("距离:" + div(distance, 1000, 2) + "公里");
                }
            } else {
                holder.mTvDistance.setText("");
            }
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = i;
                notifyDataSetChanged();
                listener.onItemTask(i);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLong(i);
                return true;
            }
        });
        return view;
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public interface DistributionTaskListener {
        void onItemTask(int i);

        void onItemLong(int i);
    }

    class ViewHolder {
        @BindView(R.id.tv_shop_name)
        TextView mTvShopName;
        @BindView(R.id.tv_deliverydate)
        TextView mTvDeliverydate;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_phone)
        TextView mTvPhone;
        @BindView(R.id.tv_address)
        TextView mTvAddress;
        @BindView(R.id.tv_salesid_name)
        TextView mTvSalesidName;
        @BindView(R.id.tv_distance)
        TextView mTvDistance;
        @BindView(R.id.ll_main)
        LinearLayout mLlMain;
        @BindView(R.id.ll_customer)
        LinearLayout mLlCustomer;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
