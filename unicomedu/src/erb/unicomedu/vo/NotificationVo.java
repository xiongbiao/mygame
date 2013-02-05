package erb.unicomedu.vo;

import java.io.Serializable;

public class NotificationVo implements Serializable {

	private String id;
	private String apiKey;
	private String title;
	private String msg;
	private String toClass;
	private String openUrl;
	/**  ******************************  */
	
     private String msgType; 
	 private String msgId;
	 private String msgToType;
	 private String msgClassName;
	 private String msgFromClassName;
	 private String strData;
	 private String msgC;
	 private boolean isPing;
	 private boolean isShock;
	 /**  ******************************  */
	 public String getMsgFromClassName() {
			return msgFromClassName;
		}

		public void setMsgFromClassName(String msgFromClassName) {
			this.msgFromClassName = msgFromClassName;
		}
	public String getMsgId() {
		return msgId;
	}

	public String getMsgC() {
		return msgC;
	}

	public void setMsgC(String msgC) {
		this.msgC = msgC;
	}

	public String getMsgToType() {
		return msgToType;
	}

	public void setMsgToType(String msgToType) {
		this.msgToType = msgToType;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	/**
	 * 1 不跳转  2  跳转
	 */
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgClassName() {
		return msgClassName;
	}

	public void setMsgClassName(String msgClassName) {
		this.msgClassName = msgClassName;
	}

	public String getStrData() {
		return strData;
	}

	public void setStrData(String strData) {
		this.strData = strData;
	}

	public boolean isPing() {
		return isPing;
	}

	public void setPing(boolean isPing) {
		this.isPing = isPing;
	}

	public boolean isShock() {
		return isShock;
	}

	public void setShock(boolean isShock) {
		this.isShock = isShock;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getToClass() {
		return toClass;
	}

	public void setToClass(String toClass) {
		this.toClass = toClass;
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}
}
