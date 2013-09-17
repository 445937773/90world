package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Indent;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.OrderApdater;
import com.zero.tools.ParseXml;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class MyOrderListActivity extends Activity implements OnClickListener{
	private int orderStatus;
	private TextView tv_title;
	private RelativeLayout ly_back;
	private ListView orderList;
	private String whichOrder;
	private List<Indent> goodsIndents;
	private String stuId;
	private FrameLayout no_order;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Builder builder = new AlertDialog.Builder(MyOrderListActivity.this);
			switch (msg.what) {
			case MyMessages.ADD_ADDRESS_OK:
				orderList.setAdapter(order);
				loginDialog.dismiss();
				break;
			case MyMessages.ADD_ADDRESS_FAILD:
				loginDialog.dismiss();
				no_order.setVisibility(View.VISIBLE);
				break;
			case MyMessages.TIME_OUT+1:
				builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	getOrderByStatus();
		            }
		            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
			            
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	onBackPressed();
			            }
			        }).show();
				break;
			case MyMessages.TIME_OUT+3:
				builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	getAllOrder();  
		            }
		            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
			            
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	onBackPressed();
			            }
			        }).show();
				break;
			}
			
		}
	};
	protected OrderApdater order;
	private ProgressDialog loginDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loginDialog = ProgressDialog.show(MyOrderListActivity.this, "请稍后...", "正在处理...", true, false);
		setContentView(R.layout.order_list);
		MyApplication.getInstance().addActivity(this);
		//获取控件
		findView();
		
		//获取bundle数据
		getBundle();
		
	}

	private void getOrderByStatus() {
		//用同一个方法，把orderStatus传过去。
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				List<String> p = new ArrayList<String>();
				List<String> values = new ArrayList<String>();
				p.add("studentId");
				p.add("orderStatus");
				values.add(stuId);
				values.add(orderStatus+"");
				goodsIndents = ParseXml.getIndentOrderStatus(p, values, "GetOrderFormsByStatues");
				if(goodsIndents!=null){
					if(goodsIndents.size() > 0){
						msg.what = MyMessages.ADD_ADDRESS_OK;
						order = new OrderApdater(MyOrderListActivity.this, goodsIndents);
					}else{
						msg.what = MyMessages.ADD_ADDRESS_FAILD;
					}
					
				}else{
					msg.what = MyMessages.TIME_OUT+1;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void getAllOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				List<String> p = new ArrayList<String>();
				List<String> values = new ArrayList<String>();
				p.add("studentId");
				values.add(stuId);
				
				goodsIndents = ParseXml.getIndent(p, values, "GetOrderFormsByStudentId");
				if(goodsIndents!=null){
					if(goodsIndents.size() > 0){
						msg.what = MyMessages.ADD_ADDRESS_OK;
						order = new OrderApdater(MyOrderListActivity.this, goodsIndents);
					}else{
						msg.what = MyMessages.ADD_ADDRESS_FAILD;
					}
					
				}else{
					msg.what = MyMessages.TIME_OUT+3;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getBundle();
	}
	private void getBundle() {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		stuId = userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
		
		Bundle b = getIntent().getExtras();
		whichOrder = b.getString("order");
		tv_title.setText(whichOrder);
		if("待发货订单".equals(whichOrder)){
			orderStatus = 0;
			getOrderByStatus();
		}
		if("已发货订单".equals(whichOrder)){
			orderStatus = 1;
			getOrderByStatus();
		}
		if("已完成订单".equals(whichOrder)){
			orderStatus = 2;
			getOrderByStatus();
		}
		if("所有的订单".equals(whichOrder)){
			getAllOrder();
		}
	}

	private void findView() {
		tv_title = (TextView) findViewById(R.id.tv_title_order_list);
		orderList = (ListView) findViewById(R.id.lv_addressList_order_list);
		no_order = (FrameLayout) findViewById(R.id.ly_no_order);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		ly_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		
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
