package com.zero.activity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class GoodsCategoryActivity extends Activity {
	ListView vlist;
	TextView tv_title;
	RelativeLayout ly_back;
	String where;
	ProgressBar pb_search;
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.GOODS_BIG_OK:
				vlist.setAdapter(ad);
				pb_search.setVisibility(View.GONE);
				vlist.setVisibility(View.VISIBLE);
				r1.setVisibility(View.GONE);
				break;

			case MyMessages.GOODS_BIG_FAILD:
				vlist.setVisibility(View.GONE);
				pb_search.setVisibility(View.GONE);
				r1.setVisibility(View.VISIBLE);
				break;
			case MyMessages.TIME_OUT:
				pb_search.setVisibility(View.GONE);
				Builder builder = new AlertDialog.Builder(GoodsCategoryActivity.this);
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	showList("超市");
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
	protected SortApdater ad;
	protected Object netWorkState;
	private RelativeLayout r1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort);
		MyApplication.getInstance().addActivity(this);
		r1 = (RelativeLayout) findViewById(R.id.no_sort_item);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		b.getString("goto");
		where = b.getString("goto");
		findView(where);
		showList(where);
		
		pb_search.setVisibility(View.VISIBLE);
		vlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				netWorkState = ConnectionDetector.getNetWorkState(GoodsCategoryActivity.this);
				if(netWorkState!=null){
					String name = (String) vlist.getItemAtPosition(arg2);
					Bundle b = new Bundle();
					b.putString("names", name);
					Intent intent;
					intent = new Intent(GoodsCategoryActivity.this,GoodscategorySmallActivity.class);
					intent.putExtras(b);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}else{
					Toast.makeText(GoodsCategoryActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
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

	private void showList(final String where) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if("超市".equals(where)){
					Message msg = new Message();
					List<String> bigs = ParseXml.getBigGoodsSort(null,null,MyMethods.GET_GOODS_CATEGORYS);
					ad = new SortApdater(GoodsCategoryActivity.this, bigs);
					if(bigs!=null){
						
						if(bigs.size()!=0){
							msg.what = MyMessages.GOODS_BIG_OK;
						}else{
							msg.what = MyMessages.GOODS_BIG_FAILD;
						}
					}
					else{
						msg.what = MyMessages.TIME_OUT;
					}
					handler.sendMessage(msg);
				}
				else if("数码".equals(where)){
					
				}
				else if("特色".equals(where)){
					
				}
			}
		}).start();
		
	}

	private void findView(String where) {
		vlist = (ListView) findViewById(R.id.lv_sort);
		tv_title = (TextView) findViewById(R.id.tv_title_sort);
		tv_title.setText(where);
		pb_search = (ProgressBar) findViewById(R.id.pb_search);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
	}
	@Override
	public void onBackPressed() {
		Intent intent1 = getIntent();
		Bundle b = intent1.getExtras();
		Intent intent = new Intent(GoodsCategoryActivity.this, MainActivity.class);
		intent.putExtras(b);
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
}
