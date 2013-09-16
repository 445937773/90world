package com.zero.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.bean.Poster;
import com.zero.cache.ImageFileCache;
import com.zero.cache.ImageGetFromHttp;
import com.zero.cache.ImageMemoryCache;
import com.zero.tools.ConnectionDetector;
import com.zero.tools.MyApplication;
import com.zero.tools.MyImageView;
import com.zero.tools.MyMessages;
import com.zero.tools.MyMethods;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
import com.zero.www.R;
@SuppressLint("HandlerLeak")
public class HomeActivity extends Activity implements OnClickListener{
	private int currentItem = 0;
	ImageFileCache fileCache = new ImageFileCache();
	ImageMemoryCache memoryCache;
	FrameLayout show;
	LinearLayout order, logout, login, register;;
	MyImageView jock, idea, shuma, tese;
	ImageView iv_icon, iv_search;
	TextView tv_userName, tv_title;
	ViewPager viewPager;
	ProgressBar pb;
	private List<ImageView> imageViews = new ArrayList<ImageView>();
	private List<String> titles = new ArrayList<String>();
	private ScheduledExecutorService scheduledExecutorService;
	protected List<Poster> posters;
	protected Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
			switch (msg.what) {
			//从网络获取广告资源完毕
			case MyMessages.CHANGE_PWD_OK:
				
				break;
			//从网络获取广告图片完毕
			case MyMessages.GET_ADD_PIC_OK:
				showAddversting();
				SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.GET_ADDVER_OK, 0);  
				userInfo.edit().putBoolean(MySharedPreferences.GET_ADDVER_OK, true).commit();
				Log.e("！！！！！！！！！！！",  "获取广告完毕");
				break;
			case MyMessages.CHANGE_PWD_FAILD:
				pb.setVisibility(View.GONE);
				Toast.makeText(HomeActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	private ArrayList<View> dots;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		MyApplication.getInstance().addActivity(this);
		memoryCache = new ImageMemoryCache(this);
		//找到组件并设置监听
		findViewAndSetlistener();
		
		showUserName();
		netWorkState = ConnectionDetector.getNetWorkState(HomeActivity.this);
		
		if(netWorkState!=null){
			getGuanggao();
		}else{
			Toast.makeText(HomeActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		//登录界面跳转
		show.setVisibility(View.GONE);
}	
	protected void showAddversting() {
		if(posters != null&&posters.size()!=0){
			for (int i = 0; i < posters.size(); i++) {
				ImageView imageView = new ImageView(HomeActivity.this);
				imageView.setImageBitmap(bitmap.get(i));
				imageView.setId(i);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				imageViews.add(imageView);
				String title = posters.get(i).getTitle();
				titles.add(title);
			}
			dots = new ArrayList<View>();
			dots.add(findViewById(R.id.v_dot0));
			dots.add(findViewById(R.id.v_dot1));
			dots.add(findViewById(R.id.v_dot2));
			dots.add(findViewById(R.id.v_dot3));
			dots.add(findViewById(R.id.v_dot4));
			tv_title.setText(titles.get(0));//	
			viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
			startPlayAdd();
			// 设置一个监听器，当ViewPager中的页面改变时调用
			viewPager.setOnPageChangeListener(new MyPageChangeListener());
			
		}
		
	}
	/**
	 * 开始播放广告
	 */
	private void startPlayAdd() {
		pb.setVisibility(View.GONE);
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2, 3, TimeUnit.SECONDS);
		onResume();
	}
	
	@Override
	protected void onRestart() {
		if(scheduledExecutorService != null){
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			// 当Activity显示出来后，每两秒钟切换一次图片显示
			scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2, 3, TimeUnit.SECONDS);
		}
		show.setVisibility(View.GONE);
		super.onRestart();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(scheduledExecutorService != null){
			scheduledExecutorService.shutdown();
		}
		super.onPause();
	}
	//广告的点击事件
		@Override
		protected void onResume() {	
			for(int i=0;i<imageViews.size();i++){
				imageViews.get(i).setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						netWorkState = ConnectionDetector.getNetWorkState(HomeActivity.this);
						if(netWorkState!=null){
							Bundle b = new Bundle();
							Poster p = posters.get(imageViews.get(currentItem).getId());
							if(p.getPointer()!=0){
								
								b.putInt("poster", p.getGoodsId());
								Intent intent = new Intent(HomeActivity.this, GoodsParticularInfoActivity.class);
								intent.putExtras(b);
								startActivity(intent);
								overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
							}else{
								Intent it = new Intent(HomeActivity.this, MyWebViewActivity.class); 
								Bundle bundle = new Bundle();
								bundle.putString("addUrl", p.getPosterChaining());
								it.putExtras(bundle);
								startActivity(it);
								overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
							}
						}else{
							Toast.makeText(HomeActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
						}
						
						

					}
				});
			}
			super.onResume();
		}
		
