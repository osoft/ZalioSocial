package net.zalio.android.social.sinaweibo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SinaWeiboObject {
	public List<StatusesInfo> statuses;
	@SerializedName("hasvisible")
	public boolean hasvisible;
	@SerializedName("previous_cursor")
	public int previous_cursor;
	@SerializedName("next_cursor")
	public String next_cursor;
	@SerializedName("total_number")
	public int total_number;
}
