package com.ktl.moment.android.fragment.find;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.adapter.CustomViewPagerAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.CustomViewPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class FindFragment extends BaseFragment {
	private static final String TAG = "FindFragment";

	@ViewInject(R.id.find_fragment_view_pager)
	private CustomViewPager viewPager;

	private List<Fragment> fragmentList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_find, container, false);
		ViewUtils.inject(this, view);

		titleFindTabClick();
		initViewPager();

		return view;
	}

	private void initViewPager() {
		fragmentList = new ArrayList<Fragment>();
		Fragment channelFragment = new ChannelFragment();
		Fragment recomendFragment = new RecomendFragment();
		fragmentList.add(channelFragment);
		fragmentList.add(recomendFragment);

		viewPager.setAdapter(new CustomViewPagerAdapter(
				getChildFragmentManager(), fragmentList));
		viewPager.setCurrentItem(0, true);
	}

	private void titleFindTabClick() {
		((HomeActivity) getActivity()).titleMiddleRecommend
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((HomeActivity) getActivity()).titleMiddleRecommend
								.setBackgroundResource(R.drawable.title_middle_channel_shape_enable);
						((HomeActivity) getActivity()).titleMiddleRecommend
								.setTextColor(getResources().getColor(
										R.color.main_title_color));

						((HomeActivity) getActivity()).titleMiddleChannel
								.setBackgroundResource(R.drawable.title_middle_shape_unable);
						((HomeActivity) getActivity()).titleMiddleChannel
								.setTextColor(getResources().getColor(
										R.color.white));

						viewPager.setCurrentItem(1, true);
					}
				});
		((HomeActivity) getActivity()).titleMiddleChannel
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((HomeActivity) getActivity()).titleMiddleChannel
								.setBackgroundResource(R.drawable.title_middle_recommend_shape_enable);
						((HomeActivity) getActivity()).titleMiddleChannel
								.setTextColor(getResources().getColor(
										R.color.main_title_color));

						((HomeActivity) getActivity()).titleMiddleRecommend
								.setBackgroundResource(R.drawable.title_middle_shape_unable);
						((HomeActivity) getActivity()).titleMiddleRecommend
								.setTextColor(getResources().getColor(
										R.color.white));

						viewPager.setCurrentItem(0, true);
					}
				});
	}

}
