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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class GoodscategorySmallActivity extends Activity {

	ListView list;
	ProgressBar pb_search;
	TextView title;
	RelativeLayout ly_back;
	protected SortApdater ad;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.GOODS_SMALL_OK:
				list.setAdapter(ad);
				pb_search.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				r1.setVisibility(View.GONE);
				break;

			case MyMessages.GOODS_SMALL_FAILD:
				pb_search.setVisibility(View.GONE);
				r1.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
				break;
			case MyMessages.TIME_OUT:
				pb_search.setVisibility(View.GONE);
				Builder builder = new AlertDialog.Builder(GoodscategorySmallActivity.this);
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	smallGoods();
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
	private String categoryName;
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
		//获取上一级分类名称
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		categoryName = b.getString("names");
		title.setText(categoryName);
		
		smallGoods();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				netWorkState = ConnectionDetector.getNetWorkState(GoodscategorySmallActivity.this);
				if(netWorkState!=null){
					Intent intent1 = getIntent();
					Bundle b1 = intent1.getExtras();
					String name = (String) list.getItemAtPosition(arg2);
					Bundle b = new Bundle();
					b.putString("name", name);
					Intent intent;
					intent = new Intent(GoodscategorySmallActivity.this,GoodsLastCategoryActivity.class);
					intent.putExtras(b);
					intent.putExtras(b1);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}else{
					Toast.makeText(GoodscategorySmallActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
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
	
	public void smallGoods(){
		pb_search.setVisibility(View.VISIBLE);
		
		final List<String> names = new ArrayList<String>();
		names.add("parentCategoryName");
		final List<String> zhi = new ArrayList<String>();
		zhi.add(categoryName);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				List<String> smalls = ParseXml.getSmallGoodsSort(names,zhi,MyMethods.GET_GOODS_SMALL_CATEGORYS);
				ad = new SortApdater(GoodscategorySmallActivity.this, smalls);
				if(smalls!=null){
					
					if(smalls.size()!=0){
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
}
