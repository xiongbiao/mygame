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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.NotificationVo;

/** 
 * Broadcast receiver that handles push notification messages from the server.
 * This should be registered as receiver in AndroidManifest.xml. 
 * 广播接收，处理从服务器推送通知消息。
 * 本应登记在AndroidManifest.xml接收机。
 * @author  
 */
public final class NotificationReceiver extends BroadcastReceiver {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationReceiver.class);

    //    private NotificationService notificationService;

    public NotificationReceiver() {
    }

    //    public NotificationReceiver(NotificationService notificationService) {
    //        this.notificationService = notificationService;
    //    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(LOGTAG, "NotificationReceiver.onReceive()...");
        String action = intent.getAction();
        LogUtil.d(LOGTAG, "action=" + action);

        if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
        	NotificationVo nv =null;
        	Bundle mRBundle = intent.getBundleExtra(Def.OBJ_Bundle);
            if(mRBundle!=null){
            	nv = (NotificationVo)mRBundle.getSerializable(Def.OBJ);
            } 
            Notifier notifier = new Notifier(context);
            notifier.notifyShow(nv);
            
            //----------------------数据开始-----------
            
//            String notificationId = intent.getStringExtra(Constants.NOTIFICATION_ID);
//            String notificationApiKey = intent.getStringExtra(Constants.NOTIFICATION_API_KEY);
//            String notificationToClass = intent.getStringExtra(Constants.NOTIFICATION_TO_CLASS);
//            String notificationTitle = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
//            String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
//            String notificationUri = intent.getStringExtra(Constants.NOTIFICATION_URI);
//
//            LogUtil.d(LOGTAG, "notificationId=" + notificationId);
//            LogUtil.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
//            LogUtil.d(LOGTAG, "notificationToClass=" + notificationToClass);
//            LogUtil.d(LOGTAG, "notificationTitle=" + notificationTitle);
//            LogUtil.d(LOGTAG, "notificationMessage=" + notificationMessage);
//            LogUtil.d(LOGTAG, "notificationUri=" + notificationUri);

//            Notifier notifier = new Notifier(context);
//            notifier.notify(notificationId, notificationApiKey,notificationTitle, notificationMessage, notificationUri);
            //----------------------数据end-----------
        }

        //        } else if (Constants.ACTION_NOTIFICATION_CLICKED.equals(action)) {
        //            String notificationId = intent
        //                    .getStringExtra(Constants.NOTIFICATION_ID);
        //            String notificationApiKey = intent
        //                    .getStringExtra(Constants.NOTIFICATION_API_KEY);
        //            String notificationTitle = intent
        //                    .getStringExtra(Constants.NOTIFICATION_TITLE);
        //            String notificationMessage = intent
        //                    .getStringExtra(Constants.NOTIFICATION_MESSAGE);
        //            String notificationUri = intent
        //                    .getStringExtra(Constants.NOTIFICATION_URI);
        //
        //            LogUtil.e(LOGTAG, "notificationId=" + notificationId);
        //            LogUtil.e(LOGTAG, "notificationApiKey=" + notificationApiKey);
        //            LogUtil.e(LOGTAG, "notificationTitle=" + notificationTitle);
        //            LogUtil.e(LOGTAG, "notificationMessage=" + notificationMessage);
        //            LogUtil.e(LOGTAG, "notificationUri=" + notificationUri);
        //
        //            Intent detailsIntent = new Intent();
        //            detailsIntent.setClass(context, NotificationDetailsActivity.class);
        //            detailsIntent.putExtras(intent.getExtras());
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_ID, notificationId);
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_API_KEY, notificationApiKey);
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_TITLE, notificationTitle);
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_MESSAGE, notificationMessage);
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
        //            detailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //            detailsIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        //
        //            try {
        //                context.startActivity(detailsIntent);
        //            } catch (ActivityNotFoundException e) {
        //                Toast toast = Toast.makeText(context,
        //                        "No app found to handle this request",
        //                        Toast.LENGTH_LONG);
        //                toast.show();
        //            }
        //
        //        } else if (Constants.ACTION_NOTIFICATION_CLEARED.equals(action)) {
        //            //
        //        }

    }

}
