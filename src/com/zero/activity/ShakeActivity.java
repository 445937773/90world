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
import com.zero.tools.ShakeListener;
import com.zero.tools.SortFoodApdater;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ShakeActivity extends Activity implements com.zero.tools.ShakeListener.OnShakeListener{


	ShakeListener shake;
	ListView list;
	ImageLoader mImageLoader;
	ImageView shake_icon;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.GOODS_SMALL_OK:
				list.setVisibility(View.VISIBLE);
				list.setAdapter(search);
				loginDialog.dismiss();
				Vibrator mVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				mVibrator.vibrate(500);
				shake.start();
				r1.setVisibility(View.GONE);
				break;

			case MyMessages.GOODS_SMALL_FAILD:
				list.setVisibility(View.GONE);
				loginDialog.dismiss();
				r1.setVisibility(View.VISIBLE);
				break;
			case MyMessages.TIME_OUT:
				loginDialog.dismiss();
				Builder builder = new AlertDialog.Builder(ShakeActivity.this);
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	onShake();
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
				Toast.makeText(ShakeActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
				break;
				
			case MyMessages.COLLECT_FAILD:
				loginDialog.dismiss();
				Toast.makeText(ShakeActivity.this, "对不起，添加失败，请重试！", Toast.LENGTH_SHORT).show();
				break;
				
			default:
				break;
			}
		}
	};
	protected SortFoodApdater search;
	protected String netWorkState;
	private ProgressDialog loginDialog;
	TextView text;
	private RelativeLayout r1;
	RelativeLayout ly_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort);
		MyApplication.getInstance().addActivity(this);
		list = (ListView) findViewById(R.id.lv_sort);
		text = (TextView) findViewById(R.id.tv_title_sort);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		text.setText("摇一摇");
		r1 = (RelativeLayout) findViewById(R.id.no_sort_item);
		r1.setVisibility(View.VISIBLE);
		TextView empty = (TextView) findViewById(R.id.tv_shake_null);
		empty.setText("让我们一起摇啊摇啊摇啊摇~");
		shake_icon = (ImageView) findViewById(R.id.shopping_cat_icon);
		shake_icon.setImageResource(R.drawable.shake_icon);
		//show();
		shake = new ShakeListener(ShakeActivity.this);
		shake.setOnShakeListener(ShakeActivity.this);
		shake.start();
		ly_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				netWorkState = ConnectionDetector.getNetWorkState(ShakeActivity.this);
				if(netWorkState!=null){
					Dish dish = (Dish) list.getItemAtPosition(arg2);
					if(dish.getIsBusy().equals("false")){
						Bundle b = new Bundle();
						b.putSerializable("dish", dish);
						b.putString("fanhui", "yaoyiyao");
						Intent intent;
						intent = new Intent(ShakeActivity.this,DishDetailActivity.class);
						intent.putExtras(b);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					}else{
						Toast.makeText(ShakeActivity.this, "该餐馆正在忙碌中，为了方便你的用餐，请找空闲的餐馆进行购买", Toast.LENGTH_SHORT).show();
					}
					
				}else{
					Toast.makeText(ShakeActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		OnScrollListener onScrollListener = new OnScrollListener() {
	        @Override
	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	        	mImageLoader = ImageLoader.getInstance(ShakeActivity.this);
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
	public void onShake() {
		shake.stop();
		loginDialog = ProgressDialog.show(ShakeActivity.this, "请稍后...", "正在处理...", true, false);
		
		final List<String> names = new ArrayList<String>();
		names.add("getCount");
		final List<String> zhi = new ArrayList<String>();
		zhi.add("5");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				List<Dish> dishs = ParseXml.getDish(names, zhi, MyMethods.GET_RANDOM_FOODS);
				search = new SortFoodApdater(ShakeActivity.this, dishs, handler);
				if(dishs!=null){
					
					if(dishs.size()!=0){
						msg.what = MyMessages.GOODS_SMALL_OK;
					}else{
						msg.what = MyMessages.GOODS_SMALL_FAILD;
					}
				}
				else{
					msg.what = MyMessages.TIME_OUT;
				}
				handler.sendMessage(msg);
				
			}
		}).start();
	}

	@Override
	protected void onStop() {
		shake.stop();
		super.onStop();
	}
	@Override
	protected void onRestart() {
		shake.start();
		super.onRestart();
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
	
	public void setAddFoodShoppingcarListener(View v) {
		Dish good = (Dish) v.getTag();
		if(good.getIsBusy().equals("false")){
			SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
			userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
			netWorkState = ConnectionDetector.getNetWorkState(ShakeActivity.this);
			
			if(netWorkState!=null){
				if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
				loginDialog = ProgressDialog.show(ShakeActivity.this, "请稍后...", "正在处理...", true, false);
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
					Intent intent = new Intent(ShakeActivity.this, Login.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}
			}else{
				Toast.makeText(ShakeActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(ShakeActivity.this, "餐馆正忙，无法购买。。。。", Toast.LENGTH_SHORT).show();
		}
		
	}
}
