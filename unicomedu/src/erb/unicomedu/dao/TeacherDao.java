package erb.unicomedu.dao;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.HttpUrls;
import erb.unicomedu.util.HttpUtil;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.KeyVo;
import erb.unicomedu.vo.SubjectVo;
import erb.unicomedu.vo.TeacherVo;

public class TeacherDao extends PublicDao {
	private static String TAG = "TeacherDao";

	/**
	 * 获得
	 * 
	 * @param param
	 * @return
	 */
	public static ArrayList<TeacherVo> getTeacherList(Map<String, Object> param) throws Exception{
		ArrayList<TeacherVo> result = new ArrayList<TeacherVo>();
		try {
			DefMap(param);
			
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "" + from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.TeacherSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("Teacherlist");
					result = getTList(jArr);
				}else{
					throw new EuException("code："+code+"数据错误");
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	public static String  addFavorites(Map<String, Object> param) throws Exception{
		String result = "";
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "addFavorites : "+from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.TeacherAddFavoritesSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					result = Def.FAV_OBJ_RESULT_OK;
				}else if("201".equals(code)){
					result = Def.FAV_OBJ_RESULT;
				}
				LogUtil.d(TAG, resultJson);
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	
	public static String  DeleteFavorites(Map<String, Object> param)  throws Exception {
		String result = "";
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "DeleteFavorites : "+from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.DeleteFavoritesSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					result = Def.FAV_OBJ_RESULT_OK;
				}else if("201".equals(code)){
					result = Def.FAV_OBJ_RESULT;
				}
				LogUtil.d(TAG, resultJson);
		}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
		}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		} 
		return result;
	}
	
	
	private static ArrayList<TeacherVo> getTList(JSONArray jArr )  throws Exception{
		ArrayList<TeacherVo> result = new ArrayList<TeacherVo>();
		try {
		JSONObject teacherItem;
		if (jArr.length() > 0) {
			for (int i = 0; i < jArr.length(); i++) {
				teacherItem = jArr.getJSONObject(i);
				TeacherVo tv = new TeacherVo();
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
				String clknumber = teacherItem.getString("clknumber");
				int star = teacherItem.getInt("star");
				int isarchived = teacherItem.getInt("isarchived");
				tv.setClknumber(clknumber);
				tv.setIsarchived(isarchived);
				tv.setStar(star);
				tv.setTeacherid(teacherid);
				tv.setCnname(cnname);
				tv.setBirthday(birthday);
				tv.setCountry(country);
				tv.setEnname(enname);
				tv.setInfo(info);
				tv.setPicture(picture);
				tv.setSex(sex);
				tv.setJingyan(jingyan);
				tv.setZhicheng(zhicheng);
				tv.setTechclass(techclass);
				result.add(tv);
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
		return result;
	}
	/**
	 * 获得
	 * 
	 * @param param
	 * @return
	 */
	public static ArrayList<TeacherVo> getTeacherFavoritesList(Map<String, Object> param) throws Exception{
		ArrayList<TeacherVo> result = new ArrayList<TeacherVo>();
		try {
			DefMap(param);
			param.put("page", 0);
			param.put("size", 30);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "" + from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.TeacherFavoritesSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("teacherlist");
					result = getTList(jArr);
				}
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	
	
	/**
	 * 老师 的相关课程
	 * @param param
	 * @return
	 */
	public static ArrayList<SubjectVo> getSubjectList(Map<String, Object> param)  throws Exception{
		ArrayList<SubjectVo> result = new ArrayList<SubjectVo>();
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.TeacherClassListSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("Subjectlist");
					JSONObject videoItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							videoItem = jArr.getJSONObject(i);
							SubjectVo subjectVo = new SubjectVo();
							String subject = videoItem.getString("subject");
							String subjectId = videoItem.getString("subjectid");
							String info = videoItem.getString("info");
							String type = videoItem.getString("type");
							String fitstudent = videoItem
									.getString("fitstudent");
							String pubtime = videoItem.getString("pubtime");
							int clknumber = videoItem.getString("clknumber") == null
									|| "null".equals(videoItem.getString("clknumber")) ? 0
									: videoItem.getInt("clknumber");
							int isarchived = videoItem.getInt("isarchived");
							subjectVo.setIsarchived(isarchived);
							subjectVo.setInfo(info);
							subjectVo.setFitstudent(fitstudent);
							subjectVo.setSubjectName(subject);
							subjectVo.setPubtime(pubtime);
							subjectVo.setClknumber(clknumber);
							subjectVo.setSubjectId(subjectId);
							
							result.add(subjectVo);
						}
					}
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	
	
	
	

	// teacher/show
	public static TeacherVo getTeacherById(Map<String, Object> param)  throws Exception{
		TeacherVo result = new TeacherVo();
		try {
			DefMap(param);
			param.put("page", 0);
			param.put("orderby", 1);

			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "" + from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.TeacherSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("Teacherlist");
					JSONObject teacherItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							teacherItem = jArr.getJSONObject(i);
							String teacherid = teacherItem
									.getString("teacherid");
							String cnname = teacherItem.getString("cnname");
							String country = teacherItem.getString("country");
							String birthday = teacherItem.getString("birthday");
							String enname = teacherItem.getString("enname");
							String info = teacherItem.getString("info");
							String sex = teacherItem.getString("sex");
							String picture = teacherItem.getString("imgurl");
							result.setTeacherid(teacherid);
							result.setCnname(cnname);
							result.setBirthday(birthday);
							result.setCountry(country);
							result.setEnname(enname);
							result.setInfo(info);
							result.setPicture(picture);
							result.setSex(sex);
						}
					}
				}
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EuException(Def.getServiceMsg(-1));
		}  
		return result;
	}

	/***
	 * 获得关键字
	 * 
	 * @param param
	 * @return
	 */
	public static ArrayList<KeyVo> getObjKey(Map<String, Object> param)throws Exception {
		ArrayList<KeyVo> result = new ArrayList<KeyVo>();
		try {
			DefMap(param);
			param.put("page", 0);
			param.put("orderby", 1);
			param.put("size", Def.KEY_MAX_SIZE);
			String url = "";
			String type = param.get("urlType") != null ? param.get("urlType")+ "" : null;
			if (type != null) {
				int t = Integer.valueOf(type);
				switch (t) {
				case Def.Teacher_KEY:
					url = HttpUrls.TeacherKEYSERVER;
					break;
				case Def.Video_KEY:
					url = HttpUrls.VideoKEYSERVER;
   					break;
				case Def.Read_KEY:
					url = HttpUrls.ReadKEYSERVER;
					break;
				case Def.Exam_KEY:
					url = HttpUrls.ExamKEYSERVER;
					break;
				case Def.Subject_KEY:
					url = HttpUrls.SubjectKEYSERVER;
					break;
				case 5:
					url = HttpUrls.IntentionSERVER;
					break;
				default:
					url = HttpUrls.SubjectKEYSERVER;
					break;
				}
			}
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "1-------" + from);
			LogUtil.d(TAG, "1--url-----" + url);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, url, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("keywordlist");
					JSONObject keyItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < ( jArr.length()>=Def.KEY_MAX_SIZE?Def.KEY_MAX_SIZE:jArr.length()); i++) {
							keyItem = jArr.getJSONObject(i);
							KeyVo kv = new KeyVo();
							String keyValue = keyItem.getString("keyword");
							 int tagId = keyItem.getInt("itemid");
								kv.setKeyId(tagId);
							kv.setKeyValue(keyValue);
							kv.setTagId(tagId);
							result.add(kv);
						}
					}
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	
	public static ArrayList<KeyVo> getIntentionKey(Map<String, Object> param)throws Exception  {
		ArrayList<KeyVo> result = new ArrayList<KeyVo>();
		try {
			DefMap(param);
			param.put("page", 0);
			param.put("orderby", 1);
			param.put("size", Def.KEY_MAX_SIZE);
			String url  = HttpUrls.IntentionSERVER;
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "1-------" + from);
			LogUtil.d(TAG, "1--url-----" + url);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, url, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("intentionlist");
					JSONObject keyItem;
					if (jArr.length() > 0) {
						for (int i = 0; i <( jArr.length()>=Def.KEY_MAX_SIZE?Def.KEY_MAX_SIZE:jArr.length()); i++) {
							keyItem = jArr.getJSONObject(i);
							KeyVo kv = new KeyVo();
							String keyValue = keyItem.getString("intention");
							 int tagId = keyItem.getInt("itemid");
							kv.setKeyId(tagId);
							kv.setKeyValue(keyValue);
							kv.setTagId(tagId);
							result.add(kv);
						}
					}
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}


    public static int   UserScore(Map<String, Object> param)throws Exception {
    	int newstar = 0 ;
    	try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "" + from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.SetstarSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					newstar =   json.getInt ("newstar");
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
    	return newstar;
    }  
}
