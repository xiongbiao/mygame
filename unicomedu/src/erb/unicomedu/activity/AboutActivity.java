package erb.unicomedu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class AboutActivity extends PublicActivity implements OnClickListener {
	private ImageButton mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		init();
	}

	private void init() {
		mBack = (ImageButton) findViewById(R.id.obj_info_back);
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.obj_info_back:
			finish();
			break;
		}
	}
	 @Override
		public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		    	if(keyCode == KeyEvent.KEYCODE_BACK) {
		    		//按手机返回键则返回首页
		    		finish();
		    	}
		    	return super.onKeyDown(keyCode, event);
	    	 
	    };
}
