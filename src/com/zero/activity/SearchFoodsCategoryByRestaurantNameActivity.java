package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.ParseXml;
import com.zero.tools.SortApdater;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class SearchFoodsCategoryByRestaurantNameActivity extends Activity {

	ListView list;
	//List<DishSort> sorts = new ArrayList<DishSort>();
	TextView title;
	RelativeLayout ly_back;
	List<String> sorts = new ArrayList<String>();
	protected SortApdater ad;
	ProgressBar pb_search;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.CHANGE_PWD_OK:
				list.setVisibility(View.VISIBLE);
				list.setAdapter(ad);
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
				Builder builder = new AlertDialog.Builder(SearchFoodsCategoryByRestaurantNameActivity.this);
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
			default:
				break;
			}
		}
	
	};
	protected String netWorkState;
	private RelativeLayout r1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort);
		MyApplication.getInstance().addActivity(this);
		list = (ListView) findViewById(R.id.lv_sort);
		pb_search = (ProgressBar) findViewById(R.id.pb_search);
		title = (TextView) findViewById(R.id.tv_title_sort);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		pb_search.setVisibility(View.VISIBLE);
		r1 = (RelativeLayout) findViewById(R.id.no_sort_item);
		Intent intent = getIntent();
		final Bundle b1 = intent.getExtras();
		getView();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				netWorkState = ConnectionDetector.getNetWorkState(SearchFoodsCategoryByRestaurantNameActivity.this);
				if(netWorkState!=null){
					String sortName = (String) list.getItemAtPosition(arg2);
					Intent intent;
					Bundle b = new Bundle();
					
					b.putString("sort1", sortName);
					intent = new Intent(SearchFoodsCategoryByRestaurantNameActivity.this,SortFoodActivity.class);
					
					intent.putExtras(b1);
					intent.putExtras(b);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}else{
					Toast.makeText(SearchFoodsCategoryByRestaurantNameActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		ly_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
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
	public void getView(){
		Intent intent = getIntent();
		final Bundle b1 = intent.getExtras();
		final List<String> names = new ArrayList<String>();
		names.add("restaurantName");
		final List<String> zhi = new ArrayList<String>();
		zhi.add(b1.getString("sort"));
		title.setText(b1.getString("sort").trim());
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				sorts = ParseXml.getDishSort(names, zhi, MyMethods.SEARCH_FOODS_CATEGORY_BY_RESTAURANT);
				if(sorts!=null){
					if(sorts.size()!=0){
						msg.what = MyMessages.CHANGE_PWD_OK;
						ad = new SortApdater(SearchFoodsCategoryByRestaurantNameActivity.this, sorts);
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
}
