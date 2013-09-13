package com.zero.tools;

import java.util.List;
import com.zero.activity.Register;
import com.zero.bean.School;
import com.zero.www.R;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SchoolAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	Register mContext;
	List<School> infos;
	public SchoolAdapter(Register modoActivity, List<School> array) {
		this.inflater=LayoutInflater.from(modoActivity);
		this.mContext=modoActivity;
		this.infos = array;
	}

	public int getCount() {
		return infos.size();
	}

	public Object getItem(int position) {
		return infos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.schoolview, null);
			holder.text = (TextView) convertView.findViewById(R.id.text);
//			holder.image = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		School info =infos.get(position);
		convertView.setVisibility(View.VISIBLE);
		holder.text.setText(info.getSchool_name());
//		holder.image.setImageResource(R.drawable.ic_launcher);
		
		
		return convertView;
	}

	private class ViewHolder{
//		ImageView image;
		TextView text;
	}
	
	
}