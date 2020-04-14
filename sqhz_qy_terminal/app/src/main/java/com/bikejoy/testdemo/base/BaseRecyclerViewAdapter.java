package com.bikejoy.testdemo.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * recyclerView封装类
 */
public abstract class BaseRecyclerViewAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    public List<T> dataList;
    public OnItemClickListener mItemClickListener;

    public abstract void setDataList(List<T> dataList);

    public void setItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public  void onBindViewHolder(VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener!=null){
                    mItemClickListener.onItemClick(dataList.get(position),position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList==null?0:dataList.size();
    }

    /**
     * ItemClick事件声明
     */
    public interface OnItemClickListener<T>{
        void onItemClick(T item, int position);
    }
}
