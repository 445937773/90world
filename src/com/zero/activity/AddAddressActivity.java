package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.tools.Analysis_Util;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class AddAddressActivity extends Activity {
	EditText et_name, et_phoneNum, et_addInfo;
	RelativeLayout btn_back;
	String studentId;
	LinearLayout btn_ok, btn_cancl;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.ADD_ADDRESS_OK:
				Toast.makeText(AddAddressActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
				onBackPressed();
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.address_add);
		findView();
	}
	private void findView() {
		Bundle b = getIntent().getExtras();
		studentId = b.getString("studentInfo");
		et_name = (EditText) findViewById(R.id.et_name_address_edit_detail_i);
		et_phoneNum = (EditText) findViewById(R.id.et_phone_address_edit_detail_i);
		et_addInfo = (EditText) findViewById(R.id.et_address_address_edit_detail_i);
		btn_ok = (LinearLayout) findViewById(R.id.btn_ok_address_edit_detail);
		btn_cancl = (LinearLayout) findViewById(R.id.ly_cancel);
		btn_back = (RelativeLayout) findViewById(R.id.ly_back);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addAddress();
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		btn_cancl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	protected void addAddress() {
		String name = et_name.getText().toString();
		String phoneNum = et_phoneNum.getText().toString();
		String addInfo = et_addInfo.getText().toString();
		if(!"".equals(name) && !"".equals(phoneNum) && !"".equals(addInfo) && studentId != null){
			
			final List<String> names = new ArrayList<String>();
			names.add(0, "studentId");
			names.add(1, "address");
			names.add(2, "isDefault");
			names.add(3, "recipient");
			names.add(4, "phoneNum");
			final List<String> p = new ArrayList<String>();
			p.add(0, studentId);
			p.add(1, addInfo);
			p.add(2, "false");
			p.add(3, name);
			p.add(4, phoneNum);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					String result = Analysis_Util.getDetail4oneResult(names, p, MyMethods.ADD_ADDRESS);
					if(result != null){
						if(Integer.parseInt(result) > 0){
							msg.what = MyMessages.ADD_ADDRESS_OK;
						}else{
							msg.what = MyMessages.ADD_ADDRESS_FAILD;
						}
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
		this.finish();
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
