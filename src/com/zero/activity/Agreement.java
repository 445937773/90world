package com.zero.activity;

import com.zero.tools.MyApplication;

import android.app.Activity;
import android.os.Bundle;


public class Agreement extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.login);
		MyApplication.getInstance().addActivity(this);
	}

}
