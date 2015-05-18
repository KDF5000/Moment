package com.ktl.moment.android.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.BottomMenu;
import com.ktl.moment.android.component.BottomMenu.OnMenuItemClickListener;
import com.ktl.moment.android.component.MenuImageText;
import com.ktl.moment.android.fragment.MomentFragment;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.im.entity.XgMessage;
import com.ktl.moment.im.xg.receiver.XgMessageReceiver;
import com.ktl.moment.im.xg.receiver.XgMessageReceiver.OnCustomMessageListener;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.DensityUtil;
import com.ktl.moment.utils.LogUtil;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.db.DbTaskHandler;
import com.ktl.moment.utils.db.DbTaskType;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.loopj.android.http.RequestParams;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

public class HomeActivity extends BaseActivity implements OnCustomMessageListener {
	private static final String TAG = "HomeAtivity";

	private BottomMenu customMenu;// 菜单
	private FragmentManager fragmentManager;// 管理器
	private FragmentTransaction fragmentTransaction;// fragment事务
	private String currentFgTag = "";// 一定要和需要默认显示的fragment 不一样
	private View meRedDotView = null;//我的界面小红点
	private View dynamicRedDotView = null;//动态小红点
	
	private DbTaskHandler dbTaskHandler = new DbTaskHandler(this);

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		initEvent();

		// 初始为发现界面
		fragmentManager = getSupportFragmentManager();// 获取fragment的管理器
		switchMenuByTag(C.menu.FRAGMENT_DEFAULT_SHOW_TAG);// 设置默认的界面

