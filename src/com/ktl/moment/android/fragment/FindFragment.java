package com.ktl.moment.android.fragment;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FindFragment extends BaseFragment {
	private static final String TAG = "FindFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_find, container, false);
		
		return view;
	}
}
