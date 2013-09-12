package com.zero.tools;

import java.util.List;

import com.zero.activity.IndentDetailsActivity;
import com.zero.bean.Indent;
import com.zero.cache.ImageLoader;
import com.zero.www.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderApdater extends BaseAdapter{
	
	private LayoutInflater inflater;
	Context mContext;
	List<Indent> infos;
	Bitmap userIcon;
	private ImageLoader mImageLoader;
	public OrderApdater(Context modoActivity, List<Indent> array) {
		this.inflater=LayoutInflater.from(modoActivity);
		this.mContext=modoActivity;
		this.infos = array;
		mImageLoader = ImageLoader.getInstance(mContext);
	}

	public int getCount() {
		return infos.size();
	}

	public Object getItem(int position) {
		return infos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.order_list_item, null);
			holder.text = (TextView) convertView.findViewById(R.id.order_item_Text);
			holder.text1 = (TextView) convertView.findViewById(R.id.order_item_totalPrice);
			holder.text2 = (TextView) convertView.findViewById(R.id.order_item_subtime);
			holder.text3 = (TextView) convertView.findViewById(R.id.order_item_status);
			holder.button = (Button) convertView.findViewById(R.id.order_item_track);
			holder.iv_a = (ImageView) convertView.findViewById(R.id.product_list_item_image_a);
			holder.iv_b = (ImageView) convertView.findViewById(R.id.product_list_item_image_b);
			holder.iv_c = (ImageView) convertView.findViewById(R.id.product_list_item_image_c);
			holder.dots = (TextView) convertView.findViewById(R.id.order_product_item_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setVisibility(View.VISIBLE);
		final Indent dish = infos.get(position);
		holder.text.setText(dish.getOrderFormNumber()+"");
		holder.text1.setText(dish.getSum()+"");
		if(dish.getOrdersTime()!=null){
			
			holder.text2.setText(dish.getOrdersTime().substring(0, 10)+" "+dish.getOrdersTime().substring(11, 16)+"");
		}
		if(dish.getIndentItate().equals("0")){
			holder.text3.setText("待发货");
		}else if(dish.getIndentItate().equals("1")){
			holder.text3.setText("已发货");
		}else if(dish.getIndentItate().equals("2")){
			holder.text3.setText("已完成");
		}else{
			holder.text3.setText("已取消");
		}
		holder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,IndentDetailsActivity.class);
				Bundle bun = new Bundle();
				bun.putSerializable("indent", dish);
				intent.putExtras(bun);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
		});
		
		List<String> urls = infos.get(position).getUrls();
		if(urls.size() ==1){
			holder.iv_a.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(urls.get(0), holder.iv_a); //添加任务
			holder.iv_a.setVisibility(View.VISIBLE);
			holder.iv_b.setVisibility(View.GONE);
			holder.iv_c.setVisibility(View.GONE);
			holder.dots.setVisibility(View.GONE);
		}
		if(urls.size() ==2){
			holder.iv_b.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(urls.get(1), holder.iv_b); //添加任务
			holder.iv_a.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(urls.get(0), holder.iv_a); //添加任务
			holder.iv_c.setVisibility(View.GONE);
			holder.iv_a.setVisibility(View.VISIBLE);
			holder.iv_b.setVisibility(View.VISIBLE);
			holder.dots.setVisibility(View.GONE);
		}
		if(urls.size() ==3){
			holder.iv_c.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(urls.get(2), holder.iv_c); //添加任务
			holder.iv_b.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(urls.get(1), holder.iv_b); //添加任务
			holder.iv_a.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(urls.get(0), holder.iv_a); //添加任务
			holder.iv_c.setVisibility(View.VISIBLE);
			holder.iv_a.setVisibility(View.VISIBLE);
			holder.iv_b.setVisibility(View.VISIBLE);
			holder.dots.setVisibility(View.GONE);
		}
		if(urls.size() > 3){
			holder.iv_c.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(urls.get(2), holder.iv_c); //添加任务
			holder.iv_b.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(urls.get(1), holder.iv_b); //添加任务
			holder.iv_a.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(urls.get(0), holder.iv_a); //添加任务
			holder.iv_c.setVisibility(View.VISIBLE);
			holder.iv_a.setVisibility(View.VISIBLE);
			holder.iv_b.setVisibility(View.VISIBLE);
			holder.dots.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private class ViewHolder{
		TextView text, dots;
		TextView text1,text2,text3;
		Button button;
		ImageView iv_a, iv_b, iv_c;
	}
	
}