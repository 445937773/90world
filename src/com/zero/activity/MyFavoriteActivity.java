package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.FoodsFavorite;
import com.zero.bean.GoodsFavorite;
import com.zero.cache.ImageLoader;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.FoodsFavoriteApdater;
import com.zero.tools.GoodsFavoriteApdater;
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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MyFavoriteActivity extends Activity implements OnClickListener{
	ImageLoader mImageLoader;
	Button jiesuan;
	RelativeLayout ly_back;
	TextView shangpin,kuancan;
	ImageView iv_foods, iv_goods;
	CheckBox quanxuan;
	ListView list;
	List<Integer> listItemID = new ArrayList<Integer>();
	private ProgressDialog loginDialog;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			Builder builder = new AlertDialog.Builder(MyFavoriteActivity.this);
			switch (msg.what) {
			case MyMessages.CHANGE_PWD_OK:
				r.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				if(goods.size()==1){
					list.setAdapter(goodsfa);
					foods.clear();
					quanxuan.setVisibility(View.GONE);
					loginDialog.dismiss();
				}else{
					list.setAdapter(goodsfa);
					foods.clear();
					quanxuan.setVisibility(View.VISIBLE);
					loginDialog.dismiss();
				}
				break;

			case MyMessages.CHANGE_PWD_FAILD:
				loginDialog.dismiss();
				quanxuan.setVisibility(View.GONE);
				r.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
				break;
			case MyMessages.GOODS_SMALL_OK:
				Toast.makeText(MyFavoriteActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				loginDialog.dismiss();
				getView();
				break;
			case MyMessages.GOODS_SMALL_FAILD:
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	getView();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				loginDialog.dismiss();
				break;
			case MyMessages.DELETE_FAVORITE_OK:
				Toast.makeText(MyFavoriteActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				loginDialog.dismiss();
				getFoodsView();
				break;
			case MyMessages.DELETE_FAVORITE_FAILD:
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	getFoodsView();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				loginDialog.dismiss();
				break;
			case MyMessages.DELETE_FOODS_OK:
				r.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				if(foods.size()==1){
					list.setAdapter(f);
					goods.clear();
					quanxuan.setVisibility(View.GONE);
					
					loginDialog.dismiss();
				}else{
					list.setAdapter(f);
					goods.clear();
					quanxuan.setVisibility(View.VISIBLE);
					loginDialog.dismiss();
				}
				
				break;
			case MyMessages.DELETE_FOODS_FAILD:
				loginDialog.dismiss();
				quanxuan.setVisibility(View.GONE);
				r.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
				break;
			case MyMessages.TIME_OUT:
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	getView();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				break;
			case MyMessages.TIME_OUT+1:
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	getFoodsView();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				break;
			default:
				break;
			}
		}
	
	};
	FoodsFavoriteApdater f;
	RelativeLayout r;
	String userId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_favorite);
		loginDialog = ProgressDialog.show(MyFavoriteActivity.this, "请稍后...", "正在处理...", true, false);
		MyApplication.getInstance().addActivity(this);
		SharedPreferences shar = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		userId = shar.getString(MySharedPreferences.STUDENT_ID, "");//用户id
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		quanxuan = (CheckBox) findViewById(R.id.btn_all_select_my_favorite);
		jiesuan = (Button) findViewById(R.id.btn_delete_my_favorite);
		shangpin = (TextView) findViewById(R.id.rb_goods_select);
		kuancan = (TextView) findViewById(R.id.rb_food_select);
		list = (ListView) findViewById(R.id.lv_favoritelist_my_favorite);
		r = (RelativeLayout) findViewById(R.id.no_cart_item);
		iv_foods = (ImageView) findViewById(R.id.iv_foods);
		iv_goods = (ImageView) findViewById(R.id.iv_goods);
		getView();
		ly_back.setOnClickListener(this);
		quanxuan.setOnClickListener(this);
		jiesuan.setOnClickListener(this);
		shangpin.setOnClickListener(this);
		kuancan.setOnClickListener(this);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(goods.size()!=0){
					GoodsFavorite goods = (GoodsFavorite) list.getItemAtPosition(arg2);
					Intent intent = new Intent(MyFavoriteActivity.this,GoodsParticularInfoActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("goods", goods);
					intent.putExtras(b);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}else{
					FoodsFavorite goods = (FoodsFavorite) list.getItemAtPosition(arg2);
					Intent intent = new Intent(MyFavoriteActivity.this,DishDetailActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("goods", goods);
					intent.putExtras(b);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}
				
			}
		});
		
		OnScrollListener onScrollListener = new OnScrollListener() {
	        @Override
	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	        	mImageLoader = ImageLoader.getInstance(MyFavoriteActivity.this);
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
	    list.setOnScrollListener(onScrollListener);
	    
		quanxuan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(goods.size()!=0){
					if(isChecked){
						for(int i=0;i<goods.size();i++){
							goodsfa.mChecked.set(i,true);
			            	
			            }
						quanxuan.setText("全不选");
					}else{
						for(int i=0;i<goods.size();i++){
							goodsfa.mChecked.set(i,false);
		            	
						}
						quanxuan.setText("全选");
					}
					goodsfa.notifyDataSetChanged();
				
				}else{
					if(isChecked){
						for(int i=0;i<foods.size();i++){
							f.mChecked.set(i,true);
			            	
			            }
						quanxuan.setText("全不选");
					}else{
						for(int i=0;i<foods.size();i++){
							f.mChecked.set(i,false);
		            	
						}
						quanxuan.setText("全选");
					}
					f.notifyDataSetChanged();
				}
			}
		});
		
	}
	boolean b;
	List<String> foodId = new ArrayList<String>();
	List<FoodsFavorite> foods = new ArrayList<FoodsFavorite>();
	List<GoodsFavorite> goods = new ArrayList<GoodsFavorite>();
	protected GoodsFavoriteApdater goodsfa;
	private String netWorkState;
	public void getView(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				Message msg = new Message();
				List<String> names = new ArrayList<String>();
				names.add("studentId");
				List<String> zhi = new ArrayList<String>();
				zhi.add(userId);
				goods = ParseXml.getGoodsPavoriteInfo(names, zhi, "GetStudentsFavoriteGoods");
				goodsfa = new GoodsFavoriteApdater(MyFavoriteActivity.this, goods, BitmapFactory.decodeResource(getResources(), R.drawable.default_pic), handler);
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
	}
	public void getFoodsView(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				Message msg = new Message();
				List<String> names = new ArrayList<String>();
				names.add("studentId");
				List<String> zhi = new ArrayList<String>();
				zhi.add(userId);
				foods = ParseXml.getFoodsPavoriteInfo(names, zhi, "GetStudentsFavoriteFoods");
				f = new FoodsFavoriteApdater(MyFavoriteActivity.this, foods, handler);
				if(foods!=null){
					if(foods.size()!=0){
						msg.what = MyMessages.DELETE_FOODS_OK;
					}else{
						msg.what = MyMessages.DELETE_FOODS_FAILD;
					}
					
				}
				else{
					msg.what = MyMessages.TIME_OUT+1;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MainActivity.instance.onCreateOptionsMenu(menu);
		return true;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		finish();
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		MainActivity.setActivity(this);
		MainActivity.instance.onMenuItemSelected(featureId, item);
		return true;
	}
	@Override
	public void onClick(View v) {
		netWorkState = ConnectionDetector.getNetWorkState(MyFavoriteActivity.this);
		if(netWorkState!=null){
			int id = v.getId();
			switch (id) {
			case R.id.ly_back:
				onBackPressed();
				break;
			case R.id.btn_all_select_my_favorite:
				
				
				break;
			case R.id.btn_delete_my_favorite:
				loginDialog = ProgressDialog.show(MyFavoriteActivity.this, "请稍后...", "正在处理...", true, false);
				if(goods.size()!=0){
					listItemID.clear();  
		            for(int i=0;i<goodsfa.mChecked.size();i++){  
		                if(goodsfa.mChecked.get(i)){  
		                    listItemID.add(i);  
		                }  
		            }  
		              
		            if(listItemID.size()==0){  
		                AlertDialog.Builder builder1 = new AlertDialog.Builder(MyFavoriteActivity.this);  
		                builder1.setMessage("没有选中任何记录");  
		                builder1.show(); 
		                loginDialog.dismiss();
		            }else{
		            	if(goods!=null){
		            		for(int i=0;i<listItemID.size();i++){  
		    	                GoodsFavorite car1 = (GoodsFavorite) list.getItemAtPosition(listItemID.get(i));
		    	                foodId.add(car1.getGoods()+"");
		                		}  
		            	}else{
		            		for(int i=0;i<listItemID.size();i++){  
			                FoodsFavorite car1 = (FoodsFavorite) list.getItemAtPosition(listItemID.get(i));
			                foodId.add(car1.getFoodId()+"");
		            		}  
		            	}
		            	
		            new Thread(new Runnable() {
						
						@Override
						public void run() {
							Message msg = new Message();
							List<String> names = new ArrayList<String>();
							List<String> zhi = new ArrayList<String>();
							
							names.add("studentId");
							names.add("goodsIdList");
							zhi.add(userId);
							String goodsids = null;
							for (int i = 0; i < foodId.size(); i++) {
								if(goodsids==null){
									goodsids  =foodId.get(i);
								}else{
									goodsids +=","+foodId.get(i);	
								}
								
							}
							zhi.add(goodsids);
							b = ParseXml.getDeleteGoodsFavorite(names, zhi, "DeleteFavoriteGoods");
							
							if(b){
								msg.what = MyMessages.GOODS_SMALL_OK;
							}
							else{
								msg.what = MyMessages.GOODS_SMALL_FAILD;
							}
							handler.sendMessage(msg);
						}
						}).start();
		            }
				}else if(foods.size()!=0){
					listItemID.clear();  
		            for(int i=0;i<f.mChecked.size();i++){  
		                if(f.mChecked.get(i)){  
		                    listItemID.add(i);  
		                }  
		            }  
		              
		            if(listItemID.size()==0){  
		                AlertDialog.Builder builder1 = new AlertDialog.Builder(MyFavoriteActivity.this);  
		                builder1.setMessage("没有选中任何记录");  
		                builder1.show();  
		                loginDialog.dismiss();
		            }else{
		            		for(int i=0;i<listItemID.size();i++){  
			                FoodsFavorite car1 = (FoodsFavorite) list.getItemAtPosition(listItemID.get(i));
			                foodId.add(car1.getFoodId()+"");
		            	}
		            	
		            new Thread(new Runnable() {
						
						@Override
						public void run() {
							Message msg = new Message();
							List<String> names = new ArrayList<String>();
							List<String> zhi = new ArrayList<String>();
							
							names.add("studentId");
							names.add("foodsIdList");
							
							for (int i = 0; i < foodId.size(); i++) {
								zhi.clear();
								zhi.add(userId);
								zhi.add(foodId.get(i));
								b = ParseXml.getDeleteFoodsFavorite(names, zhi, "DeleteFavoriteFoods");
							}
							
							
							if(b){
								msg.what = MyMessages.DELETE_FAVORITE_OK;
							}
							else{
								msg.what = MyMessages.DELETE_FAVORITE_FAILD;
							}
							handler.sendMessage(msg);
						}
						}).start();
		            }
				}else{
					Toast.makeText(MyFavoriteActivity.this, "收藏夹为空", Toast.LENGTH_SHORT).show();
					loginDialog.dismiss();
				}
				
	         
				break;
			case R.id.rb_goods_select:
				loginDialog = ProgressDialog.show(MyFavoriteActivity.this, "请稍后...", "正在处理...", true, false);
				iv_foods.setBackgroundResource(R.drawable.btn_green);
				iv_goods.setBackgroundColor(Color.WHITE);
				getView();
				break;
			case R.id.rb_food_select:
				loginDialog = ProgressDialog.show(MyFavoriteActivity.this, "请稍后...", "正在处理...", true, false);
				iv_goods.setBackgroundResource(R.drawable.btn_green);
				iv_foods.setBackgroundColor(Color.WHITE);
				getFoodsView();
				break;
			default:
				break;
			}
		}else{
			Toast.makeText(MyFavoriteActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		
	}
}
