package erb.unicomedu.dao;

import java.net.HttpURLConnection;
import java.util.Map;

import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.HttpUrls;
import erb.unicomedu.util.HttpUtil;
import erb.unicomedu.util.LogUtil;

public class BbsTakeReplyDao extends PublicDao {
	private static String TAG = "BbsTakeReplyDao";

	public static void pushAskList(Map<String, Object> param)throws Exception {
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.BbsTakeReplySERVER, "", ""); 
			if(conn.getResponseCode() == 200) {
				LogUtil.i("TUDE==", "SUCCESS=================================");
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
	}

}
