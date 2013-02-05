package erb.unicomedu.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.webkit.URLUtil;
import android.widget.Toast;
import erb.unicomedu.activity.HomeActivity;
import erb.unicomedu.activity.R;
import erb.unicomedu.dao.LoadDao;
import erb.unicomedu.vo.AppVo;


public class AppUpdateTask extends AsyncTask<String, Integer, String> {
	private final String TAG = "AppUpdate";
	
	private final String NEW_APP_FLAG = "new_app_version";
	
	private Activity activity = null;
	private String versionName = "0";
	private int fileSize = 0; // 更新文件的下载
	private int downLoadFileSize = 0; // 已下载的大小
	private String currentFilePath = "";
	private String strAppURL = "http://edusrv.100le.cn:8080/download/unicomedu.apk"; // APK下载地址
	private final String DataPath = "/data/data/erb.unicomedu.activity/files/";// 没有sd卡的情况的保存路径
	private String SdCardPath = Environment.getExternalStorageDirectory() + Def.DOWNLOAD_PATH;
	private boolean isrun = false; // 线程是否启动的标志
	private int nHandleID = -2; // handler的标志
	private Thread task; // 更新下载进度
	private File TempFile = null;

	/** 版本更新信息 */
	private static String strAppUpdateInfo="";
	private static String APKsize="";
	public static int isdownload=0;
    
	private Double currentVersion 		= 0.0;	// 已安装APK的版本号
	private Double downloadSaveVersion 	= 0.0; 	// 最新下载的版本号
	private Double newVersion 			= 0.0;	// 服务器端的版本号
	private String strNewVersion		="0.0";
    

	/**
	 * 构造函数
	 * @param activity 对象
	 */
	public AppUpdateTask(Activity activity) {
		this.activity = activity;
		getCurrentVersion();
	}

