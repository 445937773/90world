package com.zero.tools;

import java.util.List;

import com.zero.activity.SomeProductActivity;
import com.zero.bean.Eatery;
import com.zero.www.R;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EateryInfoImageApdater extends BaseAdapter{
	
	private LayoutInflater inflater;
	SomeProductActivity mContext;
	List<Eatery> infos;
	public EateryInfoImageApdater(SomeProductActivity modoActivity, List<Eatery> array) {
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
			convertView=inflater.inflate(R.layout.supermarket_orderfood_list_item, null);
			holder.tupian1 = (ImageView) convertView.findViewById(R.id.iv_supermarket_orderfood_list_item_firstimage);
			holder.text = (TextView) convertView.findViewById(R.id.iv_supermarket_orderfood_list_item_firstimage_text);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Eatery eatery = infos.get(position);
		convertView.setVisibility(View.VISIBLE);
		holder.tupian1.setImageResource(R.drawable.mylogo);
		holder.text.setText(eatery.getEateryId());
		return convertView;
	}

	private class ViewHolder{
		ImageView tupian1;
		TextView text;
		
	}
}