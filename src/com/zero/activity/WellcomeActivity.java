package com.zero.activity;


import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Student;
import com.zero.tools.Analysis_Util;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class WellcomeActivity extends Activity {
	String netWorkState = null;
	Handler handler;
	Student student;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wellcome_layout);
		MyApplication.getInstance().addActivity(this);
		
		
		
		handler = new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MyMessages.CHANGE_PWD_OK:
					goTo(MyMessages.CHANGE_PWD_OK);
					break;
				case MyMessages.CHANGE_PWD_FAILD:
					new Handler().postDelayed(new Runnable(){
	                    @Override
	                    public void run() {
	                        finish();
	                    }
	                }, 10000);
					break;
				case MyMessages.LOGIN_PWD_ERROR:
					goTo(MyMessages.LOGIN_PWD_ERROR);
					break;
					
				default:
					break;
				}
			}
		
		};
		checkNet();
		
	}
	protected void goTo(final int loginMsg) {
		new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
           	//判断是不是第一次运行
        		SharedPreferences sharedPreferences = WellcomeActivity.this.getSharedPreferences("firstRun", MODE_PRIVATE);  
        		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);  
        		Editor editor = sharedPreferences.edit(); 
        		if (isFirstRun){  
       			Intent intent = new Intent(WellcomeActivity.this, MainActivity.class);
       			startActivity(intent);
       		    Log.e("debug", "第一次运行");  
       		    
       		}else{  
       		    Log.e("debug", "不是第一次运行");
       		    Intent intent = new Intent(WellcomeActivity.this, MainActivity.class);
                startActivity(intent);
                switch (loginMsg) {
				case MyMessages.CHANGE_PWD_OK:
					Toast.makeText(WellcomeActivity.this, "自动登录成功！", Toast.LENGTH_SHORT).show();
					break;
				case MyMessages.LOGIN_PWD_ERROR:
					Toast.makeText(WellcomeActivity.this, "自动登录失败，请手动登录。", Toast.LENGTH_SHORT).show();
					SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
	                userInfo.edit().clear().commit();
					break;	
				default:
					break;
				}
       		}
               WellcomeActivity.this.finish();
               overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        }, 1000);
		
	}
	@Override
	protected void onRestart() {
		checkNet();
		super.onRestart();
	}
	private void checkNet() {
		netWorkState = ConnectionDetector.getNetWorkState(this);
		if(netWorkState ==null){
			Builder builder = new AlertDialog.Builder(this);
			 builder.setTitle("亲，网络不给力啊！").setMessage("要不先连个网络再来？").setPositiveButton("好的！", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                Intent intent=null;
		                //判断手机系统的版本  即API大于10 就是3.0或以上版本 
		                if(android.os.Build.VERSION.SDK_INT>10){
		                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		                }else{
		                    intent = new Intent();
		                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
		                    intent.setComponent(component);
		                    intent.setAction("android.intent.action.VIEW");
		                }
		                startActivity(intent);
		            }
		        }).setNegativeButton("算了吧", new DialogInterface.OnClickListener() {
		            
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                finish();
		            }
		        }).show();

		}else{
			autoLogin();
		}
	}
	private void autoLogin() {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		if(userInfo.getAll().size() > 0){
			final String stuNum = userInfo.getString(MySharedPreferences.STUDENT_PHONE, "");
			String stuPwd = userInfo.getString(MySharedPreferences.STUDENT_PWD, "");
			if(!"".equals(stuNum) && !"".equals(stuPwd)){
				Toast.makeText(this, "自动登录...", Toast.LENGTH_SHORT).show();
				final List<String> PNames = new ArrayList<String>();
				final List<String> PValues = new ArrayList<String>();
				PNames.add(0, "phone");
				PNames.add(1, "password");
				PValues.add(0, stuNum);
				PValues.add(1, stuPwd);
				new Thread(new Runnable() {
					@Override
					public void run() {
						Message msg = new Message();
						String result = Analysis_Util.getDetail4oneResult(PNames, PValues, "StudentLoginByPhone");
						if(result != null){
							if(Integer.parseInt(result) > 0){
								List<String> p = new ArrayList<String>();
								p.add(0, "phone");
								List<String> value = new ArrayList<String>();
								value.add(0, stuNum);
								//通过学号获取用户信息
								List<Student> students = ParseXml.getlStudentInfo(p, value, MyMethods.GET_STUDENT_BY_PHONE);
								if(students != null){
									if(students.size() > 0){
										student = students.get(0);
										msg.what = MyMessages.CHANGE_PWD_OK;
									}else{
										msg.what = MyMessages.LOGIN_PWD_ERROR;
										
									}
								}else{
									msg.what = MyMessages.LOGIN_PWD_ERROR;
								}
							}else{
								msg.what = MyMessages.LOGIN_PWD_ERROR;
							}
						}else{
							msg.what = MyMessages.LOGIN_PWD_ERROR;
						}
						handler.sendMessage(msg);
					}
				}).start();
			}
		}else{
			goTo(0);
		}
		
	}
}
