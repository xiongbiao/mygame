package erb.unicomedu.vo;

import java.io.Serializable;

public class ReadVo implements Serializable{
	
	private String bookid;
	private String type;
	private String info;
	private String subject;
	private String school;
	private String pubtime;
	private String typeid;
	private String imgurl;
	private String bookname;
	private String payment;
	private String integral;
	private String teacherid;
	private String teacher;
	
	private String clknumber;
	
	private String bookurl;

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

	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
		this.bookid = bookid;
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

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getTeacherid() {
		return teacherid;
	}

	public void setTeacherid(String teacherid) {
		this.teacherid = teacherid;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getClknumber() {
		return clknumber;
	}

	public void setClknumber(String clknumber) {
		this.clknumber = clknumber;
	}

	public String getBookurl() {
		return bookurl;
	}

	public void setBookurl(String bookurl) {
		this.bookurl = bookurl;
	}
}
