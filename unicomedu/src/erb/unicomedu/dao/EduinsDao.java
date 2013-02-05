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
import erb.unicomedu.vo.AreaVo;
import erb.unicomedu.vo.EduinsVo;

public class EduinsDao  extends PublicDao{
	private  static String TAG =  "EduinsDao";
	public static ArrayList<EduinsVo> getEduinsList(Map<String, Object> param)throws Exception  {
		ArrayList<EduinsVo> result = new ArrayList<EduinsVo>();
		try {
			DefMap(param);
			param.put("page", 100);
			param.put("orderby", 1);
			String from =HttpUtil.MapToParam(param) ;
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.EduinsSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					JSONArray jArr = (JSONArray) json.get("Locationlist");
					JSONObject eduinsItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							eduinsItem = jArr.getJSONObject(i);
							EduinsVo ev =new EduinsVo();
							String eduinsName = eduinsItem.getString("school_location");
							String locationid = eduinsItem.getString("locationid");
							String telphone = eduinsItem.getString("telphone");
							String eduinsAddress = eduinsItem.getString("address");
							String techclass = eduinsItem.getString("techclass");
							String traffic = eduinsItem.getString("traffic");
							String info = eduinsItem.getString("info");
							String imgpath = eduinsItem.getString("imgpath");
							double eduinsLongitude = eduinsItem.getDouble("longitude");
							double eduinsLatitude = eduinsItem.getDouble("latitude");
							ev.setEduinsName(eduinsName);
							ev.setEduinsAddress(eduinsAddress);
							ev.setEduinsInfo(info);
							ev.setEduinsCall(telphone);
							ev.setTraffic(traffic);
							ev.setEduinsSchedule(techclass);
							ev.setImgpath(imgpath);
							ev.setEduinsID(locationid);
							ev.setEduinsLatitude(eduinsLatitude);
							ev.setEduinsLongitude(eduinsLongitude);
							result.add(ev);
						}
					}  
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
	
	
	public static EduinsVo getEduinsInfo(Map<String, Object> param)throws Exception  {
		 EduinsVo result = new  EduinsVo();
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.EduinsShowSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					JSONObject eduinsItem = (JSONObject) json.get("LocationInfo");
							EduinsVo ev =new EduinsVo();
							String eduinsName = eduinsItem.getString("school_location");
							String locationid = eduinsItem.getString("locationid");
							String telphone = eduinsItem.getString("telphone");
							String eduinsAddress = eduinsItem.getString("address");
							String techclass = eduinsItem.getString("techclass");
							String traffic = eduinsItem.getString("traffic");
							String info = eduinsItem.getString("info");
							String imgpath = eduinsItem.getString("imgpath");
							double eduinsLongitude = eduinsItem.getDouble("longitude");
							double eduinsLatitude = eduinsItem.getDouble("latitude");
							ev.setEduinsName(eduinsName);
							ev.setEduinsAddress(eduinsAddress);
							ev.setEduinsInfo(info);
							ev.setEduinsCall(telphone);
							ev.setTraffic(traffic);
							ev.setEduinsSchedule(techclass);
							ev.setImgpath(imgpath);
							ev.setEduinsID(locationid);
							ev.setEduinsLatitude(eduinsLatitude);
							ev.setEduinsLongitude(eduinsLongitude);
							result = ev;
					  
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
	
	
	
	
	public static ArrayList<AreaVo> getAreaList(Map<String, Object> param) throws Exception {
		ArrayList<AreaVo> result = new ArrayList<AreaVo>();
		try {
			DefMap(param);
			param.put("page", 10);
			param.put("orderby", 1);
			String from =HttpUtil.MapToParam(param) ;
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.EduinsAreaSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					JSONArray jArr = (JSONArray) json.get("districtlist");
					JSONObject eduinsItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							eduinsItem = jArr.getJSONObject(i);
							AreaVo ev =new AreaVo();
							String distrcitid = eduinsItem.getString("distrcitid");
							String district = eduinsItem.getString("district");
							String remark = eduinsItem.getString("remark");
							String sort = eduinsItem.getString("sort");
							ev.setDistrcitid(distrcitid);
							ev.setDistrict(district);
							ev.setRemark(remark);
							ev.setSort(sort);
							result.add(ev);
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
}
