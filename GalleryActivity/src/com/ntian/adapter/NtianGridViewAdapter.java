package com.ntian.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.android.gallery3d.R;
import com.ntian.model.OptionButton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NtianGridViewAdapter extends BaseAdapter {
	Context mContext;
	List<OptionButton> mOptionList;
	private LayoutInflater mInflater;

	public NtianGridViewAdapter(Context context, List<OptionButton> optionList) {
		this.mContext = context;
		this.mOptionList = optionList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mOptionList.size();
	}

	@Override
	public Object getItem(int position) {
		return mOptionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.ntian_option_gridview_item, null);
			holder.button = (ImageButton) convertView.findViewById(R.id.ntian_option_icon);
			holder.name = (TextView) convertView
					.findViewById(R.id.ntian_option_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final OptionButton option = mOptionList.get(position);
		
		holder.name.setText(option.getOptionName());
		holder.button.setBackgroundResource(option.getDrawableId());
		
		holder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Toast.makeText(mContext, option.getOptionName(), 0).show();
				
			}
		});
		
		return convertView;
	}

	class ViewHolder {
		ImageButton button;
		TextView name;
	}

}
