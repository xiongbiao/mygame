package erb.unicomedu.util;

import android.util.Log;

/***
 * log操作
 */
public class LogUtil {

	private static boolean isDebug = true;

	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String tag, String msg, Throwable tr) {
		if (isDebug) {
			Log.e(tag, msg,tr);
		}
	}
	
	public static void v(String tag, String msg) {
		if (isDebug) {
			Log.v(tag, msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if (isDebug) {
			Log.w(tag, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}

	public static String makeLogTag(Class cls) {
        return "unicomedu_" + cls.getSimpleName();
    }
}
