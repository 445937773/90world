package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Eatery;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.EateryApdater;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.ParseXml;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class FoodsCategoryResActivity extends Activity implements OnClickListener{
	private String netWorkState;
	private RelativeLayout ly_back;
	private Button btn_shake;
//	private TextView tv_res, tv_category;
//	private ImageView iv_res, iv_category;
	private ListView list;
	private ProgressBar pb_search;
	
	private List<Eatery> list_res = new ArrayList<Eatery>();
//	private List<String> categorys = new ArrayList<String>();
	private EateryApdater eateryAdapter;
//	private SortApdater categoryAdapter;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case MyMessages.GET_RES_OK:
				list.setAdapter(eateryAdapter);
				pb_search.setVisibility(View.GONE);
				list.setOnItemClickListener(resItemClickListener);
				break;
//			case MyMessages.GET_CATEGORY_OK:
//				list.setAdapter(categoryAdapter);
//				pb_search.setVisibility(View.GONE);
//				list.setOnItemClickListener(categoryItemClickListener);
//				break;
			case MyMessages.TIME_OUT:
				pb_search.setVisibility(View.GONE);
				Builder builder = new AlertDialog.Builder(FoodsCategoryResActivity.this);
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	getRes();
			            }
			            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				break;

			default:
				break;
			}
			
		};
	};
//	private OnItemClickListener categoryItemClickListener = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			netWorkState = ConnectionDetector.getNetWorkState(FoodsCategoryResActivity.this);
//			if(netWorkState!=null){
//				Intent intent;
//				Bundle b = new Bundle();
//				String name = (String) list.getItemAtPosition(arg2);
//				intent = new Intent(FoodsCategoryResActivity.this,SortFoodActivity.class);
//				b.putString("sort", name);
//				intent.putExtras(b);
//				startActivity(intent);
//				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//			}else{
//				Toast.makeText(FoodsCategoryResActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
//			}
//		}
//	};
	private OnItemClickListener resItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			netWorkState = ConnectionDetector.getNetWorkState(FoodsCategoryResActivity.this);
			if(netWorkState!=null){
				Intent intent;
				Bundle b = new Bundle();
				Eatery sortName = (Eatery) list.getItemAtPosition(arg2);
				if(sortName.getEateryBusy().equals("false")){
					intent = new Intent(FoodsCategoryResActivity.this,SearchFoodsCategoryByRestaurantNameActivity.class);
					b.putString("sort", sortName.getEateryName());
					intent.putExtras(b);
					startActivity(intent);
				}else{
					Toast.makeText(FoodsCategoryResActivity.this, "该餐馆正在忙碌中，为了方便你的用餐，请找空闲的餐馆进行购买", Toast.LENGTH_SHORT).show();
				}
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}else{
				Toast.makeText(FoodsCategoryResActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foods_category_res);
		MyApplication.getInstance().addActivity(this);
		findView();
		netWorkState = ConnectionDetector.getNetWorkState(FoodsCategoryResActivity.this);
		if(netWorkState!=null){
			getRes();
		}else{
			Toast.makeText(FoodsCategoryResActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
	}

	private void getRes() {
		pb_search.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				list_res = ParseXml.getAllEaterySort(null, null, MyMethods.GET_ALL_RESTAURANT);
				eateryAdapter = new EateryApdater(FoodsCategoryResActivity.this, list_res);
				Message msg = new Message();
				if(list_res != null){
					msg.what = MyMessages.GET_RES_OK;
				}else{
					//超时
					msg.what = MyMessages.TIME_OUT;
				}
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
//	private void getCategory() {
//		pb_search.setVisibility(View.VISIBLE);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				categorys = ParseXml.getDishSort(null, null, MyMethods.GET_ALL_FOOD_CATERGORYS);
//				categoryAdapter = new SortApdater(FoodsCategoryResActivity.this, categorys);
//				Message msg = new Message();
//				if(list_res != null){
//					msg.what = MyMessages.GET_CATEGORY_OK;
//				}else{
//					//超时
//					msg.what = MyMessages.TIME_OUT;
//				}
//				mHandler.sendMessage(msg);
//			}
//		}).start();
//		
//	}
	private void findView() {
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		btn_shake = (Button) findViewById(R.id.btn_shake);
//		tv_res = (TextView) findViewById(R.id.tv_res);
//		tv_category = (TextView) findViewById(R.id.tv_category);
//		iv_res = (ImageView) findViewById(R.id.iv_res);
//		iv_category = (ImageView) findViewById(R.id.iv_category);
		pb_search = (ProgressBar) findViewById(R.id.pb_search);
		list = (ListView) findViewById(R.id.lv_foods_category_res);
		
		ly_back.setOnClickListener(this);
		btn_shake.setOnClickListener(this);
//		tv_res.setOnClickListener(this);
//		tv_category.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly_back:
			onBackPressed();
			break;
		case R.id.btn_shake:
			Intent intent = new Intent(FoodsCategoryResActivity.this,ShakeActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
//		case R.id.tv_res:
//			iv_res.setBackgroundResource(R.drawable.btn_green);
//			iv_category.setBackgroundColor(Color.WHITE);
//			netWorkState = ConnectionDetector.getNetWorkState(FoodsCategoryResActivity.this);
//			if(netWorkState!=null){
//				getRes();
//			}else{
//				Toast.makeText(FoodsCategoryResActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
//			}
//			break;
//		case R.id.tv_category:
//			iv_category.setBackgroundResource(R.drawable.btn_green);
//			iv_res.setBackgroundColor(Color.WHITE);
//			netWorkState = ConnectionDetector.getNetWorkState(FoodsCategoryResActivity.this);
//			if(netWorkState!=null){
//				getCategory();
//			}else{
//				Toast.makeText(FoodsCategoryResActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
//			}
//			break;

		default:
			break;
		}
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
}
