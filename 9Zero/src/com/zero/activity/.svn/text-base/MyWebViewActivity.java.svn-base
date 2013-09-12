package com.zero.activity;

import com.zero.tools.MyApplication;
import com.zero.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.ZoomControls;

@SuppressLint("SetJavaScriptEnabled")
public class MyWebViewActivity extends Activity{
	WebView wv;
	ZoomControls zoom;
	String addUrl;
	private String from;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.my_webview);
		MyApplication.getInstance().addActivity(this);
		getUrl();
		wv = (WebView) findViewById(R.id.body_webview);
		zoom = (ZoomControls) findViewById(R.id.zoomControls_webview);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(0);
		wv.getSettings().setBuiltInZoomControls(true);
		if(from != null){
			zoom.setVisibility(View.GONE);
			wv.getSettings().setBuiltInZoomControls(false);
		}
		wv.setWebChromeClient(new WebChromeClient() {
		   public void onProgressChanged(WebView view, int progress) {
			   setProgressBarIndeterminateVisibility(true);
			   MyWebViewActivity.this.setProgress(progress);
			   if(progress == 100){
				   setProgressBarIndeterminateVisibility(false);
			   }
		   }
		   @Override
			public void onReceivedTitle(WebView view, String title) {
			   MyWebViewActivity.this.setTitle(title);
				super.onReceivedTitle(view, title);
			}
		   
		});
		wv.setWebViewClient(new WebViewClient() {
			   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				    Toast.makeText(MyWebViewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			   }
			   public boolean shouldOverrideUrlLoading(WebView view, String url) {
				    view.loadUrl(url);
				    return super.shouldOverrideUrlLoading(view, url);
				}
			 });
		
		wv.loadUrl(addUrl);
		
		zoom.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wv.zoomIn();
			}
		});
		zoom.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wv.zoomOut();
			}
		});
		wv.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {    
                    if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
    
                        wv.goBack();   //后退     
                        return true;    //已处理     
                    }    
				}
				return false;
			}
		});
		
	}
	
	    
	private void getUrl() {
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		addUrl = b.getString("addUrl");
		from = b.getString("from");
	}


	public void loadurl(final WebView view,final String url){
	  new Thread(){
	   public void run(){
	     view.loadUrl(url);
	   }
	  }.start();
	}
}
