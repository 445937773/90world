package com.zero.activity;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.zero.tools.DialogHelper;
import com.zero.tools.MyApplication;
import com.zero.tools.UpdateManager;
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
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup{
	public static MainActivity instance = null;
	private static Activity activity;
	private RadioGroup radioGroup;
	private ViewFlipper body;
	private FrameLayout gard;
	//声明LocalActivityManager对象
	private LocalActivityManager m_ActivityManager;
	RadioButton home_radio, category_radio, search_radio, shopping_radio, more_radio;
	private UpdateManager updateMan;
	private ProgressDialog updateProgressDialog;
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
        	String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        	if(extras != null && !"".equals(extras)){
        		String myValue = "";
        		JSONObject extrasJson;
        		try {
					extrasJson = new JSONObject(extras);
					myValue = extrasJson.optString("canguan");
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
        		if(!"".equals(myValue)){
        			Intent intent = new Intent(this, SearchFoodsCategoryByRestaurantNameActivity.class);
        			Bundle b = new Bundle();
        			b.putString("sort", myValue);
        			intent.putExtras(b);
        			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        			startActivity(intent);
        		}
        		
        		//
        	}
    	}
    	checkVesin();
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
	//更新所用
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		private void checkVesin() {
			updateMan = new UpdateManager(this, appUpdateCb);
			updateMan.checkUpdate();
		}
		// 自动更新回调函数
		UpdateManager.UpdateCallback appUpdateCb = new UpdateManager.UpdateCallback() 
		{
			public void downloadProgressChanged(int progress) {
				if (updateProgressDialog != null
						&& updateProgressDialog.isShowing()) {
					updateProgressDialog.setProgress(progress);
				}

			}
			public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
				if (updateProgressDialog != null
						&& updateProgressDialog.isShowing()) {
					updateProgressDialog.dismiss();
				}
				if (sucess) {
					updateMan.update();
				} else {
					DialogHelper.Confirm(MainActivity.this,
					R.string.dialog_error_title,
					R.string.dialog_downfailed_msg,
					R.string.dialog_downfailed_btndown,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							updateMan.downloadPackage();
						}
					}, R.string.dialog_downfailed_btnnext, null);
				}
			}
			public void downloadCanceled(){}
			public void checkUpdateCompleted(Boolean hasUpdate, CharSequence updateInfo) {
				if (hasUpdate) {
					DialogHelper.Confirm(MainActivity.this,
							getText(R.string.dialog_update_title),
							getText(R.string.dialog_update_msg).toString()
							+updateInfo+
							getText(R.string.dialog_update_msg2).toString(),
									getText(R.string.dialog_update_btnupdate),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									updateProgressDialog = new ProgressDialog(
											MainActivity.this);
									updateProgressDialog
											.setMessage(getText(R.string.dialog_downloading_msg));
									updateProgressDialog.setIndeterminate(false);
									updateProgressDialog
											.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
									updateProgressDialog.setMax(100);
									updateProgressDialog.setProgress(0);
									updateProgressDialog.show();
									updateMan.downloadPackage();
								}
							},getText( R.string.dialog_update_btnnext), null);
				}else{
				}
			}
		};
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
