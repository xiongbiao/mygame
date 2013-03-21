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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import erb.unicomedu.util.LogUtil;

/**
 * Service that continues to run in background and respond to the push 
 * notification events from the server. This should be registered as service
 * in AndroidManifest.xml. 
 * 
 */
public class NotificationService extends Service {

    private static final String LOGTAG = LogUtil.makeLogTag(NotificationService.class);

    public static final String SERVICE_NAME = "erb.unicomedu.push.NotificationService";

    private TelephonyManager telephonyManager;

    //    private WifiManager wifiManager;
    //
    //    private ConnectivityManager connectivityManager;

    private BroadcastReceiver notificationReceiver;

    private BroadcastReceiver connectivityReceiver;

    private PhoneStateListener phoneStateListener;

    private ExecutorService executorService;

    private TaskSubmitter taskSubmitter;

    private TaskTracker taskTracker;

    private XmppManager xmppManager;

    private SharedPreferences sharedPrefs;

    private String deviceId;

    public NotificationService() {
        notificationReceiver = new NotificationReceiver();
        connectivityReceiver = new ConnectivityReceiver(this);
        phoneStateListener = new PhoneStateChangeListener(this);
        executorService = Executors.newSingleThreadExecutor();
        taskSubmitter = new TaskSubmitter(this);
        taskTracker = new TaskTracker(this);
    }
    @Override
    public void onCreate() {
        LogUtil.d(LOGTAG, "onCreate()...");
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        // connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        // Get deviceId
        deviceId = telephonyManager.getDeviceId();
         LogUtil.d(LOGTAG, "deviceId=" + deviceId);
        String tel =  telephonyManager.getLine1Number();     //取出用户手机号码， 
        String imei = telephonyManager.getSimSerialNumber();  //取出IMEI， 
        String imsi = telephonyManager.getSubscriberId();     //取出IMSI， 
        
        
        
        LogUtil.d(LOGTAG, "tel=" + tel );
        LogUtil.d(LOGTAG, "imei=" + imei );
        LogUtil.d(LOGTAG, "imsi=" + imsi );
        
        Editor editor = sharedPrefs.edit();
        editor.putString(Constants.DEVICE_ID, deviceId);
        editor.commit();
        if (deviceId == null || deviceId.trim().length() == 0 || deviceId.matches("0+")) {
            if (sharedPrefs.contains("EMULATOR_DEVICE_ID")) {
                deviceId = sharedPrefs.getString(Constants.EMULATOR_DEVICE_ID,"");
            } else {
//                deviceId = (new StringBuilder("EMU")).append((new Random(System.currentTimeMillis())).nextLong()).toString();
                editor.putString(Constants.EMULATOR_DEVICE_ID, deviceId);
                editor.commit();
            }
        }
        LogUtil.d(LOGTAG, "deviceId=" + deviceId);
        xmppManager = new XmppManager(this);
        taskSubmitter.submit(new Runnable() {
            @Override
			public void run() {
                NotificationService.this.start();
            }
        });
    }

    @Override
    public void onStart(Intent intent, int startId) {
        LogUtil.d(LOGTAG, "onStart()...");
    }

    @Override
    public void onDestroy() {
        LogUtil.d(LOGTAG, "onDestroy()...");
        stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(LOGTAG, "onBind()...");
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        LogUtil.d(LOGTAG, "onRebind()...");
    }
    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.d(LOGTAG, "onUnbind()...");
        return true;
    }
    /**
     * 获得Intent
     * @return
     */
    public static Intent getIntent() {
        return new Intent(SERVICE_NAME);
    }
    public ExecutorService getExecutorService() {
        return executorService;
    }
    public TaskSubmitter getTaskSubmitter() {
        return taskSubmitter;
    }
    public TaskTracker getTaskTracker() {
        return taskTracker;
    }

    public XmppManager getXmppManager() {
        return xmppManager;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPrefs;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void connect() {
        LogUtil.d(LOGTAG, "connect()...");
        taskSubmitter.submit(new Runnable() {
            @Override
			public void run() {
                NotificationService.this.getXmppManager().connect();
            }
        });
    }

    public void disconnect() {
        LogUtil.d(LOGTAG, "disconnect()...");
        taskSubmitter.submit(new Runnable() {
            @Override
			public void run() {
                NotificationService.this.getXmppManager().disconnect();
            }
        });
    }

    private void registerNotificationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_SHOW_NOTIFICATION);
        filter.addAction(Constants.ACTION_NOTIFICATION_CLICKED);
        filter.addAction(Constants.ACTION_NOTIFICATION_CLEARED);
        registerReceiver(notificationReceiver, filter);
    }

    private void unregisterNotificationReceiver() {
        unregisterReceiver(notificationReceiver);
    }

    private void registerConnectivityReceiver() {
        LogUtil.d(LOGTAG, "registerConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        IntentFilter filter = new IntentFilter();
        // filter.addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);
    }

    private void unregisterConnectivityReceiver() {
        LogUtil.d(LOGTAG, "unregisterConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(connectivityReceiver);
    }

    private void start() {
        LogUtil.d(LOGTAG, "start()...");
        registerNotificationReceiver();
        registerConnectivityReceiver();
        // Intent intent = getIntent();
        // startService(intent);
        xmppManager.connect();
    }

    private void stop() {
        LogUtil.d(LOGTAG, "stop()...");
        unregisterNotificationReceiver();
        unregisterConnectivityReceiver();
        xmppManager.disconnect();
        executorService.shutdown();
    }

    /**
     * Class for summiting a new runnable task.
     */
    public class TaskSubmitter {

        final NotificationService notificationService;

        public TaskSubmitter(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        @SuppressWarnings("unchecked")
        public Future submit(Runnable task) {
            Future result = null;
            if (!notificationService.getExecutorService().isTerminated()
                    && !notificationService.getExecutorService().isShutdown()
                    && task != null) {
                result = notificationService.getExecutorService().submit(task);
            }
            return result;
        }

    }

    /**
     * Class for monitoring the running task count.
     */
    public class TaskTracker {

        final NotificationService notificationService;

        public int count;

        public TaskTracker(NotificationService notificationService) {
            this.notificationService = notificationService;
            this.count = 0;
        }

        public void increase() {
            synchronized (notificationService.getTaskTracker()) {
                notificationService.getTaskTracker().count++;
                LogUtil.d(LOGTAG, "Incremented task count to " + count);
            }
        }

        public void decrease() {
            synchronized (notificationService.getTaskTracker()) {
                notificationService.getTaskTracker().count--;
                LogUtil.d(LOGTAG, "Decremented task count to " + count);
            }
        }

    }

}
