package com.ktl.moment.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ktl.moment.common.constant.C;
import com.verificationcodelib.api.VerificationCode;
import com.verificationcodelib.listener.UcsReason;
import com.verificationcodelib.listener.VerificationCodeListener;

/**
 * 云之讯智能验证
 * @author HUST_LH
 *
 */
public class VerificationUtil {
	
	private Activity activity;
	public ActionStartListener actionStartListener;
	
	public VerificationUtil(Activity activity){
		this.activity = activity;
	}

	public void requestVerificationCode(final String phone){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				String sign = "eee5c1086b5614ebd3c079323febd957";
				StringBuffer sb = new StringBuffer();
				sb.append(C.YunZhiXun.ACCOUNT_SID);
				sb.append(C.YunZhiXun.APP_ID);
				sb.append(phone);
				sb.append(C.YunZhiXun.MD5KEY);
				String sign = EncryptUtil.md5(sb.toString());
				Log.i("sign", sign);
				getVerificationCode(phone, sign);
			}
		}).start();
	}
	
	private void getVerificationCode(String phone, String sign){
		VerificationCode.getVerificationCode(activity, sign, C.YunZhiXun.ACCOUNT_SID, C.YunZhiXun.APP_ID, 
				C.YunZhiXun.APP_NAME, C.YunZhiXun.VERIFY_VALID_TIME, C.YunZhiXun.BUSSINESS_TYPE, phone, 
				new VerificationCodeListener() {
					
					@Override
					public void onVerificationCode(int arg0, UcsReason arg1) {
						// TODO Auto-generated method stub
						Log.i("verify", "arg0="+arg0+",arg1="+arg1.getReason());
						if(arg1.getReason() == 300250){
							ToastUtil.show(activity, "验证码已通过短信发送至您手机！");
						}else{
							switchToToast(arg1);
						}
					}
				});
	}
	
	public void startVerificationCode(String phone, String verifyCode){
		VerificationCode.doVerificationCode(activity, phone, verifyCode, C.YunZhiXun.ACCOUNT_SID, C.YunZhiXun.APP_ID, 
				new VerificationCodeListener() {
					
					@Override
					public void onVerificationCode(int arg0, UcsReason arg1) {
						// TODO Auto-generated method stub
						switchToToast(arg1);
						if(arg1.getReason() == 300252){
							actionStartListener.action();
						}
					}
				});
	}
	
	public interface ActionStartListener{
		public void action();
	}
	
	public void setActionStartListener(ActionStartListener actionStartListener){
		this.actionStartListener = actionStartListener;
	}
	
	/**
	 * 第三方Http请求反馈提示
	 */
	private Handler mRequestHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			System.out.println("Message:"+msg.what);
			switch(msg.what){
				case 0:
					ToastUtil.show(activity, "验证成功");
					break;
				case 1:
					ToastUtil.show(activity, "开发者账号无效");
					break;
				case 2:
					ToastUtil.show(activity, "验证码错误");
					break;
				case 3:
					ToastUtil.show(activity, "验证码过期");
					break;
				case 4:
					ToastUtil.show(activity, "30秒内重复请求");
					break;
				case 5:
					ToastUtil.show(activity, "签名错误");
					break;
				case 6:
					ToastUtil.show(activity, "手机号码无效");
					break;
				case 7:
					ToastUtil.show(activity, "验证码参数错误");
					break;
				case 8:
					ToastUtil.show(activity, "获取验证码失败");
					break;
				case 9:
					ToastUtil.show(activity, "短信模板有误，需要检查是否创建智能验证短信模板，模板审核、参数");
					break;
				case 10:
					ToastUtil.show(activity, "应用状态有误，需要检查应用是否审核通过、是否上线");
					break;
				case 99:
					ToastUtil.show(activity, "请求失败");
					break;
			}
		}
	};
	
	private void switchToToast(UcsReason arg1){
		switch (arg1.getReason()) {
		case 300250:
			mRequestHandler.sendEmptyMessage(0);
			break;
		case 300251:
			mRequestHandler.sendEmptyMessage(1);
			break;
		case 300252:
			mRequestHandler.sendEmptyMessage(2);
			break;
		case 300253:
			mRequestHandler.sendEmptyMessage(3);
			break;
		case 300254:
			mRequestHandler.sendEmptyMessage(4);
			break;
		case 300255:
			mRequestHandler.sendEmptyMessage(5);
			break;
		case 300256:
			mRequestHandler.sendEmptyMessage(6);
			break;
		case 300257:
			mRequestHandler.sendEmptyMessage(7);
			break;
		case 300258:
			mRequestHandler.sendEmptyMessage(8);
			break;
		case 300259:
			mRequestHandler.sendEmptyMessage(9);
			break;
		case 300260:
			mRequestHandler.sendEmptyMessage(10);
			break;
		default:
			mRequestHandler.sendEmptyMessage(99);
			break;
		}
	}
}
