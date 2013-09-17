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
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class Login extends Activity implements OnClickListener{
	private Button register, forgetpwd, login;
	private EditText tv_name, tv_pwd;
	RelativeLayout ly_back;
	private CheckBox cb_keepuserinfo_login;
	private boolean isKeep;
	Handler handler;
	private Student student;
	private String loginAccount;
	private ProgressDialog loginDialog;
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		MyApplication.getInstance().addActivity(this);
		findViewAndsetListener();
		getUserInfoAndAutoWrite();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MyMessages.LOGIN_OK:
					Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
					
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							onBackPressed();
							loginDialog.dismiss();
							Login.this.finish();
						}
					}, 1000);
					
					break;

				case MyMessages.LOGIN_PWD_ERROR:
					Toast.makeText(Login.this, "密码错误", Toast.LENGTH_SHORT).show();
					login.setEnabled(true);
					login.setText("登录");
					loginDialog.dismiss();
					break;
				
				case MyMessages.LOGIN_NOT_ACCOUNT:
					Toast.makeText(Login.this, "账号不存在", Toast.LENGTH_SHORT).show();
					login.setEnabled(true);
					login.setText("登录");
					loginDialog.dismiss();
					break;
					
				case MyMessages.TIME_OUT:
					showDialig();
					login.setEnabled(true);
					login.setText("登录");
					loginDialog.dismiss();
					break;
				default:
					break;
				}
			}
		};
	}
	
	protected void showDialig() {
		Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("登陆超时").setMessage("Sorry,网络好像有点问题啊!").setPositiveButton("重新登陆", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	login();
	            }
	        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
	            
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                onBackPressed();
	            }
	        }).show();
		
	}




	private void findViewAndsetListener() {
		cb_keepuserinfo_login = (CheckBox) findViewById(R.id.cb_keepuserinfo_login);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		register = (Button) findViewById(R.id.btn_register);
		login = (Button) findViewById(R.id.btn_login);
		forgetpwd = (Button) findViewById(R.id.btn_turntogettingpassword);
		tv_name = (EditText) findViewById(R.id.et_phonenumber_login);
		tv_pwd = (EditText) findViewById(R.id.et_password_login);
		RelativeLayout la = (RelativeLayout) findViewById(R.id.rea_lay_login);
		la.setClickable(false);
		ly_back.setOnClickListener(this);
		register.setOnClickListener(this);
		forgetpwd.setOnClickListener(this);
		login.setOnClickListener(this);
		cb_keepuserinfo_login.setChecked(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly_back:
			onBackPressed();
			break;

		case R.id.btn_register:
			if(ConnectionDetector.getNetWorkState(this) != null){
				goTo(Register.class);
			}else{
				Toast.makeText(this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.btn_turntogettingpassword:
			Bundle b = new Bundle();
			b.putString("addUrl", "http://90world.51idctg.com/html/find_password.html");
			Intent intent;
			intent = new Intent(Login.this, MyWebViewActivity.class);
			intent.putExtras(b);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
			
		case R.id.btn_login:
			if(ConnectionDetector.getNetWorkState(this) != null){
				login();
			}else{
				Toast.makeText(this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
			}
			break;
			
		default:
			break;
		}
		
	}
	protected void login(){
		isKeep = cb_keepuserinfo_login.isChecked();
		final String name = tv_name.getText().toString();
		final String pwd = tv_pwd.getText().toString();
		
		final List<String> PNames = new ArrayList<String>();
		final List<String> PValues = new ArrayList<String>();
		PValues.add(0, name);
		PValues.add(1, pwd);
		
		final Message msg = new Message();
		if("".equals(name)){
			Toast.makeText(this, "请输入登陆账户", Toast.LENGTH_SHORT).show();
		}
		else if("".equals(pwd)){
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
		}
		
			PNames.add(0, "phone");
			PNames.add(1, "password");
			loginDialog = ProgressDialog.show(Login.this, "请稍后...              ", "     正在登录...", true, false);
			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = Analysis_Util.getDetail4oneResult(PNames, PValues, "StudentLoginByPhone");
					if(result != null){
						if("-1".equals(result)){
							msg.what = MyMessages.LOGIN_NOT_ACCOUNT;
						}else if("-2".equals(result)){
							msg.what = MyMessages.LOGIN_PWD_ERROR;
						
						}else if(Integer.parseInt(result) > 0){
							List<String> p = new ArrayList<String>();
							p.add(0, "phone");
							List<String> value = new ArrayList<String>();
							value.add(0, name);
							//通过电话号码获取用户信息
							List<Student> students = ParseXml.getlStudentInfo(p, value, MyMethods.GET_STUDENT_BY_PHONE);
							if(students != null){
								student = students.get(0);
								loginAccount = name;
								setStudentIfo();
								msg.what = MyMessages.LOGIN_OK;
							}else{
								msg.what = MyMessages.TIME_OUT;
							}
						}
						
					}else{
						msg.what = MyMessages.TIME_OUT;
					}
					handler.sendMessage(msg);
				}
			}).start();
	}
	
	
	protected void goTo(Class<?> className) {
		Intent intent;
		intent = new Intent(Login.this, className);
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		
	}
	private void setStudentIfo(){
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);  
		userInfo.edit().putString(MySharedPreferences.STUDENT_NAME, student.getStu_name()).commit();  
		userInfo.edit().putString(MySharedPreferences.STUDENT_ID, student.getStu_id()).commit();
		userInfo.edit().putString(MySharedPreferences.STUDENT_ADD, student.getStu_address()).commit();
		userInfo.edit().putString(MySharedPreferences.STUDENT_PHONE, student.getPhone_num()).commit();
		userInfo.edit().putString(MySharedPreferences.STUDENT_SEX, student.getStu_sex()).commit();
		userInfo.edit().putString(MySharedPreferences.STUDENT_SCHOOL_NAME, student.getStu_school()).commit();
		userInfo.edit().putString(MySharedPreferences.STUDENT_ICON, student.getStu_pic()).commit();
		userInfo.edit().putString(MySharedPreferences.STUDENT_XY, student.getStu_xingyu()).commit();
		userInfo.edit().putString(MySharedPreferences.STUDENT_PWD, student.getStu_pwd()).commit();
		userInfo.edit().putString(MySharedPreferences.STUDENT_LOGIN_ACCOUNT, loginAccount).commit();
		userInfo.edit().putString(MySharedPreferences.STUDENT_LOGIN_PWD, student.getStu_pwd()).commit();
		userInfo.edit().putBoolean(MySharedPreferences.STUDENT_IS_KEEP_PWD, isKeep).commit();
		
		
	}
	private void getUserInfoAndAutoWrite() {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0); 
		if(userInfo != null){
			String username = userInfo.getString(MySharedPreferences.STUDENT_LOGIN_ACCOUNT, ""); 
			tv_name.setText(username);
			boolean iskeep = userInfo.getBoolean(MySharedPreferences.STUDENT_IS_KEEP_PWD, false);
			if(iskeep){
				String pass = userInfo.getString(MySharedPreferences.STUDENT_LOGIN_PWD, "");  
				tv_pwd.setText(pass);
				cb_keepuserinfo_login.setChecked(true);
			}else{
				tv_pwd.setText("");
			}
		}
		
	}
	@Override
	public void onBackPressed() {
		Bundle b = getIntent().getExtras();
		if(b != null){
			String where = b.getString("where");
			if(where != null){
				if("home".equals(where)){
					Intent intent = new Intent(Login.this, MainActivity.class);
					startActivity(intent);
				}
			}else{
				super.onBackPressed();
			}
		}
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
