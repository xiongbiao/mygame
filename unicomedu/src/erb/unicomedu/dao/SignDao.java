package erb.unicomedu.dao;

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
import erb.unicomedu.vo.SignAddress;
import erb.unicomedu.vo.SignRankingVo;

public class SignDao extends PublicDao{
	private  static String TAG =  "SignDao";
	public static ArrayList<SignRankingVo> getSignRankingList(Map<String, Object> param) throws Exception{
		ArrayList<SignRankingVo> result = new ArrayList<SignRankingVo>();
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.SignRankingSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					JSONArray jArr = (JSONArray) json.get("toplist");
					JSONObject mSignItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							mSignItem = jArr.getJSONObject(i);
							SignRankingVo mSignVo =new SignRankingVo();
							String id = mSignItem.getString("id");
							String locationid = mSignItem.getString("locationid");
							String memberid = mSignItem.getString("memberid");
							String nickname = mSignItem.getString("nickname");
							String locationname = mSignItem.getString("locationname");
							String latitude = mSignItem.getString("latitude");
							String longitude = mSignItem.getString("longitude");
							String rank = mSignItem.getString("rank");
							String signuptime = mSignItem.getString("signuptime");
							mSignVo.setId(id);
							mSignVo.setLatitude(latitude);
							mSignVo.setLocationid(locationid);
							mSignVo.setLocationname(locationname);
							mSignVo.setLongitude(longitude);
							mSignVo.setMemberid(memberid);
							mSignVo.setNickname(nickname);
							mSignVo.setRank(rank);
							mSignVo.setSignuptime(signuptime);
							result.add(mSignVo);
						}
					}  
				}
			} else  {
				LogUtil.d(TAG, "er code : "+conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}


	public static ArrayList<SignRankingVo> getMySignList(Map<String, Object> param) throws Exception{
		ArrayList<SignRankingVo> result = new ArrayList<SignRankingVo>();
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.MySignSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG,"getMySignList : "+ resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					JSONArray jArr = (JSONArray) json.get("signuplist");
					JSONObject mSignItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							mSignItem = jArr.getJSONObject(i);
							SignRankingVo mSignVo =new SignRankingVo();
							String id = mSignItem.getString("id");
							String locationid = mSignItem.getString("locationid");
							String memberid = mSignItem.getString("memberid");
							String nickname = mSignItem.getString("nickname");
							String locationname = mSignItem.getString("locationname");
							String latitude = mSignItem.getString("latitude");
							String longitude = mSignItem.getString("longitude");
							String rank = mSignItem.getString("rank");
							String signuptime = mSignItem.getString("signuptime");
							mSignVo.setId(id);
							mSignVo.setLatitude(latitude);
							mSignVo.setLocationid(locationid);
							mSignVo.setLocationname(locationname);
							mSignVo.setLongitude(longitude);
							mSignVo.setMemberid(memberid);
							mSignVo.setNickname(nickname);
							mSignVo.setRank(rank);
							mSignVo.setSignuptime(signuptime);
							result.add(mSignVo);
						}
					}  
				}
			} else  {
				LogUtil.d(TAG, "er code : "+conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
		
	}
	
	public static ArrayList<SignRankingVo> getOtherSignList(Map<String, Object> param) throws Exception{
		ArrayList<SignRankingVo> result = new ArrayList<SignRankingVo>();
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			LogUtil.d(TAG, HttpUrls.OtherSignSERVER);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.OtherSignSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG," getOtherSignList "+ resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					JSONArray jArr = (JSONArray) json.get("signuplist");
					JSONObject mSignItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							mSignItem = jArr.getJSONObject(i);
							SignRankingVo mSignVo =new SignRankingVo();
							String id = mSignItem.getString("id");
							String locationid = mSignItem.getString("locationid");
							String memberid = mSignItem.getString("memberid");
							String nickname = mSignItem.getString("nickname");
							String locationname = mSignItem.getString("locationname");
							String latitude = mSignItem.getString("latitude");
							String longitude = mSignItem.getString("longitude");
							String rank = mSignItem.getString("rank");
							String signuptime = mSignItem.getString("signuptime");
							mSignVo.setId(id);
							mSignVo.setLatitude(latitude);
							mSignVo.setLocationid(locationid);
							mSignVo.setLocationname(locationname);
							mSignVo.setLongitude(longitude);
							mSignVo.setMemberid(memberid);
							mSignVo.setNickname(nickname);
							mSignVo.setRank(rank);
							mSignVo.setSignuptime(signuptime);
							result.add(mSignVo);
						}
					}  
				}
			} else  {
				LogUtil.d(TAG, "er code : "+conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
		
	}
	
	
	
	public static SignAddress MyLocationSign(Map<String, Object> param)throws Exception {
		SignAddress result = new SignAddress();
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.MyLocationSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG,""+ resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					String locationid = json.getString("locationid");
					String locationname = json.getString("locationname");
					String info = json.getString("info");
					result.setLocationid(locationid);
					result.setLocationname(locationname);
					result.setInfo(info);
				}
			} else  {
				LogUtil.d(TAG, "er code : "+conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
		
	}
	
	
	
	public static String  SignUser(Map<String, Object> param)throws Exception {
		String result = "";
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "SignUser : "+from);
			LogUtil.d(TAG, "SignUser url : "+HttpUrls.SignSERVER);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.SignSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					result = Def.FAV_OBJ_RESULT_OK;
				}else if("201".equals(code)){
					result = Def.FAV_OBJ_RESULT;
				}
			}else  {
				LogUtil.d(TAG, "er code : "+conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}

	
	
}
