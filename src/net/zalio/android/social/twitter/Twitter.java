package net.zalio.android.social.twitter;

import java.util.Date;
import java.util.HashMap;

import android.content.Context;

import net.zalio.android.social.model.SocialBase;
import net.zalio.android.social.model.SocialConstants;
import net.zalio.android.social.model.Status;

public class Twitter extends SocialBase {
	private final static Twitter INSTANCE = new Twitter();
	private HashMap<Long, Status> mMsgList = new HashMap<Long, Status>();
	private Thread mRefreshThread = new Thread(){
		@Override
		public void run(){
			while(true){
				try {
					Thread.sleep(1000);
					if(!runningFlag){
						continue;
					}
					Status s = generateStatus();
					mMsgList.put(s.getIndex(), s);
					if(mStatusListener != null){
						mStatusListener.onNewStatus(s);
					}
				} catch (InterruptedException e) {
					
				}
				
			}
		}
	};
	private boolean runningFlag = false;
	private Twitter() {
		super("Twitter", SocialConstants.ID_TWITTER);
	}
	
	public static Twitter getInstance(){
		return INSTANCE;
	}

	@Override
	public void startUpdating() {
		runningFlag = true;
		if(!mRefreshThread.isAlive() && !mRefreshThread.isDaemon()){
			mRefreshThread.start();
		}
	}
	
	@Override
	public void stopUpdating() {
		if(mRefreshThread.isAlive()){
			runningFlag = false;
		}
	}

	@Override
	public Status getStatus(long index) {
		return mMsgList.get(index);
	}
	
	private Status generateStatus(){
		Date now = new Date();
		Status s = acquireStatus("TWEET" + (mMsgList.size() + 1), now, mMsgList.size());
		
		return s;
	}

	@Override
	public void startAuth(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postStatus(Status s) {
		// TODO Auto-generated method stub
		
	}


}
