 package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zero.bean.Address;
import com.zero.bean.FoodsShoppingCar;
import com.zero.bean.Indent;
import com.zero.bean.ShoppingCar;
import com.zero.bean.Student;
import com.zero.cache.ImageLoader;
import com.zero.tools.Analysis_Util;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.OrderDetailApdater;
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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

@SuppressLint("HandlerLeak")
public class IntentAddressActivity extends Activity {

	List<ShoppingCar> shoppings = new ArrayList<ShoppingCar>();
	TextView man,phone,address,jine;
	Student student = new Student();
	RelativeLayout fanhui, r;
	boolean doSaveDefault = false;
	boolean doSave = false;
	protected Address addresses;
	private int newAddressId;
	LinearLayout ly_select_address, ly_addAddrss;
	protected Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			Intent intent;
			 Bundle bun;
			 Builder builder = new AlertDialog.Builder(IntentAddressActivity.this);
//			 double goodsjin = 0;
//			 double foodsjin = 0;
			switch (msg.what) {
			case MyMessages.GET_ADDRESS_FAILD:
				doSaveDefault = true;
				man.setText(student.getStu_name());
				phone.setText(student.getPhone_num());

				loginDialog.dismiss();
				break;
				
			case MyMessages.GET_ADDRESS_OK:
				man.setText(addresses.getRecipient());
				phone.setText(addresses.getPhoneNum());
				address.setText(addresses.getAddressInfo());
				man.setEnabled(false);
				phone.setEnabled(false);
				address.setEnabled(false);
				LinearLayout ly_button = (LinearLayout) findViewById(R.id.ly_buttons);
				ly_button.setVisibility(View.VISIBLE);
				loginDialog.dismiss();
				break;
			
			case MyMessages.ADD_ADDRESS_OK:
				Toast.makeText(IntentAddressActivity.this, "添加地址成功", Toast.LENGTH_SHORT).show();
				commintOrder();
				break;
			case MyMessages.ADD_ADDRESS_FAILD:
				Toast.makeText(IntentAddressActivity.this, "地址添加失败，请重新提交！", Toast.LENGTH_SHORT).show();
				break;
			case MyMessages.COLLECT_OK:
				loginDialog.dismiss();
				intent = new Intent(IntentAddressActivity.this,IndentInfoActivity.class);
                bun = new Bundle();
                Indent indent = new Indent();
                indent.setOrderFormNumber(bn + "");
                indent.setSum(Double.parseDouble(zongjine));
                indent.setAdressId(addresses);
                indent.setReserve(et_comment.getText().toString());
                indent.setIndentItate("0");
                bun.putSerializable("indent1", indent);
                intent.putExtras(bun);
                startActivity(intent);
                IntentAddressActivity.this.finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
			case MyMessages.COLLECT_FAILD:
				loginDialog.dismiss();
				builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	getAddresses();
		            }
		            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
			            
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	onBackPressed();
			            }
			        }).show();
			
				break;
			default:
				break;
			}
		};
	};
	String zongjine;
	OrderDetailApdater order;
	private String netWorkState;
	protected List<Address> add;
	ProgressDialog loginDialog;
	Button tijiao;
	protected boolean boo;
	EditText et_comment;
	Object bn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loginDialog = ProgressDialog.show(IntentAddressActivity.this, "请稍后...", "正在处理...", true, false);
		setContentView(R.layout.order_produce_check_address);
		MyApplication.getInstance().addActivity(this);
		man = (TextView) findViewById(R.id.tv_receiver_order_produce);
		phone = (TextView) findViewById(R.id.tv_receiver_phoneNumber_order_produce);
		address = (TextView) findViewById(R.id.tv_receiver_address_order_produce);
		jine = (TextView) findViewById(R.id.order_money_id);
		r = (RelativeLayout) findViewById(R.id.layout_common_title);
		fanhui = (RelativeLayout) findViewById(R.id.ly_back); 
		tijiao = (Button) findViewById(R.id.btn_order_submit_order_produce); 
		ly_select_address = (LinearLayout) findViewById(R.id.ly_select_address);
		ly_addAddrss = (LinearLayout) findViewById(R.id.ly_address);
		et_comment = (EditText) findViewById(R.id.tv_comment_order_produce);
		netWorkState = ConnectionDetector.getNetWorkState(IntentAddressActivity.this);
		Intent intent = getIntent();
		Bundle bun = intent.getExtras();
		bun.getSerializable("aaa");
		if(netWorkState!=null){
			getStudentInfo();
			getAddresses();
		}else{
			Toast.makeText(IntentAddressActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		int number = bun.getInt("number");
		for (int i = 0; i < number; i++) {
			ShoppingCar shoppingCar = (ShoppingCar) bun.getSerializable("shopps"+i);
			shoppings.add(shoppingCar);
		}
		double goodsjin = 0;
		double foodsjin = 0;
		for (int i = 0; i < shoppings.size(); i++) {
			if(shoppings.get(i).getGoods()!=null){
				
				goodsjin = goodsjin + (shoppings.get(i).getGoodsNumber() * shoppings.get(i).getGoods().getPrice());
			}else{
				foodsjin = foodsjin + (shoppings.get(i).getGoodsNumber() * shoppings.get(i).getDish().getPrice());
			}
		}
		zongjine = goodsjin+foodsjin+"";
		jine.setText(zongjine);
		fanhui.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		ly_addAddrss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				man.setText("");
				phone.setText("");
				address.setText("");
				man.setEnabled(true);
				phone.setEnabled(true);
				address.setEnabled(true);
				doSave = true;
			}
		});
		ly_select_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IntentAddressActivity.this, AddressItemActivity.class);
				startActivityForResult(intent, 0);
			}
		});
