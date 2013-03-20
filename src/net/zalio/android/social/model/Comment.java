package net.zalio.android.social.model;

public class Comment {
	private long statusId;
	private long commentId;
	private String text;
	
	public Comment(){
		
	}

	public long getStatusId() {
		return statusId;
	}

	protected void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	public long getCommentId() {
		return commentId;
	}

	protected void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public String getText() {
		return text;
	}

	protected void setText(String text) {
		this.text = text;
	}

}
