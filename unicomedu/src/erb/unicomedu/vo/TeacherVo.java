package erb.unicomedu.vo;

import java.io.Serializable;

/***
 * 
 * @author xiongbiao
 * 
 */
public class TeacherVo  implements Serializable  {

	private String teacherid;
	private String picture;
	private String level;
	private String course;
	private String cnname;
	private String country;
	private String birthday;
	private String enname;
	private String info;
	private String sex;
	private String jingyan;
	private String zhicheng;
	private String techclass;
	private String clknumber;
	private int star;
	private int isarchived ;
	
	
	
	

	public String getClknumber() {
		return clknumber;
	}

	public void setClknumber(String clknumber) {
		this.clknumber = clknumber;
	}

	public int getIsarchived() {
		return isarchived;
	}

	public void setIsarchived(int isarchived) {
		this.isarchived = isarchived;
	}


	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getTechclass() {
		return techclass;
	}

	public void setTechclass(String techclass) {
		this.techclass = techclass;
	}

	public String getJingyan() {
		if(jingyan==null ||"null".equals(jingyan) ){
			jingyan ="-";
		}
		return jingyan;
	}

	public void setJingyan(String jingyan) {
		this.jingyan = jingyan;
	}

	public String getZhicheng() {
		if(zhicheng==null||"null".equals(zhicheng)){
			zhicheng ="-";
		}
		return zhicheng;
	}

	public void setZhicheng(String zhicheng) {
		this.zhicheng = zhicheng;
	}

	public String getTeacherid() {
		return teacherid;
	}

	public void setTeacherid(String teacherid) {
		this.teacherid = teacherid;
	}
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getCnname() {
		return cnname;
	}

	public void setCnname(String cnname) {
		this.cnname = cnname;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSex() {
		if(sex==null||"null".equals(sex)){
			sex ="-";
		}
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	 

}
