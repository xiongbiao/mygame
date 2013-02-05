package erb.unicomedu.vo;

import java.io.Serializable;

/***
 * 模块
 * @author xiong
 *
 */
public class ModelVo implements Serializable {

	private String modelid;
	private String modelcode;
	private String popedom;
	private String model;
	public String getModelid() {
		return modelid;
	}
	public void setModelid(String modelid) {
		this.modelid = modelid;
	}
	public String getModelcode() {
		return modelcode;
	}
	public void setModelcode(String modelcode) {
		this.modelcode = modelcode;
	}
	public String getPopedom() {
		return popedom;
	}
	public void setPopedom(String popedom) {
		this.popedom = popedom;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
}
