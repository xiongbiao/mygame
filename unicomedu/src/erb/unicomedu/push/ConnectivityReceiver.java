package erb.unicomedu.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import erb.unicomedu.util.LogUtil;

/** 
 * A broadcast receiver to handle the changes in network connectiion states.
 *广播接收处理网络connectiion状态的变化。
 * @author  
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String LOGTAG = LogUtil.makeLogTag(ConnectivityReceiver.class);

    private NotificationService notificationService;

    public ConnectivityReceiver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(LOGTAG, "ConnectivityReceiver.onReceive()...");
        String action = intent.getAction();
        LogUtil.d(LOGTAG, "action=" + action);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            LogUtil.d(LOGTAG, "Network Type  = " + networkInfo.getTypeName());
            LogUtil.d(LOGTAG, "Network State = " + networkInfo.getState());
            if (networkInfo.isConnected()) {
                LogUtil.i(LOGTAG, "Network connected");
                notificationService.connect();
            }
        } else {
            LogUtil.e(LOGTAG, "Network unavailable");
            notificationService.disconnect();
        }
    }

}
