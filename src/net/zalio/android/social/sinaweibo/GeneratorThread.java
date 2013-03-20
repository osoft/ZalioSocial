package net.zalio.android.social.sinaweibo;

import java.util.Date;

import net.zalio.android.social.model.SocialBase.OnNewStatusListener;
import net.zalio.android.social.model.Status;
import net.zalio.android.social.model.Status.Builder;
import net.zalio.android.social.utils.LinkFinder;
import net.zalio.android.social.utils.MyLog;

public class GeneratorThread extends Thread {
	private static final String TAG = "GeneratorThread";
	SinaWeiboObject mWeiboObj;
	private OnNewStatusListener mListener;
	
	GeneratorThread(SinaWeiboObject obj, OnNewStatusListener listener){
		mWeiboObj = obj;
		mListener = listener;
	}

	@Override
	public void run(){
		if(mWeiboObj == null) {
			return;
		}
		int size = mWeiboObj.statuses.size();
		for(int i = size - 1; i >= 0; i--){
			StatusesInfo si = mWeiboObj.statuses.get(i);
			Status.Builder b = new Builder();
			b.setText(si.getText());
			Date d = new Date(si.getCreated_at());
			b.setDate(d);
			b.setId(Long.parseLong(si.getId()));
			b.setScreenName(si.user.screen_name);
			b.setSocialName(SinaWeibo.getInstance().getName());
			b.setImageUrl(si.original_pic);
			b.setThumbUrl(si.thumbnail_pic);
			b.setLinks(LinkFinder.getLinks(si.getText()));
			b.setAvatarUrl(si.user.avatar_large);
			
			if(mListener != null){
				//MyLog.i(TAG, "Calling onNewStatus(): " + b.getStatus().getText());
				
				mListener.onNewStatus(b.getStatus());
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		
//		for(StatusesInfo i:mWeiboObj.statuses){
//			MyLog.i(TAG, "Generating Status!");
//			Status.Builder b = new Builder();
//			b.setText(i.getText());
//			Date d = new Date(i.getCreated_at());
//			b.setDate(d);
//			b.setId(Long.parseLong(i.getId()));
//			b.setScreenName(i.user.screen_name);
//			if(mListener != null){
//				MyLog.i(TAG, "Calling onNewStatus(): " + b.getStatus().getText());
//				
//				mListener.onNewStatus(b.getStatus());
//			}
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
}
