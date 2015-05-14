package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LabelSelectActivity extends Activity{

	private static final String TAG = "LabelSelectActivity";
	
	private LabelListAdapter labelListAdapter;
	private List<String> labelList;
	
	@ViewInject(R.id.label_list)
	private ListView labelListView;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_select);
		ViewUtils.inject(this);
		labelList = new ArrayList<String>();
		labelList.add("大数据");
		labelList.add("互联网");
		labelList.add("物联网");
		labelList.add("通信");
		labelList.add("大数据");
		labelList.add("互联网");
		labelList.add("物联网");
		labelList.add("通信");
		labelList.add("大数据");
		labelList.add("互联网");
		labelList.add("物联网");
		labelList.add("通信");
		labelList.add("大数据");
		labelList.add("互联网");
		labelList.add("物联网");
		labelList.add("通信");
		labelListAdapter = new LabelListAdapter(this, labelList);
		
		labelListView.setAdapter(labelListAdapter);
		
	}
	
	
	
	
	public class LabelListAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		private List<String> mLabelList;
		private Context mContext;
		public LabelListAdapter(Context context,List<String> labelList) {
			// TODO Auto-generated constructor stub
			this.mInflater = LayoutInflater.from(context);
			this.mContext = context;
			this.mLabelList = labelList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.mLabelList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.mLabelList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				convertView = this.mInflater.inflate(R.layout.select_label_listview_item, null);
				holder = new ViewHolder();
				holder.labelTv = (TextView) convertView.findViewById(R.id.label_textview);
				holder.labelCheckBox = (CheckBox) convertView.findViewById(R.id.label_checkbox);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final int mPosition = position;
			String label = mLabelList.get(position);
			holder.labelTv.setText(label);
			holder.labelCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					Log.i(TAG, "-->"+mPosition);
				}
			});
			return convertView;
		}
		
		private class ViewHolder{
			TextView labelTv;
			CheckBox labelCheckBox;
		}
	}

}
