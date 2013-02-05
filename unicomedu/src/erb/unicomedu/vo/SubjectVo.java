package erb.unicomedu.vo;

import java.io.Serializable;

public class SubjectVo implements Serializable{

	private String subjectId;
	private String subjectName;
	private String info;
	private String fitstudent;
	private String pubtime;
	private String price;
	private String type;
	private  int isarchived ;
//	private  int isbuy ;
//	private  int integral ;
	
//	public int getIntegral() {
//		return integral;
//	}
//	public void setIntegral(int integral) {
//		this.integral = integral;
//	}
	public int getIsarchived() {
		return isarchived;
	}
	public void setIsarchived(int isarchived) {
		this.isarchived = isarchived;
	}
//	public int getIsbuy() {
//		return isbuy;
//	}
//	public void setIsbuy(int isbuy) {
//		this.isbuy = isbuy;
//	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private int clknumber;

	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public int getClknumber() {
		return clknumber;
	}
	public void setClknumber(int clknumber) {
		this.clknumber = clknumber;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getFitstudent() {
		return fitstudent;
	}
	public void setFitstudent(String fitstudent) {
		this.fitstudent = fitstudent;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
}
