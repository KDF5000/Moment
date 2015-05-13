package com.ktl.moment.android.fragment.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.AccountActivity;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.utils.SharedPreferencesUtil;

public class ThirdFragment extends Fragment {

	private ImageView nextImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_splash_third, container,
				false);
		nextImg = (ImageView) view.findViewById(R.id.splash_next);

		nextImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferencesUtil.getInstance().setBoolean(
						C.SPKey.SPK_IS_SCAN_SPLASH, true);
				Intent intent = new Intent(getActivity(), AccountActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});

		return view;
	}
}
