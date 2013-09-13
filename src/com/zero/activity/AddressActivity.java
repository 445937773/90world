package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Address;
import com.zero.bean.Student;
import com.zero.tools.*;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class AddressActivity extends Activity implements OnClickListener{
	private TextView tv_name_address, tv_phone_number, tv_detail_address;
	private ListView addressList;
	private Button btn_add;
	RelativeLayout btn_back_addresslist;
	Address deAddresses;
	List<Address> addresses;
	Student student = new Student();
	AddressAdapter ad;
	private ProgressDialog loginDialog;
	protected Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.GET_DE_ADDRESS_OK:
				setDefaultAddress();
				loginDialog.dismiss();
				break;
			case MyMessages.GET_ADDRESSES_OK:
				addressList.setAdapter(ad);
				loginDialog.dismiss();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_list);
		MyApplication.getInstance().addActivity(this);
		loginDialog = ProgressDialog.show(AddressActivity.this, "请稍后...", "正在处理...", true, false);
		findView();
		getStudentInfo();
		getDefaultAddress();
		getAddresses();
		
		
	}

	private void getAddresses() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = MyMessages.GET_ADDRESSES_OK;
				List<String> name = new ArrayList<String>();
				name.add(0, "studentId");
				List<String> p = new ArrayList<String>();
				p.add(0, student.getStu_id());
				addresses = ParseXml.getAddressInfo(name, p, MyMethods.GET_ADDRESSES);
				ad = new AddressAdapter(AddressActivity.this, addresses, student);
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void getStudentInfo() {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		student.setStu_id(userInfo.getString(MySharedPreferences.STUDENT_ID, ""));
		student.setStu_name(userInfo.getString(MySharedPreferences.STUDENT_NAME, ""));
		student.setPhone_num(userInfo.getString(MySharedPreferences.STUDENT_PHONE, ""));
	}

	private void getDefaultAddress() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = MyMessages.GET_DE_ADDRESS_OK;
				List<String> name = new ArrayList<String>();
				name.add(0, "studentId");
				List<String> p = new ArrayList<String>();
				p.add(0, student.getStu_id());
				deAddresses = ParseXml.getDeAddressInfo(name, p, MyMethods.GET_DEFAULT_ADDRESS);
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void setDefaultAddress() {
		if(deAddresses != null){
			tv_name_address.setText(deAddresses.getRecipient());
			tv_phone_number.setText(deAddresses.getPhoneNum());
			tv_detail_address.setText(deAddresses.getAddressInfo());
		}else{
			tv_name_address.setText("默认收货地址");
			tv_phone_number.setText("手机号");
			tv_detail_address.setText("可在下方的地址列表进行设置");
		}
	}

	private void findView() {
		btn_back_addresslist = (RelativeLayout) findViewById(R.id.ly_back);
		tv_name_address = (TextView) findViewById(R.id.tv_name_address_list);
		tv_phone_number = (TextView) findViewById(R.id.tv_phone_number_address_list);
		tv_detail_address = (TextView) findViewById(R.id.tv_detail_address_address_list);
		addressList = (ListView) findViewById(R.id.lv_addressList_adddress_list);
		btn_add = (Button) findViewById(R.id.btn_add_address_address_list);
		btn_add.setOnClickListener(this);
		btn_back_addresslist.setOnClickListener(this);
		addressList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(ConnectionDetector.getNetWorkState(AddressActivity.this) != null){
					Address ad = (Address) addressList.getItemAtPosition(arg2);
					Bundle b = new Bundle();
					b.putSerializable("student", student);
					b.putSerializable("address", ad);
					Intent intent = new Intent(AddressActivity.this, AddressEditActivity.class);
					intent.putExtras(b);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					AddressActivity.this.finish();
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}else{
					Toast.makeText(AddressActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_address_address_list:
			if(ConnectionDetector.getNetWorkState(this) != null){
				gotoAddAddress();
			}else{
				Toast.makeText(this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
			}
			break;
		
		case R.id.ly_back:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	private void gotoAddAddress() {
		Bundle b = new Bundle();
		b.putString("studentInfo", student.getStu_id());
		Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
		intent.putExtras(b);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		
	}

	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
	@Override
	protected void onResume() {
		super.onResume();
		getDefaultAddress();
		getAddresses();		
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
