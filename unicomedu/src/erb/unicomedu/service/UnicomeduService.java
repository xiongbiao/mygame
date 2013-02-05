package erb.unicomedu.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import erb.unicomedu.util.DataAsyncTask;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;

public class UnicomeduService extends Service{
	@Override
	public void onCreate(){
		LogUtil.d("~onCreate", "start onCreate~~~");
		try {
			DataAsyncTask dat = new DataAsyncTask(this,false); 
			dat.execute("1");
			List<Object> data = dat.get();
			if(data!=null&&data.size()>0){
				String tlist =	data.get(0)+"";
				SharedPreferences settings = getSharedPreferences(Def.PREFS_NAME, 0); 
				SharedPreferences.Editor editor = settings.edit(); 
				editor.remove(Def.SP_NAV_NAME); 
				editor.putString(Def.SP_NAV_NAME, tlist); 
				editor.commit(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.d("~onCreate", "end onCreate~~~");
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		LogUtil.d("~onStart", "start onStart~~~");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
