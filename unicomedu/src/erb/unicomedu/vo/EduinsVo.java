package erb.unicomedu.vo;

import java.io.Serializable;

public class EduinsVo implements Serializable  {
	private String eduinsID;
	private String eduinsName;
	private String eduinsAddress;
	private String eduinsInfo;
	private String eduinsCall;
	private String eduinsSchedule;
	private String traffic;
	private String imgpath;
	

	public String getTraffic() {
		return traffic;
	}
	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public String getEduinsSchedule() {
		return eduinsSchedule;
	}
	public void setEduinsSchedule(String eduinsSchedule) {
		this.eduinsSchedule = eduinsSchedule;
	}
	public String getEduinsCall() {
		return eduinsCall;
	}
	public void setEduinsCall(String eduinsCall) {
		this.eduinsCall = eduinsCall;
	}
	private double eduinsLongitude;
	private double eduinsLatitude;
	
	public double getEduinsLongitude() {
		return eduinsLongitude;
	}
	public void setEduinsLongitude(double eduinsLongitude) {
		this.eduinsLongitude = eduinsLongitude;
	}
	public double getEduinsLatitude() {
		return eduinsLatitude;
	}
	public void setEduinsLatitude(double eduinsLatitude) {
		this.eduinsLatitude = eduinsLatitude;
	}
	public String getEduinsInfo() {
		return eduinsInfo;
	}
	public void setEduinsInfo(String eduinsInfo) {
		this.eduinsInfo = eduinsInfo;
	}
	
	public String getEduinsID() {
		return eduinsID;
	}
	public void setEduinsID(String eduinsID) {
		this.eduinsID = eduinsID;
	}
	public String getEduinsName() {
		return eduinsName;
	}
	public void setEduinsName(String eduinsName) {
		this.eduinsName = eduinsName;
	}
	public String getEduinsAddress() {
		return eduinsAddress;
	}
	public void setEduinsAddress(String eduinsAddress) {
		this.eduinsAddress = eduinsAddress;
	}

}
