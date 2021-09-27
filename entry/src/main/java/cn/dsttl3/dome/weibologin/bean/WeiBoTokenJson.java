package cn.dsttl3.dome.weibologin.bean;

public class WeiBoTokenJson {
	String access_token;
	String remind_in;
	String expires_in;
	String uid;
	String isRealName;
	/**
	 * @return token
	 */
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getRemind_in() {
		return remind_in;
	}
	public void setRemind_in(String remind_in) {
		this.remind_in = remind_in;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getIsRealName() {
		return isRealName;
	}
	public void setIsRealName(String isRealName) {
		this.isRealName = isRealName;
	}
}
