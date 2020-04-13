package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xdjd.distribution.R;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PaymentAdapter extends BaseAdapter {


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_payment,viewGroup,false);
            holder = new ViewHolder();
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }

    class ViewHolder{

    }
}
