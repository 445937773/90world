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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

@SuppressLint("HandlerLeak")
public class Intentctivity extends Activity {

	List<ShoppingCar> shoppings = new ArrayList<ShoppingCar>();
	TextView jine;
	ListView list;
	RelativeLayout fanhui;
	protected Address addresses;
	protected Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			double goodsjin = 0;
			double foodsjin = 0;
			switch (msg.what) {
			case MyMessages.ADD_ADDRESS_FAILD:
				loginDialog.dismiss();
				goodsjin = 0;
				foodsjin = 0;
				for (int i = 0; i < shoppings.size(); i++) {
					if(shoppings.get(i).getGoods()!=null){
						
						goodsjin = goodsjin + (shoppings.get(i).getGoodsNumber() * shoppings.get(i).getGoods().getPrice());
					}else{
						foodsjin = foodsjin + (shoppings.get(i).getGoodsNumber() * shoppings.get(i).getDish().getPrice());
					}
				}
				zongjine = goodsjin+foodsjin+"";
				jine.setText(zongjine);
				order = new OrderDetailApdater(Intentctivity.this, shoppings, BitmapFactory.decodeResource(getResources(), R.drawable.default_pic), handler);
				list.setAdapter(order);
				break;
			default:
				break;
			}
		};
	};
	String zongjine;
	OrderDetailApdater order;
	protected List<Address> add;
	ProgressDialog loginDialog;
	Button tijiao;
	protected boolean boo;
	Object bn;
	Intent intent;
	Bundle bun;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loginDialog = ProgressDialog.show(Intentctivity.this, "请稍后...", "正在处理...", true, false);
		setContentView(R.layout.order_produce_new);
		MyApplication.getInstance().addActivity(this);
		jine = (TextView) findViewById(R.id.order_money_id);
		list = (ListView) findViewById(R.id.order_product_list_order_produce);
		fanhui = (RelativeLayout) findViewById(R.id.ly_back); 
		tijiao = (Button) findViewById(R.id.btn_order_submit_order_produce); 
		
		intent = getIntent();
		bun = intent.getExtras();
		int number = bun.getInt("number");
		for (int i = 0; i < number; i++) {
			ShoppingCar shoppingCar = (ShoppingCar) bun.getSerializable("shopps"+i);
			shoppings.add(shoppingCar);
			Message msg = new Message();
			msg.what = MyMessages.ADD_ADDRESS_FAILD;
			handler.sendMessage(msg);
		}
		fanhui.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		tijiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(Intentctivity.this, IntentAddressActivity.class);
				intent.putExtras(bun);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
		});
		
		OnScrollListener onScrollListener = new OnScrollListener() {
	        @Override
	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	        	mImageLoader = ImageLoader.getInstance(Intentctivity.this);
	            switch (scrollState) {
	                case OnScrollListener.SCROLL_STATE_FLING:   
	                    mImageLoader.lock();
	                    break;
	                case OnScrollListener.SCROLL_STATE_IDLE:
	                    mImageLoader.unlock();
	                    break;
	                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
	                    mImageLoader.lock();
	                    break;
	                default:
	                    break;
	            }
	        }
	                                                               
	        @Override
	        public void onScroll(AbsListView view, int firstVisibleItem,
	                int visibleItemCount, int totalItemCount) {
	        }
	    };
	    list.setOnScrollListener(onScrollListener);
	}
	ImageLoader mImageLoader;
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
