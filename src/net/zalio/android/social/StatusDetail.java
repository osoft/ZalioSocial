package net.zalio.android.social;

import java.util.Date;

import net.zalio.android.social.model.Status;
import net.zalio.android.social.utils.ImageBufferManager;
import net.zalio.android.social.utils.ImageBufferManager.OnImageDownloadedListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusDetail extends Activity {

	private Status mStatus;
	private TextView mTvScreenName;
	private TextView mTvText;
	private TextView mTvDate;
	private ImageView mIvImage;
	
	@Override
	protected void onCreate(Bundle savedInstance ){
		super.onCreate(savedInstance);
		Intent i = getIntent();
		mStatus = (Status)i.getSerializableExtra("status");
		if(mStatus == null){
			finish();
			return;
		}
		
		setContentView(R.layout.activity_detail);
		mTvScreenName = (TextView)findViewById(R.id.tvScreenName);
		mTvText = (TextView)findViewById(R.id.tvText);
		mTvDate = (TextView)findViewById(R.id.tvDate);
		
		mTvScreenName.setText(mStatus.getUserScreenName());
		mTvText.setText(mStatus.getText());
		mTvText.setMovementMethod(LinkMovementMethod.getInstance());
		
		Date d = mStatus.getDate();
		String hh = String.format("%02d", d.getHours());
		String mm = String.format("%02d", d.getMinutes());
		String ss = String.format("%02d", d.getSeconds());
		mTvDate.setText(hh + ":" + mm + ":" + ss);
		
		mIvImage = (ImageView)findViewById(R.id.ivImage);
		final ImageBufferManager ibm = ImageBufferManager.getInstance();
		ibm.setImageDownlaodedListener(new OnImageDownloadedListener(){

			@Override
			public void onImageDownloaded(String socialName, String statusId) {
				if(socialName.equals(mStatus.getSocialName())
						&& statusId.equals(String.valueOf(mStatus.getIndex()))){
					Drawable daThumb = ibm.loadImage(mStatus.getSocialName(), 
							String.valueOf(mStatus.getIndex()), 
							mStatus.getImageUrl());
					if(daThumb != null){
						mIvImage.setImageDrawable(daThumb);
					}else{
					}
				}
				
			}
			
		});
		Drawable daThumb = ibm.loadImage(mStatus.getSocialName(), 
				String.valueOf(mStatus.getIndex()), 
				mStatus.getImageUrl());
		if(daThumb != null){
			//MyLog.i(TAG, "Image is buffered, display!");
			mIvImage.setImageDrawable(daThumb);
		}else{
			//MyLog.i(TAG, "Image need reload, waiting!");
			//holder.ivThumb.setImageResource(R.drawable.ic_launcher);
		}
	}
}
