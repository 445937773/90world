package com.zero.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.zero.bean.Poster;
import com.zero.cache.ImageLoader;
import com.zero.tools.MyApplication;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.ParseXml;
import com.zero.www.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SomeProductActivity extends Activity implements OnClickListener{
	boolean getAddPic;
	protected List<Poster> posters;
	private ImageView iv1, iv2, iv3, iv4, iv5;
	private ImageLoader mImageLoader;
	protected Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				getGuanggao();
				break;
			case 2:
				showPic();
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supermarket_orderfood_main);
		MyApplication.getInstance().addActivity(this);
		findViewAndSetListener();
		checkTheAddPic();
		
	}

	protected void showPic() {
		mImageLoader = ImageLoader.getInstance(this);
		mImageLoader.addTask(posters.get(0).getImageUri(), iv1); //添加任务
		mImageLoader.addTask(posters.get(1).getImageUri(), iv2); //添加任务
		mImageLoader.addTask(posters.get(2).getImageUri(), iv3); //添加任务
		mImageLoader.addTask(posters.get(3).getImageUri(), iv4); //添加任务
		mImageLoader.addTask(posters.get(4).getImageUri(), iv5); //添加任务
		
	}

	private void checkTheAddPic() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				SharedPreferences userInfo = getSharedPreferences(MySharedPreferences.GET_ADDVER_OK, 0);  
				getAddPic = userInfo.getBoolean(MySharedPreferences.GET_ADDVER_OK, false);
				Log.e("+++++++++++++++++++", "循环");
				if(getAddPic){
					timer.cancel();
					Log.e("-----------------", getAddPic + "退出循环");
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			}
		}, 0, 500);
	
	}

	private void findViewAndSetListener() {
		iv1 = (ImageView) findViewById(R.id.iv_1st_add);
		iv2 = (ImageView) findViewById(R.id.iv_2st_add);
		iv3 = (ImageView) findViewById(R.id.iv_3st_add);
		iv4 = (ImageView) findViewById(R.id.iv_4st_add);
		iv5 = (ImageView) findViewById(R.id.iv_5st_add);
		iv1.setOnClickListener(this);
		iv2.setOnClickListener(this);
		iv3.setOnClickListener(this);
		iv4.setOnClickListener(this);
		iv5.setOnClickListener(this);
	}
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
			if(posters!=null){
				msg.what = 2;
			}
			else{
				msg.what = 3;
			}
			handler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onClick(View arg0) {
		int flag = -1;
		int item = -1;
		switch (arg0.getId()) {
		case R.id.iv_1st_add:
			flag = posters.get(0).getPointer();
			item = 0;
			break;
		case R.id.iv_2st_add:
			flag = posters.get(1).getPointer();
			item = 1;
			break;
		case R.id.iv_3st_add:
			flag = posters.get(2).getPointer();
			item = 2;
			break;
		case R.id.iv_4st_add:
			flag = posters.get(3).getPointer();
			item = 3;
			break;
		case R.id.iv_5st_add:
			flag = posters.get(4).getPointer();
			item = 4;
			break;
		default:
			break;
		}
		Bundle b = new Bundle();
		switch (flag) {
		case 0:
			Intent it = new Intent(SomeProductActivity.this, MyWebViewActivity.class); 
			Bundle bundle = new Bundle();
			bundle.putString("addUrl", posters.get(item).getPosterChaining());
			it.putExtras(bundle);
			startActivity(it);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case 1:
			b.putInt("poster", posters.get(item).getGoodsId());
			Intent intent = new Intent(SomeProductActivity.this, GoodsParticularInfoActivity.class);
			intent.putExtras(b);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		default:
			break;
		}
	}

}
