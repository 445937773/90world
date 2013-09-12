package com.zero.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zero.activity.DishDetailActivity;
import com.zero.activity.GoodsParticularInfoActivity;
import com.zero.bean.ShoppingCar;
import com.zero.cache.ImageLoader;
import com.zero.www.R;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
public class ShoppingCarAdpater extends BaseAdapter{
	private ImageLoader mImageLoader;
	private LayoutInflater inflater;
	Context mContext;
	List<ShoppingCar> infos;
	public List<Boolean> mChecked; 
	HashMap<Integer,View> map = new HashMap<Integer,View>();
	public List<Integer> number;
	protected int numberq=-1; 
	public ShoppingCarAdpater(Context modoActivity, List<ShoppingCar> list) {
		this.inflater=LayoutInflater.from(modoActivity);
		this.mContext=modoActivity;
		infos = new ArrayList<ShoppingCar>();  
		infos = list;  
		mImageLoader = ImageLoader.getInstance(mContext);  
        mChecked = new ArrayList<Boolean>();  
        for(int i=0;i<list.size();i++){  
            mChecked.add(true);  
        }  
        number = new ArrayList<Integer>();
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
		final ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.shoppingcar_list_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.cb_goodsselect_shoppinglist_name);
			holder.number = (TextView) convertView.findViewById(R.id.edit_product_show_shoppinglist_number);
			holder.price = (TextView) convertView.findViewById(R.id.tv_price_total_shoppinglist_price);
			holder.jia = (ImageView) convertView.findViewById(R.id.edit_product_num_increse_shoppinglist_jia);
			holder.jian = (ImageView) convertView.findViewById(R.id.edit_product_num_descense_shoppinglist_jian);
			holder.check = (CheckBox) convertView.findViewById(R.id.cb_goodsselect_shoppinglist);
			holder.image = (ImageView) convertView.findViewById(R.id.iv_picture_my_shoppingcar);
			holder.tv_res = (TextView) convertView.findViewById(R.id.standard_restaurant_shoppingcar);
			holder.canguan = (TextView) convertView.findViewById(R.id.tv_standard_restaurant_shoppingcar);
			holder.manglu = (TextView) convertView.findViewById(R.id.iv_busy_shoppingcar_item);
             map.put(position, convertView);
            
			
			convertView.setTag(holder);
		}else{
			//convertView = map.get(position);  
			holder = (ViewHolder) convertView.getTag();
		}
		final ShoppingCar info =infos.get(position);
		convertView.setVisibility(View.VISIBLE);
		holder.check.setChecked(mChecked.get(position));
		holder.image.setImageResource(R.drawable.default_pic);
		
		
		holder.number.setText(info.getGoodsNumber()+"");
		
		if(info.getDish()!=null){
			holder.price.setText(info.getGoodsNumber()*info.getDish().getPrice()+"");
//			mImageLoader.addTask(info.getDish().getImage(), holder.image); //添加任务
			holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.res_image));
			holder.tv_res.setText("所属餐馆:");
			holder.name.setText(info.getDish().getDishName());
			holder.canguan.setText(info.getDish().getRestaurantName());
			if("true".equals(info.getDish().getIsBusy())){
				holder.manglu.setVisibility(View.VISIBLE);
			}else{
				holder.manglu.setVisibility(View.GONE);
			}
		}else{
			holder.price.setText(info.getGoodsNumber()*info.getGoods().getPrice()+"");
			mImageLoader.addTask(info.getGoods().getImage(), holder.image); //添加任务
			holder.tv_res.setText("商品规格:");
			holder.name.setText(info.getGoods().getGoodsName());
			if("null".equals(info.getGoods().getGoodsStandard())){
				holder.canguan.setText("");
			}else{
				holder.canguan.setText(info.getGoods().getGoodsStandard());
			}
			
			holder.manglu.setVisibility(View.GONE);
		}
		holder.jia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(numberq==-1){
					numberq = info.getGoodsNumber();
				}
				
				numberq+=1;
				info.setGoodsNumber(numberq);
				holder.number.setText(numberq+"");
				if(info.getGoods()!=null){
					holder.price.setText(numberq*info.getGoods().getPrice()+"");
				}else{
					holder.price.setText(numberq*info.getDish().getPrice()+"");
				}
				
				numberq = -1;
				number.add(1);
			}
		});
		
		holder.jian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(info.getGoodsNumber() > 1){
					numberq = info.getGoodsNumber();
					numberq-=1;
					info.setGoodsNumber(numberq);
					holder.number.setText(numberq+"");
					if(info.getGoods()!=null){
						holder.price.setText(numberq*info.getGoods().getPrice()+"");
					}else{
						holder.price.setText(numberq*info.getDish().getPrice()+"");
					}
					number.add(1);
				}
				
			}
		});
		 holder.check.setOnClickListener(new View.OnClickListener() {  
             
             @Override  
             public void onClick(View v) {  
                 CheckBox cb = (CheckBox)v;  
                 mChecked.set(p, cb.isChecked());  
             }  
         });  
		 holder.image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(mContext, "11", Toast.LENGTH_SHORT).show();
				Intent intent;
				if(info.getDish()!=null){
					intent = new Intent(mContext,DishDetailActivity.class);
				}else{
					intent = new Intent(mContext,GoodsParticularInfoActivity.class);
				}
				
				Bundle b = new Bundle();
				b.putSerializable("goodsinfo", info);
				intent.putExtras(b);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
		});
		return convertView;
	}

	private class ViewHolder{
		TextView name,tv_res,canguan,manglu;
		TextView number;
		TextView price;
		ImageView jia, image;
		ImageView jian;
		CheckBox check;
	}
}