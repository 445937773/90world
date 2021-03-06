package com.zero.tools;

import java.util.List;

import com.zero.activity.SearchActivity;
import com.zero.bean.Dish;
import com.zero.www.R;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchListingInfoApdater extends BaseAdapter{
	
	private LayoutInflater inflater;
	SearchActivity mContext;
	List<Dish> infos;
	public SearchListingInfoApdater(SearchActivity modoActivity, List<Dish> array) {
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
			convertView=inflater.inflate(R.layout.foods_list_item, null);
			holder.text = (TextView) convertView.findViewById(R.id.tv_product_name_my_favorite);
			holder.text1 = (TextView) convertView.findViewById(R.id.tv_price_my_favorite);
			holder.canguan = (TextView) convertView.findViewById(R.id.tv_restaurant_my_favorite);
			holder.manglu = (TextView) convertView.findViewById(R.id.iv_busy_foodslist_item);
			holder.addShoppingcar = (ImageView) convertView.findViewById(R.id.btn_add_shoppingcar);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.addShoppingcar.setTag(infos.get(position));
		convertView.setVisibility(View.VISIBLE);
		Dish dish = infos.get(position);
		holder.text.setText(dish.getDishName());
		holder.text1.setText(dish.getPrice()+"");
		holder.canguan.setText(dish.getRestaurantName().trim());
		if(dish.getIsBusy().equals("false")){
			holder.manglu.setVisibility(View.GONE);
		}else{
			holder.manglu.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private class ViewHolder{
		ImageView addShoppingcar;
		TextView text, manglu;
		TextView text1,canguan;
	}
	
}