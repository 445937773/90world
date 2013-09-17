package com.zero.activity;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

import com.zero.tools.DialogHelper;
import com.zero.tools.MyApplication;
import com.zero.tools.MySharedPreferences;
import com.zero.tools.UpdateManager;
import com.zero.www.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsActivity extends Activity implements OnClickListener{
	private CheckBox mCheckBox, mCheckBox_Push;
	private SharedPreferences settings;
	private boolean areGetBitmap, areGetPush;
	private RelativeLayout ly_back, layout_check_version;
	private UpdateManager updateMan;
	private ProgressDialog updateProgressDialog;
	private String curVersion;
	private TextView versionCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		MyApplication.getInstance().addActivity(this);
		mCheckBox = (CheckBox) findViewById(R.id.cb_auto_load_picture_setting);
		mCheckBox_Push = (CheckBox) findViewById(R.id.cb_get_push_setting);
		ly_back = (RelativeLayout) findViewById(R.id.ly_back);
		layout_check_version = (RelativeLayout) findViewById(R.id.layout_check_version);
		versionCode = (TextView) findViewById(R.id.tv_version_code);
		ly_back.setOnClickListener(this);
		layout_check_version.setOnClickListener(this);
		//获取SharedPreferences
		settings = getSharedPreferences(MySharedPreferences.SETTINGS, 0);
		areGetBitmap = settings.getBoolean("areGetBitmap", true);
		areGetPush = settings.getBoolean("areGetPush", true);
		
		//设置缺省值
		mCheckBox.setChecked(areGetBitmap);
		mCheckBox_Push.setChecked(areGetPush);
		
		//为两个CheckBox添加事件
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.e("areGetBitmap_mCheckBox", isChecked + "");
				settings.edit().putBoolean("areGetBitmap", isChecked).commit();
			}
		});
		mCheckBox_Push.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.e("areGetPush_mCheckBox", isChecked + "");
				settings.edit().putBoolean("areGetPush", isChecked).commit();
				if(isChecked){
					JPushInterface.resumePush(getApplicationContext());
				}else{
					JPushInterface.stopPush(getApplicationContext());
				}
			}
		});
		
		//清除缓存
		RelativeLayout clean = (RelativeLayout) findViewById(R.id.layout_clean_cache_setting);
		clean.setOnClickListener(this);
		
		//获取版本号
		curVersion = getCurVersion();
		versionCode.setText("当前版本：" + curVersion);
		
	}
	private String getCurVersion() {
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			curVersion = pInfo.versionName;
		} catch (NameNotFoundException e) {
			Log.e("update", e.getMessage());
		}
		return curVersion;

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly_back:
			onBackPressed();
			break;
		case R.id.layout_clean_cache_setting:
			long size = getFolderSize(new File(getDirectory()));
			String sSize = "";
			if(size > 0){
				if(size < 1048576){
					sSize = size/1024 + "KB";
				}
				else{
					sSize = size/1048576 + "M";
				}
				Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("您确定要删除占用您磁盘的" + sSize + "缓存吗?");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						RecursionDeleteFile(new File(getDirectory()));
						Toast.makeText(SettingsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.create().show();
			}else{
				Toast.makeText(this, "您刚刚已经清理过了!", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.layout_check_version:
			checkVesin();
		default:
			break;
		}
	}
	//更新所用
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		private void checkVesin() {
			updateMan = new UpdateManager(this, appUpdateCb);
			updateMan.checkUpdate();
		}
		// 自动更新回调函数
			UpdateManager.UpdateCallback appUpdateCb = new UpdateManager.UpdateCallback() 
			{

				public void downloadProgressChanged(int progress) {
					if (updateProgressDialog != null
							&& updateProgressDialog.isShowing()) {
						updateProgressDialog.setProgress(progress);
					}

				}

				public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
					if (updateProgressDialog != null
							&& updateProgressDialog.isShowing()) {
						updateProgressDialog.dismiss();
					}
					if (sucess) {
						updateMan.update();
					} else {
						DialogHelper.Confirm(SettingsActivity.this,
								R.string.dialog_error_title,
								R.string.dialog_downfailed_msg,
								R.string.dialog_downfailed_btndown,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										updateMan.downloadPackage();

									}
								}, R.string.dialog_downfailed_btnnext, null);
					}
				}

				public void downloadCanceled() 
				{
				}

				public void checkUpdateCompleted(Boolean hasUpdate,
						CharSequence updateInfo) {
					if (hasUpdate) {
						DialogHelper.Confirm(SettingsActivity.this,
								getText(R.string.dialog_update_title),
								getText(R.string.dialog_update_msg).toString()
								+updateInfo+
								getText(R.string.dialog_update_msg2).toString(),
										getText(R.string.dialog_update_btnupdate),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										updateProgressDialog = new ProgressDialog(
												SettingsActivity.this);
										updateProgressDialog
												.setMessage(getText(R.string.dialog_downloading_msg));
										updateProgressDialog.setIndeterminate(false);
										updateProgressDialog
												.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
										updateProgressDialog.setMax(100);
										updateProgressDialog.setProgress(0);
										updateProgressDialog.show();

										updateMan.downloadPackage();
									}
								},getText( R.string.dialog_update_btnnext), null);
					}else{
						Toast.makeText(SettingsActivity.this, "当前是最新版本!", Toast.LENGTH_SHORT).show();
					}

				}
			};
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	public void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

	/** 获得缓存目录 **/
    private String getDirectory() {
        String dir = getSDPath() + "/" + "90world";
        return dir;
    }
                                                                
    /** 取SD卡路径 **/
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
    /** 
     * 获取文件夹大小 
     * @param file File实例 
     * @return long 单位为M 
     * @throws Exception 
     */  
    public static long getFolderSize(java.io.File file){  
        long size = 0;
        java.io.File[] fileList = file.listFiles();
        if(fileList != null){
        	for (int i = 0; i < fileList.length; i++)
        	{  
        		if (fileList[i].isDirectory())  
        		{  
        			size = size + getFolderSize(fileList[i]);  
        		} else  
        		{  
        			size = size + fileList[i].length();  
        		}  
        	}  
        }
        return size;  
    }  

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
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
