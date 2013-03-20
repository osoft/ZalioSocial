package net.zalio.android.social.model;

import java.util.Date;

import android.content.Context;


public abstract class SocialBase {
	public interface OnNewStatusListener {
		abstract void onNewStatus(Status s);
	}

	protected OnNewStatusListener mStatusListener = null;
	
	private String name;
	private int typeId;
	
	public SocialBase(String name, int typeId){
		this.name = name;
		this.typeId = typeId;
	}
	
	public String getName(){
		return name;
	}
	
	public int getTypeId(){
		return typeId;
	}
	
	public void setOnNewStatusListener(OnNewStatusListener listener){
		mStatusListener = listener;
	}

	public Status acquireStatus(String text, Date date, long index){
		Status s = new Status();
		s.setText(text);
		s.setDate(date);
		s.setIndex(index);
		return s;
	}
	
	
	public abstract void startUpdating();
	public abstract void stopUpdating();
	public abstract Status getStatus(long index);
	public abstract void startAuth(Context context);
	public abstract void postStatus(Status s);
	
}
