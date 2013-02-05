package erb.unicomedu.vo;

import java.io.Serializable;

public class VideoVo implements Serializable {
	private String mediaName;
	private String mediaUrl;
	private String subjectid;
	private String imgurl;
	private String mediaid;
	private String clknumber;
	private String integral;
	private String teacher;
	private String teacherid;
	private String info;
	private String subject;

	private String type;
	private String pubtime;
	private String typeid;
	
	private  int isarchived ;
	private  int isbuy ;


	public int getIsarchived() {
		return isarchived;
	}


	public void setIsarchived(int isarchived) {
		this.isarchived = isarchived;
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


	public String getPayment() {
		return payment;
	}


	public void setPayment(String payment) {
		this.payment = payment;
	}


	public String getHowlong() {
		return howlong;
	}


	public void setHowlong(String howlong) {
		this.howlong = howlong;
	}

	private String payment;
	private String howlong;
	
	
	public String getSubject() {
		return subject;
	}


	public String getTeacherid() {
		return teacherid;
	}


	public void setTeacherid(String teacherid) {
		this.teacherid = teacherid;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	 

	public String getMediaid() {
		return mediaid;
	}


	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}


	public String getClknumber() {
		return clknumber;
	}


	public void setClknumber(String clknumber) {
		this.clknumber = clknumber;
	}


	public String getIntegral() {
		return integral;
	}


	public void setIntegral(String integral) {
		this.integral = integral;
	}


 

}
