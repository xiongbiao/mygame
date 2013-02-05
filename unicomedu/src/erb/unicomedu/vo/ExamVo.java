package erb.unicomedu.vo;

import java.io.Serializable;

public class ExamVo implements Serializable {

	private String type;
	private String info;
	private String subject;
	private String school;
	private String pubtime;
	
	private String sid;
	private String imgurl;
	private String typeid;
	private String subjectid;
	private String integral;
	
	private String payment;
	private String teacherid;
	private String teacher;
	private String clknumber;
	private String popedom;
	
	private String examid;
	private String examname;
	
	private int needtime;
	
	private  int isbuy ;
	

	public int getNeedtime() {
		return needtime;
	}

	public void setNeedtime(int needtime) {
		this.needtime = needtime;
	}

	public int getIsbuy() {
		return isbuy;
	}

	public void setIsbuy(int isbuy) {
		this.isbuy = isbuy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getPubtime() {
		return pubtime;
	}

	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}


	public String getTeacherid() {
		return teacherid;
	}

	public void setTeacherid(String teacherid) {
		this.teacherid = teacherid;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getClknumber() {
		return clknumber;
	}

	public void setClknumber(String clknumber) {
		this.clknumber = clknumber;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}


	public String getPopedom() {
		return popedom;
	}

	public void setPopedom(String popedom) {
		this.popedom = popedom;
	}

	public String getExamid() {
		return examid;
	}

	public void setExamid(String examid) {
		this.examid = examid;
	}

	public String getExamname() {
		return examname;
	}

	public void setExamname(String examname) {
		this.examname = examname;
	}

}
