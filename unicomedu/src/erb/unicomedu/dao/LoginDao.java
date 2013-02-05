package erb.unicomedu.dao;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.HttpUrls;
import erb.unicomedu.util.HttpUtil;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.ModelVo;
import erb.unicomedu.vo.RecommendVo;
import erb.unicomedu.vo.UserVo;

public class LoginDao extends PublicDao{
	private final static String TAG =  "LoginDao";
	
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public static boolean  updateUser(Map<String, Object> param) throws Exception{
		boolean  bRestule = false;
		try {
			DefMap(param);
			
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.UpdateUserSERVER, "", "");        
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					bRestule = true;
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return bRestule;
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public static String  updateUserAvatar(Map<String, Object> param,Map<String, File> files)throws Exception{
		String  bRestule = "";
		try {
			DefMapNoSmis(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			LogUtil.d(TAG, "url : "+HttpUrls.UpdateUserAvatarSERVER);
			
			HttpURLConnection conn = HttpUtil.postFiles(HttpUrls.UpdateUserAvatarSERVER, param, files);        
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					bRestule = json.getString("logourl");
					
				}else{
					LogUtil.d(TAG, " ------ code : "+code);
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return bRestule;
	}

	
	public static String getModel(Map<String, Object> param) throws Exception{
		String  restule = "";
		try {
			DefMap(param);
			
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.ModelpopedomSERVER, "", "");        
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					restule = json.getString("popedomlist");
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return restule;
	}
	
	public static String getRecommend(Map<String, Object> param) throws Exception{
		String  restule = "";
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.RecommendSERVER, "", "");        
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					restule = json.getString("Recommendlist");
				}
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return restule;
	}
	

	public static ArrayList<RecommendVo> getArrayToRecommend(JSONArray jArr) {
		ArrayList<RecommendVo> result = new ArrayList<RecommendVo>();
		try {
			LogUtil.d("RecommendVo: ", "" + jArr.toString());
			JSONObject videoItem;
			if (jArr.length() > 0) {
				for (int i = 0; i < jArr.length(); i++) {
					videoItem = jArr.getJSONObject(i);
					RecommendVo recommenVo = new RecommendVo();
					String pubtime = videoItem.getString("pubtime");
					String logo = videoItem.getString("logo");
					String appid = videoItem.getString("appid");
					String appname = videoItem.getString("appname");
					String appurl = videoItem.getString("appurl");
					recommenVo.setAppid(appid);
					recommenVo.setAppname(appname); 
					recommenVo.setAppurl(appurl);
					recommenVo.setLogo(logo);
					recommenVo.setPubtime(pubtime);
					result.add(recommenVo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<ModelVo> getArrayToModel(JSONArray jArr) {
		ArrayList<ModelVo> result = new ArrayList<ModelVo>();
		try {
			LogUtil.d(TAG, "" + jArr.toString());
			JSONObject videoItem;
			if (jArr.length() > 0) {
				for (int i = 0; i < jArr.length(); i++) {
					videoItem = jArr.getJSONObject(i);
					ModelVo modelVo = new ModelVo();
					String model = videoItem.getString("model");
					String modelid = videoItem.getString("modelid");
					String modelcode = videoItem.getString("modelcode");
					String popedom = videoItem.getString("popedom");
					modelVo.setModel(model);
					modelVo.setModelcode(modelcode);
					modelVo.setModelid(modelid);
					modelVo.setPopedom(popedom);
					result.add(modelVo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String LoginString(Map<String, Object> param) {
		String uv = ""; 
		try {
			DefMap(param);
			param.put("mobile", 11111111);
			param.put("mpopversion", 0);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.LoginSERVER, "", "");        
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					boolean modelpopedom = json.getBoolean("modelpopedom");
					uv = json.getString("memberinfo");
					LogUtil.d(TAG, ""+modelpopedom);
//					if(modelpopedom){
//						//更新权限表
//					}
				}else{
					uv = Def.USER_LOGIN_RESULT;
				}
			} else  {
				LogUtil.d(TAG, "code : "+conn.getResponseCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return uv;
	}
	
  /**
   * 注册用户
   * @param param
   * @return
   */
	public static UserVo RegUser(Map<String, Object> param){
		UserVo result = new UserVo();
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.RegSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				boolean isSuccess = json.getBoolean("success");
				if("200".equals(code)&&isSuccess){
					
					LogUtil.d(TAG, ""+isSuccess);
					JSONArray jArr = (JSONArray) json.get("memberInfo");
					if(jArr!=null){
						LogUtil.d(TAG, "" + jArr.toString());
					}
//					JSONObject eduinsItem;
//					if (jArr.length() > 0) {
//						for (int i = 0; i < jArr.length(); i++) {
//							eduinsItem = jArr.getJSONObject(i);
//							EduinsVo ev =new EduinsVo();
//							String eduinsName = eduinsItem.getString("school_location");
//							String telphone = eduinsItem.getString("telphone");
//							String eduinsAddress = eduinsItem.getString("address");
////							String birthday = teacherItem.getString("birthday");
////							String enname = teacherItem.getString("enname");
//							String info = eduinsItem.getString("info");
////							String sex = teacherItem.getString("sex");
////							String picture = teacherItem.getString("imgurl");
//							ev.setEduinsName(eduinsName);
//							ev.setEduinsAddress(eduinsAddress);
//							ev.setEduinsInfo(info);
////							tv.setBirthday(birthday);
////							tv.setCountry(country);
////							tv.setEnname(enname);
////							tv.setInfo(info);
////							tv.setPicture(picture);
////							tv.setSex(sex);
//							result.add(ev);
//						}
//					}  
				}
			} else  {
				LogUtil.d(TAG, "code : "+conn.getResponseCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return result;
	}

	
	
	public static boolean  ExistUser(Map<String, Object> param){
		boolean isExist = false;
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.ExistSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				boolean isSuccess = json.getBoolean("success");
				if("200".equals(code)&&isSuccess){
					isExist = false;
				}
				if("201".equals(code)&&isSuccess){
					isExist = true;
				}
			} else  {
				LogUtil.d(TAG, "code : "+conn.getResponseCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return isExist ;
	}
	
	public static String RegUserToString(Map<String, Object> param){
		String result = "";
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.RegSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				boolean isSuccess = json.getBoolean("success");
				if("200".equals(code)&&isSuccess){
						result = json.getString("memberInfo");
				}
				if("201".equals(code)){
					result = Def.REG_RESULT_REPEAT;
				}
			} else  {
				LogUtil.d(TAG, "code : "+conn.getResponseCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return result;
	}

}
