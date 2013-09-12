package com.zero.tools;

import java.util.List;

import com.zero.bean.OrderInfo;
import com.zero.cache.ImageLoader;
import com.zero.www.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IndentFoodsDetailApdater extends BaseAdapter{
	
	private LayoutInflater inflater;
	Context mContext;
	List<OrderInfo> infos;
	Bitmap userIcon;
	Bitmap dePic;
	Handler mHandler;
	private ImageLoader mImageLoader;
	public IndentFoodsDetailApdater(Context modoActivity, List<OrderInfo> array, Bitmap dePic, Handler handler) {
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
			convertView=inflater.inflate(R.layout.product_list_item_order, null);
			holder.tupian1 = (ImageView) convertView.findViewById(R.id.iv_picture_my_favorite_i);
			holder.text = (TextView) convertView.findViewById(R.id.tv_product_name_my_favorite);
			holder.text1 = (TextView) convertView.findViewById(R.id.tv_price_my_favorite);
			holder.text2 = (TextView) convertView.findViewById(R.id.tv_amount);
			holder.canguan = (TextView) convertView.findViewById(R.id.tv_standard_restaurant);
			holder.text3 = (TextView) convertView.findViewById(R.id.standard_restaurant);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setVisibility(View.VISIBLE);
		OrderInfo dish = infos.get(position);
		holder.text.setText(dish.getName());
		holder.text2.setText(dish.getNumber()+"");
		holder.text1.setText((dish.getNumber()*dish.getPrice())+"");
		if(dish.getRestaurantName()!=null){
			holder.text3.setText("餐馆：");
			holder.canguan.setText(dish.getRestaurantName());
			holder.tupian1.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.res_image));
		}else{
			holder.text3.setText("商品规格：");
			holder.tupian1.setImageResource(R.drawable.default_pic);
			mImageLoader.addTask(dish.getImage(), holder.tupian1); //添加任务
			if(!dish.getGoodsStandard().equals("null")){
				holder.canguan.setText(dish.getGoodsStandard()+"");
			}else{
				holder.canguan.setText("");
			}
		}
		
		return convertView;
	}

	private class ViewHolder{
		ImageView tupian1;
		TextView text;
		TextView text1,text2,canguan,text3;
	}
	
}