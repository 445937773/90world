package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Dish;
import com.zero.cache.ImageLoader;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
import com.zero.tools.SortFoodApdater;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class SortFoodActivity extends Activity{

	ListView list;
	List<Dish> dishs;
	TextView title;
	RelativeLayout ly_back;
	String names = null;
	String name1=null;
	ImageLoader mImageLoader;
	protected SortFoodApdater search;
	ProgressBar pb_search;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			Builder builder = new AlertDialog.Builder(SortFoodActivity.this);
			switch (msg.what) {
			case MyMessages.CHANGE_PWD_OK:
				list.setVisibility(View.VISIBLE);
				list.setAdapter(search);
				pb_search.setVisibility(View.GONE);
				r1.setVisibility(View.GONE);
				break;

			case MyMessages.CHANGE_PWD_FAILD:
				list.setVisibility(View.GONE);
				pb_search.setVisibility(View.GONE);
				r1.setVisibility(View.VISIBLE);
				break;
			case MyMessages.TIME_OUT:
				pb_search.setVisibility(View.GONE);
				
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
			case MyMessages.COLLECT_OK:
				loginDialog.dismiss();
				Toast.makeText(SortFoodActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
				break;
			case MyMessages.COLLECT_FAILD:
				loginDialog.dismiss();
				Toast.makeText(SortFoodActivity.this, "加入购物车失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	
	};
	protected Object netWorkState;
	private RelativeLayout r1;
	private ProgressDialog loginDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort);
		MyApplication.getInstance().addActivity(this);
		list = (ListView) findViewById(R.id.lv_sort);
		pb_search = (ProgressBar) findViewById(R.id.pb_search);
		title = (TextView) findViewById(R.id.tv_title_sort);
		r1 = (RelativeLayout) findViewById(R.id.no_sort_item);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		if(b.getString("sort1")!=null){
			title.setText(b.getString("sort1").trim());
		}else{
			title.setText(b.getString("sort").trim());
		}
		pb_search.setVisibility(View.VISIBLE);
		
		getView();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				netWorkState = ConnectionDetector.getNetWorkState(SortFoodActivity.this);
				if(netWorkState!=null){
					Dish dish = (Dish) list.getItemAtPosition(arg2);
					if(dish.getIsBusy().equals("false")){
						Bundle b = new Bundle();
						b.putSerializable("dish", dish);
						Intent intent;
						intent = new Intent(SortFoodActivity.this,DishDetailActivity.class);
						intent.putExtras(b);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					}else{
						Toast.makeText(SortFoodActivity.this, "该餐馆正在忙碌中，为了方便你的用餐，请找空闲的餐馆进行购买", Toast.LENGTH_SHORT).show();
					}
					
				}else{
					Toast.makeText(SortFoodActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		ly_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}
		});
		
		OnScrollListener onScrollListener = new OnScrollListener() {
	        @Override
	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	        	mImageLoader = ImageLoader.getInstance(SortFoodActivity.this);
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
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
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
	
	public void getView(){
		Intent intent = getIntent();
		final Bundle b = intent.getExtras();
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				List<String> zhi = new ArrayList<String>();
				List<String> name = new ArrayList<String>();
				Message msg = new Message();
				if(b!=null){
					if(b.getString("sort1")!=null){//根据餐馆和类别
						names = b.getString("sort");
						name1 = b.getString("sort1");
						zhi.add(names);
						zhi.add(name1);
						name.add("restaurantName");
						name.add("categoryName");
						
						dishs = ParseXml.getDish(name, zhi, MyMethods.SEARCH_FOODS_BY_RESTAURANT_AND_FOOD);
					}else{//根据类别
						name.clear();
						zhi.clear();
						name.add("categoryName");
						names = b.getString("sort");
						zhi.add(names);
						dishs = ParseXml.getDish(name, zhi, MyMethods.SEARCH_FOOD_BY_FOOD_CATEGORY);
					}
					
				}
				search = new SortFoodApdater(SortFoodActivity.this, dishs, handler);
				if(dishs!=null){
					if(dishs.size()!=0){
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
	
	public void setAddFoodShoppingcarListener(View v) {
		Dish good = (Dish) v.getTag();
		if(good.getIsBusy().equals("false")){
			SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
			userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
			netWorkState = ConnectionDetector.getNetWorkState(SortFoodActivity.this);
			
			if(netWorkState!=null){
				if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
			loginDialog = ProgressDialog.show(SortFoodActivity.this, "请稍后...", "正在处理...", true, false);
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
					Intent intent = new Intent(SortFoodActivity.this, Login.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}
			}else{
				Toast.makeText(SortFoodActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(SortFoodActivity.this, "餐馆正忙，无法购买。。。。", Toast.LENGTH_SHORT).show();
		}
		
	}
}
