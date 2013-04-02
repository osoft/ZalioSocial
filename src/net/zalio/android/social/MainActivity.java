package net.zalio.android.social;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zalio.android.social.model.Status;
import net.zalio.android.social.model.SocialBase.OnNewStatusListener;
import net.zalio.android.social.sinaweibo.SinaWeibo;
import net.zalio.android.social.twitter.Twitter;
import net.zalio.android.social.utils.ImageBufferManager;
import net.zalio.android.social.utils.ImageBufferManager.OnThumbDownloadedListener;
import net.zalio.android.social.utils.MyLog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	ToggleButton mTglBtn;
	ListView lvList;
	ArrayList<Status> mStatusList = new ArrayList<Status>();

	private class ViewHolder{
		TextView tvText;
		TextView tvScreenName;
		TextView tvDate;
		ImageView ivThumb;
		int position;
	}
	private class StatusArrayAdapter extends ArrayAdapter<Status>{
		private class MyClickSpan extends ClickableSpan{

			@Override
			public void onClick(View widget) {
				TextView tv = (TextView)widget;
				//this.
			}
			
		}
		

		public StatusArrayAdapter(Context context, int textViewResourceId,
				List<Status> objects) {
			super(context, R.layout.item_layout, objects);
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent){
			View v;
			ViewHolder holder;
			if(convertView == null){
				v = View.inflate(MainActivity.this, R.layout.item_layout, null);
				holder = new ViewHolder();
				holder.tvText = (TextView)v.findViewById(R.id.tvText);
				//holder.tvText.setMovementMethod(LinkMovementMethod.getInstance());
				holder.tvScreenName = (TextView)v.findViewById(R.id.tvScreenName);
				holder.tvDate = (TextView)v.findViewById(R.id.tvDate);
				holder.ivThumb = (ImageView)v.findViewById(R.id.ivThumb);
				v.setTag(holder);
			}else{
				v = convertView;
				holder = (ViewHolder)v.getTag();
			}
			
			//Status s = getItem(position);
			Status s = mStatusList.get(position);
			if(s == null){
				return v;
			}

			holder.tvText.setText(s.getText());
			holder.tvScreenName.setText(s.getUserScreenName());
//			Date d = s.getDate();
//			String hh = String.format("%02d", d.getHours());
//			String mm = String.format("%02d", d.getMinutes());
//			String ss = String.format("%02d", d.getSeconds());
//			holder.tvDate.setText(hh + ":" + mm + ":" + ss);
			
			holder.position = position;
			// Set thumb image
//			new AsyncTask<Object, Void, Drawable>(){
//				private ViewHolder holder;
//				private net.zalio.android.social.model.Status s;
//
//				@Override
//				protected Drawable doInBackground(Object... params) {
//					holder = (ViewHolder)params[0];
//					s = (net.zalio.android.social.model.Status)params[1];
//					ImageBufferManager ibm = ImageBufferManager.getInstance();
//					Drawable daThumb = ibm.loadImageThumb(s.getSocialName(), 
//							String.valueOf(s.getIndex()), 
//							s.getThumbUrl());
//					return daThumb;
//				}
//				
//				@Override
//				protected void onPostExecute(Drawable d){
//					super.onPostExecute(d);
////					if(holder.position == position){
////						if(d != null){
////							MyLog.i(TAG, "Image is buffered, display!");
////							holder.ivThumb.setImageDrawable(d);
////						}else{
////							MyLog.i(TAG, "Image need reload, waiting!");
////							holder.ivThumb.setImageResource(R.drawable.ic_launcher);
////						}
////					}
//				}
//			}.execute(holder, s);
			
			if(s.getImageUrl() != null && !s.getImageUrl().equals("")){
				ImageBufferManager ibm = ImageBufferManager.getInstance();
				Drawable daThumb = ibm.loadImageThumb(s.getSocialName(), 
						String.valueOf(s.getIndex()), 
						s.getThumbUrl());
				if(daThumb != null){
					//MyLog.i(TAG, "Image is buffered, display!");
					holder.ivThumb.setImageDrawable(daThumb);
				}else{
					//MyLog.i(TAG, "Image need reload, waiting!");
					//holder.ivThumb.setImageResource(R.drawable.ic_launcher);
				}
			}else{
				holder.ivThumb.setImageDrawable(null);
			}
			
			Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
			v.setAnimation(anim);
			return v;
		}

	}	
	
	private StatusArrayAdapter statusAdapter;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				//Status s = t.getStatus(msg.arg1);
				Status s = (Status) msg.obj;
				//MyLog.i(TAG, "Adding status to list");
				if(s.getIndex() == -1 && s.getText().equals("FINISH")){
					statusAdapter.notifyDataSetChanged();
					break;
				}
				mStatusList.add(0,s);
				int size = mStatusList.size();
				//for(int i = 0; i < size; i++){
				//	MyLog.i(TAG, mStatusList.get(i).getDate().toString());
				//}
				statusAdapter.notifyDataSetChanged();
				break;
			}
		}
	};
	final Twitter t = Twitter.getInstance();
	final SinaWeibo s = SinaWeibo.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		statusAdapter = new StatusArrayAdapter(getApplicationContext(), 0, mStatusList);
		lvList = (ListView)findViewById(R.id.listView1);
		lvList.setAdapter(statusAdapter);
		
		mTglBtn = (ToggleButton)findViewById(R.id.tglBtn);
		mTglBtn.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					//t.startUpdating();
					MyLog.i(TAG, "startAuth..");
					s.startAuth(MainActivity.this);
				}else{
					//t.stopUpdating();
				}
				
			}
			
		});
		
		s.setOnNewStatusListener(new OnNewStatusListener(){

			@Override
			public void onNewStatus(Status s) {
				//mStatusList.add(s);
				Message msg = new Message();
				msg.what = 1;
				msg.arg1 = (int) s.getIndex();
				msg.obj = s;
				mHandler.sendMessage(msg);
			}
			
		});
		
		ImageBufferManager.getInstance().setThumbDownloadedLIstener(new OnThumbDownloadedListener(){

			@Override
			public void onThumbDownloaded(String socialName, String statusId) {
				//statusAdapter.notifyDataSetChanged();
				MyLog.i(TAG, "onThumbDownloaded:" + statusId);
				int count = lvList.getChildCount();
				for(int i = 0; i < count; i++){
					View v = lvList.getChildAt(i);
					ViewHolder holder = (ViewHolder)v.getTag();
					if(holder == null){
						MyLog.e(TAG, "ViewHolder is null!");
						return;
					}
					Status s = (Status) lvList.getItemAtPosition(holder.position);
					if(String.valueOf(s.getIndex()).equals(statusId)){
						MyLog.i(TAG, "Found view to update thumb");
						ImageBufferManager ibm = ImageBufferManager.getInstance();
						Drawable thumb = ibm.loadImageThumb(s.getSocialName(), String.valueOf(s.getIndex()), s.getThumbUrl());
						if(thumb != null){
							holder.ivThumb.setImageDrawable(thumb);
						}
					}
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
