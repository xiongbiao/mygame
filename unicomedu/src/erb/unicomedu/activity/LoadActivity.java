package erb.unicomedu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import erb.unicomedu.push.ServiceManager;
import erb.unicomedu.util.DataAsyncTask;
import erb.unicomedu.util.Def;

public class LoadActivity extends Activity {
	DataAsyncTask dat ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.load);
		initData();
//		initService();
	}
	private void initService(){
		   ServiceManager serviceManager = new ServiceManager(this);
	        serviceManager.setNotificationIcon(R.drawable.icon);
	        serviceManager.startService();
	}
	private void initData(){
		try {
		    dat = new DataAsyncTask(this,false); 
			dat.execute("1");

		} catch (Exception e) {
			e.printStackTrace();
		}
//		Intent intent = new Intent(this, UnicomeduService.class);
//		startService(intent);
		
    }

	@Override
	protected void onStart() {
		super.onStart();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				openLoad();
			}
		}, 500);
	}
	
	private void openLoad () {
		Intent intent = new Intent(LoadActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				finish();
			}
	    	return super.onKeyDown(keyCode, event);
	    }
	
}
