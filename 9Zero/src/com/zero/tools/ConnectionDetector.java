package com.zero.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    public static String getNetWorkState(Context context){
    	String state = null;
    	ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	 NetworkInfo info = connectMgr.getActiveNetworkInfo();
    	 if(info != null){
    		 if(info.getType() == ConnectivityManager.TYPE_WIFI){
    			 state = "WIFI连接";
    		 }else if(info.getType() == ConnectivityManager.TYPE_MOBILE){
    			 state = "手机网络连接";
    		 }
    	 }
    	 return state;
    }
    
}
