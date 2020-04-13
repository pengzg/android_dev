package com.xdjd.steward.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.steward.bean.ReportTaskBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SalesmanVisitingAdapter extends BaseAdapter {

    List<ReportTaskBean> list;
    OnImgListener listener;

    private boolean isShowImg = true;

    public void setGoneImg(boolean isShowImg){
        this.isShowImg = isShowImg;
    }

    public SalesmanVisitingAdapter(OnImgListener listener) {
        this.listener = listener;
    }

    public void setData(List<ReportTaskBean> list) {
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
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_salesman_visiting, viewGroup, false);
            holder = new ViewHolder(view);
            holder.adapter = new PictureAdapter();
            holder.mGvPicture.setAdapter(holder.adapter);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvName.setText(list.get(i).getClct_customerid_nameref());
        holder.mTvLineName.setText("线路名称:" + list.get(i).getBl_name());
        holder.mTvArrivetime.setText(list.get(i).getClct_arrivetime());
        holder.mTvLeavetime.setText(list.get(i).getClct_leavetime());

        holder.mTvStaffName.setText(list.get(i).getClct_userid_nameref());

        if (list.get(i).getService_time() == null || "".equals(list.get(i).getService_time())) {
            holder.mTvServiceTime.setVisibility(View.INVISIBLE);
        } else {
            holder.mTvServiceTime.setVisibility(View.VISIBLE);
            holder.mTvServiceTime.setText("服务时间:" + list.get(i).getService_time());
        }

        if (isShowImg){
            holder.adapter.setData(list.get(i).getImageList());
            holder.mGvPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int k, long l) {
                    listener.onImgItem(i,k);
                }
            });
        }

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_line_name)
        TextView mTvLineName;
        @BindView(R.id.tv_arrivetime)
        TextView mTvArrivetime;
        @BindView(R.id.tv_service_time)
        TextView mTvServiceTime;
        @BindView(R.id.tv_leavetime)
        TextView mTvLeavetime;
        @BindView(R.id.tv_staff_name)
        TextView mTvStaffName;
        @BindView(R.id.gv_picture)
        GridView mGvPicture;

        PictureAdapter adapter;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnImgListener{
        void onImgItem(int i,int k);
    }

    public class PictureAdapter extends BaseAdapter {

        List<String> imgList;

        public int getCount() {
            return imgList == null ? 0 : imgList.size();
        }

        public Object getItem(int arg0) {
            return imgList.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_img,
                        parent, false);
                holder = new ViewHolder(convertView);
                holder.mItemGridaImage = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(parent.getContext())
                    .load(imgList.get(position))
                    .into(holder.mItemGridaImage);

            return convertView;
        }

        public void setData(List<String> imageList) {
            this.imgList = imageList;
            notifyDataSetChanged();
        }

        class ViewHolder {
            @BindView(R.id.item_grida_image)
            ImageView mItemGridaImage;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
