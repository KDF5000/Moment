package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.android.fragment.splash.FirstFragment;
import com.ktl.moment.android.fragment.splash.SecondFragment;
import com.ktl.moment.android.fragment.splash.ThirdFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class SplashActivity extends FragmentActivity implements
		OnPageChangeListener {

	private ViewPager viewPager;
	private ImageView moveLine;
	private List<Fragment> fragmentList;

	private int moveWidth;
	private int currentPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		viewPager = (ViewPager) findViewById(R.id.splash_viewpager);
		moveLine = (ImageView) findViewById(R.id.splash_move_line);

		// 获取滑动条的宽度
		Bitmap bmp = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.splash_move_line);
		moveWidth = bmp.getWidth();

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new FirstFragment());
		fragmentList.add(new SecondFragment());
		fragmentList.add(new ThirdFragment());

		viewPager.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return fragmentList.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return fragmentList.get(arg0);
			}
		});
		viewPager.setCurrentItem(0, true);
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		RelativeLayout.LayoutParams params = (LayoutParams) moveLine
				.getLayoutParams();

		switch (currentPage) {
		case 0:
			if (arg0 == 0) {// 0--->1
				params.leftMargin = (int) (currentPage * moveWidth + arg1
						* moveWidth);
			}
			break;
		case 1:
			if (arg0 == 1) {// 1--->2
				params.leftMargin = (int) (currentPage * moveWidth + arg1
						* moveWidth);
			} else if (arg0 == 0) {// 1--->0
				params.leftMargin = (int) (currentPage * moveWidth - ((1 - arg1) * moveWidth));
			}
			break;
		case 2:
			if (arg0 == 1) {// 2--->1
				params.leftMargin = (int) (currentPage * moveWidth - ((1 - arg1) * moveWidth));
			}
			break;
		default:
			break;
		}
		moveLine.setLayoutParams(params);
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		currentPage = arg0;
	}

}
