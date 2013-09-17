package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Address;
import com.zero.bean.Student;
import com.zero.tools.Analysis_Util;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class AddressEditActivity extends Activity implements OnClickListener{
	private Address address;
	private Student student;
	private TextView tv_name, tv_phone, tv_addressID;
	private EditText tv_addressDetail;
	private RelativeLayout btn_back_address_edit_detail;
	private CheckBox cb_setaddress_address_edit_detail;
	private ProgressDialog loginDialog;
	private LinearLayout delete, save;
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.UPDATE_ADDRESS_OK:
				loginDialog.dismiss();
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
		setContentView(R.layout.address_edit_detail);
		MyApplication.getInstance().addActivity(this);
		getBundle();
		findView();
	}
	private void getBundle() {
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		student = (Student) b.getSerializable("student");
		address = (Address) b.getSerializable("address");
		
	}
	private void findView() {
		cb_setaddress_address_edit_detail = (CheckBox) findViewById(R.id.cb_setaddress_address_edit_detail);
		btn_back_address_edit_detail = (RelativeLayout) findViewById(R.id.ly_back);
		save = (LinearLayout) findViewById(R.id.btn_keep_address_edit_detail);
		delete = (LinearLayout) findViewById(R.id.btn_delete_address_edit_detail);
		
		tv_name = (TextView) findViewById(R.id.tv_name_address_edit_detail_i);
		tv_phone = (TextView) findViewById(R.id.tv_phone_address_edit_detail_i);
		tv_addressDetail = (EditText) findViewById(R.id.tv_address_address_edit_detail_i);
		tv_addressID = (TextView) findViewById(R.id.tv_addressId_address_edit_detail);
		
		tv_name.setText(address.getRecipient());
		tv_phone.setText(address.getPhoneNum());
		tv_addressDetail.setText(address.getAddressInfo());
		tv_addressID.setText(address.getAddressId() + "");
		cb_setaddress_address_edit_detail.setChecked(address.isDefault());
		btn_back_address_edit_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
				finish();
			}
		});
		save.setOnClickListener(this);
		delete.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(ConnectionDetector.getNetWorkState(this) != null){
			switch (v.getId()) {
			
			//保存
			case R.id.btn_keep_address_edit_detail:
				String addressIfo = tv_addressDetail.getText().toString();
				if("".equals(addressIfo) || "".equals(tv_name.getText().toString()) || "".equals(tv_phone.getText().toString())){
					Toast.makeText(this, "请填写详细地址信息", Toast.LENGTH_SHORT).show();
				}else{
					loginDialog = ProgressDialog.show(AddressEditActivity.this, "请稍后...", "正在处理...", true, false);
					
					final List<String> p = new ArrayList<String>();
					p.add(0, "studentId");
					p.add(1, "addressId");
					p.add(2, "addressInfor");
					p.add(3, "isDefault");
					p.add(4, "recipient");
					p.add(5, "phoneNum");
					
					final List<String> values = new ArrayList<String>();
					values.add(0, student.getStu_id());
					values.add(1, address.getAddressId() + "");
					values.add(2, addressIfo);
					values.add(3, cb_setaddress_address_edit_detail.isChecked() +"");
					values.add(4, tv_name.getText().toString());
					values.add(5, tv_phone.getText().toString());
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							Message msg = new Message();
							String result = Analysis_Util.getDetail4oneResult(p, values, MyMethods.UPDATE_ADDRESS);
							if("true".equals(result)){
								msg.what = MyMessages.UPDATE_ADDRESS_OK;
							}else{
								msg.what = MyMessages.UPDATE_ADDRESS_FAILD;
							}
							handler.sendMessage(msg);
						}
					}).start();
				}
				break;
				
			//删除
			case R.id.btn_delete_address_edit_detail:
				
				Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("您确定要删除吗？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						loginDialog = ProgressDialog.show(AddressEditActivity.this, "请稍后...", "正在处理...", true, false);
						final List<String> p1 = new ArrayList<String>();
						p1.add(0, "studentId");
						p1.add(0, "addressId");
						final List<String> values1 = new ArrayList<String>();
						values1.add(0, student.getStu_id());
						values1.add(0, address.getAddressId() + "");
						new Thread(new Runnable() {
							@Override
							public void run() {
								Message msg = new Message();
								String result = Analysis_Util.getDetail4oneResult(p1, values1, MyMethods.DELETE_ADDRESS);
								if("true".equals(result)){
									msg.what = MyMessages.UPDATE_ADDRESS_OK;
								}else{
									msg.what = MyMessages.UPDATE_ADDRESS_FAILD;
								}
								handler.sendMessage(msg);
							}
						}).start();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.create().show();
				
				break;
			default:
				break;
			}
		}else{
			Toast.makeText(this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(AddressEditActivity.this, AddressActivity.class);
		startActivity(intent);
		AddressEditActivity.this.finish();
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