		// 注册信鸽
		registXgPush();
		//初始化数据库
		//初始化小红点
		meRedDotView = initRedDotView(C.menu.FRAGMENT_ME_MENU_ID);
		dynamicRedDotView = initRedDotView(C.menu.FRAGMENT_DYNAMIC_MENU_ID);
		meRedDotView.setVisibility(View.GONE);
		dynamicRedDotView.setVisibility(View.GONE);
	}
	
	/**
	 * 初始化view
	 */
	private void initView() {
		getLayoutInflater()
				.inflate(R.layout.activity_home, contentLayout, true);

		setMiddleTitleVisible(true);
		setMiddleTitleName(R.string.attention_text_view);
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));// 设置title颜色
		setBaseContainerBgColor(getResources().getColor(
				R.color.main_content_container_color));// 设置内容区域颜色
		customMenu = (BottomMenu) findViewById(R.id.bottom_menu);
		customMenu.setDefaultCheckedMenu(R.id.menu_foucs);

	}

	/**
	 * 初始化一些事件
	 */
	private void initEvent() {

		customMenu.setOnMenuItemClick(new OnMenuItemClickListener() {

			@Override
			public void OnClick(int id) {
				// TODO Auto-generated method stub
				String tag = "";
				switch (id) {
				case C.menu.FRAGMENT_FIND_MENU_ID:
					tag = C.menu.FRAGMENT_FIND_TAG;
					setTitleTvNameEmpty();
					setMiddleFindTabVisible(true);
					setTitleRightImgVisible(false);
					setMiddleTitleVisible(false);
					break;
				case C.menu.FRAGMENT_DYNAMIC_MENU_ID:
					tag = C.menu.FRAGMENT_DYNAMIC_TAG;
					setMiddleTitleVisible(true);
					setMiddleTitleName(R.string.attention_text_view);
					setTitleRightImgVisible(false);
					setMiddleFindTabVisible(false);
					setTitleTvNameEmpty();
					hideRedDot(dynamicRedDotView);
					break;
				case C.menu.FRAGMENT_ADD_MOMENT_MENU_ID:
					Intent editorIntent = new Intent(HomeActivity.this,
							EditorActivity.class);
					startActivity(editorIntent);
					
					return;
				case C.menu.FRAGMENT_MOMENT_MENU_ID:
					tag = C.menu.FRAGMENT_MOMENT_TAG;
					setHomeTitleVisible(true);
					setTitleTvName(R.string.moment_text_view);
					setTitleRightImgVisible(true);
					setTitleRightImg(R.drawable.sync_1);
					setMiddleTitleVisible(false);
					setMiddleFindTabVisible(false);
					break;
				case C.menu.FRAGMENT_ME_MENU_ID:
					tag = C.menu.FRAGMENT_ME_TAG;
					setMiddleTitleVisible(true);
					setMiddleTitleName(R.string.me_text_view);
					setMiddleFindTabVisible(false);
					setTitleRightImgVisible(false);
					setTitleTvNameEmpty();
					hideRedDot(meRedDotView);
					break;
				}
				switchMenuByTag(tag);
			}

		});
	}

	private void setCurrentTag(String tag) {
		this.currentFgTag = tag;
	}

	/**
	 * 通过tag切换标签
	 * 
	 * @param selectedTag
	 */
	private void switchMenuByTag(String selectedTag) {
		if (selectedTag == currentFgTag || selectedTag.equals(currentFgTag)) {
			return;
		}
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//
		if (selectedTag != null && !selectedTag.equals("")) {
			detachFragment(getFragmentByTag(currentFgTag));// detach当前的fragment
		}
		attachFragment(R.id.home_content_container,
				getFragmentByTag(selectedTag), selectedTag);
		commitTransactions();// 提交事务
		setCurrentTag(selectedTag);// 设置当前的tag
	}

	/**
	 * 提交fragmenttransaction
	 */
	private void commitTransactions() {
		if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
			fragmentTransaction.commitAllowingStateLoss();
			fragmentTransaction = null;
		}
	}

	/**
	 * attach fragment
	 * 
	 * @param layout
	 * @param f
	 * @param tag
	 */
	private void attachFragment(int layout, Fragment f, String tag) {
		if (f != null) {
			if (fragmentTransaction == null) {
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			}
			if (f.isDetached()) {
				fragmentTransaction.attach(f);
			} else if (!f.isAdded()) {
				fragmentTransaction.add(layout, f, tag);
			} else {
				// Nothing to do
			}
		}
	}

	/**
	 * detach fragment
	 * 
	 * @param f
	 */
	private void detachFragment(Fragment f) {
		if (f != null && !f.isDetached()) {
			if (fragmentTransaction == null) {
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			}
			fragmentTransaction.detach(f);
		}
	}

	/**
	 * 通过标签获取fragment
	 * 
	 * @param tag
	 * @return
	 */
	private Fragment getFragmentByTag(String tag) {
		Fragment f = fragmentManager.findFragmentByTag(tag);
		if (f == null) {
			f = BaseFragment.getInstance(tag);
		}
		return f;
	}

	/**
	 * 注册信鸽推送服务
	 */
	public void registXgPush() {
		final User user = Account.getUserInfo();
		if (user == null) {
			return;
		}
		XGPushConfig.enableDebug(this, true);
		XGPushManager.registerPush(getApplicationContext(), user.getUserId()
				+ "", new XGIOperateCallback() {

			@Override
			public void onSuccess(Object data, int flag) {
				// TODO Auto-generated method stub
				Log.i(TAG,"注册成功"+ XGPushConfig.getAccessId(getApplicationContext()));
				// 向服务上传token
				RequestParams params = new RequestParams();
				params.put("user_id", user.getUserId());
				params.put("xg_token", data);
				ApiManager.getInstance().post(HomeActivity.this,
						C.API.XG_UPLOAD_TOKEN, params, new HttpCallBack() {

							@Override
							public void onSuccess(Object res) {
								// TODO Auto-generated method stub
								LogUtil.i("信鸽token上传成功");
							}

							@Override
							public void onFailure(Object res) {
								// TODO Auto-generated method stub
								LogUtil.i(res.toString());
							}
						}, "");
			}

			@Override
			public void onFail(Object data, int errCode, String msg) {
				// TODO Auto-generated method stub
				Log.i(TAG, "注册失败" + msg);
			}
		});
	}

	/**
	 * 数据库操作的回调
	 */
	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub
		int taskId = res.what;
		MomentFragment momentFragment;
		switch (taskId) {
		case C.DbTaskId.MOMENT_GET_DIRTY_MOMENT:
			momentFragment = (MomentFragment) getFragmentByTag(C.menu.FRAGMENT_MOMENT_TAG);
			momentFragment.dataFinish(C.DbTaskId.MOMENT_GET_DIRTY_MOMENT,
					res.obj);
			break;
		case C.DbTaskId.GET_MOMENT_LIST:
			momentFragment = (MomentFragment) getFragmentByTag(C.menu.FRAGMENT_MOMENT_TAG);
			momentFragment.dataFinish(C.DbTaskId.GET_MOMENT_LIST,res.obj);
			break;
		case C.DbTaskId.MOMENT_LIST_SAVE:
			momentFragment = (MomentFragment) getFragmentByTag(C.menu.FRAGMENT_MOMENT_TAG);
			momentFragment.dataFinish(C.DbTaskId.MOMENT_LIST_SAVE,res.obj);
			break;
		case C.DbTaskId.EDITOR_MOMENT_SAVE:
			momentFragment = (MomentFragment) getFragmentByTag(C.menu.FRAGMENT_MOMENT_TAG);
			momentFragment.dataFinish(C.DbTaskId.EDITOR_MOMENT_SAVE,res.obj);
			break;
		default:
			break;
		}
	}

	/**
	 * 保存数据到数据库
	 * @param taskId
	 * @param entityType
	 * @param datas
	 */
	public void saveDbData(int taskId, Class<?> entityType, List<?> datas) {
		// 可以根据不同的任务 设置不同的tasktype
		saveDbDataAsync(taskId, DbTaskType.saveOrUpdateAll, entityType, datas,
				dbTaskHandler);
	}

	/**
	 * 从数据库获取数据
	 * 
	 * @param taskId
	 * @param entityType
	 */
	public void getDbData(int taskId, Class<?> entityType) {
		// 可以根据不同的任务 设置不同的tasktype
		getDbDataAsync(taskId, DbTaskType.findByPage, entityType, dbTaskHandler);
	}

	/**
	 * 
	 * @param taskId
	 * @param taskType
	 * @param entityType
	 */
	public void getDbData(int taskId, DbTaskType taskType, Class<?> entityType,
			WhereBuilder whereBuilder) {
		// 可以根据不同的任务 设置不同的tasktype
		getDbDataAsync(taskId, taskType, entityType, whereBuilder,
				dbTaskHandler);
	}

	/**
	 * 
	 * @param taskId
	 * @param taskType
	 * @param selector
	 */
	public void getDbData(int taskId, DbTaskType taskType, Selector selector) {
		getDbDataAsync(taskId, taskType, selector, dbTaskHandler);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "-->onResume");
		//跳回到主页面刷新moment页面的标志
		boolean isSwitch2Moment =  SharedPreferencesUtil.getInstance().getBoolean(C.SPKey.SWITCH_TO_MOMENT_FG, false);
		if(isSwitch2Moment == true){
			customMenu.clickMenuItem(C.menu.FRAGMENT_MOMENT_MENU_ID);
			//需要刷新一下选中的页面
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					((BaseFragment)getFragmentByTag(currentFgTag)).refreshFragmentContent();
				}
			}, 300);
			SharedPreferencesUtil.getInstance().setBoolean(C.SPKey.SWITCH_TO_MOMENT_FG, false);
		}
		XGPushManager.onActivityStarted(this);
		XgMessageReceiver.addCustomMessageListener(this);
	}
	
	 
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		XGPushManager.onActivityStoped(this);
		XgMessageReceiver.removeCustomMessageListener(this);
	}

	@Override
	public void OnReceive(XgMessage msg) {
		// TODO Auto-generated method stub
		int messageType = msg.getMessageType();
		ToastUtil.show(this, msg.getContent().getMessage());
		switch(messageType){
		case 1://我的消息
			//显示小红点
			showRedDot(meRedDotView);
			break;
		case 2://动态
			showRedDot(dynamicRedDotView);
		}
	}
	/**
	 * 
	 */
	public void returnLogin(){
		//可以做一些退出登录前进行的操作，比如清除缓存
		actionStart(AccountActivity.class);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferencesUtil.getInstance().setBoolean(C.SPKey.SWITCH_TO_MOMENT_FG, false);
	}
	/**
	 * 初始化小红点
	 */
	private View initRedDotView(int menuId){
		//显示小红点
		MenuImageText notifyMenu = customMenu.getMenuIem(menuId);
		View redDotView = getLayoutInflater().inflate(R.layout.reddot, null);
		ViewGroup parentContainer = (ViewGroup) notifyMenu.getParent();
		int groupIndex = parentContainer.indexOfChild(notifyMenu);
		parentContainer.removeView(notifyMenu);

		FrameLayout badgeContainer = new FrameLayout(this);
		ViewGroup.LayoutParams parentlayoutParams = notifyMenu.getLayoutParams();

		badgeContainer.setLayoutParams(parentlayoutParams);
		notifyMenu.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		
		badgeContainer.addView(notifyMenu);
		parentContainer.addView(badgeContainer, groupIndex,
				parentlayoutParams);
		

		badgeContainer.addView(redDotView);
		FrameLayout.LayoutParams params = (LayoutParams) redDotView.getLayoutParams();
		params.leftMargin = DensityUtil.dip2px(this, 53);
		params.topMargin = DensityUtil.dip2px(this,5);
		params.rightMargin = DensityUtil.dip2px(this, 10);
		params.bottomMargin = DensityUtil.dip2px(this, 10);
		redDotView.setLayoutParams(params);
		return redDotView;
	}
	/**
	 * 隐藏小红点
	 * @param redView
	 */
	private void hideRedDot(View redView){
		if(redView!=null && redView.getVisibility() == View.VISIBLE){
			redView.setVisibility(View.GONE);
		}
	}
	/**
	 * 显示小红点
	 * @param redView
	 */
	private void showRedDot(View redView){
		if(redView!=null){
			redView.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 通过菜单id隐藏红点
	 * @param menuId
	 */
	public void hideRedDot(int menuId){
		switch (menuId) {
		case C.menu.FRAGMENT_ME_MENU_ID:
			if(meRedDotView!=null && meRedDotView.getVisibility() == View.VISIBLE){
				meRedDotView.setVisibility(View.GONE);
			}
			break;
		case C.menu.FRAGMENT_DYNAMIC_MENU_ID:
			if(dynamicRedDotView != null && dynamicRedDotView.getVisibility() == View.VISIBLE){
				dynamicRedDotView.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}
}
