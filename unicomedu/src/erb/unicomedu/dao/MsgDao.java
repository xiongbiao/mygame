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
import erb.unicomedu.vo.MsgTypeVo;
import erb.unicomedu.vo.MsgVo;

public class MsgDao extends PublicDao {

	private static String TAG = "MsgDao";

	/**
	 * 
	 * @param param
	 * @return
	 */
	public static ArrayList<MsgTypeVo> getMsgList(Map<String, Object> param)
			throws Exception {
		ArrayList<MsgTypeVo> msgList = new ArrayList<MsgTypeVo>();
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.MsgListSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray appJson = json.getJSONArray("msgslist");
					for (int i = 0; i < appJson.length(); i++) {
						MsgTypeVo msjson = new MsgTypeVo();
						JSONObject jo = (JSONObject) appJson.get(i);
						String typeid = jo.getString("typeid");
						String msgTypeName = jo.getString("type");
						String msglist = jo.getString("msglist");
						if (msgList != null && !"".equals(msglist)) {
							JSONArray msg = new JSONArray(msglist);
							ArrayList<MsgVo> msgVoList = new ArrayList<MsgVo>();
							if (msg != null && msg.length() > 0) {
								for (int j = 0; j < msg.length(); j++) {
									MsgVo mv = new MsgVo();
									JSONObject jomsg = (JSONObject) msg.get(j);
									String msgType = jomsg.getString("type");
									String sid = jomsg.getString("sid");
									String title = jomsg.getString("title");
									String pubtime = jomsg.getString("pubtime");
									String msgTypeid = jomsg
											.getString("typeid");
									String iswav = jomsg.getString("iswav");
									String isshock = jomsg.getString("isshock");
									String msgid = jomsg.getString("msgid");
									String summary = jomsg.getString("summary");
									mv.setIsshock(isshock);
									mv.setIswav(iswav);
									mv.setMsgid(msgid);
									mv.setPubtime(pubtime);
									mv.setSid(sid);
									mv.setSummary(summary);
									mv.setTitle(title);
									mv.setType(msgType);
									mv.setTypeid(msgTypeid);
									msgVoList.add(mv);
								}
								msjson.setMsgList(msgVoList);
							}
						}
						msjson.setMsgTypeId(typeid);
						msjson.setMsgTypeName(msgTypeName);
						msgList.add(msjson);
					}
				} else {
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return msgList;
	}

	public static MsgVo getMsgString(Map<String, Object> param) throws Exception{
		MsgVo msgVo = new MsgVo();
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.PushInfoSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					MsgVo mv = new MsgVo();
					JSONObject jomsg = json.getJSONObject("MsgInfo");
					String msgType = jomsg.getString("type");
					String sid = jomsg.getString("sid");
					String title = jomsg.getString("title");
					String pubtime = jomsg.getString("pubtime");
					String msgTypeid = jomsg.getString("typeid");
					String iswav = jomsg.getString("iswav");
					String isshock = jomsg.getString("isshock");
					String msgid = jomsg.getString("msgid");
					String summary = jomsg.getString("summary");
					String linktype = jomsg.getString("linktype");
					if(linktype!=null&&!"".equals(linktype)&&!"0".equals(linktype) ){
						String linkinfo = json.getString("linkinfo").toString();
						mv.setLinkinfo(linkinfo);
					}
					mv.setLinktype(linktype);
					mv.setIsshock(isshock);
					mv.setIswav(iswav);
					mv.setMsgid(msgid);
					mv.setPubtime(pubtime);
					mv.setSid(sid);
					mv.setSummary(summary);
					mv.setTitle(title);
					mv.setType(msgType);
					mv.setTypeid(msgTypeid);
					msgVo = mv;
				} else {
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return msgVo;
	}
	
	/**
	 * 获取信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static MsgVo getMsg(Map<String, Object> param) throws Exception {
		MsgVo msgVo = new MsgVo();
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.MsgListSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					MsgVo mv = new MsgVo();
					JSONObject jomsg = json.getJSONObject("msgslist");
					String msgType = jomsg.getString("type");
					String sid = jomsg.getString("sid");
					String title = jomsg.getString("title");
					String pubtime = jomsg.getString("pubtime");
					String msgTypeid = jomsg.getString("typeid");
					String iswav = jomsg.getString("iswav");
					String isshock = jomsg.getString("isshock");
					String msgid = jomsg.getString("msgid");
					String summary = jomsg.getString("summary");
					mv.setIsshock(isshock);
					mv.setIswav(iswav);
					mv.setMsgid(msgid);
					mv.setPubtime(pubtime);
					mv.setSid(sid);
					mv.setSummary(summary);
					mv.setTitle(title);
					mv.setType(msgType);
					mv.setTypeid(msgTypeid);

				} else {
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return null;
	}

	/***
	 * 上报信息
	 * 
	 * @param param
	 * @return
	 */
	public static boolean pushMsgOk(Map<String, Object> param) throws Exception{
		boolean bRestule = false;
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.PushOKSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					bRestule = true;
				} else {
					LogUtil.d(TAG, " ------ code : " + code);
				}
			} else {
				LogUtil.d(TAG, " dds---code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return bRestule;
	}

}
