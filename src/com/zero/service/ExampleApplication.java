package com.zero.service;


import com.zero.www.R;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * For developer startup JPush SDK
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class ExampleApplication extends Application {
    private static final String TAG = "ExampleApplication";

    @Override
    public void onCreate() {
    	 Log.d(TAG, "onCreate");
         super.onCreate();
         JPushInterface.setDebugMode(false); 	//设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush
         
        //自定义样式1
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(ExampleApplication.this,R.layout.customer_notitfication_layout_a,R.id.icon, R.id.title, R.id.text);
 		builder.layoutIconDrawable = R.drawable.ic_launcher;
 		builder.statusBarDrawable = R.drawable.ic_launcher; 
 		JPushInterface.setPushNotificationBuilder(1, builder);
    }
}
