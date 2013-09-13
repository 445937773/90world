package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.GoodsInfo;
import com.zero.cache.ImageLoader;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
import com.zero.tools.ScortlastRightAdpate;
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
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class GoodsLastCategoryActivity extends Activity {
	ListView right;
	ImageButton btn_shoppingcar;
	Spinner left;
	protected ArrayAdapter<String> ad;
	TextView title;
	RelativeLayout ly_back, no_item;
	ImageLoader mImageLoader;
	List<String> smalls = new ArrayList<String>();
	List<GoodsInfo> goods = new ArrayList<GoodsInfo>();
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Builder builder = new AlertDialog.Builder(GoodsLastCategoryActivity.this);
			switch (msg.what) {
			
			case MyMessages.GOODS_SMALL_OK:
				right.setVisibility(View.VISIBLE);
				left.setAdapter(ad);
				right.setAdapter(ap);
				pb_search.setVisibility(View.GONE);
				no_item.setVisibility(View.GONE);
				break;

			case MyMessages.GOODS_SMALL_FAILD:
				pb_search.setVisibility(View.GONE);
				no_item.setVisibility(View.VISIBLE);
				right.setVisibility(View.GONE);
				break;
			case MyMessages.TIME_OUT:
				pb_search.setVisibility(View.GONE);
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	showLeftInfo();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				break;
			case MyMessages.GOODS_INFO_OK:
				right.setVisibility(View.VISIBLE);
				right.setAdapter(ap);
				pb_search.setVisibility(View.GONE);
				no_item.setVisibility(View.GONE);
				break;
			case MyMessages.CHANGE_PWD_OK:
				loginDialog.dismiss();
				Toast.makeText(GoodsLastCategoryActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
				break;
			case MyMessages.CHANGE_PWD_FAILD:
				loginDialog.dismiss();
				Toast.makeText(GoodsLastCategoryActivity.this, "加入购物车失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	protected ScortlastRightAdpate ap;
	ProgressBar pb_search;
	List<String> goodsName = new ArrayList<String>();
	protected String netWorkState;
	private String categoryName;
	private ProgressDialog loginDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.sortlast_menu_layout);
		left = (Spinner) findViewById(R.id.spinner_brand_goods);
		right = (ListView) findViewById(R.id.lv_menu_listview_right);
		pb_search = (ProgressBar) findViewById(R.id.pb_search);
		title = (TextView) findViewById(R.id.text_title_sortlast);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		no_item = (RelativeLayout) findViewById(R.id.no_sort_item);
		pb_search.setVisibility(View.VISIBLE);
		//获取上一级分类名称
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		categoryName = b.getString("name");
		title.setText(categoryName.trim());
		showLeftInfo();
		left.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				netWorkState = ConnectionDetector.getNetWorkState(GoodsLastCategoryActivity.this);
				if(netWorkState!=null){
					final String rand = (String) left.getItemAtPosition(arg2);
					Intent intent = getIntent();
					Bundle b = intent.getExtras();
					final String name = b.getString("name");
					pb_search.setVisibility(View.VISIBLE);
					new Thread(new Runnable() {
						@Override
						public void run() {
							Message msg = new Message();
							List<String> goodsName = new ArrayList<String>();
							List<String> goodszhi = new ArrayList<String>();
							if(rand.equals("所有品牌")){
								goodsName.add("smallCategoryName");
								goodszhi.add(name);
								goods = ParseXml.getGoodsInfo(goodsName,goodszhi,"GetGoodsInforBySmallCategoryName");
							}else{
								goodsName.add("brandName");
								goodsName.add("smallCategory");
								goodszhi.add(rand);
								goodszhi.add(name);
								goods = ParseXml.getGoodsInfo(goodsName,goodszhi,MyMethods.GET_GOODS_BY_BRAND_AND_SMALL_CATEGORY);
							}
							
							
							if(goods!=null){
								if(goods.size()!=0){
									ap = new ScortlastRightAdpate(GoodsLastCategoryActivity.this,goods, BitmapFactory.decodeResource(getResources(), R.drawable.default_pic), handler);
									msg.what = MyMessages.GOODS_INFO_OK;
								}else{
									msg.what = MyMessages.GOODS_SMALL_FAILD;
								}
								
							}
							else{
								msg.what = MyMessages.TIME_OUT;
							}
							handler.sendMessage(msg);
						}
					}).start();
				}else{
					Toast.makeText(GoodsLastCategoryActivity.this,MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	
		
		ly_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		right.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = getIntent();
				Bundle b1 = i.getExtras();
				GoodsInfo goodsName = (GoodsInfo) right.getItemAtPosition(arg2);
				Bundle b = new Bundle();
				b.putSerializable("goodsName", goodsName);
				Intent intent;
				intent = new Intent(GoodsLastCategoryActivity.this,GoodsParticularInfoActivity.class);
				intent.putExtras(b);
				intent.putExtras(b1);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				
			}
		});
		
		OnScrollListener onScrollListener = new OnScrollListener() {
	        @Override
	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	        	mImageLoader = ImageLoader.getInstance(GoodsLastCategoryActivity.this);
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
	    right.setOnScrollListener(onScrollListener);
	    
	    
	}
	//添加购物车
	public void setAddShoppingcarListener(View v) {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
		netWorkState = ConnectionDetector.getNetWorkState(GoodsLastCategoryActivity.this);
		
		if(netWorkState!=null){
			if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
				loginDialog = ProgressDialog.show(GoodsLastCategoryActivity.this, "请稍后...", "正在处理...", true, false);
				SharedPreferences shar = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
				String userId = shar.getString(MySharedPreferences.STUDENT_ID, "");//用户id
				final List<String> names = new ArrayList<String>();
				final List<String> zhi = new ArrayList<String>();
				GoodsInfo good = (GoodsInfo) v.getTag();
				names.add("studentId");
				names.add("goodsId");
				names.add("goodsAmount");
				zhi.add(userId);
				zhi.add(good.getGoodsId()+"");
				zhi.add("1");
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Message msg = new Message();
						boolean reply = ParseXml.getAddShoopingCar(names, zhi, "AddGoodsToShoppingCart");
						if(reply){
							msg.what = MyMessages.CHANGE_PWD_OK;
							
						}
						else{
							msg.what = MyMessages.CHANGE_PWD_FAILD;
						}
						handler.sendMessage(msg);
					}
				}).start();
			}else{
				Intent intent = new Intent(GoodsLastCategoryActivity.this, Login.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
		}else{
			Toast.makeText(GoodsLastCategoryActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		
		
	}
	public void showLeftInfo(){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				final List<String> names = new ArrayList<String>();
				names.add("goodsSmallCategoryName");
				final List<String> zhi = new ArrayList<String>();
				zhi.add(categoryName);
				Message msg = new Message();
				smalls = ParseXml.getbrandGoodsSort(names,zhi,MyMethods.GET_BRANDS_BY_GOODS_SMALL_CATEGORY);
				smalls.add(0,"所有品牌");
				if(smalls!=null){
					if(smalls.size() > 0){
						List<String> goodsName = new ArrayList<String>();
						List<String> goodszhi = new ArrayList<String>();
						goodsName.add("smallCategoryName");
						goodszhi.add(categoryName);
						goods = ParseXml.getGoodsInfo(goodsName,goodszhi,"GetGoodsInforBySmallCategoryName");
						ap = new ScortlastRightAdpate(GoodsLastCategoryActivity.this,goods, BitmapFactory.decodeResource(getResources(), R.drawable.default_pic), handler);
						ad = new ArrayAdapter<String>(GoodsLastCategoryActivity.this, android.R.layout.simple_list_item_1, smalls);
						msg.what = MyMessages.GOODS_SMALL_OK;
					}else{
						msg.what = MyMessages.GOODS_SMALL_FAILD;
					}
				}
				else{
					msg.what = MyMessages.TIME_OUT;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	@Override
	public void onBackPressed() {
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
