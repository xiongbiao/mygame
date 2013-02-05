package erb.unicomedu.dao;

import java.io.InputStream;
import java.net.ConnectException;
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
import erb.unicomedu.vo.ReadVo;
import erb.unicomedu.vo.VideoVo;

public class ReadDao extends PublicDao {
	
	 private  static String TAG =  "ReadDao";
		public static ArrayList<ReadVo> getReadList(Map<String, Object> param)throws Exception {
			ArrayList<ReadVo> result = new ArrayList<ReadVo>();
			try {
				
			    DefMap(param);
				String from =HttpUtil.MapToParam(param) ;
				LogUtil.d(TAG, from);
				HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.ReadSERVER, "", ""); 
				if (conn.getResponseCode() == 200) {
					InputStream in = conn.getInputStream();
					String resultJson = HttpUtil.inputStreamString(in);
					LogUtil.d(TAG, resultJson);
					JSONObject json = new JSONObject(resultJson);
					String code = json.getString("code");
					if("200".equals(code)){
						JSONArray jArr = (JSONArray) json.get("Booklist");
						result = getTList(jArr);
					}else{
						LogUtil.d(TAG, "server code : "+ code);
					}
				} else  {
					LogUtil.d(TAG, "code : "+conn.getResponseCode());
					throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
				}
			}  catch (Exception e) {
				e.printStackTrace();
				throw new EuException(e);
			}
			return result;
		}
		public static String  addFavorites(Map<String, Object> param) throws Exception {
			String result = "";
			try {
				DefMap(param);
				String from = HttpUtil.MapToParam(param);
				LogUtil.d(TAG, "addFavorites : "+from);
				HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.ReadAddFavoritesSERVER, "", "");
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
				}else  {
					LogUtil.d(TAG, "code : "+conn.getResponseCode());
					throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
				}
			}  catch (Exception e) {
				e.printStackTrace();
				throw new EuException(e);
			}
			return result;
		}
		
		public static String  ReadBuy(Map<String, Object> param) throws Exception {
			String result = "";
			try {
				DefMap(param);
				String from = HttpUtil.MapToParam(param);
				LogUtil.d(TAG, "ReadBuy : "+from);
				HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.ReadBuySERVER, "", "");
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
				}else  {
					LogUtil.d(TAG, "code : "+conn.getResponseCode());
					throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
				}
			}  catch (Exception e) {
				e.printStackTrace();
				throw new EuException(e);
			}
			return result;
		}
		
		public static ArrayList<ReadVo> getReadFavoritesList(Map<String, Object> param)throws Exception  {
			ArrayList<ReadVo> result = new ArrayList<ReadVo>();
			try {
				DefMap(param);
				param.put("page", 0);
				param.put("size", 20);
				String from =HttpUtil.MapToParam(param) ;
				LogUtil.d(TAG, from);
				HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.ReadFavoritesSERVER, "", ""); 
				if (conn.getResponseCode() == 200) {
					InputStream in = conn.getInputStream();
					String resultJson = HttpUtil.inputStreamString(in);
					LogUtil.d(TAG, resultJson);
					JSONObject json = new JSONObject(resultJson);
					String code = json.getString("code");
					if("200".equals(code)){
						JSONArray jArr = (JSONArray) json.get("booklist");
						result = getTList(jArr);
					}else{
						LogUtil.d(TAG, "server code : "+ code);
					}
				} else  {
					LogUtil.d(TAG, "code : "+conn.getResponseCode());
					throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
				}
			}  catch (Exception e) {
				e.printStackTrace();
				throw new EuException(e);
			}
			return result;
		}
		
		private static ArrayList<ReadVo> getTList(JSONArray jArr ){
			ArrayList<ReadVo> result = new ArrayList<ReadVo>();
			try {
				JSONObject videoItem;
				if (jArr.length() > 0) {
					for (int i = 0; i < jArr.length(); i++) {
						videoItem = jArr.getJSONObject(i);
						ReadVo readVo =new ReadVo();
						String bookid = videoItem.getString("bookid");
						String type = videoItem.getString("type");
						String info = videoItem.getString("info");
						String school = videoItem.getString("school");
						String subject = videoItem.getString("subject");
						String imgurl = videoItem.getString("imgurl");
						String pubtime = videoItem.getString("pubtime");
						String typeid = videoItem.getString("typeid");
						String bookname = videoItem.getString("bookname");
						String payment = videoItem.getString("payment");
						String integral = videoItem.getString("integral");
						String teacherid = videoItem.getString("teacherid");
						String teacher = videoItem.getString("teacher");
						String bookurl = videoItem.getString("bookurl");
						String clknumber = videoItem.getString("clknumber")==null||"null".equals(videoItem.getString("clknumber")) ?"0":videoItem.getString("clknumber");
						int isarchived = videoItem.getInt("isarchived");
						int isbuy = videoItem.getInt("isbuy");
						readVo.setIsarchived(isarchived);
						readVo.setIsbuy(isbuy);
						readVo.setBookid(bookid);
						readVo.setBookname(bookname);
						readVo.setBookurl(bookurl);
						readVo.setClknumber(clknumber);
						readVo.setInfo(info);
						readVo.setImgurl(imgurl);
						readVo.setIntegral(integral);
						readVo.setPayment(payment);
						readVo.setPubtime(pubtime);
						readVo.setSchool(school);
						readVo.setSubject(subject);
						readVo.setTeacher(teacher);
						readVo.setTeacherid(teacherid);
						readVo.setType(type);
						readVo.setTypeid(typeid);
						result.add(readVo);
					}
				}  
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
			return result;
		}
		public static ArrayList<ReadVo> getReadOtherList(Map<String, Object> param) throws Exception {
			ArrayList<ReadVo> result = new ArrayList<ReadVo>();
			try {
				DefMap(param);
				String from =HttpUtil.MapToParam(param) ;
				LogUtil.d(TAG, from);
				HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.ReadOtherSERVER, "", ""); 
				if (conn.getResponseCode() == 200) {
					InputStream in = conn.getInputStream();
					String resultJson = HttpUtil.inputStreamString(in);
					LogUtil.d(TAG, resultJson);
					JSONObject json = new JSONObject(resultJson);
					String code = json.getString("code");
					if("200".equals(code)){
						JSONArray jArr = (JSONArray) json.get("Booklist");
						result = getTList(jArr);
					}else{
						LogUtil.d(TAG, "server code : "+ code);
					}
				} else  {
					LogUtil.d(TAG, "code : "+conn.getResponseCode());
					throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
				}
			}  catch (Exception e) {
				e.printStackTrace();
				throw new EuException(e);
			}
			return result;
		}


}
