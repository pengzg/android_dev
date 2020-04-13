package com.xdjd.distribution.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.release.Bimp;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends Activity {

	/**
	 * 最后一次修改的东西
	 */
	private List<String> drr;

	private ArrayList<View> listViews = null;
	private ViewPager pager;
	private MyPageAdapter adapter;
	// 返回按钮
	private ImageButton imageButton;
	// 网络图片(特殊情况，当本地和网络图片都传或只传网络图片时用到。)
	private List<String> list = new ArrayList<String>();
	// 第几张
	private int count;
	// 个数
	private TextView textView;
	private int isNetwork;//1.网络;2.本地

	private RelativeLayout photo_relativeLayout,left_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);

		// 接受数据
		drr = JsonUtils.getObjectsList(getIntent().getStringExtra("drr"),
				String.class);
		isNetwork = getIntent().getIntExtra("isNetwork",2);

		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);

		left_layout = (RelativeLayout) findViewById(R.id.left_layout);

		left_layout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				JSONArray jsonArray = new JSONArray();
				for (int i = 0; i < drr.size(); i++) {
					jsonArray.put(drr.get(i));
				}
				Intent intent = new Intent();
				intent.putExtra("drr", jsonArray.toString());
				setResult(102, intent);
				finish();
			}
		});

		textView = (TextView) findViewById(R.id.textview);

		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);

		Intent intent = getIntent();
		//获取图片路径
		/*String path = getIntent().getStringExtra("picPath");
		Bitmap bm = null;

		List<String> ll = new ArrayList<>();
		ll.add(path);
		if (ll != null && ll.size() > 0) {
			list.addAll(ll);
		}

		try {
			bm = Bimp.revitionImageSize(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initListViews(bm, 0);*/

		// 设置图片
		for (int i = 0; i < drr.size(); i++) {
			try {
				String path = drr.get(i);
				Bitmap bm = null;
				if (isNetwork == 2){
					bm = Bimp.revitionImageSize(path);
				}

				initListViews(bm, i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		count = intent.getIntExtra("ID", 0);

		textView.setText((count + 1) + "/" + drr.size());
		adapter = new MyPageAdapter(listViews);// 构造adapter
		pager.setAdapter(adapter);// 设置适配器
		pager.setCurrentItem(count);
	}

	private void initListViews(Bitmap bm, int position) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView img = new ImageView(this);// 构造textView对象
		img.setBackgroundColor(0xff000000);
		if (bm == null) {
			Glide.with(getApplicationContext()).load(drr.get(position))
					.placeholder(R.color.image_gray).into(img);
		} else {
			img.setImageBitmap(bm);
		}
		img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		listViews.add(img);// 添加view
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			count = arg0;
			textView.setText((count + 1) + "/" + drr.size());
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// 页数

		public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
			// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);
				listViews.get(arg1 % size).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						finish();
					}
				});
			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 网络图片
		// JSONArray jsonArray = new JSONArray();
		// for (int i = 0; i < list.size(); i++) {
		// jsonArray.put(list.get(i));
		// }
		// EventBus.getDefault().post(
		// new AuthenticationEnterpriseEvent(jsonArray.toString()));
	}
}