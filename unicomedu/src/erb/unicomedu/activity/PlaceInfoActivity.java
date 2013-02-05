package erb.unicomedu.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.PlaceVo;

/**
 * 上课地点
 * @author xiongbiao
 *
 */
public class PlaceInfoActivity extends PublicActivity implements OnClickListener{
	private Button mEInfo;
	private Button mExqcx;
	private LinearLayout mLinfo;
	private LinearLayout mLExqcx;
	PlaceVo mPlaceVo;
	private TextView mSubjectName;
	private TextView mSubjectAddress;
	private TextView mPlaceInfo;
	private ImageView mSubjectAddressImag;
	private ImageButton mBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.place_info);
		mPlaceVo = (PlaceVo)getIntent().getSerializableExtra("place_info");
		init();
		initData();
	} 
	private void init(){
		mSubjectName = (TextView)findViewById(R.id.subject_name);
		mSubjectAddress = (TextView)findViewById(R.id.subject_address);
		mSubjectAddressImag = (ImageView)findViewById(R.id.img_map);
		mBack = (ImageButton)findViewById(R.id.place_search_back);
		mBack.setOnClickListener(this);
		mEInfo = (Button) findViewById(R.id.filter_info);
		mExqcx = (Button) findViewById(R.id.filter_xqcx);
		mEInfo.setOnClickListener(this);
		mExqcx.setOnClickListener(this);
		mLinfo = (LinearLayout) findViewById(R.id.edu_ll_info);
		mLExqcx = (LinearLayout) findViewById(R.id.edu_ll_xqjs);
		mPlaceInfo = (TextView) findViewById(R.id.tv_traffic); 
	}
    private void initData(){
    	if(mPlaceVo!=null){
			mSubjectName.setText(mPlaceVo.getSchoollocation());
			String addressStr="<font color='#6D6B6B'>地 &nbsp;&nbsp;&nbsp;&nbsp;  址&nbsp;  :&nbsp; </font> ";  
			CharSequence charSequence=Html.fromHtml(addressStr+mPlaceVo.getAddress()); 
			mSubjectAddress.setText(charSequence);
			LogUtil.d("PlaceInfoActivity", ""+mPlaceVo.getMapurl());
			if(mPlaceVo.getMapurl()!=null&&!"".equals(mPlaceVo.getMapurl())){
//				mSubjectAddressImag.setImageBitmap(ImageUtil.returnBitMap(mPlaceVo.getMapurl()));
				 Drawable cachedImage = new AsyncImageLoader().loadDrawable(mPlaceVo.getMapurl(), new ImageCallback() {
			            @Override
						public void imageLoaded(Drawable imageDrawable, String imageUrl) {
			                ImageView imageViewByTag = mSubjectAddressImag;
			                if (imageViewByTag != null&&imageDrawable!=null) {
			                     Bitmap bitmap = ((BitmapDrawable)imageDrawable).getBitmap();
			                    imageViewByTag.setImageBitmap( bitmap );
			                }
			            }
			        });
			    if(cachedImage!=null){
			    	Bitmap bitmap = ((BitmapDrawable)cachedImage).getBitmap();
			    	mSubjectAddressImag.setImageBitmap(  bitmap );
			    }else{
			    	mSubjectAddressImag.setImageResource(R.drawable.avatar_icon);
			    }
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.place_search_back:
			finish();
			break;	
		case R.id.filter_info:
			mEInfo.setBackgroundResource(R.drawable.button_4);
			mEInfo.setTextColor(getResources().getColor(R.color.text_color_orange));
			mExqcx.setBackgroundResource(R.drawable.button_4_uncheck);
			mExqcx.setTextColor(getResources().getColor(R.color.black));
			mLinfo.setVisibility(View.VISIBLE);
			mLExqcx.setVisibility(View.INVISIBLE);
			break;
		case R.id.filter_xqcx:
			mExqcx.setBackgroundResource(R.drawable.button_4);
			mEInfo.setBackgroundResource(R.drawable.button_4_uncheck);
			mEInfo.setTextColor(getResources().getColor(R.color.black));
			mExqcx.setBackgroundResource(R.drawable.button_4);
			mExqcx.setTextColor(getResources().getColor(R.color.text_color_orange));
			mLinfo.setVisibility(View.GONE);
			mLExqcx.setVisibility(View.VISIBLE);
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
