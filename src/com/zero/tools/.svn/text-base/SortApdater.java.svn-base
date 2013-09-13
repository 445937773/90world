package com.zero.tools;

import java.util.List;

import com.zero.www.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SortApdater extends BaseAdapter{
	
	private LayoutInflater inflater;
	Context mContext;
	private List<String> arrays;
	Bitmap userIcon;
	Bitmap dePic;
	Handler mHandler;
	public SortApdater(Context modoActivity, List<String> arrays) {
		this.inflater=LayoutInflater.from(modoActivity);
		this.mContext=modoActivity;
		this.arrays = arrays;
	}

	public int getCount() {
		return arrays.size();
	}

	public Object getItem(int position) {
		return arrays.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.sort_item, null);
			holder.text2 = (TextView) convertView.findViewById(R.id.tv_name_sort_item);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setVisibility(View.VISIBLE);
		String sort = arrays.get(position);
		holder.text2.setText(sort.trim());
		return convertView;
	}

	private class ViewHolder{
		TextView text2;
	}
	
}