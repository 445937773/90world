package com.zero.activity;

import java.util.ArrayList;
import java.util.List;

import com.zero.bean.Eatery;
import com.zero.bean.Goods;
import com.zero.tools.EateryInfoImageApdater;
import com.zero.tools.GoodsInfoImageApdater;
import com.zero.tools.MyApplication;
import com.zero.www.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SomeProductActivity extends Activity {
	//String where;
	GoodsInfoImageApdater adapter;
	RadioButton rb_supermarket, rb_orderfood;
	GridView listView;
	TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supermarket_orderfood_main);
		MyApplication.getInstance().addActivity(this);
		findViewAndSetListener();
		//where = getExtras();
		showWhat("market");
	}

	private void findViewAndSetListener() {
		rb_supermarket = (RadioButton) findViewById(R.id.rb_supermarket);
		rb_orderfood = (RadioButton) findViewById(R.id.rb_orderfood);
		listView = (GridView) findViewById(R.id.lv_supermarket_or_orderfood);
	}

	private void showWhat(String where) {
		if("market".equals(where)){
			rb_supermarket.setChecked(true);
			List<Goods> goods = new ArrayList<Goods>();
			Goods good = new Goods(null, 8.5, "haha");
			Goods good1 = new Goods(null, 70.3, "haha");
			goods.add(good);
			goods.add(good1);
			goods.add(good);
			goods.add(good);
			goods.add(good);
			
			adapter = new GoodsInfoImageApdater(this, goods);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Toast.makeText(SomeProductActivity.this, "进来了", Toast.LENGTH_SHORT).show();
					text = (TextView) findViewById(R.id.iv_supermarket_orderfood_list_item_firstimage_text);
					Goods goods3 = (Goods)listView.getItemAtPosition(arg2);
					Toast.makeText(SomeProductActivity.this, goods3.getPrice()+"", Toast.LENGTH_SHORT).show();
				}
			});
		}
		if("res".equals(where)){
			rb_orderfood.setChecked(true);
			List<Eatery> eateries = new ArrayList<Eatery>();
			Eatery eatery = new Eatery(1, "顺兴美食", "", "", "", "");
			eateries.add(eatery);
			eateries.add(eatery);
			eateries.add(eatery);
			eateries.add(eatery);
			eateries.add(eatery);
			eateries.add(eatery);
			eateries.add(eatery);
			EateryInfoImageApdater ad = new EateryInfoImageApdater(this, eateries);
			listView.setAdapter(ad);
			
		}
		
		
	}
}
