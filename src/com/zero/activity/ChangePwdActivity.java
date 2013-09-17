package com.zero.activity;

import java.util.ArrayList;

import com.zero.tools.Analysis_Util;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ChangePwdActivity extends Activity {
	EditText et_oldPassWord, et_newPassWord, et_comfirmPwd;
	Button btn_comfirm;
	RelativeLayout back;
	String oldPwd, newPwd, comfirmPwd;
	String stuId;
	ProgressDialog loginDialog;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.CHANGE_PWD_OK:
				loginDialog.dismiss();
				Toast.makeText(ChangePwdActivity.this, "修改成功,请重新登录!", Toast.LENGTH_SHORT).show();
				SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
				userInfo.edit().clear().commit();
				goBack();
				break;

			case MyMessages.CHANGE_PWD_FAILD:
				loginDialog.dismiss();
				Toast.makeText(ChangePwdActivity.this, "原密码不正确，请重试", Toast.LENGTH_SHORT).show();
				break;
				
			case MyMessages.TIME_OUT:
				loginDialog.dismiss();
				showDialig();
				break;
				
			default:
				break;
			}
		}

	};
	private void goBack() {
		Intent intent;
		intent = new Intent(ChangePwdActivity.this, Login.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
	
	protected void showDialig() {
		Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("网络连接超时").setMessage("Sorry,网络好像有点问题啊!").setPositiveButton("重试", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	changePwd();
	            }
	        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
	            
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                onBackPressed();
	            }
	        }).show();
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_change);
		MyApplication.getInstance().addActivity(this);
		findView();
		getStuId();
	}

	private void getStuId() {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0); 
		if(userInfo != null){
			stuId = userInfo.getString(MySharedPreferences.STUDENT_ID, "");  
		}
	}

	private void findView() {
		back = (RelativeLayout) findViewById(R.id.ly_back);
		et_oldPassWord = (EditText) findViewById(R.id.et_oldpassword_password_change);
		et_newPassWord = (EditText) findViewById(R.id.et_newpassword_password_change);
		et_comfirmPwd = (EditText) findViewById(R.id.et_password_confirm_password_change);
		btn_comfirm = (Button) findViewById(R.id.btn_complete_password_change);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
		btn_comfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ConnectionDetector.getNetWorkState(ChangePwdActivity.this) != null){
					changePwd();
				}else{
					Toast.makeText(ChangePwdActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	protected void changePwd() {
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ChangePwdActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   
		oldPwd = et_oldPassWord.getText().toString();
		newPwd = et_newPassWord.getText().toString();
		comfirmPwd = et_comfirmPwd.getText().toString();
		
		if("".equals(oldPwd) || oldPwd == null){
			Toast.makeText(ChangePwdActivity.this, "请填写您现在的密码", Toast.LENGTH_SHORT).show();
		}
		else if("".equals(newPwd) || newPwd == null){
			Toast.makeText(ChangePwdActivity.this, "请填写您需要修改的密码", Toast.LENGTH_SHORT).show();
		}
		else if("".equals(comfirmPwd) || comfirmPwd == null){
			Toast.makeText(ChangePwdActivity.this, "请再次填写您需要修改的密码", Toast.LENGTH_SHORT).show();
		}
		else if(!newPwd.equals(comfirmPwd)){
			Toast.makeText(ChangePwdActivity.this, "您输入的两次新密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
		}
		else{
			loginDialog = ProgressDialog.show(ChangePwdActivity.this, "请稍后...              ", "     正在登录...", true, false);
			final ArrayList<String> p = new ArrayList<String>();
			p.add(0, "studentId");
			p.add(1, "oldPassword");
			p.add(2, "newPassword");
			final ArrayList<String> values = new ArrayList<String>();
			values.add(0, stuId);
			values.add(1, oldPwd);
			values.add(2, newPwd);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					String result = Analysis_Util.getDetail4oneResult(p, values, MyMethods.CHANGE_STU_PWD);
					if(result != null){
						if("1".equals(result)){
							msg.what = MyMessages.CHANGE_PWD_OK;
						}
						else{
							msg.what = MyMessages.CHANGE_PWD_FAILD;
						}
					}else{
						msg.what = MyMessages.TIME_OUT;
					}
					
					handler.sendMessage(msg);
				}
			}).start();
		}
		
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
