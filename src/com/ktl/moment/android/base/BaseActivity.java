/*** 
 * activity�Ļ��࣬����ʵ��activity�Ĺ������֣����繫��UI���㲥...
 * @author KDF5000
 * @date 2015-3-29
 */
package com.ktl.moment.android.base;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.manager.AppManager;

public class BaseActivity extends FragmentActivity {
	
	protected TextView titleNameTv;
	protected ImageView setImg;
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
		AppManager.getInstance().addActivity(this);
	}
	
	private void findViews(){
		titleNameTv = (TextView) findViewById(R.id.title_left_view_name_tv);
		setImg = (ImageView) findViewById(R.id.title_add_img_view);
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
			setImg.setVisibility(View.VISIBLE);
		}else{
			titleNameTv.setVisibility(View.GONE);
			setImg.setVisibility(View.GONE);
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
	}
	protected void setBaseContainerBgColor(int resId){
		contentLayout.setBackgroundColor(resId);
	}
	
	/**
	 *  销毁当前界面跳到指定界面 参数为map --- map里类型暂定  根据实际过程修改
	 * @param classObj
	 * @param params
	 */
	protected void actionStart(Class<?> classObj,Map<String,String> params){
		Intent intent = new Intent(this,classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle bundle = new Bundle();
		for(String key : params.keySet()){
			bundle.putString(key, params.get(key));
		}
		intent.putExtra("data", bundle);
		this.startActivity(intent);
		this.finish();
	}
	/**
	 * 销毁当前界面跳到指定界面
	 * @param classObj
	 */
	protected void actionStart(Class<?> classObj){
		Intent intent = new Intent(this,classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	    AppManager.getInstance().removeActivity(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			exitBy2Click();
		}
		return false;
	}
	/** 
	 * 双击退出函数 
	 */  
	private static Boolean isExit = false;  
	  
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true; // 准备退出  
	        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                isExit = false; // 取消退出  
	            }  
	        }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
	  
	    } else { 
	    	AppManager.getInstance().AppExit(this);
	    }  
	}
}
