package erb.unicomedu.dao;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.HttpUrls;
import erb.unicomedu.util.HttpUtil;
import erb.unicomedu.util.LogUtil;

public class BbsTakeAskDao extends PublicDao {
	private static String TAG = "BbsTakeAskDao";

	public static void pushAskList(Map<String, Object> param)throws Exception {
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.BbsTakeAskSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.i("TUDE==", "SUCCESS=================================" + resultJson);
//			} else {
//				LogUtil.d(TAG, "code : " + conn.getResponseCode());
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