//
//		@Override
//		protected void onStop() {
//			// 当Activity不可见的时候停止切换
//			if(scheduledExecutorService != null){
//				scheduledExecutorService.shutdown();
//			}
//			super.onStop();
//		}
		List<Bitmap> bitmap = new ArrayList<Bitmap>();
		private Object netWorkState;
	/**
	 * 从网络获取广告资源
	 */
	private void getGuanggao() {
		new Thread(new Runnable() {

			@Override
			public void run() {
			
			Message msg = new Message();
			//获取广告对象
			posters = ParseXml.getPosterInfo(null, null, "GetAllAdvertising");
			if(posters != null){
				//根据广告对象获取对应的图片
				for (int i = 0; i < posters.size(); i++) {
					String url = posters.get(i).getImageUri();
					// 从内存缓存中获取图片
			        Bitmap result = memoryCache.getBitmapFromCache(url);
			        if (result == null) {
			            // 文件缓存中获取
			            result = fileCache.getImage(url);
			            Log.e("getPic:" + url + "from", "FileCache");
			            if (result == null) {
			                // 从网络获取
			                result = ImageGetFromHttp.downloadBitmap(url);
			                if (result != null) {
			                    fileCache.saveBitmap(result, url);
			                    memoryCache.addBitmapToCache(url, result);
			                   
			                }
			            } else {
			                // 添加到内存缓存
			                memoryCache.addBitmapToCache(url, result);
			            }
			        }else{
			        	 Log.e("getPic:" + url + "from", "MemoryCache");
			        }
			        bitmap.add(result);
				}
				if(bitmap.size() == 5){
		        	msg.what = MyMessages.GET_ADD_PIC_OK;
		        }else{
		        	msg.what = MyMessages.CHANGE_PWD_FAILD;
		        }
		        handler .sendMessage(msg);
			}
			}
		}).start();
	}
	/**
	 * 找到学生姓名并显示
	 */
	private void showUserName() {
		SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0); 
		if(userInfo != null){
			String username = userInfo.getString(MySharedPreferences.STUDENT_NAME, "登陆");  
			tv_userName.setText(username); 
			if(username.equals("登陆")){
				logout.setVisibility(View.GONE);
				login.setVisibility(View.VISIBLE);
			}else{
				logout.setVisibility(View.VISIBLE);
				login.setVisibility(View.GONE);
			}
		}
		
	}

	private void findViewAndSetlistener() {
		pb = (ProgressBar) findViewById(R.id.pb_home);
		tv_userName = (TextView) findViewById(R.id.tv_usernameture_main);
		iv_icon = (ImageView) findViewById(R.id.iv_top_login);
		iv_search = (ImageView) findViewById(R.id.iv_top_search);
		jock = (MyImageView) findViewById(R.id.c_joke);
		idea = (MyImageView) findViewById(R.id.c_idea);
		shuma = (MyImageView) findViewById(R.id.c_constellation);
		tese = (MyImageView) findViewById(R.id.c_recommend);
		
		logout = (LinearLayout) findViewById(R.id.layout_logout);
		jock.setOnClickListener(this);
		idea.setOnClickListener(this);
		shuma.setOnClickListener(this);
		tese.setOnClickListener(this);
		logout.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		LinearLayout l_login = (LinearLayout) findViewById(R.id.layout_home_page_top);
		l_login.setOnClickListener(this);
		show = (FrameLayout) findViewById(R.id.layout_frame_show);
		show.setVisibility(View.GONE);
		viewPager = (ViewPager) findViewById(R.id.vp);
		
		register = (LinearLayout) findViewById(R.id.layout_register);
		login = (LinearLayout) findViewById(R.id.layout_login);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		iv_icon.setOnClickListener(this);
		iv_search.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		netWorkState = ConnectionDetector.getNetWorkState(HomeActivity.this);
		if(netWorkState!=null){
			Intent intent;
			Bundle b;
			switch (v.getId()) {
			case R.id.iv_top_login:
				
				TextView t_login = (TextView) findViewById(R.id.tv_username_main);
				String sLogin = t_login.getText().toString();
				if(!sLogin.equals("登录")){
					
				}
				if(show.getVisibility() == View.VISIBLE){
					show.setVisibility(View.GONE);
				}else{
					show.setVisibility(View.VISIBLE);
				}
				break;

			case R.id.layout_login:
				b = new Bundle();
				b.putString("where", "home");
				intent = new Intent(HomeActivity.this,Login.class);
				intent.putExtras(b);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				
				break;
			case R.id.layout_register:
				b = new Bundle();
				b.putString("where", "home");
				intent = new Intent(HomeActivity.this,Register.class);
				intent.putExtras(b);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
			case R.id.c_joke:
				b = new Bundle();
				b.putString("goto", "超市");
				goTo(GoodsCategoryActivity.class, b);
				break;
			
			case R.id.c_idea:
				goTo(FoodsCategoryResActivity.class, null);
				break;
				
			case R.id.c_constellation:
				b = new Bundle();
				b.putString("names", "肯德基");
				goTo(GoodscategorySmallActivity.class, b);
				break;
			
			case R.id.c_recommend:
				b = new Bundle();
				b.putString("names", "90创业平台");
				goTo(GoodscategorySmallActivity.class, b);
				break;
			case R.id.iv_top_search:
				//跳到搜索
				b = new Bundle();
				b.putString("check", "searchActivity");
				intent = new Intent(HomeActivity.this, MainActivity.class);
				intent.putExtras(b);
				startActivity(intent);
				finish();
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
				
			case R.id.layout_logout:
				Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("您确定要注销该账号吗?");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.STUDENT_INFO, 0); 
						userInfo.edit().clear().commit();
						Toast.makeText(HomeActivity.this, "注销成功!", Toast.LENGTH_SHORT).show();
						tv_userName.setText("登录");
						iv_icon.setVisibility(View.VISIBLE);
						show.setVisibility(View.GONE);
						logout.setVisibility(View.GONE);
						login.setVisibility(View.VISIBLE);
					}
				});
				builder.setNegativeButton("点错了", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.create().show();
			default:
				break;
			}
		}else{
			Toast.makeText(HomeActivity.this, MyMethods.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	private void goTo(Class<?> className, Bundle bundle) {
		Intent intent = new Intent(HomeActivity.this, className);
		if(bundle != null){
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return posters.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			arg0.setFocusable(true);
			
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
	
	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;
		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(titles.get(position));
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
		}

	}

	
}