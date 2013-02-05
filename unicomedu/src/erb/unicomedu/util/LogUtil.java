package erb.unicomedu.util;

import android.util.Log;

/***
 * log操作
 */
public class LogUtil {

	private static boolean isDebug = true;

	private static String TAG = "eru-1"; 
	
	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(TAG, "[" + tag + "]" + msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.d(TAG, "[" + tag + "]" + msg);
		}
	}
	
	public static void e(String tag, String msg, Throwable tr) {
		if (isDebug) {
			Log.d(TAG, "[" + tag + "]" + msg,tr);
		}
	}
	
	public static void v(String tag, String msg) {
		if (isDebug) {
			Log.v(TAG, "[" + tag + "]" + msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if (isDebug) {
			Log.w(TAG, "[" + tag + "]" + msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (isDebug) {
			Log.i(TAG, "[" + tag + "]" + msg);
		}
	}

	public static String makeLogTag(Class cls) {
        return "unicomedu_" + cls.getSimpleName();
    }
}
