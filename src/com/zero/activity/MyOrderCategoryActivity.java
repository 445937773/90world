package com.zero.activity;

import com.zero.tools.MyApplication;
import com.zero.www.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MyOrderCategoryActivity extends Activity implements OnClickListener{
	private RelativeLayout allOrder, deliveryOrder, takeOrder, completedOrder, ly_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order);
		MyApplication.getInstance().addActivity(this);
		findView();
	}
	private void findView() {
		allOrder = (RelativeLayout) findViewById(R.id.layout_all_order_my_order);
		deliveryOrder = (RelativeLayout) findViewById(R.id.layout_delivery_order_my_order);
		takeOrder = (RelativeLayout) findViewById(R.id.layout_take_delivery_order_my_order);
		completedOrder = (RelativeLayout) findViewById(R.id.layout_completed_order_my_order);
		
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		allOrder.setOnClickListener(this);
		deliveryOrder.setOnClickListener(this);
		takeOrder.setOnClickListener(this);
		completedOrder.setOnClickListener(this);
		
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
		Bundle b = new Bundle();
		Intent intent;
		switch (v.getId()) {
		case R.id.layout_all_order_my_order:
			b.putString("order", "所有的订单");
			break;
			
		case R.id.layout_delivery_order_my_order:
			b.putString("order", "待发货订单");
			break;
			
		case R.id.layout_take_delivery_order_my_order:
			b.putString("order", "已发货订单");
			break;
			
		case R.id.layout_completed_order_my_order:
			b.putString("order", "已完成订单");
			break;
			
		default:
			break;
		}
		intent = new Intent(this, MyOrderListActivity.class);
		intent.putExtras(b);
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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
