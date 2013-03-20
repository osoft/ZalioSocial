package net.zalio.android.social.sinaweibo;

import android.content.Context;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.ActivityInvokeAPI;
import com.weibo.sdk.android.api.CommonAPI;
import com.weibo.sdk.android.api.SearchAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;

import net.zalio.android.social.model.SocialBase;
import net.zalio.android.social.model.SocialConstants;
import net.zalio.android.social.model.Status;
import net.zalio.android.social.model.SocialBase.OnNewStatusListener;
import net.zalio.android.social.utils.MyLog;

public class SinaWeibo extends SocialBase {
	private final static SinaWeibo INSTANCE = new SinaWeibo();
	private static final String TAG = "SinaWeibo";
	protected static Oauth2AccessToken accessToken;
	protected final String APPKEY_SinaWeibo = "4231003195";
	public final String REDIRECT_URL = "http://www.sina.com";
	private Weibo mWeibo;
	private SinaWeiboListener mWeiboListener;
	private AccountAPI accountApi;
	private StatusesAPI statusApi;
	private CommonAPI commonApi;
	private SearchAPI searchApi;
	private Context mContext;
	
	public static SinaWeibo getInstance(){
		return INSTANCE;
	}
	
	private SinaWeibo() {
		super("SinaWeibo", SocialConstants.ID_SINAWEIBO);
		mWeibo = Weibo.getInstance(APPKEY_SinaWeibo, REDIRECT_URL);
		
		mWeiboListener = new SinaWeiboListener();
	}
	
	protected Context getContext(){
		return mContext;
	}
	
	protected OnNewStatusListener getOnNewStatusListener(){
		return mStatusListener;
	}

	@Override
	public void startUpdating() {
		statusApi.homeTimeline(
				SinaWeiboDataKeeper.readLastStatusId(mContext), 					// since_id
				0, 					// max_id
				20, 				// count per page
				1,	 				// page
				false, 				// only show tweets from this app
				FEATURE.ALL, 		// filter
				false, 				// trim user info
				mWeiboListener);
	}

	@Override
	public void stopUpdating() {
		// TODO Auto-generated method stub

	}

	@Override
	public Status getStatus(long index) {
		// TODO Auto-generated method stub
		MyLog.i(TAG, "getStatus");
		return null;
	}

	@Override
	public void startAuth(Context context) {
		mContext = context;
		Oauth2AccessToken token = SinaWeiboDataKeeper.readAccessToken(context);
		if(token.isSessionValid()){
			MyLog.i(TAG, "Using saved token!");
			accessToken = token;
			refreshAPIs(token);
			startUpdating();
		}else{
			mWeibo.authorize(context, mWeiboListener);
		}
		
	}

	@Override
	public void postStatus(Status s) {
		statusApi.update(s.getText(), "", "", mWeiboListener);
	}
	
	protected void refreshAPIs(Oauth2AccessToken accessToken){
		SinaWeibo.accessToken = accessToken;
		accountApi = new AccountAPI(SinaWeibo.accessToken);
		//activityApi = new ActivityInvokeAPI();
		statusApi = new StatusesAPI(SinaWeibo.accessToken);
		commonApi = new CommonAPI(SinaWeibo.accessToken);
		searchApi = new SearchAPI(accessToken);
	}

}
