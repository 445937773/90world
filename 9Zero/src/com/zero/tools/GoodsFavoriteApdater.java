package com.zero.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zero.bean.GoodsFavorite;
import com.zero.cache.ImageLoader;
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
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
public class GoodsFavoriteApdater extends BaseAdapter{
	private ImageLoader mImageLoader;
	private LayoutInflater inflater;
	Bitmap dePic;
	Context mContext;
	List<GoodsFavorite> infos;
	Bitmap userIcon;
	public List<Boolean> mChecked; 
	HashMap<Integer,View> map = new HashMap<Integer,View>();
	public GoodsFavoriteApdater(Context modoActivity, List<GoodsFavorite> array, Bitmap dePic, Handler handler) {
		this.inflater=LayoutInflater.from(modoActivity);
		this.mContext=modoActivity;
		this.infos = array;
		mChecked = new ArrayList<Boolean>();  
        for(int i=0;i<array.size();i++){  
            mChecked.add(false);  
        } 
        this.dePic = dePic;
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
		final int p = position;  
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.favarite_list_item, null);
			holder.tupian1 = (ImageView) convertView.findViewById(R.id.iv_picture_my_shoppingcar);
			holder.text = (TextView) convertView.findViewById(R.id.cb_goodsselect_shoppinglist_name);
			holder.text1 = (TextView) convertView.findViewById(R.id.tv_price_my_favorite);
			holder.text2 = (TextView) convertView.findViewById(R.id.canguan);
			holder.check = (CheckBox) convertView.findViewById(R.id.ck_select_my_favorite);
			holder.canguan = (TextView) convertView.findViewById(R.id.standard_restaurant_shoppingcar);
            map.put(position, convertView);
            
            convertView.setTag(holder);
		}else{
			//convertView = map.get(position);  
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setVisibility(View.VISIBLE);
		GoodsFavorite dish = infos.get(position);
		holder.tupian1.setImageResource(R.drawable.default_pic);
		mImageLoader.addTask(dish.getGoodsPictureUrl(), holder.tupian1); //添加任务
		holder.tupian1.setId(dish.getGoods());
		holder.check.setChecked(mChecked.get(position));
		holder.text.setText(dish.getGoodsName());
		holder.text1.setText(dish.getPrice()+"");
		holder.text2.setText(dish.getGoodsStandard());
		holder.canguan.setText("商品规格：");
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
		ImageView tupian1;
		TextView text;
		TextView text1,text2,canguan;
		CheckBox check;
	}
	
	
}