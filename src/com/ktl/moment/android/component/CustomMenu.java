package com.ktl.moment.android.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;

public class CustomMenu extends ViewGroup {
	private Status menuStatus = Status.SHOW;
	private OnMenuItemClickListener onMenuItemClickListener;
	
	public void setOnMenuItemClickListener(
			OnMenuItemClickListener itemClickListener) {
		this.onMenuItemClickListener = itemClickListener;
		int count = getChildCount();
		for (int i = 0; i < count ; i++) {
			View childView = getChildAt(i);
			final int pos = i;
			childView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onMenuItemClickListener!=null){
						onMenuItemClickListener.OnClick(v, pos);
					}
				}
			});
		}
	}

	public enum Status{
		SHOW,HIDDEN
	}
	public CustomMenu(Context context) {
		// TODO Auto-generated constructor stub
		this(context,null);
	}
	public CustomMenu(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		this(context,attrs,0);
	}

	public CustomMenu(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if(changed){
			int count = getChildCount();
			View child = getChildAt(0);
			int cWidth = child.getMeasuredWidth();
			int cHeight = child.getMeasuredHeight();
			int totalWidth = getMeasuredWidth();
			int totalHeight = getMeasuredHeight();
			int step = ((totalWidth - count * cWidth) - 30 * 2 )/(count-1);
			for (int i = 0; i < count ; i++) {
				View childView = getChildAt(i);
				int cl = 30 + i * cWidth + i*step;
				int ct = totalHeight - cHeight - 15;
				childView.layout(cl, ct, cl + cWidth, ct + cHeight);
			}
		}
	}
	/**
	 * 菜单显示隐藏
	 * @param duration
	 */
	public void toggleMenu (int duration){
		int count = getChildCount();

		for (int i = 0; i < count ; i++){
			final View childView = getChildAt(i);
			AlphaAnimation alphaAnim = null;
			
			if(menuStatus == Status.SHOW){
				alphaAnim = new AlphaAnimation(1f, 0.0f);
			}else{
				alphaAnim = new AlphaAnimation(0.0f, 1f);
			}
			alphaAnim.setDuration(duration);
			alphaAnim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					if(menuStatus==Status.SHOW){
						childView.setVisibility(View.GONE);
						childView.setClickable(true);
						
					}else{
						childView.setVisibility(View.VISIBLE);
						childView.setClickable(false);
					}
				}
			});
			alphaAnim.setFillAfter(true);
			childView.startAnimation(alphaAnim);
			final int pos = i;
			childView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onMenuItemClickListener!=null){
						onMenuItemClickListener.OnClick(v, pos);
					}
				}
			});
		}
		menuStatus = (menuStatus == Status.SHOW) ? Status.HIDDEN : Status.SHOW;	
	}
	
	public interface OnMenuItemClickListener{
		public void OnClick(View v,int pos);
	}
	
}
