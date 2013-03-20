package net.zalio.android.social.model;

public class Retweet extends Status {

	private static final long serialVersionUID = 2952136781333947468L;
	private long originalStatusId;
	
	public Retweet(){
		
	}
	
	public long getOriginalStatusId() {
		return originalStatusId;
	}
	protected void setOriginalStatusId(long originalStatusId) {
		this.originalStatusId = originalStatusId;
	}
	
	
}
