package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.FoodsShoppingCar;
import com.zero.bean.ShoppingCar;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
import com.zero.tools.ShoppingCarAdpater;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ShoppingCarActivity extends Activity implements OnClickListener{
	List<Integer> listItemID = new ArrayList<Integer>();
	ShoppingCarAdpater adapter;
	Button button, delete;
	List<ShoppingCar> cars = new ArrayList<ShoppingCar>();
	List<ShoppingCar> shopping = new ArrayList<ShoppingCar>();
	List<FoodsShoppingCar> foods = new ArrayList<FoodsShoppingCar>();
	ListView list;
	CheckBox all;
	RelativeLayout ly_pay_shoppingcar;
	private ProgressDialog loginDialog;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Builder builder = new AlertDialog.Builder(ShoppingCarActivity.this);
			switch (msg.what) {
			case MyMessages.GOODS_SMALL_OK:
				if(cars.size()<2){
					all.setVisibility(View.GONE);
				}
				delete.setVisibility(View.VISIBLE);
				list.setVisibility(View.VISIBLE);
				ly_pay_shoppingcar.setVisibility(View.VISIBLE);
				r1.setVisibility(View.GONE);
				list.setAdapter(adapter);
				loginDialog.dismiss();
				break;

			case MyMessages.GOODS_SMALL_FAILD:
				if(loginDialog!=null){
					loginDialog.dismiss();
				}
				
				
				delete.setVisibility(View.GONE);
				all.setVisibility(View.GONE);
				list.setVisibility(View.GONE);
				r1.setVisibility(View.VISIBLE);
				ly_pay_shoppingcar.setVisibility(View.GONE);

				break;

			case MyMessages.DELETE_FAVORITE_FAILD:
				loginDialog.dismiss();
				delete.setVisibility(View.GONE);
				all.setVisibility(View.GONE);
				list.setVisibility(View.GONE);
				r1.setVisibility(View.VISIBLE);
				ly_pay_shoppingcar.setVisibility(View.GONE);

				break;	
			case MyMessages.ADD_SHOPPINGCAR_OK:
				loginDialog.dismiss();
				Intent intent = new Intent(ShoppingCarActivity.this,Intentctivity.class);
                Bundle bun = new Bundle();
                bun.putInt("number", shopps.size());
                for (int i = 0; i < shopps.size(); i++) {
					bun.putSerializable("shopps"+i, shopps.get(i));
				}
                intent.putExtras(bun);
                startActivity(intent);
				
				break;
			case MyMessages.DELETE_GOODSSHOPPING_OK:
				Toast.makeText(ShoppingCarActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				
				loginDialog.dismiss();
				getShoopingCart();
				break;
			case MyMessages.ADD_SHOPPINGCAR_FAILD:
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	getShoopingCart();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				loginDialog.dismiss();
				break;
			case MyMessages.TIME_OUT:
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	getShoopingCart();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				loginDialog.dismiss();
				break;
			case MyMessages.TIME_OUT+1:
				 builder.setTitle("网络连接已经超时").setMessage("下面你想干嘛呢").setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	getShoopingCart();
			            }
			            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
				            
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	onBackPressed();
				            }
				        }).show();
				loginDialog.dismiss();
				break;
			default:
				loginDialog.dismiss();
				break;
			}
		}
	};
	protected String netWorkState;
	RelativeLayout r,r1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.shoppingcar);
		if(loginDialog != null){
			loginDialog.dismiss();
		}
		ly_pay_shoppingcar = (RelativeLayout) findViewById(R.id.ly_pay_shoppingcar);
		list = (ListView) findViewById(R.id.lv_shoppinglist_shoppingcar);
		all = (CheckBox) findViewById(R.id.btn_select_shoppingcar_all);
		delete = (Button) findViewById(R.id.btn_collect_shopingcar);
		button = (Button) findViewById(R.id.btn_pay_shoppingcar);
		r = (RelativeLayout) findViewById(R.id.ly_shoppinglist_shoppingcar);
		r1 = (RelativeLayout) findViewById(R.id.no_cart_item);
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
		userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL");
		if(!"NULL".equals(userInfo.getString(MySharedPreferences.STUDENT_ID, "NULL"))){
			
			getShoopingCart();
		}else{
			ly_pay_shoppingcar.setVisibility(View.GONE);
			all.setVisibility(View.GONE);
			delete.setVisibility(View.GONE);
			Toast.makeText(ShoppingCarActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
		}
		
		delete.setOnClickListener(this);
		all.setOnClickListener(this);
		button.setOnClickListener(this);
		
		all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(cars.size()!=0){
					if(isChecked){
						for(int i=0;i<cars.size();i++){
			            		adapter.mChecked.set(i,false);
			            	
			            }
						all.setText("全选");
						adapter.notifyDataSetChanged();
					}else{
						for(int i=0;i<cars.size();i++){
		            		adapter.mChecked.set(i,true);
		            	
						}
						all.setText("全不选");
						adapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
	public void getShoopingCart(){
		loginDialog = ProgressDialog.show(ShoppingCarActivity.this, "请稍后...", "正在处理...", true, true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				SharedPreferences shar = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0);
				String userId = shar.getString(MySharedPreferences.STUDENT_ID, "");//用户id
				List<String> names = new ArrayList<String>();
				names.add("StudentI");
				List<String> zhi = new ArrayList<String>();
				zhi.add(userId);
				cars = ParseXml.getShoppingInfo(names, zhi, "GetAllThingsFromStudentId");
				if(cars!=null){
					if(cars.size()!=0){
						adapter = new ShoppingCarAdpater(ShoppingCarActivity.this,cars);
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
		Intent intent = new Intent(ShoppingCarActivity.this, MainActivity.class);
		startActivity(intent);
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
	private void deleteShoppingCar(){

		listItemID.clear();  
        for(int i=0;i<adapter.mChecked.size();i++){  
            if(adapter.mChecked.get(i)){  
                listItemID.add(i);  
            }  
        }  
          
        if(listItemID.size()==0){  
        	Toast.makeText(ShoppingCarActivity.this, "请勾选需要删除的商品", Toast.LENGTH_SHORT).show();
            loginDialog.dismiss();
        }else{
        	for(int i=0;i<listItemID.size();i++){  
                ShoppingCar car1 = (ShoppingCar) list.getItemAtPosition(listItemID.get(i));
           	 	shopps.add(car1);
       	 	}  
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				List<String> names = new ArrayList<String>();
				List<String> zhi = new ArrayList<String>();
				
				names.add("shoppingCartIdList");
				String goodsids = null;
				String foodsids = null;
				for (int i = 0; i < shopps.size(); i++) {
					if(shopps.get(i).getGoods()!=null){
						if(goodsids==null){
							goodsids  =shopps.get(i).getCategoryId()+"";
						}else{
							goodsids +=","+shopps.get(i).getCategoryId();	
						}
					}else{
						if(foodsids==null){
							foodsids  =shopps.get(i).getCategoryId()+"";
						}else{
							foodsids +=","+shopps.get(i).getCategoryId();	
						}
					}
					
					
				}
				if(goodsids!=null){
					
					zhi.add(goodsids);
					fanhui = ParseXml.getDeleteGoodsFavorite(names, zhi, "DeleteGoodsListFromShoppingCart");
				}
				if(foodsids!=null){
					names.clear();
					zhi.clear();
					names.add("shopingCartIdList");
					zhi.add(foodsids);
					fanhui = ParseXml.getDeleteGoodsFavorite(names, zhi, "DeleteFoodsListFromShoppingCart");
				}
				if(fanhui){
					msg.what = MyMessages.DELETE_GOODSSHOPPING_OK;
				}
				else{
					msg.what = MyMessages.TIME_OUT+1;
				}
				handler.sendMessage(msg);
			}
			}).start();
        }
	
	}
	
	private void updateShoppingCar(){
		if(shopps.size()!=0){
			shopps.clear();
		}
		
		listItemID.clear();  
        for(int i=0;i<adapter.mChecked.size();i++){  
            if(adapter.mChecked.get(i)){  
                listItemID.add(i);  
            }  
        }  
        if(listItemID.size()==0){  
            loginDialog.dismiss();
        }else{  
            for(int i=0;i<listItemID.size();i++){  
                ShoppingCar car1 = (ShoppingCar) list.getItemAtPosition(listItemID.get(i));
                if(car1.getDish()!=null){
	                if("false".equals(car1.getDish().getIsBusy())){
	                	shopps.add(car1);
                	}
                }else{
                	shopps.add(car1);
                }
                
                
           	 }  
            
            if(adapter.number.size()!=0&&shopps.size()>0){
            	loginDialog = ProgressDialog.show(ShoppingCarActivity.this, "请稍后...", "正在处理...", true, true);
            	new Thread(new Runnable() {
					
					@Override
					public void run() {
						Message msg = new Message();
						List<String> names = new ArrayList<String>();
						
						
						List<String> zhi = new ArrayList<String>();
						for (int i = 0; i < shopps.size(); i++) {
							if(shopps.get(i).getDish()!=null){
								names.clear();
								
								names.add("shoppingCartFoodsId");
								names.add("ammount");
								zhi.add(shopps.get(i).getCategoryId()+"");
								zhi.add(shopps.get(i).getGoodsNumber()+"");
								addshopping = ParseXml.getAddShoopingCar(names,zhi,"EditShoppingCartFoods");
							}else{
								names.clear();
								
								names.add("shoppingCartId");
								names.add("ammount");
								zhi.add(shopps.get(i).getCategoryId()+"");
								zhi.add(shopps.get(i).getGoodsNumber()+"");
								addshopping = ParseXml.getAddShoopingCar(names,zhi,"EditShoppingCart");
								
							}
							
							zhi.clear();
						}
						if(addshopping){
							msg.what = MyMessages.ADD_SHOPPINGCAR_OK;
						}
						else{
							msg.what = MyMessages.ADD_SHOPPINGCAR_FAILD;
							Toast.makeText(ShoppingCarActivity.this, "操作失败，可能是因为您的购物车中有暂时不可购买的商品", Toast.LENGTH_SHORT).show();
						}
						handler.sendMessage(msg);
					}
				}).start();
            }else if(shopps.size()>0){
            	Intent intent = new Intent(ShoppingCarActivity.this,Intentctivity.class);
                Bundle bun = new Bundle();
                bun.putInt("number", shopps.size());
                for (int i = 0; i < shopps.size(); i++) {
					bun.putSerializable("shopps"+i, shopps.get(i));
				}
                intent.putExtras(bun);
                startActivity(intent);
            }else{
            	Toast.makeText(ShoppingCarActivity.this, "操作失败，可能是因为您的购物车中有暂时不可购买的商品", Toast.LENGTH_SHORT).show();
            }
        }
	
	}
	boolean addshopping;
	List<ShoppingCar> shopps = new ArrayList<ShoppingCar>();
	List<FoodsShoppingCar> foodsshopps = new ArrayList<FoodsShoppingCar>();
	protected boolean fanhui;
	@Override
	public void onClick(View v) {
		netWorkState = ConnectionDetector.getNetWorkState(ShoppingCarActivity.this);
		if(netWorkState!=null){
		int b = v.getId();
		switch (b) {
		case R.id.btn_collect_shopingcar:
			loginDialog = ProgressDialog.show(ShoppingCarActivity.this, "请稍后...", "正在处理...", true, false);
			deleteShoppingCar();
			
        
			break;
		case R.id.btn_pay_shoppingcar:
			updateShoppingCar();
			break;
		default:
			break;
		}
		}else{
			Toast.makeText(ShoppingCarActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
	}
}
