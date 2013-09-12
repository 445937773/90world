package com.zero.tools;

import java.util.List;

import com.zero.bean.Eatery;
import com.zero.www.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EateryApdater extends BaseAdapter{
	
	private LayoutInflater inflater;
	Context mContext;
	List<Eatery> infos;
	Bitmap userIcon;
	Bitmap dePic;
	Handler mHandler;
	public EateryApdater(Context modoActivity, List<Eatery> array) {
		this.inflater=LayoutInflater.from(modoActivity);
		this.mContext=modoActivity;
		this.infos = array;
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
			convertView=inflater.inflate(R.layout.res_sort_item, null);
			holder.text2 = (TextView) convertView.findViewById(R.id.tv_name_sort_item);
			holder.isbusy = (TextView) convertView.findViewById(R.id.iv_busy_foodslist_item);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setVisibility(View.VISIBLE);
		Eatery dish = infos.get(position);
		holder.text2.setText(dish.getEateryName().trim());
		if(("false").equals(dish.getEateryBusy())){
			holder.isbusy.setVisibility(View.GONE);
		}else{
			holder.isbusy.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private class ViewHolder{
		TextView text2;
		TextView isbusy;
	}
	
}