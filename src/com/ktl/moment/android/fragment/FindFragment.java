package com.ktl.moment.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.FindListViewAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class FindFragment extends BaseFragment {
	private static final String TAG = "FindFragment";
	
//	@ViewInject(R.id.fragment_find_list)
	private ListView findListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_find, container, false);
		findListView = (ListView)view.findViewById(R.id.fragment_find_list);
//		ViewUtils.inject(view);
		
		findListView.setAdapter(new FindListViewAdapter(getActivity()));
		return view;
	}
}
