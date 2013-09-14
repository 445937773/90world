package com.zero.activity;
import com.zero.tools.MyApplication;
import com.zero.www.R;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ViewFlipper;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;

public class MainActivity extends ActivityGroup{
	public static MainActivity instance = null;
	private static Activity activity;
	private RadioGroup radioGroup;
	private ViewFlipper body;
	private FrameLayout gard;
	//声明LocalActivityManager对象
	private LocalActivityManager m_ActivityManager;
	RadioButton home_radio, category_radio, search_radio, shopping_radio, more_radio;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		instance = this;
		setActivity(this);
		findViews();
		//获取Activity消息
    	m_ActivityManager = getLocalActivityManager();
		addView();
    	setListeners();
    	Bundle bundle = getIntent().getExtras();
    	if(bundle == null){
    		home_radio.setChecked(true);
    	}else{
    		String check  = bundle.getString("check");
        	if("shoppingCart".equals(check)){
        		shopping_radio.setChecked(true);
        	}
        	else if("searchActivity".equals(check)){
        		search_radio.setChecked(true);
        	}
        	else{
        		home_radio.setChecked(true);
        	}
    	}
	}
	
		
		private void addView() {
		Intent intent;
		
		intent = new Intent(MainActivity.this,HomeActivity.class);
		body.addView((m_ActivityManager.startActivity("",intent ).getDecorView()),0);
		
		body.addView((m_ActivityManager.startActivity("", new Intent(MainActivity.this,SomeProductActivity.class)).getDecorView()),1);
		
		intent = new Intent(MainActivity.this, MoreActivity.class);
		body.addView((m_ActivityManager.startActivity("", intent).getDecorView()),2);
		
		
		body.addView((m_ActivityManager.startActivity("", new Intent(MainActivity.this,Agreement.class)).getDecorView()),3);
		body.addView((m_ActivityManager.startActivity("", new Intent(MainActivity.this,SearchActivity.class)).getDecorView()),4);
		
	}

	private void findViews() {
		radioGroup = (RadioGroup) findViewById(R.id.group);
		body = (ViewFlipper) findViewById(R.id.layout_main_relative);
		home_radio = (RadioButton) findViewById(R.id.btn_home);
		category_radio = (RadioButton) findViewById(R.id.btn_categry);
		search_radio = (RadioButton) findViewById(R.id.btn_search);
		shopping_radio = (RadioButton) findViewById(R.id.btn_shopping);
		more_radio = (RadioButton) findViewById(R.id.btn_more);
		gard = (FrameLayout) findViewById(R.id.home_gard);
		
		//判断是不是第一次运行
		SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("firstRun", MODE_PRIVATE);  
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);  
		Editor editor = sharedPreferences.edit(); 
		if (isFirstRun){  
		    Log.e("debug", "第一次运行");
		    gard.setVisibility(View.VISIBLE);
		    editor.putBoolean("isFirstRun", false);  
		    editor.commit(); 
		    
		}else{  
		    Log.e("debug", "不是第一次运行");
		    gard.setVisibility(View.GONE);
		}
	}
	
	private void setListeners() {
		radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
		gard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gard.setVisibility(View.GONE);
				
			}
		});
	}
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			Intent intent;
			switch (checkedId) {
			case R.id.btn_home:
				body.setDisplayedChild(0);
				body.removeViewAt(3);
				body.addView((m_ActivityManager.startActivity("", new Intent(MainActivity.this,Agreement.class)).getDecorView()),3);
				break;
			case R.id.btn_categry:
				body.setDisplayedChild(1);
				body.removeViewAt(3);
				body.addView((m_ActivityManager.startActivity("", new Intent(MainActivity.this,Agreement.class)).getDecorView()),3);
				break;
			case R.id.btn_search:
				body.setDisplayedChild(4);
				body.removeViewAt(3);
				body.addView((m_ActivityManager.startActivity("", new Intent(MainActivity.this,Agreement.class)).getDecorView()),3);
				break;
			case R.id.btn_shopping:
				intent = getIntent();
				Bundle b = intent.getExtras();
				intent = new Intent(MainActivity.this,ShoppingCarActivity.class);
				if(b!=null){
					intent.putExtras(b);
				}
				body.removeViewAt(3);
				body.addView((m_ActivityManager.startActivity("", intent).getDecorView()),3);
				body.setDisplayedChild(3);
				break;
			case R.id.btn_more:
				body.setDisplayedChild(2);
				body.removeViewAt(3);
				body.addView((m_ActivityManager.startActivity("", new Intent(MainActivity.this,Agreement.class)).getDecorView()),3);
				break;
				
			default:
				break;
			}
		}
	};
	
	public void onBackPressed() {
		if(category_radio.isChecked() || search_radio.isChecked() || shopping_radio.isChecked() || more_radio.isChecked()){
			home_radio.setChecked(true);
		}else{
			showBackDialog(this);
		}
	}
	public void showBackDialog(final Context mainActivity){
		Builder builder = new AlertDialog.Builder(mainActivity);
		builder.setTitle("您确定要退出吗？");
		builder.setPositiveButton("是", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MyApplication.getInstance().exit();
				finish();
			}
		});
		builder.setNegativeButton("否", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.addSubMenu(0, 1, 0, "主页");
		menu.addSubMenu(0, 2, 1, "退出");
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 1:
				Intent intent;
				intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				if(activity != null){
					activity.finish();
				}
			break;
			
		case 2:
			MainActivity.instance.showBackDialog(activity);
			break;

		default:
			break;
		}
		return true;
	}
	public static void setActivity(Activity activity){
		MainActivity.activity = activity;
	}

}
