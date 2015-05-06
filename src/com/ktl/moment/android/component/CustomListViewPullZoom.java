package com.ktl.moment.android.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;

import com.ktl.moment.android.component.pullzoomview.PullToZoomListViewEx;

public class CustomListViewPullZoom extends PullToZoomListViewEx {
 
	private static final String TAG = "CustomListViewPullZoom";
	private OnScrollListener mOnScrollListener = null;
	/**
	 * 滑动监听接口
	 * @author KDF5000
	 *
	 */
	public interface OnScrollListener{
		public void OnScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount,int headerBottom);
		
	}
	
	public void setOnScrollListener(OnScrollListener onScrollListener){
		this.mOnScrollListener =  onScrollListener;
	}
	public CustomListViewPullZoom(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomListViewPullZoom(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		super.onScrollStateChanged(view, scrollState);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		if(this.mOnScrollListener != null){
			mOnScrollListener.OnScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, getHeaderContainerBottom());
		}
	}
	
}
