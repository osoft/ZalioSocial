package net.zalio.android.social.sinaweibo;

import java.io.IOException;

import net.zalio.android.social.utils.MyLog;
import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class SinaWeiboListener implements WeiboAuthListener, RequestListener {
	private static final String TAG = "SinaWeiboListener";

	/************** Auth Listener ***************/
	@Override
	public void onCancel() {
		MyLog.w(TAG, "Canceled!");
	}

	@Override
	public void onComplete(Bundle values) {
		MyLog.w(TAG, "Complete!");
		String token = values.getString("access_token");
		String expires_in = values.getString("expires_in");
		SinaWeibo.accessToken = new Oauth2AccessToken(token, expires_in);
		SinaWeibo.getInstance().refreshAPIs(new Oauth2AccessToken(token, expires_in));
		SinaWeibo.getInstance().startUpdating();
		if(SinaWeibo.getInstance().getContext() != null){			
			MyLog.i(TAG, "Keeping access token");
			SinaWeiboDataKeeper.keepAccessToken(SinaWeibo.getInstance().getContext(), SinaWeibo.accessToken);
		}
	}

	@Override
	public void onError(WeiboDialogError arg0) {
		MyLog.w(TAG, "Error!");

	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		MyLog.w(TAG, "WeiboException!");

	}
	
	/************** Request Listener ***************/
	@Override
	public void onComplete(String s) {
		MyLog.i(TAG, "Request Complete: " + s);
		Gson g = new Gson();
		SinaWeiboObject weiboObj;
		try{
			weiboObj = g.fromJson(s, SinaWeiboObject.class);
		}catch(JsonSyntaxException e){
			MyLog.e(TAG, "Failed parsing msg");
			return;
		}
		if(weiboObj == null){
			MyLog.e(TAG, "No response");
			return;
		}
		MyLog.i(TAG, "Size: " + weiboObj.statuses.size());
		for(StatusesInfo i: weiboObj.statuses){
			MyLog.i(TAG, "-------------------------");
			MyLog.i(TAG, "Status Id: " + i.getId());
			MyLog.i(TAG, "Status Text: " + i.getText());
			MyLog.i(TAG, "From: " + i.user.screen_name);
			MyLog.i(TAG, "Time: " + i.created_at);
		}
		if(weiboObj.statuses.size() > 0){
			Context context = SinaWeibo.getInstance().getContext();
			SinaWeiboDataKeeper.saveLastStatusId(context, 
					Long.parseLong(weiboObj.statuses.get(0).idstr));
		}
		
		GeneratorThread gThread = new GeneratorThread(weiboObj, SinaWeibo.getInstance().getOnNewStatusListener());
		gThread.start();
	}

	@Override
	public void onError(WeiboException e) {
		MyLog.i(TAG, "Request Error: " + e.getMessage());
		
	}

	@Override
	public void onIOException(IOException e) {
		MyLog.i(TAG, "Request IOException: " + e.getMessage());
		
	}

}
