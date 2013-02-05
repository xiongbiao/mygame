package erb.unicomedu.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import erb.unicomedu.vo.MsgVo;
import erb.unicomedu.vo.NotificationVo;
import erb.unicomedu.vo.SubjectVo;
import erb.unicomedu.vo.TeacherVo;

public class MsgUtil {
	private static final String TAG = LogUtil.makeLogTag(MsgUtil.class);
	
	//{"type":"系统消息","sid":"1","title":"版本更新","pubtime":"2012-09-19 10:53:19","typeid":1,"iswav":0,"isshock":0,"msgid":1,"summary":"有新版本更新"}
	
	public static NotificationVo getStringToObj(String jArr , NotificationVo nv){
		NotificationVo result = nv;
		try {
			LogUtil.d(TAG, "json :---------" + jArr.toString());
		 	JSONObject  objJson = new JSONObject(jArr);
		 	String msgType = objJson.getString("msg_type");
		 	String msgId = objJson.getString("msgId");
		 	String msgClassName = objJson.getString("msgClassName");
		 	String msgFromClassName = objJson.getString("mfcName");
		 	boolean isPing = objJson.getBoolean("isPing");
		 	boolean isShock = objJson.getBoolean("isShock");
		 	String msgc = objJson.getString("msg_c");
		 	if("2".equals(msgType)){
				String msgToType = objJson.getString("msgToType");
				String strData = objJson.getString("strData");
				result.setStrData(strData);
				result.setMsgToType(msgToType);
		 	}else {
		 		String openUrl = objJson.getString("open_url");
		 		nv.setOpenUrl(openUrl);
		 	}
			result.setMsgId(msgId);
			result.setMsgClassName(msgClassName);
			result.setMsgFromClassName(msgFromClassName);
			result.setMsgType(msgType);
			result.setPing(isPing);
			result.setShock(isShock);
			result.setMsgC(msgc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static MsgVo getStringToObj(String jArr ){
		MsgVo result = new MsgVo();
		try {
			LogUtil.d(TAG, "json :---------" + jArr.toString());
		 	JSONObject  objJson = new JSONObject(jArr);
		 	String msgid = objJson.getString("msgid");
		 	String summary = objJson.getString("summary");
		 	String title = objJson.getString("title");
		 	String iswav = objJson.getString("iswav");
		 	String isshock = objJson.getString("isshock");
		 	String pubtime = objJson.getString("pubtime");
		 	result.setIsshock(isshock);
		 	result.setIswav(iswav);
		 	result.setPubtime(pubtime);
		 	result.setMsgid(msgid);
		 	result.setSummary(summary);
		 	result.setTitle(title);
		 	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static Map<String, Object> getStringPlace(String json){
		 Map<String, Object>  res = new HashMap<String, Object>();
		 
		 try {
  			    SubjectVo mSV = new SubjectVo();
			    JSONObject objItem = new JSONObject(json);
				String subjectname = objItem.getString("subjectname");
				String subjectid = objItem.getString("subjectid");
				mSV.setSubjectId(subjectid);
				mSV.setSubjectName(subjectname);
				res.put("sv", mSV);
				String[] id = null;
				String[] name = null;
				JSONArray classList = objItem.getJSONArray("classlist");
				if(classList!=null && classList .length()>0){
					 id = new String[classList .length()];
					  name = new String[classList .length()];
					for(int i = 0 ;i < classList.length();i++){
						  JSONObject classItem =  (JSONObject)classList.get(i);
						  String classid = classItem.getString("classid");			
						  String schoollocation = classItem.getString("schoollocation");	
						  name[i] = schoollocation;
							id[i] = classid;
							LogUtil.d("&&&&&&&", schoollocation + "  classid : "+classid );
					}
					res.put("name", name);
					res.put("id", id);
				}
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		 
		 
		return res;
	}
	
	public static TeacherVo getTeacher(String json ){
		 TeacherVo  result = new  TeacherVo();
		try {
		        JSONObject teacherItem = new JSONObject(json);
				String teacherid = teacherItem.getString("teacherid");
				String cnname = teacherItem.getString("cnname");
				String country = teacherItem.getString("country");
				String birthday = teacherItem.getString("birthday");
				String enname = teacherItem.getString("enname");
				String info = teacherItem.getString("info");
				String sex = teacherItem.getString("sex");
				String picture = teacherItem.getString("imgurl");
				String zhicheng = teacherItem.getString("zhicheng");
				String jingyan = teacherItem.getString("jingyan");
				String techclass = teacherItem.getString("techclass");
				int star = teacherItem.getInt("star");
				int isarchived = teacherItem.getInt("isarchived");
				result.setIsarchived(isarchived);
				result.setStar(star);
				result.setTeacherid(teacherid);
				result.setCnname(cnname);
				result.setBirthday(birthday);
				result.setCountry(country);
				result.setEnname(enname);
				result.setInfo(info);
				result.setPicture(picture);
				result.setSex(sex);
				result.setJingyan(jingyan);
				result.setZhicheng(zhicheng);
				result.setTechclass(techclass);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
		return result;
	}
	
}
