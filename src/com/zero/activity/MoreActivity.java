package com.zero.activity;

import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.www.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MoreActivity extends Activity implements OnClickListener{
	RelativeLayout layout_userinfo, layout_address , layout_favorite, layout_order, layout_settings, layout_about;
	private String netWorkState;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		MyApplication.getInstance().addActivity(this);
		findView();
	}
	private void findView() {
		layout_userinfo = (RelativeLayout) findViewById(R.id.layout_userinfo_more);
		layout_address = (RelativeLayout) findViewById(R.id.layout_address_setting_more);
		layout_favorite = (RelativeLayout) findViewById(R.id.layout_my_favorite_more);
		layout_order = (RelativeLayout) findViewById(R.id.layout_my_order_more);
		layout_settings = (RelativeLayout) findViewById(R.id.layout_setting_more);
		layout_about = (RelativeLayout) findViewById(R.id.layout_help_more);
		layout_userinfo.setOnClickListener(this);
		layout_address.setOnClickListener(this);
		layout_favorite.setOnClickListener(this);
		layout_order.setOnClickListener(this);
		layout_settings.setOnClickListener(this);
		layout_about.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
		netWorkState = ConnectionDetector.getNetWorkState(MoreActivity.this);
		if(netWorkState!=null){
			switch (v.getId()) {
			case R.id.layout_userinfo_more:
				
				if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
					goTo(UserInfoActivity.class);
				}else{
					goTo(Login.class);
				}
				break;
				
			case R.id.layout_address_setting_more:
				if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
					goTo(AddressActivity.class);
				}else{
					goTo(Login.class);
				}
				
				break;
				
			case R.id.layout_my_favorite_more:
				if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
					goTo(MyFavoriteActivity.class);
				}else{
					goTo(Login.class);
				}
				break;
				
			case R.id.layout_my_order_more:
				if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
					goTo(MyOrderCategoryActivity.class);
				}else{
					goTo(Login.class);
				}
				
				break;
			case R.id.layout_setting_more:
					goTo(SettingsActivity.class);
				break;
			case R.id.layout_help_more:
				Intent intent = new Intent(MoreActivity.this, MyWebViewActivity.class);
				Bundle b = new Bundle();
				b.putString("addUrl", "http://90world.51idctg.com/html/about_v1.1.0.html");
				b.putString("from", "about");
				intent.putExtras(b);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
				
			default:
				break;
			}
		}else{
			Toast.makeText(MoreActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		
	}
	private void goTo(Class<?> className) {
		Intent intent = new Intent(MoreActivity.this, className);
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
}
