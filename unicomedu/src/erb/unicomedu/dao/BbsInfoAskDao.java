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
import erb.unicomedu.vo.BbsAskVo;

public class BbsInfoAskDao extends PublicDao {
	private static String TAG = "BbsInfoAskDao";

	public static ArrayList<BbsAskVo> getAskList(Map<String, Object> param)throws Exception {
		ArrayList<BbsAskVo> result = new ArrayList<BbsAskVo>();
		try {
			DefMap(param);
			// param.put("typeid", 1);
			param.put("page", 0);
			param.put("size", 20);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.BbsAskSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("asklist");
					JSONObject MyAskItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							MyAskItem = jArr.getJSONObject(i);
							BbsAskVo ask = new BbsAskVo();
							String context = MyAskItem.getString("context");
							String member = MyAskItem.getString("member");
							String school = MyAskItem.getString("school");
							String topicid = MyAskItem.getString("topicid");
							int askid = MyAskItem.getInt("askid");
							String topic = MyAskItem.getString("topic");
							String memberid = MyAskItem.getString("memberid");
							String title = MyAskItem.getString("title");
							String pubtime = MyAskItem.getString("pubtime");
							String sid = MyAskItem.getString("sid");
							int replynumber = MyAskItem.getInt("replynumber");
							int readnumber = MyAskItem.getInt("readnumber");
							int isrecommend = MyAskItem.getInt("isrecommend");
							int isimgcontext = MyAskItem.getInt("isimgcontext");
//							int isarchived = MyAskItem.getInt("isarchived");
//							ask.setIsarchived(isarchived);
							ask.setContext(context);
							ask.setMember(member);
							ask.setSchool(school);
							ask.setTopicid(topicid);
							ask.setAskid(askid);
							ask.setTopic(topic);
							ask.setMemberid(memberid);
							ask.setTitle(title);
							ask.setPubtime(pubtime);
							ask.setSid(sid);
							ask.setReplynumber(replynumber);
							ask.setReadnumber(readnumber);
							ask.setIsrecommend(isrecommend);
							ask.setIsimgcontext(isimgcontext);
							result.add(ask);
						}
					}
					LogUtil.i("TUDE==", "result"+result.size());
				} else {
					LogUtil.d(TAG, "server code : " + code);
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

	
	public static ArrayList<BbsAskVo> getMyAskList(Map<String, Object> param)throws Exception {
		ArrayList<BbsAskVo> result = new ArrayList<BbsAskVo>();
		try {
			DefMap(param);
			param.put("page", 0);
			param.put("size", 20);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.BbsMyThemeSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				LogUtil.i("TUDE==", "IN");
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("myasklist");
					JSONObject MyAskItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							MyAskItem = jArr.getJSONObject(i);
							BbsAskVo myAsk = new BbsAskVo();
							String context = MyAskItem.getString("context");
							String member = MyAskItem.getString("member");
							String school = MyAskItem.getString("school");
							String topicid = MyAskItem.getString("topicid");
							int askid = MyAskItem.getInt("askid");
							String topic = MyAskItem.getString("topic");
							String memberid = MyAskItem.getString("memberid");
							String title = MyAskItem.getString("title");
							String pubtime = MyAskItem.getString("pubtime");
							String sid = MyAskItem.getString("sid");
							int replynumber = MyAskItem.getInt("replynumber");
							int readnumber = MyAskItem.getInt("readnumber");
							myAsk.setContext(context);
							myAsk.setMember(member);
							myAsk.setSchool(school);
							myAsk.setTopicid(topicid);
							myAsk.setAskid(askid);
							myAsk.setTopic(topic);
							myAsk.setMemberid(memberid);
							myAsk.setTitle(title);
							myAsk.setPubtime(pubtime);
							myAsk.setSid(sid);
							myAsk.setReplynumber(replynumber);
							myAsk.setReadnumber(readnumber);
							result.add(myAsk);
						}
					}
					LogUtil.i("TUDE==", "result"+result.size());
				} else {
					LogUtil.d(TAG, "server code : " + code);
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

	
	
	public static  BbsAskVo  getAskShow(Map<String, Object> param) throws Exception{
		 BbsAskVo  result = new   BbsAskVo();
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.BbsAskShowSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONObject MyAskItem   = (JSONObject) json.get("bbsinfo");
					int isarchived = json.getInt("isarchived");
							BbsAskVo ask = result;
							String context = MyAskItem.getString("context");
							String member = MyAskItem.getString("member");
							String school = MyAskItem.getString("school");
							String topicid = MyAskItem.getString("topicid");
							int askid = MyAskItem.getInt("askid");
							String topic = MyAskItem.getString("topic");
							String memberid = MyAskItem.getString("memberid");
							String title = MyAskItem.getString("title");
							String pubtime = MyAskItem.getString("pubtime");
							String sid = MyAskItem.getString("sid");
							int replynumber = MyAskItem.getInt("replynumber");
							int readnumber = MyAskItem.getInt("readnumber");
							int isrecommend = MyAskItem.getInt("isrecommend");
							int isimgcontext = MyAskItem.getInt("isimgcontext");
							ask.setIsarchived(isarchived);
							ask.setContext(context);
							ask.setMember(member);
							ask.setSchool(school);
							ask.setTopicid(topicid);
							ask.setAskid(askid);
							ask.setTopic(topic);
							ask.setMemberid(memberid);
							ask.setTitle(title);
							ask.setPubtime(pubtime);
							ask.setSid(sid);
							ask.setReplynumber(replynumber);
							ask.setReadnumber(readnumber);
							ask.setIsrecommend(isrecommend);
							ask.setIsimgcontext(isimgcontext);
				} else {
					LogUtil.d(TAG, "server code : " + code);
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
