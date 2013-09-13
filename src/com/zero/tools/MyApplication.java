package com.zero.tools;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	private static MyApplication instance;
	private List<Activity> activityList = new LinkedList<Activity>();
	public MyApplication() {
	}
	public static MyApplication getInstance(){
		if(instance == null){
			instance = new MyApplication();
		}
		return instance;
	}
	public void addActivity(Activity activity){
		activityList.add(activity);
		Log.e("currentActivity", activity.getClass().getName());
//		if(activityList.size() > 10){
//			for(int i = 0; i < 5; i++){
//				activityList.get(i).finish();
//				Log.e("ExitActivity", activityList.get(i).getClass().getName());
//			}
//			
//			
//		}
	}
	public void exit(){
		for(Activity activity : activityList){
			activity.finish();
			Log.e("ExitActivity", activity.getClass().getName());
		}
		System.exit(0);
	}
}
