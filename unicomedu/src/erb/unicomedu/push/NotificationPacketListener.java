package erb.unicomedu.push;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import android.content.Intent;
import android.os.Bundle;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.MsgVo;
import erb.unicomedu.vo.NotificationVo;

 
public class NotificationPacketListener implements PacketListener {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationPacketListener.class);

    private final XmppManager xmppManager;

    public NotificationPacketListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    @Override
    public void processPacket(Packet packet) {
        LogUtil.d(LOGTAG, "NotificationPacketListener.processPacket()...");
        LogUtil.d(LOGTAG, "packet.toXML()=" + packet.toXML());

        if (packet instanceof NotificationIQ) {
            NotificationIQ notification = (NotificationIQ) packet;

            if (notification.getChildElementXML().contains("androidpn:iq:notification")) {
                String notificationId = notification.getId();
                String notificationApiKey = notification.getApiKey();
                String notificationTitle = notification.getTitle();
                String notificationMessage = notification.getMessage();
                //                String notificationTicker = notification.getTicker();
                String notificationUri = notification.getUri();
                
                String notificationToClass = notification.getToClass();

                Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
//                intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
//                intent.putExtra(Constants.NOTIFICATION_API_KEY,
//                        notificationApiKey);
//                intent
//                        .putExtra(Constants.NOTIFICATION_TITLE,
//                                notificationTitle);
//                intent.putExtra(Constants.NOTIFICATION_MESSAGE,
//                        notificationMessage);
//                intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
                
                LogUtil.d(LOGTAG, "------------------====: " +notificationId);
                NotificationVo nv  = new NotificationVo();
                nv.setApiKey(notificationApiKey);
                nv.setId(notificationId);
                nv.setMsg(notificationMessage);
                nv.setOpenUrl(notificationUri);
                nv.setTitle(notificationTitle);
                nv.setToClass(notificationToClass);
                
              
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Def.OBJ,nv); 
                intent.putExtra(Def.OBJ_Bundle, mBundle);
                //                intent.setData(Uri.parse((new StringBuilder(
                //                        "notif://notification.androidpn.org/")).append(
                //                        notificationApiKey).append("/").append(
                //                        System.currentTimeMillis()).toString()));

                xmppManager.getContext().sendBroadcast(intent);
            }
        }

    }

}
