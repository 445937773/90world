package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Dish;
import com.zero.bean.FoodsFavorite;
import com.zero.bean.ShoppingCar;
import com.zero.cache.ImageLoader;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class DishDetailActivity extends Activity implements OnClickListener{

	ImageView image,jia,jian;
	TextView name,price,title,number, tv_res_text, tv_res;
	Bitmap userIcon;
	Dish dish;
	Object xiaoxi;
	LinearLayout addShopping, collect;
	RelativeLayout ly_back;
	boolean falg = true;
	private ProgressDialog loginDialog;
	private ImageLoader mImageLoader;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Builder builder = new AlertDialog.Builder(DishDetailActivity.this);
			switch (msg.what) {
			case MyMessages.CHANGE_PWD_OK:
				loginDialog.dismiss();
				Toast.makeText(DishDetailActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
				
				 builder.setTitle("成功加入购物车").setMessage("点击去购物车生成订单").setPositiveButton("去购物车", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	Bundle b = new Bundle();
							b.putString("check", "shoppingCart");
							b.putString("name", "foods");
							Intent intent = new Intent(DishDetailActivity.this, MainActivity.class);
							intent.putExtras(b);
							startActivity(intent);
							finish();
			            }
			            }).setNegativeButton("继续浏览", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				break;
			case MyMessages.CHANGE_PWD_FAILD:
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	addShopping();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				loginDialog.dismiss();
				break;
			case MyMessages.COLLECT_OK:
				if(xiaoxi.equals("0")){
					Toast.makeText(DishDetailActivity.this, "加入收藏成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(DishDetailActivity.this, "该商品已收藏", Toast.LENGTH_SHORT).show();
				}
				loginDialog.dismiss();
				break;
			case MyMessages.COLLECT_FAILD:
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	addFavrtie();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				loginDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	private FoodsFavorite foods;
	String imageUrl;
	String prices;
	String Names;
	ShoppingCar goodsinfo;
	int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_detailed);
		MyApplication.getInstance().addActivity(this);
		mImageLoader = ImageLoader.getInstance(this);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		image = (ImageView) findViewById(R.id.iv_right_goodsimage);
		jia = (ImageView) findViewById(R.id.edit_product_num_increse_details);
		jian = (ImageView) findViewById(R.id.edit_product_num_descense_details);
		name = (TextView) findViewById(R.id.tv_right_item_goodsname);
		price = (TextView) findViewById(R.id.tv_right_item_oldpricevalue);
		number = (TextView) findViewById(R.id.edit_product_show_details);
		title = (TextView) findViewById(R.id.text_title_goods);
		tv_res_text = (TextView) findViewById(R.id.tv_standard_res_text);
		tv_res = (TextView) findViewById(R.id.tv_standard_res);
		addShopping = (LinearLayout) findViewById(R.id.btn_add_to_shoppingcar);
		collect = (LinearLayout) findViewById(R.id.btn_register);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		if(b.getSerializable("dish")!=null){
			dish = (Dish) b.getSerializable("dish");
			id = dish.getDishId();
			imageUrl = dish.getImage();
			image.setImageResource(R.drawable.default_pic);  
			mImageLoader.addTask(imageUrl, image); //添加任务
			
			Names = dish.getDishName();
			name.setText(Names);
			title.setText(Names.trim());
			prices = dish.getPrice()+"";
			price.setText(prices);
			tv_res_text.setText("规格：");
			tv_res.setText(dish.getRestaurantName());
		}else if(b.getSerializable("goods")!=null){
			foods = (FoodsFavorite) b.getSerializable("goods");
			id = foods.getFoodId();
			imageUrl = foods.getFoodPicture();
			image.setImageResource(R.drawable.default_pic);  
			mImageLoader.addTask(imageUrl, image); //添加任务
			Names = foods.getFoodName();
			name.setText(Names);
			title.setText(Names.trim());
			prices = foods.getPrice()+"";
			price.setText(prices);
			collect.setVisibility(View.GONE);
			tv_res_text.setText("规格：");
			tv_res.setText(foods.getRestaurantName());
		}else{
			goodsinfo = (ShoppingCar) b.getSerializable("goodsinfo");
			id = goodsinfo.getDish().getDishId();
			imageUrl = goodsinfo.getDish().getImage();
			image.setImageResource(R.drawable.default_pic);  
			mImageLoader.addTask(imageUrl, image); //添加任务
			Names = goodsinfo.getDish().getDishName();
			name.setText(Names);
			title.setText(Names.trim());
			prices = goodsinfo.getDish().getPrice()+"";
			price.setText(prices);
			tv_res_text.setText("规格：");
			tv_res.setText(goodsinfo.getDish().getRestaurantName());
		}
		
		number.setText("1");
		jia.setOnClickListener(this);
		jian.setOnClickListener(this);
		ly_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		getinfo();
		addShopping.setOnClickListener(this);
		collect.setOnClickListener(this);
	}
	
	int numberq=0;
	private String netWorkState;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
		netWorkState = ConnectionDetector.getNetWorkState(DishDetailActivity.this);
		
		if(netWorkState!=null){
			if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
				switch (v.getId()) {
				case R.id.edit_product_num_increse_details:
					numberq+=1;
					number.setText(numberq+"");
					price.setText(dish.getPrice()*numberq+"");
					
					break;
				case R.id.edit_product_num_descense_details:
					if(Integer.parseInt(number.getText().toString())>1){
						numberq-=1;
						number.setText(numberq+"");
						price.setText(dish.getPrice()*numberq+"");
						
					}else if(Integer.parseInt(number.getText().toString())==2){
						numberq-=1;
						number.setText(numberq+"");
					}
					break;
				
				case R.id.btn_add_to_shoppingcar://加入购物车
					loginDialog = ProgressDialog.show(DishDetailActivity.this, "请稍后...", "正在处理...", true, false);
					addShopping();
					break;
				case R.id.btn_register://加入收藏
					addFavrtie();
					break;
				default:
					break;
				}
			}else{
				Intent intent = new Intent(DishDetailActivity.this, Login.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
		
		}else{
			Toast.makeText(DishDetailActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
	} 
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MainActivity.instance.onCreateOptionsMenu(menu);
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		MainActivity.setActivity(this);
		MainActivity.instance.onMenuItemSelected(featureId, item);
		return true;
	}
	String userId;
	String numbers;
	List<String> names = new ArrayList<String>();
	List<String> zhi = new ArrayList<String>();
	public void getinfo(){
		SharedPreferences shar = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		userId = shar.getString(MySharedPreferences.STUDENT_ID, "");//用户id
		numbers = number.getText().toString();//数量
	}
	public void addShopping(){
		names.clear();
		names.add("studentId");
		names.add("foodsId");
		names.add("foodsAmount");
		zhi.clear();
		zhi.add(userId);
		zhi.add(id+"");
		zhi.add(numbers);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				boolean reply = ParseXml.getAddShoopingCar(names, zhi, "AddFoodsToShoppingCart");
				if(reply){
					msg.what = MyMessages.CHANGE_PWD_OK;
				}
				else{
					msg.what = MyMessages.CHANGE_PWD_FAILD;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	public void addFavrtie(){
		loginDialog = ProgressDialog.show(DishDetailActivity.this, "请稍后...", "正在处理...", true, false);
		names.add("studentId");
		names.add("FoodsId");
		zhi.add(userId);
		zhi.add(id+"");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				xiaoxi = ParseXml.getAddFavorite(names, zhi, MyMethods.ADD_FOODS_TO_FAVORICE);
				if(!xiaoxi.equals("1")){
					msg.what = MyMessages.COLLECT_OK;
				}
				else{
					msg.what = MyMessages.COLLECT_FAILD;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
}
