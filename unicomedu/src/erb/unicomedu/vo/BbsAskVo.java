package erb.unicomedu.vo;

import java.io.Serializable;

public class BbsAskVo  implements Serializable  {
	private String context;
	private String member;
	private String school;
	private int askid;
	private String sid;
	private String topicid;
	private String topic;
	private String memberid;
	private String title;
	private String pubtime;
	private int readnumber;
	private int replynumber;
	private int isrecommend;
	private int isimgcontext;
	private  int isarchived ;
	
	
	
	
	
	public int getIsarchived() {
		return isarchived;
	}
	public void setIsarchived(int isarchived) {
		this.isarchived = isarchived;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public int getAskid() {
		return askid;
	}
	public void setAskid(int askid) {
		this.askid = askid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getTopicid() {
		return topicid;
	}
	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
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
	public int getReadnumber() {
		return readnumber;
	}
	public void setReadnumber(int readnumber) {
		this.readnumber = readnumber;
	}
	public int getReplynumber() {
		return replynumber;
	}
	public void setReplynumber(int replynumber) {
		this.replynumber = replynumber;
	}
	public int getIsrecommend() {
		return isrecommend;
	}
	public void setIsrecommend(int isrecommend) {
		this.isrecommend = isrecommend;
	}
	public int getIsimgcontext() {
		return isimgcontext;
	}
	public void setIsimgcontext(int isimgcontext) {
		this.isimgcontext = isimgcontext;
	}
	 
	
}
