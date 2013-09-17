package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zero.bean.Dish;
import com.zero.bean.Eatery;
import com.zero.bean.GoodsInfo;
import com.zero.cache.ImageLoader;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.EateryApdater;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
import com.zero.tools.SearchGoodsInfoApdater;
import com.zero.tools.SearchListingInfoApdater;
import com.zero.www.R;

@SuppressLint("HandlerLeak")
public class SearchActivity extends Activity implements OnClickListener{
	Button button1,button2,button3;
	ProgressBar pb_search;
	ListView list1;
	List<Dish> dishs = new ArrayList<Dish>();
	List<GoodsInfo> goods = new ArrayList<GoodsInfo>();
	EditText name;
	ImageView delete;
	ImageLoader mImageLoader;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			Builder builder = new AlertDialog.Builder(SearchActivity.this);
			switch (msg.what) {
			case MyMessages.CHANGE_PWD_OK:
				goodsAdpater.notifyDataSetChanged();
				list1.setAdapter(goodsAdpater);
				list1.setVisibility(View.VISIBLE);
				pb_search.setVisibility(View.GONE);
				list1.setBackgroundResource(R.color.search_bgcolor);
				r1.setVisibility(View.GONE);
				break;
			case MyMessages.CHANGE_PWD_FAILD:
				pb_search.setVisibility(View.GONE);
				list1.setVisibility(View.GONE);
				r1.setVisibility(View.VISIBLE);
				break;
			case MyMessages.TIME_OUT:
				pb_search.setVisibility(View.GONE);
				
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	getGoods();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            	
				            }
				        }).show();
				break;
			case MyMessages.SEARCH_FOODS_OK:
				search.notifyDataSetChanged();
				list1.setAdapter(search);
				pb_search.setVisibility(View.GONE);
				list1.setVisibility(View.VISIBLE);
				list1.setBackgroundResource(R.color.search_bgcolor);
				r1.setVisibility(View.GONE);
				break;
			case MyMessages.TIME_OUT+1:
				pb_search.setVisibility(View.GONE);
			builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	getFoods();
	            
	            }
	            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
		            
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	onBackPressed();
		            }
		        }).show();
				break;
			case MyMessages.SEARCH_RESTAURANT_OK:
				list1.setVisibility(View.VISIBLE);
				r1.setVisibility(View.GONE);
				adpater.notifyDataSetChanged();
				list1.setAdapter(adpater);
				list1.setBackgroundResource(R.color.search_bgcolor);
				pb_search.setVisibility(View.GONE);
				break;
			case MyMessages.TIME_OUT+2:
				
				pb_search.setVisibility(View.GONE);
				builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	getCanGuan();
	            }
	            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
		            
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	onBackPressed();
		            }
		        }).show();
				break;
			case MyMessages.COLLECT_OK:
				loginDialog.dismiss();
				Toast.makeText(SearchActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
				break;
			case MyMessages.COLLECT_FAILD:
				loginDialog.dismiss();
				Toast.makeText(SearchActivity.this, "加入购物车失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	
	};
	protected SearchListingInfoApdater search;
	private String netWorkState;
	protected SearchGoodsInfoApdater goodsAdpater;
	private RelativeLayout r1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		MyApplication.getInstance().addActivity(this);
		pb_search = (ProgressBar) findViewById(R.id.pb_search);
		button1 = (Button) findViewById(R.id.rb_searchRadio_goods);
		button2 = (Button) findViewById(R.id.btn_search_food);
		button3 = (Button) findViewById(R.id.btn_search_restaurant);
		list1 = (ListView) findViewById(R.id.lv_search);
		r1 = (RelativeLayout) findViewById(R.id.no_search_item);
		name = (EditText) findViewById(R.id.actv_autoComplete);
		delete = (ImageView) findViewById(R.id.actv_delete);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		delete.setOnClickListener(this);
		
		
		
		OnScrollListener onScrollListener = new OnScrollListener() {
	        @Override
	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	        	mImageLoader = ImageLoader.getInstance(SearchActivity.this);
	            switch (scrollState) {
	                case OnScrollListener.SCROLL_STATE_FLING:   
	                    mImageLoader.lock();
	                    break;
	                case OnScrollListener.SCROLL_STATE_IDLE:
	                    mImageLoader.unlock();
	                    break;
	                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
	                    mImageLoader.lock();
	                    break;
	                default:
	                    break;
	            }
	        }
	                                                               
	        @Override
	        public void onScroll(AbsListView view, int firstVisibleItem,
	                int visibleItemCount, int totalItemCount) {
	        }
	    };
	    list1.setOnScrollListener(onScrollListener);
	}
	
	ArrayAdapter<String> ad;
	List<Eatery> sorts = new ArrayList<Eatery>();
	protected ProgressDialog loginDialog;
	protected EateryApdater adpater;
	List<String> names = new ArrayList<String>();
	List<String> string = new ArrayList<String>();
	@Override
	public void onClick(View v) {
		netWorkState = ConnectionDetector.getNetWorkState(SearchActivity.this);
		if(netWorkState!=null){
			if(!"".equals(name.getText().toString())){
				switch (v.getId()) {
				//搜索商品
				case R.id.rb_searchRadio_goods:
					pb_search.setVisibility(View.VISIBLE);
					getGoods();
					break;
					
				//搜索菜品
				case R.id.btn_search_food:
					pb_search.setVisibility(View.VISIBLE);
					getFoods();
					break;
					
				//搜索餐馆
				case R.id.btn_search_restaurant:
					pb_search.setVisibility(View.VISIBLE);
					getCanGuan();
					break;
				//删除输入的文字	
				case R.id.actv_delete:
					name.setText("");
					break;
					
				default:
					break;
				}
			}else{
				Toast.makeText(SearchActivity.this,"请输入关键字！", Toast.LENGTH_SHORT).show();
			}
			
		}else{
			Toast.makeText(SearchActivity.this,MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(SearchActivity.this, MainActivity.class);
		startActivity(intent);
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
	
	public void getGoods(){
		pb_search.setVisibility(View.VISIBLE);
		names.clear();
		string.clear();
		names.add("goodsName");
		string.add(name.getText().toString());
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				goods = ParseXml.getGoodsInfo(names, string, "SearchGoodsByName");
				goodsAdpater = new SearchGoodsInfoApdater(SearchActivity.this,goods, BitmapFactory.decodeResource(getResources(), R.drawable.default_pic), handler);
				if(goods!=null){
					if(goods.size()!=0){
						msg.what = MyMessages.CHANGE_PWD_OK;
					}else{
						msg.what = MyMessages.CHANGE_PWD_FAILD;
					}
					
				}
				else{
					msg.what = MyMessages.TIME_OUT;
				}
				handler.sendMessage(msg);
			}
		}).start();
		
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				loginDialog = ProgressDialog.show(SearchActivity.this, "请稍后...", "正在处理...", true, false);
				GoodsInfo dish = (GoodsInfo) list1.getItemAtPosition(arg2);
				Bundle b = new Bundle();
				b.putSerializable("goodsName", dish);
				Intent intent;
				intent = new Intent(SearchActivity.this,GoodsParticularInfoActivity.class);
				intent.putExtras(b);
				startActivity(intent);
				loginDialog.dismiss();
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
		});
	}
	public void getFoods(){
		names.clear();
		string.clear();
		names.add("foodName");
		string.add(name.getText().toString());
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				dishs = ParseXml.getDish(names, string, MyMethods.SEARCH_FOODS_BY_FOODNAME);
				search = new SearchListingInfoApdater(SearchActivity.this,dishs);
				if(dishs!=null){
					if(dishs.size()!=0){
						msg.what = MyMessages.SEARCH_FOODS_OK;
					}else{
						msg.what = MyMessages.CHANGE_PWD_FAILD;
					}
					
				}
				else{
					msg.what = MyMessages.TIME_OUT+1;
				}
				handler.sendMessage(msg);
			}
		}).start();
		
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Dish dish = (Dish) list1.getItemAtPosition(arg2);
				if(dish.getIsBusy().equals("false")){
					Bundle b = new Bundle();
					b.putSerializable("dish", dish);
					Intent intent;
					intent = new Intent(SearchActivity.this,DishDetailActivity.class);
					intent.putExtras(b);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}else{
					Toast.makeText(SearchActivity.this, "该餐馆正在忙碌中，为了方便你的用餐，请找空闲的餐馆进行购买", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	public void getCanGuan(){
		names.clear();
		string.clear();
		names.add("restaurantName");
		string.add(name.getText().toString());
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				sorts = ParseXml.getAllEaterySort(names, string, "SerachRestaurantByName");
				adpater = new EateryApdater(SearchActivity.this, sorts);
				if(sorts!=null){
					if(sorts.size()!=0){
						msg.what = MyMessages.SEARCH_RESTAURANT_OK;
					}else{
						msg.what = MyMessages.CHANGE_PWD_FAILD;
					}
					
				}
				else{
					msg.what = MyMessages.TIME_OUT+2;
				}
				handler.sendMessage(msg);
			}
		}).start();
		
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				netWorkState = ConnectionDetector.getNetWorkState(SearchActivity.this);
				if(netWorkState!=null){
					Eatery sortName = (Eatery) list1.getItemAtPosition(arg2);
					if("false".equals(sortName.getEateryBusy())){
						Intent intent;
						Bundle b = new Bundle();
						b.putString("sort", sortName.getEateryName());
						intent = new Intent(SearchActivity.this,SearchFoodsCategoryByRestaurantNameActivity.class);
						intent.putExtras(b);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					}else{
						Toast.makeText(SearchActivity.this, "忙碌中", Toast.LENGTH_SHORT).show();
					}
					
				}else{
					Toast.makeText(SearchActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	//添加购物车
		public void setAddShoppingcarListener(View v) {
			SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
			userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
			netWorkState = ConnectionDetector.getNetWorkState(SearchActivity.this);
			
			if(netWorkState!=null){
				if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
					loginDialog = ProgressDialog.show(SearchActivity.this, "请稍后...", "正在处理...", true, false);
					SharedPreferences shar = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
					String userId = shar.getString(MySharedPreferences.STUDENT_ID, "");//用户id
					final List<String> names = new ArrayList<String>();
					final List<String> zhi = new ArrayList<String>();
					GoodsInfo good = (GoodsInfo) v.getTag();
					names.add("studentId");
					names.add("goodsId");
					names.add("goodsAmount");
					zhi.add(userId);
					zhi.add(good.getGoodsId()+"");
					zhi.add("1");
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							Message msg = new Message();
							boolean reply = ParseXml.getAddShoopingCar(names, zhi, "AddGoodsToShoppingCart");
							if(reply){
								msg.what = MyMessages.COLLECT_OK;
								
							}
							else{
								msg.what = MyMessages.COLLECT_FAILD;
							}
							handler.sendMessage(msg);
						}
					}).start();
				}else{
					Intent intent = new Intent(SearchActivity.this, Login.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}
			}else{
				Toast.makeText(SearchActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
			}
			
			
		}
		public void setAddFoodShoppingcarListener(View v) {
			Dish good = (Dish) v.getTag();
			if(good.getIsBusy().equals("false")){
				SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
				userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
				netWorkState = ConnectionDetector.getNetWorkState(SearchActivity.this);
				
				if(netWorkState!=null){
					if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
				loginDialog = ProgressDialog.show(SearchActivity.this, "请稍后...", "正在处理...", true, false);
				SharedPreferences shar = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
				String userId = shar.getString(MySharedPreferences.STUDENT_ID, "");//用户id
				final List<String> names = new ArrayList<String>();
				final List<String> zhi = new ArrayList<String>();
				
				names.clear();
				names.add("studentId");
				names.add("foodsId");
				names.add("foodsAmount");
				zhi.clear();
				zhi.add(userId);
				zhi.add(good.getDishId()+"");
				zhi.add("1");
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Message msg = new Message();
						boolean reply = ParseXml.getAddShoopingCar(names, zhi, "AddFoodsToShoppingCart");
						if(reply){
							msg.what = MyMessages.COLLECT_OK;
						}
						else{
							msg.what = MyMessages.COLLECT_FAILD;
						}
						handler.sendMessage(msg);
					}
				}).start();
					}else{
						Intent intent = new Intent(SearchActivity.this, Login.class);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					}
				}else{
					Toast.makeText(SearchActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(SearchActivity.this, "餐馆正忙，无法购买。。。。", Toast.LENGTH_SHORT).show();
			}
			
		}
}