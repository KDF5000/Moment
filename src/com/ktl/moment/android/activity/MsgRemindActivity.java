package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.CustomViewPagerAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.fragment.message.NewFansFragment;
import com.ktl.moment.android.fragment.message.NotificationFragment;
import com.ktl.moment.android.fragment.message.PersonalLetterFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MsgRemindActivity extends BaseActivity {

	@ViewInject(R.id.msg_viewpager)
	private ViewPager msgViewPager;

	private List<Fragment> fragmentList;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_msg_remind,
				contentLayout, true);

		ViewUtils.inject(this);
		initView();
		initViewPager();
	}

	private void initView() {
		setMiddleTitleVisible(true);
		setMiddleTitleName("消息提醒");
		setTitleRightImgVisible(true);
		setTitleRightImg(R.drawable.more);
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));
	}

	private void initViewPager() {
		if (fragmentList == null) {
			fragmentList = new ArrayList<Fragment>();
		}
		Fragment notificationFragment = new NotificationFragment();
		Fragment newFansFragment = new NewFansFragment();
		Fragment personalLetterFragment = new PersonalLetterFragment();
		fragmentList.add(notificationFragment);
		fragmentList.add(newFansFragment);
		fragmentList.add(personalLetterFragment);
		
		msgViewPager.setAdapter(new CustomViewPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		msgViewPager.setCurrentItem(0, true);
	}
}
