package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import erb.unicomedu.dao.BbsTakeReplyDao;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsCheckVo;

/**
 * 回复帖子
 *
 */
public class BbsReplyActivity extends PublicActivity {
	private final String TAG = "BbsReplyActivity";
	private Button button_TopReply;
	private Button button_TopBack;
	private EditText edReplyContext;
	private BbsCheckVo bbsVoObj;
	/** true-回复楼主，false-回复评论 */
	private boolean bReplyOwner = true;
	private Bundle mBundle;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_reply);
        bReplyOwner = true;
        
//        bbsVoObj = (BbsCheckVo)getIntent().getSerializableExtra(Def.BBS_REPLY_PL);
        mBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
        if(mBundle!=null){
        	 bbsVoObj = (BbsCheckVo)mBundle.getSerializable(Def.BBS_REPLY_PL);
        	 if(bbsVoObj != null) {	//回复评论
             	bReplyOwner = false;
             	LogUtil.d(TAG,"---回复评论----");
             }else{
            	 bbsVoObj = (BbsCheckVo)mBundle.getSerializable(Def.OBJ_bbs_ask);
            	 bReplyOwner = true;
             }
        }
//        if(bbsVoObj != null) {	//回复评论
//        	bbsVoObj = (BbsCheckVo)getIntent().getSerializableExtra(Def.OBJ);
//        	bReplyOwner = false;
//        	
//        	LogUtil.d(TAG,"---回复评论----");
//        }
//	    else {	//回复给楼主
//	    	mBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
//	    	if(mBundle!=null){
//	    		 bbsVoObj = (BbsCheckVo)mBundle.getSerializable(Def.OBJ);
//	    		 bReplyOwner = true;
//	    		 LogUtil.d(TAG,"---回复给楼主----A");
//	    	}
//	    	LogUtil.d(TAG,"---回复给楼主----B");
//    	}
        
        LogUtil.d(TAG, "bbsVoObj="+bbsVoObj);
    	
        edReplyContext = (EditText)findViewById(R.id.bbs_reply_context);
        button_TopReply = (Button)findViewById(R.id.right_btn);
        button_TopReply.setText(R.string.btn_right_reply);
        button_TopReply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
				Map<String, Object> param = new HashMap<String, Object>();
				if(userInfo==null){
					Toast.makeText(BbsReplyActivity.this, "注册后才可以提问", Toast.LENGTH_SHORT).show();
					String className = BbsReplyActivity.this.getClass().getName();
					Intent mIntent = new Intent(BbsReplyActivity.this,RegActivity.class);  
			        Bundle mBundle = new Bundle();  
			        mBundle.putString(Def.TO_CLASS_NAME_TAG, className);
			        mIntent.putExtra(Def.OBJ_Bundle,mBundle); //------------?
			        startActivity(mIntent); 
				}		
				else
				{
					String strRelpleText = edReplyContext.getText().toString().trim();
					if ("".equals(strRelpleText))
					{
						Toast.makeText(BbsReplyActivity.this, "请输入问题内容", Toast.LENGTH_SHORT).show();
					}
					else 
					{
						if(bReplyOwner) {//回复楼主
							param.put("askid", bbsVoObj.getAskid());
							param.put("replyid", "");
							param.put("replyidstr", "");
							param.put("title", bbsVoObj.getTitle());
							param.put("memberid", userInfo.getMemberid());
							param.put("context", strRelpleText);
							param.put("member", userInfo.getNickname());
						} else {//回复评论
							param.put("askid", bbsVoObj.getAskid());
							param.put("replyid", bbsVoObj.getReplyid());
							param.put("replyidstr", bbsVoObj.getReplyto());
							param.put("title", bbsVoObj.getTitle());
							param.put("memberid", userInfo.getMemberid());
							param.put("context", strRelpleText);
							param.put("member", userInfo.getNickname());
						}
						button_TopReply.setEnabled(false);
						BbsTakeReplyDao.pushAskList(param);
						finish();
					}
				}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
        
		button_TopBack = (Button) findViewById(R.id.back);
		button_TopBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
