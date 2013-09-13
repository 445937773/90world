package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Indent;
import com.zero.bean.OrderInfo;
import com.zero.cache.ImageLoader;
import com.zero.tools.IndentFoodsDetailApdater;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.ParseXml;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

@SuppressLint("HandlerLeak")
public class IndentDetailsActivity extends Activity {

	private TextView text1,text2,text3,text4,text5,text6,text7,text8,text9;
	ListView list;
	Indent indent;
	Button btn_cancel;
	RelativeLayout ly_back;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			 Builder builder = new AlertDialog.Builder(IndentDetailsActivity.this);
			switch (msg.what) {
			case MyMessages.ADD_ADDRESS_OK:
				if(indent.getIndentItate().equals("0")){
					text1.setText("待发货");
					if(!btn_vivibilty){
						btn_cancel.setVisibility(View.VISIBLE);
					}
				}else if(indent.getIndentItate().equals("1")){
					text1.setText("已发货");
				}else if(indent.getIndentItate().equals("2")){
					text1.setText("已完成");
				}else{
					text1.setText("已取消");
				}
				text2.setText(indent.getOrderFormNumber()+"");
				text3.setText(indent.getAddress());
				text4.setText(indent.getReserve());
				text5.setText(indent.getSum()+"");
				text6.setText("0");
				text7.setText(indent.getSum()+"");
				list.setAdapter(foodsorder);
				
				loginDialog.dismiss();
				break;
			case MyMessages.GOODS_BIG_FAILD:
				text1.setText("已取消");
				btn_cancel.setVisibility(View.GONE);
				Toast.makeText(IndentDetailsActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
				break;
			
			case MyMessages.TIME_OUT:
				builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	getView();
		            }
		            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
			            
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	onBackPressed();
			            }
			        }).show();
				break;
			case MyMessages.TIME_OUT+1:
				builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	getView();
		            }
		            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
			            
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	onBackPressed();
			            }
			        }).show();
			break;
			}
		}
	};
	private ProgressDialog loginDialog;
	protected boolean bool;
	private boolean btn_vivibilty = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loginDialog = ProgressDialog.show(IndentDetailsActivity.this, "请稍后...", "正在处理...", true, false);
		setContentView(R.layout.order_detail);
		MyApplication.getInstance().addActivity(this);
		text1 = (TextView) findViewById(R.id.detail_order_status_comment);
		text2 = (TextView) findViewById(R.id.detail_order_id_number_comment);
		text3 = (TextView) findViewById(R.id.detail_order_address_comment);
		text4 = (TextView) findViewById(R.id.tv_comment_order_detail);
		text5 = (TextView) findViewById(R.id.product_all_money_content);
		text6 = (TextView) findViewById(R.id.cheap_money_content);
		text7 = (TextView) findViewById(R.id.should_pay_content);
		list = (ListView) findViewById(R.id.order_product_list);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		btn_cancel = (Button) findViewById(R.id.btn_cancel_order_detail);
		Intent intent = getIntent();
		Bundle bun = intent.getExtras();
		indent = (Indent) bun.getSerializable("indent1");
		if(indent != null){
			btn_vivibilty  = true;
		}
		getView();
		ly_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		//点击取消订单
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Builder builder = new AlertDialog.Builder(IndentDetailsActivity.this);
				 builder.setTitle("你确定要取消此订单么").setMessage("取消订单对你的信誉有影响").setPositiveButton("好的！", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	cancelIndent();
			            }
			        }).setNegativeButton("算了吧", new DialogInterface.OnClickListener() {
			            
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			                
			            }
			        }).show();
			}

			
		});
	}
	private void cancelIndent() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				name.clear();
				zhi.clear();
				name.add("OrderFormId");
				name.add("status");
				name.add("staffId");
				name.add("comment");
				zhi.add(indent.getIndentId()+"");
				if(text1.getText().toString().equals("待发货")){
					zhi.add("3");
				}
				zhi.add("2");
				zhi.add("CancelByUser");
				bool = ParseXml.getAddShoopingCar(name, zhi, "ChangeOrderFormStatus");
				if(bool){
					msg.what = MyMessages.GOODS_BIG_FAILD;
				}else{
					msg.what = MyMessages.TIME_OUT+1;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	List<String> name = new ArrayList<String>();
	List<String> zhi = new ArrayList<String>();
	List<OrderInfo> goods = new ArrayList<OrderInfo>();
	protected IndentFoodsDetailApdater foodsorder;
	protected String indentNumber;
	protected Indent indents;
	protected List<OrderInfo> orderinfo;
	private void getView(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Intent intent = getIntent();
				Bundle bun = intent.getExtras();
				if(bun.getSerializable("indent")!=null){
					indent = (Indent) bun.getSerializable("indent");
					name.clear();
					zhi.clear();
					name.add("orderFormNumber");
					zhi.add(indent.getOrderFormNumber()+"");
					orderinfo = ParseXml.getIndentgoodsInfo(name, zhi, "GetOrderFormsByOrderFormNumber");
				}else{
					indent = (Indent) bun.getSerializable("indent1");
					indent.setAddress(indent.getAdressId().getAddressInfo());
					name.clear();
					zhi.clear();
					name.add("orderFormNumber");
					zhi.add(indent.getOrderFormNumber()+"");
					orderinfo = ParseXml.getIndentgoodsInfo(name, zhi, "GetOrderFormsByOrderFormNumber");
				}				
				if(orderinfo != null){
					if(orderinfo.size()!=0){
						
						msg.what = MyMessages.ADD_ADDRESS_OK;
						foodsorder = new IndentFoodsDetailApdater(IndentDetailsActivity.this, orderinfo, BitmapFactory.decodeResource(getResources(), R.drawable.default_pic), handler);
					}else{
						msg.what = MyMessages.ADD_ADDRESS_FAILD;
					}
				}else{
					msg.what = MyMessages.TIME_OUT;
				}
				handler.sendMessage(msg);
			}
		}).start();
		
		OnScrollListener onScrollListener = new OnScrollListener() {
	        @Override
	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	        	mImageLoader = ImageLoader.getInstance(IndentDetailsActivity.this);
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
	private ImageLoader mImageLoader;
	@Override
	public void onBackPressed() {
		Intent intent = getIntent();
		Bundle bun = intent.getExtras();
		if(bun.getSerializable("indent")!=null){
			super.onBackPressed();
		}else{
			Bundle b = new Bundle();
			b.putString("check", "shoppingCart");
			Intent intent1 = new Intent(IndentDetailsActivity.this, MainActivity.class);
			intent1.putExtras(b);
			startActivity(intent1);
		}
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		finish();
	}
}
