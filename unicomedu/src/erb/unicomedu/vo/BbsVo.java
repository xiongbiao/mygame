package erb.unicomedu.vo;

import java.io.Serializable;

public class BbsVo  implements Serializable  {
	private String info;
	private String school;
	private String topicid;
	private int asknumber;
	private int replynumber;
	private String topic;
	private String sid;
	private String parentid;
	private String intentionid;
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getTopicid() {
		return topicid;
	}
	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}
	public int getAsknumber() {
		return asknumber;
	}
	public void setAsknumber(int asknumber) {
		this.asknumber = asknumber;
	}
	public int getReplynumber() {
		return replynumber;
	}
	public void setReplynumber(int replynumber) {
		this.replynumber = replynumber;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getIntentionid() {
		return intentionid;
	}
	public void setIntentionid(String intentionid) {
		this.intentionid = intentionid;
	}
	@Override
	public String toString() {
		return "BbsVo [info=" + info + ", school=" + school + ", topicid="
				+ topicid + ", asknumber=" + asknumber + ", replynumber="
				+ replynumber + ", topic=" + topic + ", sid=" + sid
				+ ", parentid=" + parentid + ", intentionid=" + intentionid
				+ "]";
	}
	
	
}
