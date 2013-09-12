package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.GoodsFavorite;
import com.zero.bean.GoodsInfo;
import com.zero.bean.ShoppingCar;
import com.zero.cache.ImageLoader;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class GoodsParticularInfoActivity extends Activity implements OnClickListener{
	private ImageLoader mImageLoader;
	ImageView image,jia,jian;
	TextView name,price,title,number, tv_standard_res_text, tv_standard_res, tv_details_of_goods;
	RelativeLayout ly_back;
	Button goumai;
	GoodsInfo dish;
	Object xiaoxi;
	String NetWorkState, url;
	WebView myWebView;
	ProgressBar progressBar1;
	LinearLayout addShopping, collect, btn_loading_html;
	boolean falg = true;
	private ProgressDialog loginDialog;
	LinearLayout r;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Builder builder = new AlertDialog.Builder(GoodsParticularInfoActivity.this);
			switch (msg.what) {
			case MyMessages.CHANGE_PWD_OK:
				loginDialog.dismiss();
				Toast.makeText(GoodsParticularInfoActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
				
				 builder.setTitle("成功加入购物车").setMessage("点击去购物车生成订单").setPositiveButton("去购物车", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	Bundle b = new Bundle();
							b.putString("check", "shoppingCart");
							b.putString("name", "goods");
							Intent intent = new Intent(GoodsParticularInfoActivity.this, MainActivity.class);
							intent.putExtras(b);
							startActivity(intent);
							finish();
			            }
			            }).setNegativeButton("继续浏览", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				break;
			case MyMessages.CHANGE_PWD_FAILD:
				builder.setTitle("网络连接超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	Intent intent = getIntent();
		        		Bundle b = intent.getExtras();
		        		int id = b.getInt("poster");
						getGoodsInfo(id);
		            }
		            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
			            
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	onBackPressed();
			            }
			        }).show();
				break;
			case MyMessages.COLLECT_OK:
				if(xiaoxi.equals("0")){
					Toast.makeText(GoodsParticularInfoActivity.this, "加入收藏成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(GoodsParticularInfoActivity.this, "该商品已收藏", Toast.LENGTH_SHORT).show();
				}
				
				loginDialog.dismiss();
				break;
			case MyMessages.COLLECT_FAILD:
				builder.setTitle("网络连接超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	addFavorite();
		            }
		            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
			            
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	onBackPressed();
			            }
			        }).show();
				loginDialog.dismiss();
				break;
			case MyMessages.CHANGE_GOODS_OK:
				imageUrl  = dish.getImage();
				image.setImageResource(R.drawable.default_pic);  
				mImageLoader.addTask(imageUrl, image); //添加任务
				Names = dish.getGoodsName();
				name.setText(Names);
				prices = dish.getPrice()+"";
				price.setText(prices);
				title.setText(Names);
				tv_standard_res_text.setText("规格：");
				tv_standard_res.setText(dish.getGoodsStandard());
				url = dish.getGoodsIntroduce();
				if(url == null || "".equals(url) || "anyType{}".equals(url)){
					tv_details_of_goods.setVisibility(View.VISIBLE);
				}else{
					if(NetWorkState != null){
						if("手机网络连接".equals(NetWorkState)){
							tv_details_of_goods.setVisibility(View.VISIBLE);
							tv_details_of_goods.setText("亲，移动网络下加载详情需要不少流量呢！");
							btn_loading_html.setVisibility(View.VISIBLE);
						}else{
							myWebView.loadUrl(url);
						}
					}
				}
				break;
			case MyMessages.CHANGE_GOODS_FAILD:
				builder.setTitle("网络连接超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	addShopping();
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
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		loginDialog = ProgressDialog.show(GoodsParticularInfoActivity.this, "请稍后...", "正在处理...", true, false);
		setContentView(R.layout.dish_detailed);
		mImageLoader = ImageLoader.getInstance(this);
		image = (ImageView) findViewById(R.id.iv_right_goodsimage);
		jia = (ImageView) findViewById(R.id.edit_product_num_increse_details);
		jian = (ImageView) findViewById(R.id.edit_product_num_descense_details);
		name = (TextView) findViewById(R.id.tv_right_item_goodsname);
		price = (TextView) findViewById(R.id.tv_right_item_oldpricevalue);
		number = (TextView) findViewById(R.id.edit_product_show_details);
		title = (TextView) findViewById(R.id.text_title_goods);
		r = (LinearLayout) findViewById(R.id.layout_numeber_change);
		tv_standard_res_text = (TextView) findViewById(R.id.tv_standard_res_text);
		tv_standard_res = (TextView) findViewById(R.id.tv_standard_res);
		addShopping = (LinearLayout) findViewById(R.id.btn_add_to_shoppingcar);
		collect = (LinearLayout) findViewById(R.id.btn_register);
		myWebView = (WebView) findViewById(R.id.wv_details_of_goods);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		tv_details_of_goods = (TextView) findViewById(R.id.tv_details_of_goods);
		btn_loading_html = (LinearLayout) findViewById(R.id.btn_loading_html);
		btn_loading_html.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myWebView.loadUrl(url);
				tv_details_of_goods.setVisibility(View.GONE);
				btn_loading_html.setVisibility(View.GONE);
			}
		});
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		//判断网络
		NetWorkState = ConnectionDetector.getNetWorkState(this);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.setScrollBarStyle(0);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.setWebChromeClient(new WebChromeClient() {
		   public void onProgressChanged(WebView view, int progress) {
			   progressBar1.setVisibility(View.VISIBLE);
			   GoodsParticularInfoActivity.this.setProgress(progress);
			   if(progress == 100){
				   progressBar1.setVisibility(View.GONE);
			   }
		  }
		});
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		dish = (GoodsInfo) b.getSerializable("goodsName");
		if(dish!=null){		//从搜索过来
			imageUrl = dish.getImage();
			image.setImageResource(R.drawable.default_pic);  
			mImageLoader.addTask(imageUrl, image); //添加任务
			Names = dish.getGoodsName();
			name.setText(Names);
			prices = dish.getPrice()+"";
			price.setText(prices);
			tv_standard_res_text.setText("规格：");
			tv_standard_res.setText(dish.getGoodsStandard());
			url = dish.getGoodsIntroduce();
			if(url == null || "".equals(url) || "anyType{}".equals(url)){
				tv_details_of_goods.setVisibility(View.VISIBLE);
			}else{
				if(NetWorkState != null){
					if("手机网络连接".equals(NetWorkState)){
						tv_details_of_goods.setVisibility(View.VISIBLE);
						tv_details_of_goods.setText("亲，移动网络下加载详情需要不少流量呢！");
						btn_loading_html.setVisibility(View.VISIBLE);
					}else{
						myWebView.loadUrl(url);
					}
				}
			}
		}else if(b.getInt("poster")!=0){	//从广告过来
			netWorkState = ConnectionDetector.getNetWorkState(GoodsParticularInfoActivity.this);
			if(netWorkState!=null){
				int id = b.getInt("poster");
				getGoodsInfo(id);
			}else{
				Toast.makeText(GoodsParticularInfoActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
			}
			
			
		}else if(b.getSerializable("goods")!=null){
			GoodsFavorite goods = (GoodsFavorite) b.getSerializable("goods");
			imageUrl = goods.getGoodsPictureUrl();
			image.setImageResource(R.drawable.default_pic);  
			mImageLoader.addTask(imageUrl, image); //添加任务
			Names = goods.getGoodsName();
			name.setText(Names);
			prices = goods.getPrice()+"";
			price.setText(prices);
			tv_standard_res_text.setText("规格：");
			tv_standard_res.setText(dish.getGoodsStandard());
			collect.setVisibility(View.GONE);
			r.setVisibility(View.GONE);
			url = dish.getGoodsIntroduce();
			if(url == null || "".equals(url) || "anyType{}".equals(url)){
				tv_details_of_goods.setVisibility(View.VISIBLE);
			}else{
				if(NetWorkState != null){
					if("手机网络连接".equals(NetWorkState)){
						tv_details_of_goods.setVisibility(View.VISIBLE);
						tv_details_of_goods.setText("亲，移动网络下加载详情需要不少流量呢！");
						btn_loading_html.setVisibility(View.VISIBLE);
					}else{
						myWebView.loadUrl(url);
					}
				}
			}
		}else{
			ShoppingCar goods = (ShoppingCar) b.getSerializable("goodsinfo");
			imageUrl = goods.getGoods().getImage();
			image.setImageResource(R.drawable.default_pic);  
			mImageLoader.addTask(imageUrl, image); //添加任务
			Names = goods.getGoods().getGoodsName();
			name.setText(Names);
			price.setText(goods.getGoods().getPrice()+"");
			tv_standard_res_text.setText("规格：");
			tv_standard_res.setText(dish.getGoodsStandard());
			addShopping.setVisibility(View.GONE);
			r.setVisibility(View.GONE);
			url = dish.getGoodsIntroduce();
			if(url == null || "".equals(url) || "anyType{}".equals(url)){
				tv_details_of_goods.setVisibility(View.VISIBLE);
			}else{
				if(NetWorkState != null){
					if("手机网络连接".equals(NetWorkState)){
						tv_details_of_goods.setVisibility(View.VISIBLE);
						tv_details_of_goods.setText("亲，移动网络下加载详情需要不少流量呢！");
						btn_loading_html.setVisibility(View.VISIBLE);
					}else{
						myWebView.loadUrl(url);
					}
				}
			}
		}
		title.setText(Names);
		loginDialog.dismiss();
		number.setText("1");
		jia.setOnClickListener(this);
		jian.setOnClickListener(this);
		ly_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		getInfo();
		addShopping.setOnClickListener(this);
		collect.setOnClickListener(this);
	}
	public void getGoodsInfo(final int poster){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				List<String> names = new ArrayList<String>();
				names.add("goodsId");
				List<String> zhi = new ArrayList<String>();
				zhi.add(poster+"");
				dish = ParseXml.getIdGoodsInfo(names, zhi, MyMethods.GET_GOODS_INFOR_BY_GOODSID);
				
				if(dish!=null){
					msg.what = MyMessages.CHANGE_GOODS_OK;
				}
				else{
					msg.what = MyMessages.CHANGE_GOODS_FAILD;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	int numberq=1;
	private String netWorkState;
	private String prices;
	private String imageUrl;
	String Names;
	String numbers;
	String dishid = null;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
		netWorkState = ConnectionDetector.getNetWorkState(GoodsParticularInfoActivity.this);
		if(netWorkState!=null){
			if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
				
				switch (v.getId()) {
				case R.id.edit_product_num_increse_details:
					numberq+=1;
					number.setText(numberq+"");
					price.setText(dish.getPrice()*numberq+"");
					
					break;
				case R.id.edit_product_num_descense_details:
					if(Integer.parseInt(number.getText().toString())>1){
						numberq-=1;
						number.setText(numberq+"");
						price.setText(dish.getPrice()*numberq+"");
						
					}else if(Integer.parseInt(number.getText().toString())==2){
						numberq-=1;
						number.setText(numberq+"");
					}
					break;
				case R.id.btn_add_to_shoppingcar://加入购物车
					loginDialog = ProgressDialog.show(GoodsParticularInfoActivity.this, "请稍后...", "正在处理...", true, false);
					addShopping();
					break;
				case R.id.btn_register://加入收藏
					addFavorite();
					break;
				default:
					break;
				}
			}else{
				Intent intent = new Intent(GoodsParticularInfoActivity.this, Login.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
			
		}else{
			Toast.makeText(GoodsParticularInfoActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		
	} 
	protected void goTo() {
		onBackPressed();
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
	String userId;
	List<String> names = new ArrayList<String>();
	List<String> zhi = new ArrayList<String>();
	public void getInfo(){
		numbers = number.getText().toString();//数量
		Intent intent1 = getIntent();
		Bundle b1 = intent1.getExtras();
		if(b1.getSerializable("goodsName")!=null){
			dish = (GoodsInfo) b1.getSerializable("goodsName");
			dishid = String.valueOf(dish.getGoodsId());//菜的id
		}else if(b1.getSerializable("poster")!=null){
			dishid = String.valueOf(b1.getSerializable("poster"));//菜的id
		}else if(b1.getSerializable("goods")!=null){
			GoodsFavorite goods = (GoodsFavorite) b1.getSerializable("goods");
			dishid = String.valueOf(goods.getGoods());
		}else{
			ShoppingCar goods = (ShoppingCar) b1.getSerializable("goodsinfo");
			dishid = String.valueOf(goods.getGoods().getGoodsId());
		}
		
		SharedPreferences shar = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		userId = shar.getString(MySharedPreferences.STUDENT_ID, "");//用户id
		
	}
	public void addShopping(){
		names.add("studentId");
		names.add("goodsId");
		names.add("goodsAmount");
		zhi.add(userId);
		zhi.add(dishid);
		zhi.add(numbers);
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
	}
	
	public void addFavorite(){
		loginDialog = ProgressDialog.show(GoodsParticularInfoActivity.this, "请稍后...", "正在处理...", true, false);
		names.add("studentId");
		names.add("goodsId");
		zhi.add(userId);
		zhi.add(dishid);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				xiaoxi = ParseXml.getAddFavorite(names, zhi, MyMethods.ADD_GOODS_INFOR_BY_GOODSID);
				if(!xiaoxi.equals("1")){
					msg.what = MyMessages.COLLECT_OK;
				}
				else{
					msg.what = MyMessages.COLLECT_FAILD;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
}
