package com.zero.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UpdateManager {

	private String curVersion;
	private String newVersion;
	private int curVersionCode;
	private int newVersionCode;
	private String updateInfo;
	private UpdateCallback callback;
	private Context ctx;
    
	private int progress;  
	private Boolean hasNewVersion;
	private Boolean canceled;

	//存放更新APK文件的路径
	public static final String UPDATE_DOWNURL = "http://90world.51idctg.com/versionManager/90World.apk";
	//存放更新APK文件相应的版本说明路径
	public static final String UPDATE_CHECKURL = "http://90world.51idctg.com/versionManager/versionInfo.txt";
	public static final String UPDATE_APKNAME = "90World.apk";
	//public static final String UPDATE_VERJSON = "ver.txt";
	public static final String UPDATE_SAVENAME = "90World.apk";
	private static final int UPDATE_CHECKCOMPLETED = 1;
	 private static final int UPDATE_DOWNLOADING = 2; 
	 private static final int UPDATE_DOWNLOAD_ERROR = 3; 
	 private static final int UPDATE_DOWNLOAD_COMPLETED = 4; 
	 private static final int UPDATE_DOWNLOAD_CANCELED = 5;

	 //从服务器上下载apk存放文件夹
	 private String savefolder = getSDPath() + "/"+ "90world";
	public UpdateManager(Context context, UpdateCallback updateCallback) {
		ctx = context;
		callback = updateCallback;
		canceled = false;
		getCurVersion();
	}
	
	public String getNewVersionName()
	{
		return newVersion;
	}
	
	public String getUpdateInfo()
	{
		return updateInfo;
	}
	/** 获取SD卡路径 **/
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    } 
	private void getCurVersion() {
		try {
			PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			curVersion = pInfo.versionName;
			curVersionCode = pInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.e("update", e.getMessage());
		}

	}

	public void checkUpdate() {		
		hasNewVersion = false;
		new Thread(){
			@Override
			public void run() {
				Log.i("@@@@@", ">>>>>>>>>>>>>>>>>>>>>>>>>>>getServerVerCode() ");
				try {
					String verjson = getContent(UPDATE_CHECKURL);
					Log.i("@@@@", verjson
							+ "**************************************************");
					JSONArray array = new JSONArray(verjson);

					if (array.length() > 0) {
						JSONObject obj = array.getJSONObject(0);
						try {
							newVersionCode = Integer.parseInt(obj.getString("verCode"));
							newVersion = obj.getString("verName");
							updateInfo = "";
							Log.i("newVerCode", newVersionCode
									+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
							Log.i("newVerName", newVersion
									+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
							if (newVersionCode > curVersionCode) {
								hasNewVersion = true;
							}
						} catch (Exception e) {
							newVersionCode = -1;
							newVersion = "";
							updateInfo = "";
							
						}
					}
				} catch (Exception e) {
					Log.e("update", e.getMessage());
				}
				updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
			};
			// ***************************************************************
		}.start();

	}
	/**
     * 获取网址内容
     * @param url
     * @return
     * @throws Exception
     */
     public static String getContent(String url) throws Exception{
         StringBuilder sb = new StringBuilder();
         
         HttpClient client = new DefaultHttpClient();
         HttpParams httpParams = client.getParams();
         //设置网络超时参数
         HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
         HttpConnectionParams.setSoTimeout(httpParams, 5000);
         HttpResponse response = client.execute(new HttpGet(url));
         HttpEntity entity = response.getEntity();
         if (entity != null) {
             BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
             
             String line = null;
             while ((line = reader.readLine())!= null){
                 sb.append(line + "\n");
             }
             reader.close();
         }
         return sb.toString();
     } 
     
	public void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		
		intent.setDataAndType(
				Uri.fromFile(new File(savefolder, UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		ctx.startActivity(intent);
	}
	public void downloadPackage() 
	{
		
		
		new Thread() {			
			 @Override  
		        public void run() {  
		            try {  
		                URL url = new URL(UPDATE_DOWNURL);  
		              
		                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		                conn.connect();  
		                int length = conn.getContentLength();  
		                InputStream is = conn.getInputStream();  
		                  
		               
		                File ApkFile = new File(savefolder,UPDATE_SAVENAME);
		                
		                
		                if(ApkFile.exists())
		                {
		                	
		                	ApkFile.delete();
		                }
		                
		                
		                FileOutputStream fos = new FileOutputStream(ApkFile);  
		                 
		                int count = 0;  
		                byte buf[] = new byte[512];  
		                  
		                do{  
		                	
		                    int numread = is.read(buf);  
		                    count += numread;  
		                    progress =(int)(((float)count / length) * 100);  
		                   
		                    updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOADING)); 
		                    if(numread <= 0){      
		                        
		                    	updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
		                        break;  
		                    }  
		                    fos.write(buf,0,numread);  
		                }while(!canceled);  
		                if(canceled)
		                {
		                	updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_CANCELED);
		                }
		                fos.close();  
		                is.close();  
		            } catch (MalformedURLException e) {  
		                e.printStackTrace(); 
		                
		                updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR,e.getMessage()));
		            } catch(IOException e){  
		                e.printStackTrace();  
		                
		                updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR,e.getMessage()));
		            }  
		              
		        } 
		}.start();
	}

	public void cancelDownload()
	{
		canceled = true;
	}
	
	Handler updateHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UPDATE_CHECKCOMPLETED:
				
				callback.checkUpdateCompleted(hasNewVersion, newVersion);
				break;
			case UPDATE_DOWNLOADING:
				
				callback.downloadProgressChanged(progress);
				break;
			case UPDATE_DOWNLOAD_ERROR:
				
				callback.downloadCompleted(false, msg.obj.toString());
				break;
			case UPDATE_DOWNLOAD_COMPLETED:
				
				callback.downloadCompleted(true, "");
				break;
			case UPDATE_DOWNLOAD_CANCELED:
				
				callback.downloadCanceled();
			default:
				break;
			}
		}
	};

	public interface UpdateCallback {
		public void checkUpdateCompleted(Boolean hasUpdate,
				CharSequence updateInfo);

		public void downloadProgressChanged(int progress);
		public void downloadCanceled();
		public void downloadCompleted(Boolean sucess, CharSequence errorMsg);
	}

}
