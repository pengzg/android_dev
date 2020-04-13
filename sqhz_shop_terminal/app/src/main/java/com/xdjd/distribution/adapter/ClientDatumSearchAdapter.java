package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ClientBean;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 客户已知资料adapter
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ClientDatumSearchAdapter extends BaseAdapter {

    private ItemClientOnListener listener;

    public int index = 0;
    List<ClientBean> list;

    public void setData(List<ClientBean> list) {
        index = 0;
        this.list = list;
        notifyDataSetChanged();
    }

    public ClientDatumSearchAdapter(ItemClientOnListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ClientBean getItem(int i) {
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
                    inflate(R.layout.item_client_datum_search, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mNameTv.setText(list.get(i).getCc_name());

        if ("Y".equals(list.get(i).getCc_islocation()) && (!"".equals(list.get(i).getDistance()) && list.get(i).getDistance() != null)){
            BigDecimal distance = new BigDecimal(list.get(i).getDistance());
            BigDecimal km = new BigDecimal("1000");

            if (distance.compareTo(km) == -1){
                holder.mLocationStatus.setText(distance+"米");
            }else if (distance.compareTo(km) == 1 || distance.compareTo(km) == 0){
                //                Double dou = Double.valueOf(distance/1000);
                holder.mLocationStatus.setText(distance.divide(km).setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"公里");
            }
        } else if ("Y".equals(list.get(i).getCc_islocation())){
            holder.mLocationStatus.setText("已定位");
        } else {
            holder.mLocationStatus.setText("未定位");
        }

       /* if ("Y".equals(list.get(i).getCc_islocation()) && (!"".equals(list.get(i).getDistance()) && list.get(i).getDistance() != null)){
            int distance = Integer.parseInt(list.get(i).getDistance());
            if (distance<1000){
                holder.mLocationStatus.setText(distance+"米");
            }else if (distance >= 1000){
//                Double dou = Double.valueOf(distance/1000);
                holder.mLocationStatus.setText(div(distance,1000,2)+"公里");
            }
        }else{
            holder.mLocationStatus.setText("未定位");
        }*/

        if (!"".equals(list.get(i).getCc_address()) && list.get(i).getCc_address() != null){
            holder.mAddressTv.setVisibility(View.VISIBLE);
            holder.mAddressTv.setText(list.get(i).getCc_address());
        }else{
            holder.mAddressTv.setVisibility(View.GONE);
        }

//        if (index == i) {
            //            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_blue));
//            holder.mLine.setVisibility(View.VISIBLE);
//        } else {
            //            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_gray));
//            holder.mLine.setVisibility(View.INVISIBLE);
//        }

        //N是没有签到，其它是签到了
        if ("N".equals(list.get(i).getIssign())){
            holder.mIvSign.setVisibility(View.GONE);
        }else{
            holder.mIvSign.setVisibility(View.VISIBLE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = i;
                notifyDataSetChanged();
                listener.onItemClientSearch(i);
            }
        });
        return view;
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    class ViewHolder {
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.location_status)
        TextView mLocationStatus;
        @BindView(R.id.address_tv)
        TextView mAddressTv;
        @BindView(R.id.line)
        View mLine;
        @BindView(R.id.iv_sign)
        ImageView mIvSign;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface ItemClientOnListener{
        void onItemClientSearch(int position);
    }
}
