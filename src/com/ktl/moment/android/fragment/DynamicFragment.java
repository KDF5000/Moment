package com.ktl.moment.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseFragment;

public class DynamicFragment extends BaseFragment {
	private static final String TAG = "DynamicFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
		
		return view;
	}
}
