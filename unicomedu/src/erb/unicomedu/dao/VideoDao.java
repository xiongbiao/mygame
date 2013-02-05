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
import erb.unicomedu.vo.SubjectVo;
import erb.unicomedu.vo.VideoVo;

public class VideoDao extends PublicDao {
	private static String TAG = "VideoDao";

	public static ArrayList<VideoVo> getVideoList(Map<String, Object> param) throws Exception{
		ArrayList<VideoVo> result = new ArrayList<VideoVo>();
		try {
			DefMap(param);
			
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			LogUtil.d(TAG, HttpUrls.VideoSERVER);
			
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.VideoSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("Medialist");
					result = getTList(jArr);
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	
	
	private static ArrayList<VideoVo> getTList(JSONArray jArr ){
		ArrayList<VideoVo> result = new ArrayList<VideoVo>();
		try {
			JSONObject videoItem;
			if (jArr.length() > 0) {
				for (int i = 0; i < jArr.length(); i++) {
					videoItem = jArr.getJSONObject(i);
					VideoVo video = new VideoVo();
					String teacherid = videoItem.getString("teacherid");
					String subjectid = videoItem.getString("subjectid");
					String subject = videoItem.getString("subject");
					String imgurl = videoItem.getString("imgurl");
					String teacher = videoItem.getString("teacher");
					String info = videoItem.getString("info");
					String mediaName = videoItem.getString("medianame");
					String mediaUrl = videoItem.getString("mediaurl");
					String integral = videoItem.getString("integral");
					String clknumber = videoItem.getString("clknumber");
					String mediaid = videoItem.getString("mediaid");
					String type = videoItem.getString("type");
					String pubtime = videoItem.getString("pubtime");
					String typeid = videoItem.getString("typeid");
					String payment = videoItem.getString("payment");
					String howlong = videoItem.getString("howlong");
					int isarchived = videoItem.getInt("isarchived");
					int isbuy = videoItem.getInt("isbuy");
					video.setIsarchived(isarchived);
					video.setIsbuy(isbuy);
					video.setMediaid(mediaid);
					video.setType(type);
					video.setTypeid(typeid);
					video.setPayment(payment);
					video.setHowlong(howlong);
					video.setPubtime(pubtime);
					video.setTeacher(teacher);
					video.setClknumber(clknumber);
					video.setIntegral(integral);
					video.setImgurl(imgurl);
					video.setMediaUrl(mediaUrl);
					video.setMediaName(mediaName);
					video.setInfo(info);
					video.setSubjectid(subjectid);
					video.setSubject(subject);
					video.setTeacherid(teacherid);
					result.add(video);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
		return result;
	}
	
	public static String  addFavorites(Map<String, Object> param) throws Exception {
		String result = "";
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "addFavorites : "+from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.VideoAddFavoritesSERVER, "", "");
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
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	
	public static String  videoBuy(Map<String, Object> param) throws Exception {
		String result = "";
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "videoBuy : "+from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.VideoBuySERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					result = Def.FAV_OBJ_RESULT_OK;
				}else if("201".equals(code)){
					result = Def.FAV_OBJ_RESULT;
				}else if("202".equals(code)){
					result = Def.NO_OBJ_RESULT;
				}
				LogUtil.d(TAG, resultJson);
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	
	public static ArrayList<VideoVo> getVideoFavoritesList(Map<String, Object> param) throws Exception{
		ArrayList<VideoVo> result = new ArrayList<VideoVo>();
		try {
			DefMap(param);
			param.put("page", 0);
			param.put("size", 20);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			LogUtil.d(TAG, HttpUrls.VideoFavoritesSERVER);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.VideoFavoritesSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("medialist");
					result = getTList(jArr);
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	
	public static ArrayList<VideoVo> getVideoOtherList(Map<String, Object> param) throws Exception{
		ArrayList<VideoVo> result = new ArrayList<VideoVo>();
		try {
			DefMap(param);
			param.put("subjectid", "1");
			param.put("page", 0);
			param.put("size", 20);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.VideoOtherKEYSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("Medialist");
					result = getTList(jArr);
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}

}
