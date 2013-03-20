package net.zalio.android.social.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Status implements Serializable{
	private static final long serialVersionUID = 5091871699615896712L;
	private String text;
	private String userScreenName;
	private String userAvatarUrl;
	private String imageUrl;
	private String thumbUrl;
	private String videoUrl;
	private String socialName;
	private Date date;
	private long index;
	private ArrayList<String> links;
	private Status oriStatus;
	
	public static class Builder{
		private Status mStatusToBuild;
		public Builder(){
			mStatusToBuild = new Status();
			mStatusToBuild.setDate(new Date());
		}
		
		public Builder setText(String text){
			mStatusToBuild.setText(text);
			return this;
		}
		
		public Builder setDate(Date date){
			mStatusToBuild.setDate(date);
			return this;
		}
		
		public Builder setId(Long id){
			mStatusToBuild.setIndex(id);
			return this;
		}
		
		public Builder setScreenName(String screenName){
			mStatusToBuild.setUserScreenName(screenName);
			return this;
		}
		
		public Builder setImageUrl(String url){
			mStatusToBuild.setImageUrl(url);
			return this;
		}
		
		public Builder setVideoUrl(String url){
			mStatusToBuild.setVideoUrl(url);
			return this;
		}		
		
		public Builder setThumbUrl(String url){
			mStatusToBuild.setThumbUrl(url);
			return this;
		}
		
		public Builder setSocialName(String name){
			mStatusToBuild.setSocialName(name);
			return this;
		}
		
		public Builder setOriginalStatus(Status s){
			mStatusToBuild.setOriginalStatus(s);
			return this;
		}
		
		public Builder setAvatarUrl(String url){
			mStatusToBuild.setUserAvatarUrl(url);
			return this;
		}
		
		public Builder setLinks(ArrayList<String> links){
			mStatusToBuild.setLinks(links);
			return this;
		}
		
		public Status getStatus(){
			return mStatusToBuild;
		}
		
		
	}
	
	public Status(){
		
	}
	public String getText() {
		return text;
	}
	protected void setText(String text) {
		this.text = text;
	}
	public Status getOriginalStatus(){
		return oriStatus;
	}
	protected void setOriginalStatus(Status s){
		this.oriStatus = s;
	}
	public Date getDate() {
		return date;
	}
	protected void setDate(Date date) {
		this.date = date;
	}
	public long getIndex() {
		return index;
	}
	protected void setIndex(long index) {
		this.index = index;
	}
	public String getUserScreenName() {
		return userScreenName;
	}
	protected void setUserScreenName(String user_screen_name) {
		this.userScreenName = user_screen_name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	protected void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	protected void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getSocialName(){
		return socialName;
	}
	protected void setSocialName(String socialName) {
		this.socialName = socialName;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	protected void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public String getUserAvatarUrl() {
		return userAvatarUrl;
	}
	protected void setUserAvatarUrl(String userAvatarUrl) {
		this.userAvatarUrl = userAvatarUrl;
	}
	public ArrayList<String> getLinks() {
		return links;
	}
	protected void setLinks(ArrayList<String> links) {
		this.links = links;
	}
}
