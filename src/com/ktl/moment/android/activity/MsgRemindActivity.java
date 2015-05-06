package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.CustomViewPagerAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.BadgeView;
import com.ktl.moment.android.fragment.message.NewFansFragment;
import com.ktl.moment.android.fragment.message.NotificationFragment;
import com.ktl.moment.android.fragment.message.PersonalLetterFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MsgRemindActivity extends BaseActivity {

	@ViewInject(R.id.msg_viewpager)
	private ViewPager msgViewPager;

	@ViewInject(R.id.msg_tab_notification)
	private TextView tabNotification;

	@ViewInject(R.id.msg_tab_new_fans)
	private TextView tabNewFans;

	@ViewInject(R.id.msg_tab_personal_msg)
	private TextView tabPersonalMsg;

	@ViewInject(R.id.msg_under_tag)
	private ImageView underTag;

	@ViewInject(R.id.msg_main_layout)
	private RelativeLayout mainLayout;

	@ViewInject(R.id.msg_menu_layout)
	private LinearLayout menuLayout;
	
	private List<Fragment> fragmentList;
	private int width;// 下方的小滑块的宽度
	private int currentItem = 0;

	private boolean isShow = false;
	
	private BadgeView fansBadge;
	private BadgeView msgBadge;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_msg_remind,
				contentLayout, true);

		ViewUtils.inject(this);
		initView();
		initTab();
		initViewPager();
		addBadge();
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

	private void initTab() {
		tabNotification.setTextColor(getResources().getColor(R.color.black));
		tabNewFans.setTextColor(getResources().getColor(
				R.color.text_color_unable));
		tabPersonalMsg.setTextColor(getResources().getColor(
				R.color.text_color_unable));
		tabNotification.setOnClickListener(new TitleTabOnClickListener(0));
		tabNewFans.setOnClickListener(new TitleTabOnClickListener(1));
		tabPersonalMsg.setOnClickListener(new TitleTabOnClickListener(2));

		DisplayMetrics ds = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(ds);
		width = ds.widthPixels / 3;
		LayoutParams params = underTag.getLayoutParams();
		params.width = width;
		Log.i("width", width + "");
		underTag.setLayoutParams(params);

		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);
		underTag.setImageMatrix(matrix);// 设置动画初始位置

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
		msgViewPager.setOnPageChangeListener(new ViewPagerChangeListener());
	}

	@OnClick({ R.id.title_back_img, R.id.title_right_img, R.id.msg_main_layout,
			R.id.msg_menu_layout })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.title_back_img:
			finish();
			break;
		case R.id.title_right_img:
			if (!isShow) {
				mainLayout.setVisibility(View.VISIBLE);
				isShow = true;
			} else {
				mainLayout.setVisibility(View.GONE);
				isShow = false;
			}
			break;
		case R.id.msg_main_layout:
			if (isShow) {
				mainLayout.setVisibility(View.GONE);
				isShow = false;
			}
			break;
		case R.id.msg_menu_layout:

			break;
		default:
			break;
		}
	}
	
	private void addBadge(){
		fansBadge = new BadgeView(this);
		fansBadge.setBackground(1, Color.parseColor("#ec584d"));
		fansBadge.setTargetView(tabNewFans);
		fansBadge.setBadgeMargin(0, 10, 28, 0);
		fansBadge.setText("");
		
		msgBadge = new BadgeView(this);
//		msgBadge.setBackground(3, Color.parseColor("#ec584d"));
		msgBadge.setTargetView(tabPersonalMsg);
		msgBadge.setBadgeMargin(0, 10, 28, 0);
		msgBadge.setBadgeCount(10);
	}

	/**
	 * title_tab点击监听
	 */
	public class TitleTabOnClickListener implements OnClickListener {
		private int index = 0;

		public TitleTabOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			msgViewPager.setCurrentItem(index);
		}
	};

	/**
	 * viewpager切换监听
	 * 
	 * @author HUST_LH
	 * 
	 */
	public class ViewPagerChangeListener implements OnPageChangeListener {
		int one = width;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currentItem == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currentItem == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				tabNotification.setTextColor(getResources().getColor(
						R.color.black));
				tabNewFans.setTextColor(getResources().getColor(
						R.color.text_color_unable));
				tabPersonalMsg.setTextColor(getResources().getColor(
						R.color.text_color_unable));
				break;
			case 1:
				if (currentItem == 0) {
					animation = new TranslateAnimation(0, one, 0, 0);
				} else if (currentItem == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				tabNotification.setTextColor(getResources().getColor(
						R.color.text_color_unable));
				tabNewFans.setTextColor(getResources().getColor(R.color.black));
				tabPersonalMsg.setTextColor(getResources().getColor(
						R.color.text_color_unable));
				break;
			case 2:
				if (currentItem == 0) {
					animation = new TranslateAnimation(0, two, 0, 0);
				} else if (currentItem == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				tabNotification.setTextColor(getResources().getColor(
						R.color.text_color_unable));
				tabNewFans.setTextColor(getResources().getColor(
						R.color.text_color_unable));
				tabPersonalMsg.setTextColor(getResources().getColor(
						R.color.black));
				break;
			}
			currentItem = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(250);
			underTag.startAnimation(animation);

		}

	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub
		
	}
}
