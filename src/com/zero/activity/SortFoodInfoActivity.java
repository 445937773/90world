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
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class SortFoodInfoActivity extends Activity {

	ListView list;
	//List<DishSort> sorts = new ArrayList<DishSort>();
	TextView title;
	ProgressBar pb_search;
	RelativeLayout ly_back;
	List<String> sorts = new ArrayList<String>();
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.CHANGE_PWD_OK:
				list.setVisibility(View.VISIBLE);
				Intent intent = getIntent();
				Bundle b1 = intent.getExtras();
				if(b1.getString("name").equals("name")){
					list.setAdapter(ad);
				}else{
					list.setAdapter(adpater);
				}
				
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
				Builder builder = new AlertDialog.Builder(SortFoodInfoActivity.this);
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
	protected ArrayAdapter<String> ad;
	protected String netWorkState;
	protected List<Eatery> eaterys;
	protected EateryApdater adpater;
	private RelativeLayout r1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort);
		MyApplication.getInstance().addActivity(this);
		list = (ListView) findViewById(R.id.lv_sort);
		title = (TextView) findViewById(R.id.tv_title_sort);
		r1 = (RelativeLayout) findViewById(R.id.no_sort_item);
		pb_search = (ProgressBar) findViewById(R.id.pb_search);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		getView();
		Intent intent = getIntent();
		final Bundle b1 = intent.getExtras();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				netWorkState = ConnectionDetector.getNetWorkState(SortFoodInfoActivity.this);
				if(netWorkState!=null){
					
					Intent intent;
					Bundle b = new Bundle();
					
					if(b1.getString("name").equals("name")){
						String name = (String) list.getItemAtPosition(arg2);
						intent = new Intent(SortFoodInfoActivity.this,SortFoodActivity.class);
						b.putString("sort", name);
						intent.putExtras(b);
						startActivity(intent);
					}else{
						Eatery sortName = (Eatery) list.getItemAtPosition(arg2);
						if(sortName.getEateryBusy().equals("false")){
							intent = new Intent(SortFoodInfoActivity.this,SearchFoodsCategoryByRestaurantNameActivity.class);
							b.putString("sort", sortName.getEateryName());
							intent.putExtras(b);
							startActivity(intent);
						}else{
							Toast.makeText(SortFoodInfoActivity.this, "该餐馆正在忙碌中，为了方便你的用餐，请找空闲的餐馆进行购买", Toast.LENGTH_SHORT).show();
						}
						
					}
					
					
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}else{
					Toast.makeText(SortFoodInfoActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
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
		final Bundle b1 = intent.getExtras();
		pb_search.setVisibility(View.VISIBLE);
		//String s = b.getString("name");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				if(b1.getString("name").equals("name")){
					title.setText("按类别");
					sorts = ParseXml.getDishSort(null, null, MyMethods.GET_ALL_FOOD_CATERGORYS);
					ad = new ArrayAdapter<String>(SortFoodInfoActivity.this, android.R.layout.simple_list_item_1, sorts);
				}else{
					title.setText("按餐馆");
					eaterys = ParseXml.getAllEaterySort(null, null, MyMethods.GET_ALL_RESTAURANT);
					adpater = new EateryApdater(SortFoodInfoActivity.this, eaterys);
				}
				if(sorts!=null||eaterys!=null){
					if(sorts.size()!=0||eaterys.size()!=0){
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
}
