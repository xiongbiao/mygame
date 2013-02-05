package erb.unicomedu.vo;

import java.io.Serializable;

/**
 * 区域 
 * @author xiong
 *
 */
public class AreaVo implements Serializable  {
	
	private String distrcitid;
	private String district;
	private String remark;
	public String getDistrcitid() {
		return distrcitid;
	}
	public void setDistrcitid(String distrcitid) {
		this.distrcitid = distrcitid;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	private String sort;
	

}
