package erb.unicomedu.vo;

import java.io.Serializable;
import java.util.List;

public class BbsCheckVo  implements Serializable  {
	private String context;
	private String replyto;
	private int askid;
	private int replyid;
	private String replytolist;
	private String title;
	private String pubtime;
	private String asktitle;
	private String memberid;
	private String member;
	private int answerid;
	private String answer;
	private String floor;//楼层 
	 
	 
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	private List<BbsCheckVo> bbsCheckVoList = null;
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getReplyto() {
		return replyto;
	}
	public void setReplyto(String replyto) {
		this.replyto = replyto;
	}
	public int getAskid() {
		return askid;
	}
	public void setAskid(int askid) {
		this.askid = askid;
	}
	public int getReplyid() {
		return replyid;
	}
	public void setReplyid(int replyid) {
		this.replyid = replyid;
	}
	public String getReplytolist() {
		return replytolist;
	}
	public void setReplytolist(String replytolist) {
		this.replytolist = replytolist;
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
	public String getAsktitle() {
		return asktitle;
	}
	public void setAsktitle(String asktitle) {
		this.asktitle = asktitle;
	}
	public int getAnswerid() {
		return answerid;
	}
	public void setAnswerid(int answerid) {
		this.answerid = answerid;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public List<BbsCheckVo> getBbsCheckVoList() {
		return bbsCheckVoList;
	}
	public void setBbsCheckVoList(List<BbsCheckVo> bbsCheckVoList) {
		this.bbsCheckVoList = bbsCheckVoList;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
}
