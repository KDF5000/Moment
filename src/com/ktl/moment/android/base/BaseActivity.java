/*** 
 * activity的基类，可以实现activity的公共部分，比如公共UI，广播...
 * @author KDF5000
 * @date 2015-3-29
 */
package com.ktl.moment.android.base;

import com.ktl.moment.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {
	
	protected TextView titleNameTv;
	protected ImageView refreshImg;
	protected ImageView searchImg;
	protected ImageView remindImg;
	protected ImageView addImg;
	protected RelativeLayout baseTitleReLayout;
	protected LinearLayout baseActivityLayout;
	protected FrameLayout contentLayout;
	protected TextView middleTitleTv;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_base);
		
		findViews();
		hideAllNavigationInfo();
	}
	
	private void findViews(){
		titleNameTv = (TextView) findViewById(R.id.title_left_view_name_tv);
		refreshImg = (ImageView) findViewById(R.id.title_refresh_img_view);
		searchImg = (ImageView) findViewById(R.id.title_search_img_view);
		remindImg = (ImageView) findViewById(R.id.title_remind_img_view);
		addImg = (ImageView) findViewById(R.id.title_add_img_view);
		baseTitleReLayout = (RelativeLayout) findViewById(R.id.activity_base_title_container_layout);
		baseActivityLayout = (LinearLayout) findViewById(R.id.activity_base_layout);
		contentLayout = (FrameLayout) findViewById(R.id.activity_base_content_container);
		
		middleTitleTv = (TextView) findViewById(R.id.middle_title_tv);
	}

	private void hideAllNavigationInfo(){
		setHomeTitleVisible(false);
		setMiddleTitleVisible(false);
	}
	
	protected void setHomeTitleVisible(boolean isVisible){
		if(isVisible){
			titleNameTv.setVisibility(View.VISIBLE);
			refreshImg.setVisibility(View.VISIBLE);
			searchImg.setVisibility(View.VISIBLE);
			remindImg.setVisibility(View.VISIBLE);
			addImg.setVisibility(View.VISIBLE);
		}else{
			titleNameTv.setVisibility(View.GONE);
			refreshImg.setVisibility(View.GONE);
			searchImg.setVisibility(View.GONE);
			remindImg.setVisibility(View.GONE);
			addImg.setVisibility(View.GONE);
		}
	}
	
	protected void setMiddleTitleVisible(boolean isVisible){
		if(isVisible){
			middleTitleTv.setVisibility(View.VISIBLE);
		}else{
			middleTitleTv.setVisibility(View.GONE);
		}
	}
	
	protected void setTitleTvName(int resStringId){
		titleNameTv.setText(resStringId);
	}
	
	protected void setMiddleTitleName(int resStringId){
		middleTitleTv.setText(resStringId);
	}
	
	protected void setBaseActivityBgColor(int resColorId){
		baseTitleReLayout.setBackgroundColor(resColorId);
		baseActivityLayout.setBackgroundColor(resColorId);
	}
	
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.title_refresh_img_view:
			Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
