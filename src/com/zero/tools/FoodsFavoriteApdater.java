package com.zero.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zero.bean.FoodsFavorite;
import com.zero.www.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
public class FoodsFavoriteApdater extends BaseAdapter{
	private LayoutInflater inflater;
	Bitmap dePic;
	Context mContext;
	List<FoodsFavorite> infos;
	public List<Boolean> mChecked; 
	HashMap<Integer,View> map = new HashMap<Integer,View>();
	public FoodsFavoriteApdater(Context modoActivity, List<FoodsFavorite> array, Handler handler) {
		this.inflater=LayoutInflater.from(modoActivity);
		this.mContext=modoActivity;
		this.infos = array;
		mChecked = new ArrayList<Boolean>();  
        for(int i=0;i<array.size();i++){  
            mChecked.add(false);  
        }  
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
		final int p = position;  
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.foods_favorite_list_item, null);
			holder.text = (TextView) convertView.findViewById(R.id.cb_goodsselect_shoppinglist_name);
			holder.text1 = (TextView) convertView.findViewById(R.id.tv_price_my_favorite);
			holder.text2 = (TextView) convertView.findViewById(R.id.canguan);
			holder.check = (CheckBox) convertView.findViewById(R.id.ck_select_my_favorite);
			holder.manglu = (TextView) convertView.findViewById(R.id.iv_busy_shoppingcar_item);
            map.put(position, convertView);
            
            convertView.setTag(holder);
		}else{
			//convertView = map.get(position);  
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setVisibility(View.VISIBLE);
		FoodsFavorite dish = infos.get(position);
		holder.check.setChecked(mChecked.get(position));
		holder.text.setText(dish.getFoodName());
		holder.text1.setText(dish.getPrice()+"");
		holder.text2.setText(dish.getRestaurantName());
		if("true".equals(dish.getIsBusy())){
			holder.manglu.setVisibility(View.VISIBLE);
		}else{
			holder.manglu.setVisibility(View.GONE);
		}
		holder.check.setOnClickListener(new View.OnClickListener() {  
            
            @Override  
            public void onClick(View v) {  
                CheckBox cb = (CheckBox)v;  
                mChecked.set(p, cb.isChecked());  
            }  
        });  
		return convertView;
		
	}

	private class ViewHolder{
		TextView text;
		TextView text1,text2,manglu;
		CheckBox check;
	}
	
	
}