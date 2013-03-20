package net.zalio.android.social.sinaweibo;

public class StatusesInfo {
	class UserInfo{
		long id;
		String screen_name;
		String avatar_large;
		int followers_count;
		int friends_count;
		int statuses_count;
		
	}
	public String created_at;
	public String id; // change to int
	public String idstr;
	public String text;
	public boolean favorited;
	public boolean truncated;
	public String in_reply_to_status_id;
	public String in_reply_to_user_id;
	public String in_reply_to_screen_name;
	public String thumbnail_pic;
	public String original_pic;
	//public List<GeoInfo> geo;
	public int uid;
	public String mid; // change to int
	public int reposts_count;
	public int comments_count;
	public int melvel;
	//public VisibleInfo visible;
	//public UserInfo user;
	//public RetweetedStatus retweeted_status;
	public String source;
//	public List<GeoInfo> getGeo() {
//		return geo;
//	}
//	public void setGeo(List<GeoInfo> geo) {
//		this.geo = geo;
//	}
	public UserInfo user;
	public String getIdstr() {
		return idstr;
	}
	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
	//public UserInfo getUser() {return user;}
	//public void setUser(UserInfo user) {this.user = user;}
	public String getSource() {return source;}
	public void setSource(String source) {this.source = source;}
	public String getCreated_at() {return created_at;}
	public void setCreated_at(String created_at) {this.created_at = created_at;}
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
	public boolean isFavorited() {return favorited;}
	public void setFavorited(boolean favorited) {this.favorited = favorited;}
	public boolean isTruncated() {return truncated;}
	public void setTruncated(boolean truncated) {this.truncated = truncated;}
	public String getIn_reply_to_status_id() {return in_reply_to_status_id;}
	public void setIn_reply_to_status_id(String in_reply_to_status_id) {this.in_reply_to_status_id = in_reply_to_status_id;}
	public String getIn_reply_to_user_id() {return in_reply_to_user_id;}
	public void setIn_reply_to_user_id(String in_reply_to_user_id) {this.in_reply_to_user_id = in_reply_to_user_id;}
	public String getIn_reply_to_screen_name() {return in_reply_to_screen_name;}
	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {this.in_reply_to_screen_name = in_reply_to_screen_name;}
	public String getMid() {return mid;}public void setMid(String mid) {this.mid = mid;}
	public int getReposts_count() {return reposts_count;}
	public void setReposts_count(int reposts_count) {this.reposts_count = reposts_count;}
	public int getComments_count() {return comments_count;}
	public void setComments_count(int comments_count) {this.comments_count = comments_count;}
	public int getMelvel() {return melvel;}
	public void setMelvel(int melvel) {this.melvel = melvel;}
	//public VisibleInfo getVisible() {return visible;}
	//public void setVisible(VisibleInfo visible) {this.visible = visible;}
	//public RetweetedStatus getRetweeted_status() {return retweeted_status;}
	//public void setRetweeted_status(RetweetedStatus retweeted_status) {this.retweeted_status = retweeted_status;}}
















}