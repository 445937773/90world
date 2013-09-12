package com.zero.tools;

import java.util.List;

import com.zero.bean.Address;
import com.zero.bean.Student;
import com.zero.www.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressAdapter extends BaseAdapter {

	Context content;
	List<Address> address;
	private LayoutInflater inflater;
	public AddressAdapter(Context content,List<Address> address, Student student) {
		// TODO Auto-generated constructor stub
		this.inflater=LayoutInflater.from(content);
		this.address = address;
		this.content = content;
	}
	@Override
	public int getCount() {
		return address.size();
		}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return address.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder=null;
		if(arg1==null){
			holder = new ViewHolder();
			arg1=inflater.inflate(R.layout.address_item, null);
			holder.text1 = (TextView) arg1.findViewById(R.id.tv_name_address_item);
			holder.text2 = (TextView) arg1.findViewById(R.id.tv_phone_number_address_item);
			holder.text3 = (TextView) arg1.findViewById(R.id.tv_detail_address_address_item);
			holder.text4 = (TextView) arg1.findViewById(R.id.tv_name_address_item_gone);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		Address info =address.get(arg0);
		arg1.setVisibility(View.VISIBLE);
		holder.text1.setText(info.getRecipient());
		holder.text2.setText(info.getPhoneNum());
		holder.text3.setText(info.getAddressInfo());
		holder.text4.setText(info.getAddressId() + "");
		
		return arg1;
	}
	private class ViewHolder{
		TextView text1,text2,text3,text4;
	}
	
}
