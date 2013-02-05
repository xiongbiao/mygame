package erb.unicomedu.vo;

import java.io.Serializable;

public class MsgVo implements Serializable {
 
	private String type;
	private String sid;
	private String title;
	private String pubtime;
	private String typeid;
	private String iswav;
	private String isshock;
	private String msgid;
	private String summary;
	private String apiKey;
	private String linktype;
	
	private String linkinfo; 
	
	
	public String getLinkinfo() {
		return linkinfo;
	}
	public void setLinkinfo(String linkinfo) {
		this.linkinfo = linkinfo;
	}
	public String getLinktype() {
		return linktype;
	}
	public void setLinktype(String linktype) {
		this.linktype = linktype;
	}
	 
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getIswav() {
		return iswav;
	}
	public void setIswav(String iswav) {
		this.iswav = iswav;
	}
	public String getIsshock() {
		return isshock;
	}
	public void setIsshock(String isshock) {
		this.isshock = isshock;
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	 
}
