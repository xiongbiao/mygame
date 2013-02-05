package erb.unicomedu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import erb.unicomedu.push.ServiceManager;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;

public class PushActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.d("PushActivity", "onCreate()...");

        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.push);

        // Settings
        Button okButton = (Button) findViewById(R.id.btn_settings);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View view) {
                ServiceManager.viewNotificationSettings(PushActivity.this);
            }
        });

        // Start the service
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.home_btn_msg);
        serviceManager.startService();
    }

}
