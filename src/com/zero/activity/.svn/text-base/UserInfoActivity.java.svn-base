package com.zero.activity;

import com.zero.tools.MyApplication;
import com.zero.tools.MySharedPreferences;
import com.zero.www.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserInfoActivity extends Activity{
	TextView tv_userName, tv_userPhone, tv_userSchool;
	RelativeLayout back;
	Button layout_changepwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);
		MyApplication.getInstance().addActivity(this);
		findView();
		showUserInfo();
	}

	private void findView() {
		back = (RelativeLayout) findViewById(R.id.ly_back);
		tv_userName = (TextView) findViewById(R.id.tv_username_userinfo);
		tv_userPhone = (TextView) findViewById(R.id.tv_phone_userinfo);
		tv_userSchool = (TextView) findViewById(R.id.tv_school_userinfo);
		layout_changepwd = (Button) findViewById(R.id.btn_changepwd_userinfo);
		layout_changepwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserInfoActivity.this, ChangePwdActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	private void showUserInfo() {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0); 
			String username = userInfo.getString(MySharedPreferences.STUDENT_NAME, "");
			String userPhone = userInfo.getString(MySharedPreferences.STUDENT_PHONE, "");
			String userSchool = userInfo.getString(MySharedPreferences.STUDENT_SCHOOL_NAME, "");
			tv_userName.setText(username);
			tv_userPhone.setText(userPhone);
			tv_userSchool.setText(userSchool);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
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
