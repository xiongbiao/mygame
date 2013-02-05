package erb.unicomedu.util;

import android.content.Context;
import android.net.ConnectivityManager;


public class AndroidUtil {
	private static String TAG = LogUtil.makeLogTag(AndroidUtil.class);
	
	public static boolean checkInternetConnection(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if ((cm.getActiveNetworkInfo() != null)&& (cm.getActiveNetworkInfo().isAvailable())&& (cm.getActiveNetworkInfo().isConnected())) {
				return true;
			}
		  	LogUtil.e(TAG, "Internet Connection not found.");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
