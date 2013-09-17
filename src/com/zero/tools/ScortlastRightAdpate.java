package com.zero.tools;

import java.util.List;

import com.zero.activity.GoodsLastCategoryActivity;
import com.zero.bean.GoodsInfo;
import com.zero.cache.ImageLoader;
import com.zero.www.R;



import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class ScortlastRightAdpate extends BaseAdapter{
	
	private LayoutInflater inflater;
	GoodsLastCategoryActivity mContext;
	private ImageLoader mImageLoader;
	List<GoodsInfo> infos;
	int numberq=0;
	protected Bitmap userIcon;
	Bitmap dePic;
	Handler mHandler;
	public ScortlastRightAdpate(GoodsLastCategoryActivity modoActivity, List<GoodsInfo> array, Bitmap dePic, Handler handler) {
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
		final ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.goods_list_item, null);
			holder.image = (ImageView) convertView.findViewById(R.id.iv_picture_my_favorite_i);
			holder.goodsName = (TextView) convertView.findViewById(R.id.tv_product_name_my_favorite);
			holder.price = (TextView) convertView.findViewById(R.id.tv_price_my_favorite);
			holder.text2 = (TextView) convertView.findViewById(R.id.tv_standard_goods_item);
			holder.addShoppingcar = (ImageView) convertView.findViewById(R.id.btn_add_shoppingcar);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.addShoppingcar.setTag(infos.get(position));
		convertView.setVisibility(View.VISIBLE);
		GoodsInfo info =infos.get(position);
		
		holder.image.setImageResource(R.drawable.default_pic);
		mImageLoader.addTask(info.getImage(), holder.image); //添加任务
		
		holder.price.setText(info.getPrice()+"");
		holder.goodsName.setText(info.getGoodsName());
		holder.text2.setText(info.getGoodsStandard());
		return convertView;
	}

	private class ViewHolder{ 
		ImageView image, addShoppingcar;
		TextView price,text2;
		TextView goodsName;
	}
	
}