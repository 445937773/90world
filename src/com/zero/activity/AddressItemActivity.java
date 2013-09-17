package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Address;
import com.zero.bean.Student;
import com.zero.tools.AddressAdapter;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


@SuppressLint("HandlerLeak")
public class AddressItemActivity extends Activity {

	ListView list;
	RelativeLayout fanhui;
	protected List<Address> addresses;
	Student student = new Student();
	protected Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyMessages.GET_DE_ADDRESS_OK:
				break;
				
			case MyMessages.GET_ADDRESSES_OK:
				list.setAdapter(ad);
				loginDialog.dismiss();
				break;
			
			default:
				break;
			}
		};
	};
	protected AddressAdapter ad;
	private ProgressDialog loginDialog;
	private String netWorkState;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_check_list);
		MyApplication.getInstance().addActivity(this);
		loginDialog = ProgressDialog.show(AddressItemActivity.this, "请稍后...", "正在处理...", true, false);
		fanhui = (RelativeLayout) findViewById(R.id.ly_back);
		list = (ListView) findViewById(R.id.lv_addressList_adddress_list);
		netWorkState = ConnectionDetector.getNetWorkState(AddressItemActivity.this);
		if(netWorkState!=null){
		getStudentInfo();
		getAddresses();
		}else{
			Toast.makeText(AddressItemActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		fanhui.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				netWorkState = ConnectionDetector.getNetWorkState(AddressItemActivity.this);
				if(netWorkState!=null){
					
					Address ad = (Address) list.getItemAtPosition(arg2);
					Intent intent = getIntent();
					Bundle data = new Bundle();
					data.putSerializable("address", ad);
					intent.putExtras(data);
					AddressItemActivity.this.setResult(0, intent);
					AddressItemActivity.this.finish();
//				Bundle b = new Bundle();
//				b.putSerializable("address", ad);
//				Intent intent1 = getIntent();
//				Bundle bun = intent1.getExtras();
//				Intent intent = new Intent(AddressItemActivity.this, Intentctivity.class);
//				intent.putExtras(b);
//				intent.putExtras(bun);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//				finish();
//				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}else{
					Toast.makeText(AddressItemActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
				}
				}
		});
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
				ad = new AddressAdapter(AddressItemActivity.this, addresses, student);
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
