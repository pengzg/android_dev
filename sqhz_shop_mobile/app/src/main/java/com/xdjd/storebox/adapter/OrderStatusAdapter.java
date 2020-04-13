package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.OrderStatusBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2016/12/12.
 */

public class OrderStatusAdapter extends BaseAdapter {
    private List<OrderStatusBean>list  = new ArrayList<OrderStatusBean>();
    public void setData(List<OrderStatusBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    private int[] image_status = new int[]{R.drawable.status1 ,R.drawable.status2 ,R.drawable.status3 ,R.drawable.status4 ,
    R.drawable.status5,R.drawable.status7 ,R.drawable.status8 ,R.drawable.status9 ,R.drawable.status10 ,R.drawable.status11,
            R.drawable.status12 ,R.drawable.status13 ,R.drawable.status14};




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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_order_status, viewGroup, false);
           viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        } else {
            viewHolder  = (ViewHolder)view.getTag();
        }
        if(list.size() == 1  ){
            viewHolder.up.setVisibility(View.INVISIBLE );
            viewHolder.down.setVisibility(View.INVISIBLE);
        }else{
            if(i== 0){//第一个状态去掉上，下线
                viewHolder.up.setVisibility(View.INVISIBLE);
                viewHolder.down.setVisibility(View.VISIBLE);
            }
            if( i == (list.size()-1) ){
                viewHolder.up.setVisibility(View.VISIBLE);
                viewHolder.down.setVisibility(View.INVISIBLE);
            }
        }
        switch(list.get(i).getWol_progress_type()){
            case "1":viewHolder.statusImage.setImageResource(image_status[0]);break;//提交
            case "2":viewHolder.statusImage.setImageResource(image_status[1]);break; //审核通过
            case "3": viewHolder.statusImage.setImageResource(image_status[2]);break; //已发货
            case "4":viewHolder.statusImage.setImageResource(image_status[3]);break; //已取消
            case "5":viewHolder.statusImage.setImageResource(image_status[4]);break; //退货中
            case "7":viewHolder.statusImage.setImageResource(image_status[5]);break; //已签收完成
            case "8":viewHolder.statusImage.setImageResource(image_status[6]);break; //退货完成
            case "9":viewHolder.statusImage.setImageResource(image_status[7]);break; //备货完成
            case "10":viewHolder.statusImage.setImageResource(image_status[8]);break; //已取货
            case "11":viewHolder.statusImage.setImageResource(image_status[9]);break; //拒收
            case "12":viewHolder.statusImage.setImageResource(image_status[10]);break; //退货审核通过
            case "13":viewHolder.statusImage.setImageResource(image_status[11]);break; //退货审核不通过
            case "14":viewHolder.statusImage.setImageResource(image_status[12]);break; //收取货物完成
            default :break;
        }
        //viewHolder.statusImage.setImageResource(image_status[i]);

        viewHolder.time.setText(list.get(i).getWol_progress_time());//时间
        viewHolder.statusFirst.setText(list.get(i).getWol_progress_type_nameref());//状态
        viewHolder.statusTwo.setText(list.get(i).getWol_progress_explain());//状态描述
        return view;
    }

    class ViewHolder {
        @BindView(R.id.up)
        View up;
        @BindView(R.id.status_image)
        ImageView statusImage;
        @BindView(R.id.down)
        View down;
        @BindView(R.id.status_first)
        TextView statusFirst;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.status_two)
        TextView statusTwo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
