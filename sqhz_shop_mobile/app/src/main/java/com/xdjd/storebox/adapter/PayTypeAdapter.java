package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.PayMethods;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

public class PayTypeAdapter extends BaseAdapter {
	private List<PayMethods> list;
	public int mIndex;

	public void setData(List<PayMethods> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public PayTypeAdapter(OnPayClickListener listener) {
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return list == null?0:list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_pay_type, parent,false);
			holder.pic = (ImageView) convertView.findViewById(R.id.pay_img);
			holder.select = (ImageView) convertView
					.findViewById(R.id.pay_Select);
			holder.name = (TextView) convertView.findViewById(R.id.pay_name);
			holder.title = (TextView) convertView.findViewById(R.id.pay_title);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.payLayout);
			convertView.setTag(holder);

			AutoUtils.auto(convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Glide.with(parent.getContext()).
				load(list.get(position).getPayImg()).into(holder.pic);


		if ("1".equals(list.get(position).getIsDefault())) {
			holder.select.setSelected(true);
			//holder.layout.setBackgroundColor(Color.parseColor("#f2f2f2"));
		} else {
			holder.select.setSelected(false);
			//holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
		}

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onPayClick(position);
				}
			}
		});

		holder.name.setText(list.get(position).getPayName());
		holder.title.setText(list.get(position).getPayTitle());
		return convertView;
	}

	public class ViewHolder {
		TextView name, title;
		ImageView pic, select;
		LinearLayout layout;
	}
	private OnPayClickListener listener;
	public void setOnClickListener(OnPayClickListener listener){
		this.listener = listener;
	}
	public interface OnPayClickListener{
		void onPayClick(int index);
	}
}