//		r.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			netWorkState = ConnectionDetector.getNetWorkState(IntentAddressActivity.this);
//			if(netWorkState!=null){
//				Intent intent1 = getIntent();
//				Bundle bun = intent1.getExtras();
//				bun.remove("aaa");
//				Intent intent = new Intent(IntentAddressActivity.this, AddressItemActivity.class);
//				intent.putExtras(bun);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//			}else{
//				Toast.makeText(IntentAddressActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
//			}
//			}
//		});
		tijiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loginDialog = ProgressDialog.show(IntentAddressActivity.this, "请稍后...", "正在处理...", true, false);
				String stuName = man.getText().toString().trim();
				String stuPhone = phone.getText().toString().trim();
				String stuAddress = address.getText().toString().trim();
				if(!"".equals(stuName) && !"".equals(stuPhone) && !"".equals(stuAddress)){
					if(doSaveDefault){
						saveAddress(stuName, stuPhone, stuAddress, "true");
					}
					if(doSave){
						saveAddress(stuName, stuPhone, stuAddress, "false");
					}
					if(!doSaveDefault && !doSave){
						commintOrder();
					}
					
			}else{
				loginDialog.dismiss();
				Toast.makeText(IntentAddressActivity.this, "请填写完整的收货地址", Toast.LENGTH_SHORT).show();
			}
			}
		});
	}
		
	protected void commintOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Message msg = new Message();
					JSONArray array = new JSONArray();
					JSONArray array1 = new JSONArray();
					List<String> names = new ArrayList<String>();
					List<String> zhi = new ArrayList<String>();
					names.add("studentId");
					names.add("addressId");
					names.add("TotalAmount");
					names.add("privilegeAmount");
					names.add("payableAmount");
					names.add("comment");
					zhi.add(student.getStu_id()+"");
					zhi.add(addresses.getAddressId()+"");
					zhi.add(zongjine);
					zhi.add("0");
					zhi.add(zongjine);
					zhi.add(et_comment.getText().toString());
					
					for (int i = 0; i < shoppings.size(); i++) {
						JSONObject ob = new JSONObject();
						
						if(shoppings.get(i).getDish()!=null){
							ob.put("Id", shoppings.get(i).getDish().getDishId());
							ob.put("Amount", shoppings.get(i).getGoodsNumber());
							array.put(ob);
							
						}else{
							ob.put("Id", shoppings.get(i).getGoods().getGoodsId());
							ob.put("Amount", shoppings.get(i).getGoodsNumber());
							array1.put(ob);
						}
						
					}
					zhi.add(array.toString());
					names.add("foodsIdAndAmount");
					zhi.add(array1.toString());
					names.add("goodsIdAndAmount");
					bn = ParseXml.getAddFavorite(names, zhi, "AddNewOrder");
					if(!bn.equals("-1")){
						msg.what = MyMessages.COLLECT_OK;
						names.clear();
						zhi.clear();
						String goodsids = null;
						String foodsids = null;
						for (int i = 0; i < shoppings.size(); i++) {
							if(shoppings.get(i).getGoods()!=null){
								if(goodsids==null){
									goodsids  =shoppings.get(i).getCategoryId()+"";
								}else{
									goodsids +=","+shoppings.get(i).getCategoryId();	
								}
							}else{
								if(foodsids==null){
									foodsids  =shoppings.get(i).getCategoryId()+"";
								}else{
									foodsids +=","+shoppings.get(i).getCategoryId();	
								}
							}
						}
						if(goodsids!=null){
							names.clear();
							zhi.clear();
							names.add("shoppingCartIdList");
							zhi.add(goodsids);
							boo = ParseXml.getDeleteGoodsFavorite(names, zhi, "DeleteGoodsListFromShoppingCart");
						}
						if(foodsids!=null){
							names.clear();
							zhi.clear();
							names.add("shopingCartIdList");
							zhi.add(foodsids);
							boo = ParseXml.getDeleteGoodsFavorite(names, zhi, "DeleteFoodsListFromShoppingCart");
						}
					}
					else{
						msg.what = MyMessages.COLLECT_FAILD;
					}
					handler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	protected void saveAddress(String stuName, String stuPhone,
			final String stuAddress, String isDefault) {
		Toast.makeText(IntentAddressActivity.this, "正在保存地址", Toast.LENGTH_SHORT).show();
		final List<String> names = new ArrayList<String>();
		names.add(0, "studentId");
		names.add(1, "address");
		names.add(2, "isDefault");
		names.add(3, "recipient");
		names.add(4, "phoneNum");
		final List<String> p = new ArrayList<String>();
		p.add(0, student.getStu_id());
		p.add(1, stuAddress);
		p.add(2, isDefault);
		p.add(3, stuName);
		p.add(4, stuPhone);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				String result = Analysis_Util.getDetail4oneResult(names, p, MyMethods.ADD_ADDRESS);
				if(result != null){
					newAddressId = Integer.parseInt(result);
					if(newAddressId > 0){
						addresses.setAddressId(newAddressId);
						addresses.setAddressInfo(stuAddress);
						msg.what = MyMessages.ADD_ADDRESS_OK;
					}else{
						msg.what = MyMessages.ADD_ADDRESS_FAILD;
					}
				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void getStudentInfo() {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		student.setStu_id(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"));
		student.setStu_name(userInfo.getString(MySharedPreferences.STUDENT_NAME, "NULL"));
		student.setPhone_num(userInfo.getString(MySharedPreferences.STUDENT_PHONE, "NULL"));
	}
	private void getAddresses() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					List<String> name = new ArrayList<String>();
					name.add("studentId");
					List<String> p = new ArrayList<String>();
					p.add(student.getStu_id());
					addresses = ParseXml.getDeAddressInfo(name, p, MyMethods.GET_DEFAULT_ADDRESS);;
					if(addresses!=null){
						msg.what = MyMessages.GET_ADDRESS_OK;
					}else{
						addresses = new Address();
						msg.what = MyMessages.GET_ADDRESS_FAILD;
					}
					handler.sendMessage(msg);
				}
			}).start();
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0 && resultCode == 0 && data != null){
			Bundle bundle = data.getExtras();
			addresses = (Address) bundle.getSerializable("address");
			Message msg = new Message();
			msg.what = MyMessages.GET_ADDRESS_OK;
			handler.sendMessage(msg);
			doSave = false;
			doSaveDefault = false;
		}
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
