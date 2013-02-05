package erb.unicomedu.activity;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.util.SIMCardInfo;

public class MyApplication extends Application implements UncaughtExceptionHandler {
	private static final String TAG = "MyApplication";
	
	private Context context;
	// 系统默认的UncaughtException处理
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	// 存储已打开的Activity集合，以便退出APP时finish掉  */
	public static List<Activity> activityList = new ArrayList<Activity>();
	public static SIMCardInfo mSIMCard = null;
	
//	public static int  signNum = 0;
	
	
	/**
	 * 添加Activity到集合中
	 */
	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	@Override
	public void onTerminate() {
		LogUtil.d(TAG, "MyApplication 终止");
		super.onTerminate();
	}

	public void onCreate() {
		super.onCreate();
		
		context = getApplicationContext();
		// 获取系统默认的UncaughtException处理?
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置重写的uncaughtException为程序的默认处理?
		Thread.setDefaultUncaughtExceptionHandler(this);
		mSIMCard = new SIMCardInfo(getApplicationContext());
		LogUtil.d(TAG, "MyApplication 启动");
		
		if(activityList!=null&&activityList.size()>0){
			activityList.clear();
		}
	}
	
	

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处??
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				ex.printStackTrace();
				LogUtil.e(TAG, "sleep : ", ex);
				Thread.sleep(3000);	//等待3秒.
				//退出程序
				exitApp(0);
				
			} catch (InterruptedException e) {
				LogUtil.e(TAG, "Error : ", e);
			}
		}
	}

	/**
	 * @param 线程弹出自定义提示
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		// 使用Toast来显示异常信
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, "抱歉，程序异常错误，即将退出应用！", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		return true;
	}
	
	/**
	 * 退出程序
	 */
	public static void exitApp(int type) {
		// 关闭所有打开的Activity对象
		LogUtil.d(TAG, "关闭所有打开的Activity对象activityList.size[]="+activityList.size());
		for (Activity activity : activityList) {
			activity.finish();
		}
		activityList.clear();
		LogUtil.d(TAG, "杀死线程，退出应用。");
		//退出后台线程,以及销毁静态变量，退出应用。 问题 如果销毁 服务也被delete了 无法接收离线消息
		if(0 == type){
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		}		
	}
	
}