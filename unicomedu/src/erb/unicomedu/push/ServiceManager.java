/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package erb.unicomedu.push;

import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import erb.unicomedu.dao.LoadDao;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.PushCofigVo;

/**
 * This class is to manage the notificatin service and to load the
 * configuration.
 * 
 */
public final class ServiceManager {

	private static final String LOGTAG = LogUtil.makeLogTag(ServiceManager.class);

	private Context context;

	/**
	 * 轻量级的存储类
	 */
	private SharedPreferences sharedPrefs;

	private Properties props;

	private String version = "0.5.0";

	private String apiKey;

	private String xmppHost;

	private String xmppPort;

	private String callbackActivityPackageName;

	private String callbackActivityClassName;

	public ServiceManager(Context context) {
		this.context = context;

		if (context instanceof Activity) {
			LogUtil.i(LOGTAG, "Callback Activity...");
			Activity callbackActivity = (Activity) context;
			callbackActivityPackageName = callbackActivity.getPackageName();
			callbackActivityClassName = callbackActivity.getClass().getName();
		}

		// apiKey = getMetaDataValue("ANDROIDPN_API_KEY");
		// LogUtil.i(LOGTAG, "apiKey=" + apiKey);
		// // if (apiKey == null) {
		// // LogUtil.e(LOGTAG,
		// "Please set the androidpn api key in the manifest file.");
		// // throw new RuntimeException();
		// // }

		// 从网络回去 ip地址 做成动态的
		String cofig = "";
		try { 
			cofig = LoadDao.getPushCofig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!"".equals(cofig)) {
			PushCofigVo pcv = LoadDao.getCofigVo(cofig);
			apiKey = pcv.getApikey();
			xmppHost = pcv.getIp();
			xmppPort = pcv.getPort();
		} else {
			props = loadProperties();
			apiKey = props.getProperty("apiKey", "");
			xmppHost = props.getProperty("xmppHost", "113.105.153.221");
			xmppPort = props.getProperty("xmppPort", "5222");
		}
		LogUtil.i(LOGTAG, "apiKey=" + apiKey);
		LogUtil.i(LOGTAG, "xmppHost=" + xmppHost);
		LogUtil.i(LOGTAG, "xmppPort=" + xmppPort);

		//

		/**
		 * 把参数 保存 起来
		 */
		sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.API_KEY, apiKey);
		editor.putString(Constants.VERSION, version);
		editor.putString(Constants.XMPP_HOST, xmppHost);
		editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
		editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME,
				callbackActivityPackageName);
		editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME,
				callbackActivityClassName);
		editor.commit();
		// LogUtil.i(LOGTAG, "sharedPrefs=" + sharedPrefs.toString());
	}

	public void startService() {
		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Intent intent = NotificationService.getIntent();
				context.startService(intent);
			}
		});
		serviceThread.start();
	}

	public void stopService() {
		Intent intent = NotificationService.getIntent();
		context.stopService(intent);
	}

	// private String getMetaDataValue(String name, String def) {
	// String value = getMetaDataValue(name);
	// return (value == null) ? def : value;
	// }
	//
	// private String getMetaDataValue(String name) {
	// Object value = null;
	// PackageManager packageManager = context.getPackageManager();
	// ApplicationInfo applicationInfo;
	// try {
	// applicationInfo = packageManager.getApplicationInfo(context
	// .getPackageName(), 128);
	// if (applicationInfo != null && applicationInfo.metaData != null) {
	// value = applicationInfo.metaData.get(name);
	// }
	// } catch (NameNotFoundException e) {
	// throw new RuntimeException(
	// "Could not read the name in the manifest file.", e);
	// }
	// if (value == null) {
	// throw new RuntimeException("The name '" + name
	// + "' is not defined in the manifest file's meta data.");
	// }
	// return value.toString();
	// }

	private Properties loadProperties() {
		// InputStream in = null;
		// Properties props = null;
		// try {
		// in = getClass().getResourceAsStream(
		// "/org/androidpn/client/client.properties");
		// if (in != null) {
		// props = new Properties();
		// props.load(in);
		// } else {
		// LogUtil.e(LOGTAG, "Could not find the properties file.");
		// }
		// } catch (IOException e) {
		// LogUtil.e(LOGTAG, "Could not find the properties file.", e);
		// } finally {
		// if (in != null)
		// try {
		// in.close();
		// } catch (Throwable ignore) {
		// }
		// }
		// return props;

		Properties props = new Properties();
		try {
			int id = context.getResources().getIdentifier("androidpn", "raw",
					context.getPackageName());
			props.load(context.getResources().openRawResource(id));
		} catch (Exception e) {
			LogUtil.e(LOGTAG, "Could not find the properties file.", e);
			// e.printStackTrace();
		}
		return props;
	}

	// public String getVersion() {
	// return version;
	// }
	//
	// public String getApiKey() {
	// return apiKey;
	// }

	public void setNotificationIcon(int iconId) {
		Editor editor = sharedPrefs.edit();
		editor.putInt(Constants.NOTIFICATION_ICON, iconId);
		editor.commit();
	}

	// public void viewNotificationSettings() {
	// Intent intent = new Intent().setClass(context,
	// NotificationSettingsActivity.class);
	// context.startActivity(intent);
	// }

	public static void viewNotificationSettings(Context context) {
		Intent intent = new Intent().setClass(context,
				NotificationSettingsActivity.class);
		context.startActivity(intent);
	}

}
