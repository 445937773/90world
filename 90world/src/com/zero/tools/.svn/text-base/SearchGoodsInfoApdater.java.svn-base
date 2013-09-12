package com.zero.tools;

import java.util.List;

import com.zero.activity.SearchActivity;
import com.zero.bean.GoodsInfo;
import com.zero.cache.ImageLoader;
import com.zero.www.R;


import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchGoodsInfoApdater extends BaseAdapter{
	private LayoutInflater inflater;
	SearchActivity mContext;
	List<GoodsInfo> infos;
	Bitmap dePic;
	Handler mHandler;
	protected boolean falg = true;
	private ImageLoader mImageLoader;
	public SearchGoodsInfoApdater(SearchActivity modoActivity, List<GoodsInfo> array, Bitmap dePic, Handler handler) {
		this.inflater=LayoutInflater.from(modoActivity);
		this.mContext=modoActivity;
		this.infos = array;
		this.dePic = dePic;
		this.mHandler = handler;
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
			convertView=inflater.inflate(R.layout.goods_list_item, null);
			holder.tupian1 = (ImageView) convertView.findViewById(R.id.iv_picture_my_favorite_i);
			holder.text = (TextView) convertView.findViewById(R.id.tv_product_name_my_favorite);
			holder.text1 = (TextView) convertView.findViewById(R.id.tv_price_my_favorite);
			holder.text2 = (TextView) convertView.findViewById(R.id.tv_standard_goods_item);
			holder.addShoppingcar = (ImageView) convertView.findViewById(R.id.btn_add_shoppingcar);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.addShoppingcar.setTag(infos.get(position));
		convertView.setVisibility(View.VISIBLE);
		GoodsInfo dish = infos.get(position);
		holder.tupian1.setImageResource(R.drawable.default_pic);
		mImageLoader.addTask(dish.getImage(), holder.tupian1); //添加任务

		holder.text.setText(dish.getGoodsName());
		holder.text1.setText(dish.getPrice()+"");
		holder.text2.setText(dish.getGoodsStandard());
		return convertView;
	}

	private class ViewHolder{
		ImageView tupian1,addShoppingcar;
		TextView text;
		TextView text1,text2;
	}
	
}