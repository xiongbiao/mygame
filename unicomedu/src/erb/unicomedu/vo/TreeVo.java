package erb.unicomedu.vo;

import java.io.Serializable;

/**
 * 树
 * @author xiong
 *
 */
public class TreeVo implements Serializable {

	private String treeName;
	private int treeNum;
	private String treeTag;
	private String treeId;
	private String parentid;
	private int subjectnumber;
	private int booknumber;
	private int medianumber;
	private int examnumber;
	public int getSubjectnumber() {
		return subjectnumber;
	}
	public void setSubjectnumber(int subjectnumber) {
		this.subjectnumber = subjectnumber;
	}
	public int getBooknumber() {
		return booknumber;
	}
	public void setBooknumber(int booknumber) {
		this.booknumber = booknumber;
	}
	public int getMedianumber() {
		return medianumber;
	}
	public void setMedianumber(int medianumber) {
		this.medianumber = medianumber;
	}
	public int getExamnumber() {
		return examnumber;
	}
	public void setExamnumber(int examnumber) {
		this.examnumber = examnumber;
	}
	
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getTreeName() {
		return treeName;
	}
	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}
	public int getTreeNum() {
		return treeNum;
	}
	public void setTreeNum(int treeNum) {
		this.treeNum = treeNum;
	}
	public String getTreeTag() {
		return treeTag;
	}
	public void setTreeTag(String treeTag) {
		this.treeTag = treeTag;
	}
	public String getTreeId() {
		return treeId;
	}
	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
}
