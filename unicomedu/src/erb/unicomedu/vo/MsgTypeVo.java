package erb.unicomedu.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class MsgTypeVo   implements Serializable {
	private String msgTypeId;
	private String msgTypeName;
	private ArrayList<MsgVo> msgList;
	
	
	public String getMsgTypeId() {
		return msgTypeId;
	}
	public void setMsgTypeId(String msgTypeId) {
		this.msgTypeId = msgTypeId;
	}
	public String getMsgTypeName() {
		return msgTypeName;
	}
	public void setMsgTypeName(String msgTypeName) {
		this.msgTypeName = msgTypeName;
	}
	public ArrayList<MsgVo> getMsgList() {
		return msgList;
	}
	public void setMsgList(ArrayList<MsgVo> msgList) {
		this.msgList = msgList;
	}
}
