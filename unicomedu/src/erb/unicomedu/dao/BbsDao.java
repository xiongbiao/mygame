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
import erb.unicomedu.vo.BbsVo;

public class BbsDao extends PublicDao {
	private static String TAG = "BbsDao";

	public static ArrayList<BbsVo> getBbsList(Map<String, Object> param)throws Exception {
		ArrayList<BbsVo> result = new ArrayList<BbsVo>();
		try {

			DefMap(param);
			param.put("subjectid", "1");
			param.put("page", 0);
			param.put("size", 20);

			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.BbsSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("topiclist");
					JSONObject bbsItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							bbsItem = jArr.getJSONObject(i);
							BbsVo bbsVo = new BbsVo();
							String info = bbsItem.getString("info");
							String school = bbsItem.getString("school");
							String topicid = bbsItem.getString("topicid");
							int asknumber = bbsItem.getInt("asknumber");
							int replynumber = bbsItem.getInt("replynumber");
							String topic = bbsItem.getString("topic");
							String sid = bbsItem.getString("sid");
							String parentid = bbsItem.getString("parentid");
							String intentionid = bbsItem
									.getString("intentionid");

							bbsVo.setInfo(info);
							bbsVo.setSchool(school);
							bbsVo.setTopicid(topicid);
							bbsVo.setAsknumber(asknumber);
							bbsVo.setReplynumber(replynumber);
							bbsVo.setTopic(topic);
							bbsVo.setSid(sid);
							bbsVo.setParentid(parentid);
							bbsVo.setIntentionid(intentionid);
							result.add(bbsVo);
						}
					}
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

	
	public static String  addFavorites(Map<String, Object> param)throws Exception {
		String result = "";
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "addFavorites : "+from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.BbsAddFavoritesSERVER, "", "");
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

	public static ArrayList<BbsAskVo> getBbsFavoritesList(
			Map<String, Object> param) throws Exception{
		ArrayList<BbsAskVo> result = new ArrayList<BbsAskVo>();
		try {

			DefMap(param);
			param.put("page", 0);
			param.put("size", 20);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.BbsFavoritesSERVER, "", "");
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
}