	// 是否更新版本提示
	public void showUpdateDialog() {
		String str[] = strAppUpdateInfo.split("@");
		int strsize = str.length;
		String info = "";
		for (int i = 0; i < strsize; i++) {
			info = info + str[i] + "\n";
		}
		@SuppressWarnings("unused")
		AlertDialog alert = new AlertDialog.Builder(this.activity)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("版本更新")
				.setMessage(
						"版本号：" + strNewVersion + "      大小：" + APKsize + "\n"
								+ info)
				.setCancelable(false)
				// 设置进度条是否可以按退回键取消
				.setPositiveButton("更新", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						HomeActivity.notification = new Notification(
								R.drawable.icon, activity
										.getString(R.string.app_name), System
										.currentTimeMillis());
						HomeActivity.mIntent = new Intent();
						PackageInfo info = null;

						// 有sd卡
						if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
						{
							TempFile = new File(SdCardPath + Def.APP_FILE_NAME);
							if (TempFile.exists()) {
								PackageManager pm = activity.getPackageManager();
								info = pm.getPackageArchiveInfo(SdCardPath + Def.APP_FILE_NAME,PackageManager.GET_ACTIVITIES);
							}
						} 
						else //无SD卡 
						{
							TempFile = new File(DataPath+Def.APP_FILE_NAME);
							if (TempFile.exists()) {
								PackageManager pm = activity.getPackageManager();
								info = pm.getPackageArchiveInfo(DataPath+Def.APP_FILE_NAME,PackageManager.GET_ACTIVITIES);
							}
						}
						
						//读取下载文件的版本号
						if (info != null)
							downloadSaveVersion = Double.parseDouble(info.versionName);
						
						//LogUtil.d(TAG, "downloadSaveVersion=" + downloadSaveVersion + ",newVersion=" + newVersion);
						
						if (downloadSaveVersion >= newVersion) {
							showDialog();
							isdownload = 2;
						}
						else {	//重新下载
							downloadTheFile(strAppURL);
							showWaitDialog();
							isdownload = 2;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						isdownload = 2;
					}
				}).show();
	}

	// 下载完成安装提示
	public void showDialog() {
		new AlertDialog.Builder(this.activity).setTitle("版本更新")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("程序已下载完毕，现在安装更新吗？").setCancelable(false)
				// 设置进度条是否可以按退回键取消
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setAction(android.content.Intent.ACTION_VIEW);
						String type = getMIMEType(TempFile);
						intent.setDataAndType(Uri.fromFile(TempFile), type);
						activity.startActivity(intent);
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}

	public void showWaitDialog() {
		Toast.makeText(activity, "后台下载中", Toast.LENGTH_SHORT).show();
	}

	/** 获取当前APP版本号 */
	public void getCurrentVersion() {
		try {
			PackageInfo info = activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0);
			this.versionName = info.versionName;
			Def.VERSION = ""+info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void downloadTheFile(final String strPath) {
		isrun = true;
		task = new Thread(new shownocit());
		task.start();
		try {
			if (strPath.equals(currentFilePath)) {
				doDownloadTheFile(strPath);
			}
			currentFilePath = strPath;
			HomeActivity.notification.flags = HomeActivity.notification.flags | Notification.FLAG_ONGOING_EVENT;
			HomeActivity.mPendingIntent = PendingIntent.getActivity(activity, 0, HomeActivity.mIntent, 1);
			HomeActivity.notification.setLatestEventInfo(activity, activity.getString(R.string.app_name), "已下载 0%", HomeActivity.mPendingIntent);
			HomeActivity.mNotificationManager.notify(0, HomeActivity.notification);
			Runnable r = new Runnable() {
				public void run() {
					try {
						doDownloadTheFile(strPath);
					} catch (Exception e) {
						LogUtil.e(TAG, "Runnable() Exception"+e.getMessage());
					}
				}
			};
			new Thread(r).start();
		} catch (Exception e) {
			LogUtil.e(TAG, "Exception"+e.getMessage());
		}
	}

	private void openFile(File f) {
		HomeActivity.mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		HomeActivity.mIntent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		HomeActivity.mIntent.setDataAndType(Uri.fromFile(f), type);
		HomeActivity.notification.flags = Notification.FLAG_INSISTENT;
		HomeActivity.mPendingIntent = PendingIntent.getActivity(activity, 0,
				HomeActivity.mIntent, 1);
		HomeActivity.notification.setLatestEventInfo(activity, activity.getString(R.string.app_name),
				"已下载完成，点击安装", HomeActivity.mPendingIntent);
		HomeActivity.mNotificationManager
				.notify(0, HomeActivity.notification);

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f), type);
		activity.startActivity(intent);
	}

	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result.contains(NEW_APP_FLAG)) {
			currentVersion = Double.parseDouble(versionName);
			newVersion = Double.parseDouble(strNewVersion);
			if (newVersion > currentVersion) {
				isdownload = 1;
			} else
				isdownload = 2;
		} 
	}

	@Override
	protected String doInBackground(String... params) {
		StringBuffer sb = new StringBuffer();
		try {
			//查询当前最新版本
			Map<String, Object> param = new HashMap<String, Object>();
			AppVo av = LoadDao.getApp(param);
			if(av!=null){
				APKsize = av.getFilesize();
				strAppUpdateInfo =av.getInfo();
				strNewVersion = av.getVersion();
				strAppURL = av.getAppurl();
				sb.append(NEW_APP_FLAG);
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb.append("");
		}
		return sb.toString();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (isrun) {
				if (!Thread.currentThread().isInterrupted()) {  // 定义一个Handler，用于处理下载线程与UI间通讯
					switch (msg.what) {
					case 0:
					case 1:
						int result = downLoadFileSize * 100 / fileSize;
						//LogUtil.d(TAG, "what" + msg.what + "    result" + result);
						HomeActivity.notification.flags = HomeActivity.notification.flags
								| Notification.FLAG_ONGOING_EVENT;
						HomeActivity.notification.setLatestEventInfo(activity,
								activity.getString(R.string.app_name), "已下载 " + result + "%",
								HomeActivity.mPendingIntent);
						HomeActivity.mNotificationManager.notify(0,
								HomeActivity.notification);
						break;
					case 2:
						HomeActivity.notification.flags = Notification.FLAG_INSISTENT;
						HomeActivity.notification.setLatestEventInfo(activity,
								activity.getString(R.string.app_name), "已下载完成，点击安装",
								HomeActivity.mPendingIntent);
						HomeActivity.mNotificationManager.notify(0, HomeActivity.notification);
						isrun = false;
						Thread.currentThread().interrupt();
						break;

					case -1:
						//String error = msg.getData().getString("error");
						break;
					}
				}
			}
			super.handleMessage(msg);

		}
	};

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}

	private void doDownloadTheFile(String strPath) throws Exception {
		if (!URLUtil.isNetworkUrl(strPath)) {
			LogUtil.d(TAG, "A.getDataSource() It's a wrong URL!");
		} else {
			URL myURL = new URL(strPath);
			URLConnection conn = myURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			this.fileSize = conn.getContentLength();// 根据响应获取文件大小
			if (is == null) {
				LogUtil.e(TAG, "stream is null");
				throw new RuntimeException("stream is null");
			}

			File myTempFile = null;
			FileOutputStream fos = null;
			
			// 有SD卡
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
			{
				try {
					File f = new File(SdCardPath);
					try {
						if (!f.isDirectory()) {
							f.mkdirs();
						}
					} catch (Exception e) {
						LogUtil.e(TAG, "mkdirs()Exception:"+e.getMessage());
					}
					
					myTempFile = new File(SdCardPath + Def.APP_FILE_NAME);
					myTempFile.createNewFile();
					TempFile = myTempFile;
					fos = new FileOutputStream(myTempFile);
				} catch (Exception e) {
					LogUtil.e(TAG, "有SD,Exception:"+e.getMessage());
				}
			} 
			else // 没有SD存
			{
				try
				{
					File f = new File(DataPath);
					try {
						if (!f.exists()) {
							f.mkdirs();
						}
					} catch (Exception e) {
						LogUtil.e(TAG, "无SD.mkdirs()Exception:"+e.getMessage());
					}
					
					myTempFile = new File(DataPath+Def.APP_FILE_NAME);
					myTempFile.createNewFile();
					TempFile = myTempFile;
					fos = activity.openFileOutput(Def.APP_FILE_NAME, 1);
				}
				catch (Exception e)
				{
					LogUtil.e(TAG, "无SD,Exception:"+e.getMessage());
				}
			}

			int numread = 0;
			byte buf[] = new byte[128];
			nHandleID = 0;

			do {
				// 循环读取
				numread = is.read(buf);
				if (numread == -1) {
					break;
				}
				fos.write(buf, 0, numread);
				downLoadFileSize += numread;
				nHandleID = 1;// 更新进度条
			} while (true);
			nHandleID = 2;// 通知下载完成

			String cmd = "chmod +x " + myTempFile.getPath();
			try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}

			openFile(myTempFile);
			try {
				fos.close();
				is.close();
			} catch (Exception e) {
				LogUtil.e(TAG, "openFile(myTempFile)->Exception:"+e.getMessage());
			}
		}
	}

	class shownocit implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				if (isrun) {
					sendMsg(nHandleID);
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

			}
		}
	}
}
