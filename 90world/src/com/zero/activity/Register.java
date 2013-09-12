package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.School;
import com.zero.bean.Student;
import com.zero.tools.Analysis_Util;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
import com.zero.tools.SchoolAdapter;
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
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener{
	private Button register, agreement, btn_getvalidate;
	RelativeLayout ly_back;
	private CheckBox checkBox;
	private EditText et_userName, et_Phone, et_passWord, et_confirmPass, et_vailidate;
	private String userName, phone, passWord, confirmPass, schoolId, vailidate;
	private Spinner sp_school;
	private String S_num;
	Handler handler;
	List<School> schools;
	private ProgressDialog loginDialog;
	protected Student student;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		MyApplication.getInstance().addActivity(this);
		findViewAndSetLinstener();
		showSchool();
		//生成随机数
		long num = Math.round(Math.random()*899999+100000);
		S_num = Long.toString(num);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MyMessages.GET_SCHOOL_COMPLETE:
					SchoolAdapter ad = new SchoolAdapter(Register.this, schools);
					sp_school.setAdapter(ad);
					loginDialog.dismiss();
					break;

				case MyMessages.REGISTER_OK:
					Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
					//loginDialog.dismiss();
//					onBackPressed();
//					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					//自动登录
					autoLogin();
					break;
				
				case MyMessages.REGISTER_EXIST_USERNAME:
					loginDialog.dismiss();
					Toast.makeText(Register.this, "此电话号码已被注册", Toast.LENGTH_SHORT).show();
					break;
					
				case MyMessages.REGISTER_NOT_REAL_STUDENT:
					loginDialog.dismiss();
					Toast.makeText(Register.this, "学号姓名不对应", Toast.LENGTH_SHORT).show();
					break;
				
				case MyMessages.UNKNOW_EXCEPTION:
					loginDialog.dismiss();
					Toast.makeText(Register.this, "服务器掉地上", Toast.LENGTH_SHORT).show();
					break;
					
				case MyMessages.REGISTER_EXIST_PHONE:
					loginDialog.dismiss();
					Toast.makeText(Register.this, "已存在的手机号", Toast.LENGTH_SHORT).show();
					break;
				
				case MyMessages.REGISTER_NOT_STUDENT:
					loginDialog.dismiss();
					Toast.makeText(Register.this, "没有该学生的信息", Toast.LENGTH_SHORT).show();
					break;
				case MyMessages.TIME_OUT:
					loginDialog.dismiss();
					showDialig();
					break;
					
				case MyMessages.TIME_OUT + 1:
					loginDialog.dismiss();
					showDialig4Register();
					break;
				case MyMessages.SEND_SMS_OK:
					Toast.makeText(Register.this, "发送成功", Toast.LENGTH_SHORT).show();
				break;
				case MyMessages.LOGIN_OK:
					loginDialog.dismiss();
					startActivity(new Intent(Register.this, MainActivity.class));
					Register.this.finish();
					break;
					
				default:
					break;
				}
				
			}
		};
	}
	
	protected void showDialig4Register() {
		Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("网络超时").setMessage("Sorry,网络好像有点问题啊!").setPositiveButton("重试", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	register();
	            }
	        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
	            
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                onBackPressed();
	            }
	        }).show();
		
	}

	protected void showDialig() {
		Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("获取学校信息超时").setMessage("Sorry,网络好像有点问题啊!").setPositiveButton("重新获取", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	showSchool();
	            }
	        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
	            
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                onBackPressed();
	            }
	        }).show();
		
	}

	private void showSchool() {
		loginDialog = ProgressDialog.show(Register.this, "请稍后...              ", "     正在处理...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				schools = ParseXml.getSchoolInfo(null, null, MyMethods.GET_ALL_SCHOOL);
				if(schools != null){
					if(schools.size() > 0){
						msg.what = MyMessages.GET_SCHOOL_COMPLETE;
					}
				}else{
					msg.what = MyMessages.TIME_OUT;
				}
				handler.sendMessage(msg);
			}
		}).start();
		
	}

	private void findViewAndSetLinstener() {
		et_userName = (EditText) findViewById(R.id.et_username_register);
		et_Phone = (EditText) findViewById(R.id.et_phonenumber_register);
		et_passWord = (EditText) findViewById(R.id.et_password_register);
		et_confirmPass = (EditText) findViewById(R.id.et_password_confirm_register);
		et_vailidate = (EditText) findViewById(R.id.et_validate_register);
		sp_school = (Spinner) findViewById(R.id.sp_school);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		checkBox = (CheckBox) findViewById(R.id.cb_register);
		register = (Button) findViewById(R.id.btn_register);
		btn_getvalidate = (Button) findViewById(R.id.btn_getvalidate);
		agreement = (Button) findViewById(R.id.btn_turntoagreement);
		ly_back.setOnClickListener(this);
		agreement.setOnClickListener(this);
		register.setOnClickListener(this);
		btn_getvalidate.setOnClickListener(this);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					register.setEnabled(true);
				}else{
					register.setEnabled(false);
				}
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			if(ConnectionDetector.getNetWorkState(this) != null){
				register();
			}else{
				Toast.makeText(this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.btn_turntoagreement:
			Intent intent = new Intent(Register.this, MyWebViewActivity.class);
			Bundle b = new Bundle();
			b.putString("addUrl", "http://90world.51idctg.com/html/registe_agreement.html");
			intent.putExtras(b);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
			
		case R.id.ly_back:
			onBackPressed();
			break;
		case R.id.btn_getvalidate:
			sendVailidateNum();
			break;
		
		default:
			break;
		}
		
	}

	private void sendVailidateNum() {
		//获取电话号码
		phone = et_Phone.getText().toString();
		if("".equals(phone)){
			Toast.makeText(this, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
		}
		else if(!phone.matches("^(\\+86)?1[3-8]\\d{9}$")){
			Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
		}else{
			
			new CountDownTimer(1000*60, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					btn_getvalidate.setText("" + millisUntilFinished / 1000);  
					btn_getvalidate.setClickable(false);
				}
				@Override
				public void onFinish() {
					btn_getvalidate.setText("重新获取");  
					btn_getvalidate.setClickable(true);
				}
			}.start();
			//调用发短信接口
			final List<String> p = new ArrayList<String>();
			p.add(0, "PhoneNum");
			p.add(1, "Code");
			final List<String> values = new ArrayList<String>();
			values.add(0, phone);
			values.add(1, S_num);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					String sendSmsResult = Analysis_Util.getDetail4oneResult(p, values, "SendSMS");
					if(sendSmsResult != null){
						if("0".equals(sendSmsResult)){
							msg.what = MyMessages.REGISTER_EXIST_PHONE;
						}
						else if("1".equals(sendSmsResult)){
							msg.what = MyMessages.SEND_SMS_OK;
						}
					}else{
						msg.what = MyMessages.TIME_OUT + 1;
					}
					handler.sendMessage(msg);
				}
			}).start();
		}
		
	}
	private void register() {
		userName = et_userName.getText().toString(); 			//名字
		phone = et_Phone.getText().toString();		 			//电话
		passWord = et_passWord.getText().toString();			//密码
		confirmPass = et_confirmPass.getText().toString();		//确认密码
		schoolId = (sp_school.getSelectedItemId() + 1) + "";	//学校Id
		vailidate = et_vailidate.getText().toString();	//学校Id
		
		if("".equals(userName)){
			Toast.makeText(this, "请输入您的姓名", Toast.LENGTH_SHORT).show();
		}
		else if("".equals(phone)){
			Toast.makeText(this, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
		}
		else if(!phone.matches("^(\\+86)?1[3-8]\\d{9}$")){
			Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
		}
		else if("".equals(vailidate)){
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
		}
		else if(!S_num.equals(vailidate)){
			Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
		}
		else if("".equals(passWord)){
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
		}
		else if(passWord.length() < 6){
			Toast.makeText(this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
		}
		else if("".equals(confirmPass)){
			Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
		}
		
		else if(!passWord.equals(confirmPass)){
			Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
		}else{
			loginDialog = ProgressDialog.show(Register.this, "请稍后...              ", "     正在处理...", true, false);
			final List<String> p = new ArrayList<String>();
			p.add(0, "name");
			p.add(1, "phone");
			p.add(2, "SchoolId");
			p.add(3, "password");
			final List<String> values = new ArrayList<String>();
			values.add(0, userName);
			values.add(1, phone);
			values.add(2, schoolId);
			values.add(3, passWord);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					String realStudentResult = Analysis_Util.getDetail4oneResult(p, values, MyMethods.REGISTER);
					if(realStudentResult != null){
						if("0".equals(realStudentResult)){
							msg.what = MyMessages.REGISTER_OK;
						}else if("-1".equals(realStudentResult)){
							msg.what = MyMessages.UNKNOW_EXCEPTION;
						}
						else if("1".equals(realStudentResult)){
							msg.what = MyMessages.REGISTER_EXIST_USERNAME;
						}
						else if("5".equals(realStudentResult)){
							msg.what = MyMessages.REGISTER_EXIST_PHONE;
						}
						else{
							msg.what = MyMessages.UNKNOW_EXCEPTION;
						}
					}else{
						msg.what = MyMessages.TIME_OUT + 1;
					}
					
					handler.sendMessage(msg);
				}
			}).start();
		}
	}
	private void autoLogin() {
		final Message msg = new Message();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<String> p = new ArrayList<String>();
				p.add(0, "phone");
				List<String> value = new ArrayList<String>();
				value.add(0, phone);
				//通过电话号码获取用户信息
				List<Student> students = ParseXml.getlStudentInfo(p, value, MyMethods.GET_STUDENT_BY_PHONE);
				if(students != null){
					student = students.get(0);
					setStudentIfo();
					msg.what = MyMessages.LOGIN_OK;
				}else{
					msg.what = MyMessages.TIME_OUT;
				}
				handler.sendMessage(msg);
			}
		}).start();
		
	}
	protected void setStudentIfo() {
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
		userInfo.edit().putString(MySharedPreferences.STUDENT_LOGIN_PWD, student.getStu_pwd()).commit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
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
