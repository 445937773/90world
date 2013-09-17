package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Indent;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class IndentInfoActivity extends Activity {

	TextView state,indentid,adress,sum,zhuangtai,tv_comment;
	RelativeLayout ly_back;
	LinearLayout Button01, Button02;
	private ProgressDialog loginDialog;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.ADD_ADDRESS_OK:
				
				break;
			}
		}
	};
	TextView sum1,sum2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loginDialog = ProgressDialog.show(IndentInfoActivity.this, "请稍后...", "正在处理...", true, false);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.order_commit_succeed);
		state = (TextView) findViewById(R.id.detail_order_status);
		tv_comment = (TextView) findViewById(R.id.tv_comment_order_detail);
		indentid = (TextView) findViewById(R.id.detail_order_id_number_comment);
		adress = (TextView) findViewById(R.id.detail_order_address_comment);
		zhuangtai = (TextView) findViewById(R.id.detail_order_status_comment);
		sum = (TextView) findViewById(R.id.product_all_money_content);
		Button01 = (LinearLayout) findViewById(R.id.Button01);
		Button02 = (LinearLayout) findViewById(R.id.btn_order_submit_order_produce);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		sum1 = (TextView) findViewById(R.id.cheap_money_content);
		sum2 = (TextView) findViewById(R.id.should_pay_content);
		geyView();
		loginDialog.dismiss();
		Button01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Bundle b = new Bundle();
//				b.putString("check", "shoppingCart");
				Intent intent = new Intent(IndentInfoActivity.this, MainActivity.class);
//				intent.putExtras(b);
				startActivity(intent);
				finish();
			}
		});
		Button02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = getIntent();
				Bundle bu = intent1.getExtras();
				Intent intent = new Intent(IndentInfoActivity.this,IndentDetailsActivity.class);
				intent.putExtras(bu);
				startActivity(intent);
				finish();
			}
		});
		ly_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	Indent indent;
	List<String> name = new ArrayList<String>();
	List<String> zhi = new ArrayList<String>();
	public void geyView(){
		Intent intent = getIntent();
		Bundle bun = intent.getExtras();
		indent = (Indent) bun.getSerializable("indent1");
		indentid.setText(indent.getOrderFormNumber()+"");
		adress.setText(indent.getAdressId().getAddressInfo());
		zhuangtai.setText("待发货");
		sum.setText(indent.getSum()+"");
		sum1.setText("0");
		sum2.setText(indent.getSum()+"");
		tv_comment.setText(indent.getReserve());
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Bundle b = new Bundle();
		b.putString("check", "shoppingCart");
		Intent intent = new Intent(IndentInfoActivity.this, MainActivity.class);
		intent.putExtras(b);
		startActivity(intent);
		finish();
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
