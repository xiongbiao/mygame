package erb.unicomedu.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import erb.unicomedu.util.AppUpdateTask;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;

public class HomeActivity  extends PublicActivity  implements OnClickListener, android.content.DialogInterface.OnClickListener{
	private String TAG = "HomeActivity";
	
	/** 飞的位置 */
	private static final int[][] POSITION= {
		{0, 0},
		{0, 0},
		{0, 0},
		{0, 0},
		{0, 0},
		{0, 0},
		{0, 0},
		{0, 0},
		{0, 0},
		{0, 0},
	};
	
	/** ImageView资源ID */
	private static final int[] RESID = {
		R.drawable.no,
		R.drawable.personal_zone,
		R.drawable.school_area,
		R.drawable.online_test,
		R.drawable.online_read,
		R.drawable.course_introduce,
		R.drawable.online_video,
		R.drawable.teacher_introduce,
		R.drawable.question_and_answer,
		R.drawable.collection,
	};
	
	/** 要获取屏幕的宽和高等参数，首先需要声明一个DisplayMetrics对象，屏幕的宽高等属性存放在这个对象中 */
	private static DisplayMetrics DM;
	
	/** 当前的点击位置对应的UI界面上的实际x，y坐标值（不含状态栏） */
	private int eventX,eventY;
	
	/** 中间过渡变量，用于动态加入ImageView */
	private MImageView imageView;
	/** 屏幕实际显示的ImageView */
	private List<MImageView> imageViews = new ArrayList<MImageView>();
	/** 用于参考的ImageView，从XML文件中得到，显示状态visibility="invisible" */
	private List<ImageView> refImageView = new ArrayList<ImageView>();
	/** 平移Animation */
	private Animation translateAnimation;
	/** 伸缩Animation */
	private Animation scaleAnimation;
	/** 主Layout */
	private static RelativeLayout layout;
	/** 主LayoutInflater */
	private LayoutInflater inflater;
	/** 控件布局参数 */
	private RelativeLayout.LayoutParams layoutParams;
	/** 底部菜单 */
	private ImageView mImgBtnUnicom;
	private ImageView mImgBtnSetting;
	private ImageView mImgBtnMsg;
	private ImageView mImgBtnCheckIn;
	
	/** 底部弹出菜单 */
	private RelativeLayout Linear_MenuView;
	/** true为底部菜单处于弹出状态，false为未弹出 */
	private boolean isMenuState = false;
	/** 参考Layout,从XML文件中得到 */
	private RelativeLayout refLayout;
	/** 主handler,用于接收跳转主线程执行指定实例化函数消息 */
	private static Handler mHandler;
	private final static int msgUI = 201;
	private final int msgUpdate = 202;	//版本更新消息ID
	private boolean isRun=true;
	private boolean bShowUpdateDlg = false;
	private	AppUpdateTask appUpdateTask;
	private Thread mDownloadThread;
	public static NotificationManager mNotificationManager;
	public static Notification notification = new Notification();
	public static PendingIntent mPendingIntent;
	public static Intent mIntent;
	
	//-------begin 2012年1月27日 22:24:51--------
    private String mLt = "http://www.10010.com";
    private String mRes = "http://res.mall.10010.com/mall/front/msp/wopai/preSale.html";
    private String mWo17 = "http://17.wo.com.cn";
    private String mWo = "http://www.wo.com.cn";
	//--------end  2012年1月27日 22:25:01--------
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		DM = new DisplayMetrics();
		/** 获取窗口管理器,获取当前的窗口,调用getDefaultDisplay()后，其将关于屏幕的一些信息写进DM对象中,最后通过getMetrics(DM)获取 */
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		
		//获取LayoutInflater实例 
        inflater  = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE); 
        layout = (RelativeLayout)inflater.inflate(R.layout.home, null, false);
        refLayout = (RelativeLayout)layout.findViewById(R.id.r2);
        for(int i=0; i<refLayout.getChildCount(); i++) {
        	refImageView.add((ImageView)refLayout.getChildAt(i));
        }
        layout.setBackgroundResource(R.drawable.homepage_bg);
        
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case msgUI:
					init();
					break;
				case msgUpdate:
					if (isRun) {
						isRun = false;
						if (true == bShowUpdateDlg) {
							AppUpdateTask.isdownload = 2;
							appUpdateTask.showUpdateDialog();
							bShowUpdateDlg = false;
						}
					}
					break;
				}
				super.handleMessage(msg);
			}
		};
        
        //监听是否能得到控件的实际坐标值了，能就为记录所有ImageView的坐标并向hanler发消息
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if(refImageView.get(9).getLeft() != 0) {
						for(int j=refImageView.size()-1; j>=0; j--) {
							POSITION[j][0] = refImageView.get(j).getLeft();
							POSITION[j][1] = refImageView.get(j).getTop();
						}
						break;
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
					}
				}
				mHandler.sendEmptyMessage(msgUI);
			}
		}).start();
        
        setContentView(layout);
        
		//版本更新
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
		if (CheckNetwork()) {
			appUpdateTask = new AppUpdateTask(HomeActivity.this);
			appUpdateTask.execute();
			mDownloadThread = new Thread(new download());
			mDownloadThread.start();
		}
    }
    
    /** 实例化部分显示界面 */
	private void init() {
		refLayout.removeAllViews();
		
		setScalAnimation();
		int j=9;
    	for(int i=0; i<10; i++) {
    		imageView = new MImageView(HomeActivity.this, RESID[j], i);
            layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            translateAnimation = getAnimationTranslate(DM.widthPixels/2, DM.heightPixels, POSITION[j][0], POSITION[j][1], 0, 200+i*50, imageView);
            imageView.startAnimation(translateAnimation);
            imageView.setLayoutParams(layoutParams);
            imageView.setId(i-100);
            layout.addView(imageView,i);
//            imageView.setOnClickListener(this);
            imageViews.add((MImageView)layout.getChildAt(i));
            j--;
    	}
    	
    	mImgBtnUnicom = (ImageView)layout.findViewById(R.id.unicom_view);
    	mImgBtnUnicom.setOnClickListener(this);
    	mImgBtnSetting = (ImageView)layout.findViewById(R.id.setting_view);
    	mImgBtnSetting.setOnClickListener(this);
    	mImgBtnMsg = (ImageView)layout.findViewById(R.id.message_view);
    	mImgBtnMsg.setOnClickListener(this);
    	mImgBtnCheckIn = (ImageView)layout.findViewById(R.id.checkin_view);
    	mImgBtnCheckIn.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		bShowUpdateDlg = true;
		isRun=true;
	}
	
	protected void onPause() {
		bShowUpdateDlg = false;
		super.onPause();
	}
	
	protected void onStop() {
		isRun=false;
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		mHandler.removeCallbacks(mDownloadThread);
		super.onDestroy();
	}
	
//    private OnTouchListener bottomOnTouchListener = new OnTouchListener() {
//    	
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			switch (v.getId()) {
//			case R.id.unicom_view:
//				setMenuLayout();
//				layout.addView(Linear_MenuView);
//				isMenuState = true;
//				break;	
//			default:
//				break;
//			}
//			return false;
//		}
//	};

	/***
	 * 实例化弹出菜单的Layout
	 */
	private void setMenuLayout() {
    	Linear_MenuView = (RelativeLayout)inflater.inflate(R.layout.home_menu, null, false);
        layoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        Linear_MenuView.setLayoutParams(layoutParams);
        
      //底部
//		mBottomGallery = (Gallery) Linear_MenuView.findViewById(R.id.mian_bottom_ad_gallery);
//        List<RecommendVo> mData  = null;
//        try {
//			SharedPreferences settings =  getSharedPreferences(Def.PREFS_NAME, 0);
//			String rjson = settings.getString(Def.SP_Recommend_NAME, "");
//			if (rjson != null && !"".equals(rjson)) {
//				JSONArray userJson = new JSONArray(rjson);
//				mData = LoginDao.getArrayToRecommend(userJson);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        
//		mBottomGallery.setAdapter(new AdAdapter(this,R.layout.ad_item,mData ));
//		mBottomGallery.setOnItemClickListener(new AdItemClickListener());
//		alignGalleryToLeft( Linear_MenuView,mBottomGallery);
	}
	
//	private void alignGalleryToLeft(View parentView, Gallery gallery) {
//		int galleryWidth = parentView.getWidth();// 得到Parent控件的宽度
//		// 在这边我们必须先从资源尺寸中得到子控件的宽度跟间距，因为:
//		// 1. 在运行时，我们无法得到间距(因为Gallery这个类，没有这样的权限)
//		// 2.有可能在运行得宽度的时候，item资源还没有准备好。
//
//		int itemWidth = 100;
//		int spacing = 10;
//		// 那么这个偏移量是多少，我们将左边的gallery，以模拟的第一项的左对齐
//		int offset;
//		if (galleryWidth <= itemWidth) {
//			offset = galleryWidth / 2 - itemWidth / 2 - spacing;
//		} else {
//			offset = galleryWidth - itemWidth - 2 * spacing;
//		}
//		offset = 0;
//
//		// 现在就可以根据更新的布局参数设置做对其了。
//		MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
//		mlp.setMargins(-offset, mlp.topMargin, mlp.rightMargin,
//				mlp.bottomMargin);
//	}

	 class AdItemClickListener implements OnItemClickListener{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LogUtil.d("AD : ", ""+arg2);
			}
	    }
	
	/**
	 * 实例化伸缩Animation
	 */
	private void setScalAnimation() {
		scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scal);
		scaleAnimation.setFillAfter(true);
		scaleAnimation.setFillEnabled(true);
	}

	/***
	 * 获取平移Animation实例
	 * @param from_x
	 * @param from_y
	 * @param to_x
	 * @param to_y
	 * @param repeat 	
	 * 				重复次数 0为1次，-1为循环， 其大于0的数字表示多次
	 * @param time 
	 * 				平移所需时间
	 * @param imageView 
	 * 				平移的载体对象
	 * @return
	 */
	public Animation getAnimationTranslate(float from_x, float from_y, final float to_x, final float to_y, int repeat, long time, final MImageView imageView) {
    	Animation animation = new TranslateAnimation(from_x, to_x, from_y, to_y);
    	animation.setDuration(time);
    	animation.setRepeatCount(repeat);
    	animation.setFillAfter(true);
    	animation.setFillEnabled(true);
    	animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				imageView.x = (int)to_x;
				imageView.y = (int)to_y;
				imageView.clearAnimation();
				imageView.rect = new Rect((int)to_x, (int)to_y+10, (int)to_x+imageView.w+20, (int)to_y+imageView.h+10);
				imageView.layout((int)to_x, (int)to_y, (int)to_x+imageView.w, (int)to_y+imageView.h);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
				params.leftMargin = imageView.x;
				params.topMargin = imageView.y;
				layout.postInvalidate();
			}
		});
    	
    	return animation;
    }
    /***
     * 自定义View,继承于View类
     * @author Tude
     *
     */
    class MImageView extends View {

    	private Bitmap bitmap;
    	private Paint paint = new Paint();
    	/** 分别代表view的宽度、高度、和view当前所在x坐标y坐标 */
    	public int w, h, x, y;
    	/** view的矩形区域 */
    	private Rect rect;
    	Context context;
    	/** 是否处于放大状态 */
    	public boolean beBig = false;
    	/** 图片的资源id */
    	public int resId;
    	/** View的加入位置 */
    	public int layerId;
    	
		public MImageView(Context context, int resId, int layerId) {
			super(context);
			this.context = context;
			this.resId = resId;
			this.layerId = layerId;
			setBitmap(resId);
			w = bitmap.getWidth();
			h = bitmap.getHeight();
		}
		/***
		 * 实例化图片对象
		 * @param id
		 */
		public void setBitmap(int id) {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig=Bitmap.Config.RGB_565;//表示16位位图 565代表对应三原色占的位数
			opt.inInputShareable=true;
			opt.inPurgeable=true;//设置图片可以被回收
			InputStream is = context.getResources().openRawResource(id);
			bitmap = BitmapFactory.decodeStream(is, null, opt);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawBitmap(bitmap, 0, 0, paint);
		}
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
		//获取系统状态栏高度
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

		//纠正x,y坐标值
		eventX = (int)event.getX();
    	eventY = (int)event.getY()-frame.top;
    	
    	//循环所有view判断触点是否在view的rect区域内
		for(int i=0; i<imageViews.size(); i++) {
    		if(imageViews.get(i).rect != null) {
        		if(imageViews.get(i).rect.contains(eventX, eventY)) {
        			//是否已经处于放大状态，不是的话就放大
            		if(!imageViews.get(i).beBig) {
	            		MImageView view = imageViews.get(i);
	            		layout.removeView(imageViews.get(i));
	            		layout.addView(view);
	            		view.startAnimation(scaleAnimation);
	            		layout.postInvalidate();
	            		view.beBig = true;
	            		imageViews.set(i, view);
            		}
            	} else {
            		imageViews.get(i).beBig = false;
            		imageViews.get(i).clearAnimation();
            		layout.postInvalidate();
            	}
        	}
    	}
		//循环所有View判断触摸抬起时所在点在哪个View的rect区域内，是的话就定位选中了指定控件
    	if(event.getAction() == MotionEvent.ACTION_UP) {
    		boolean tag = false;
    		for(int j=0; j<imageViews.size(); j++) {
    			imageViews.get(j).beBig = false;
    			imageViews.get(j).clearAnimation();
    			layout.postInvalidate();
    			
        		if(imageViews.get(j).rect != null) {
        			
            		if(imageViews.get(j).rect.contains(eventX, eventY)) {
            			String fromClassName =  this.getClass().getName();
            			String className = "";
            			boolean isOK = false;
            			switch (j) {
            			case 0:	//收藏
            				className =  new FavoriteActivity().getClass().getName();
              			    isOK = isModel(Def.MODEl_FA_List, className,fromClassName,null);
              				if(isOK){
              					startActivity(new Intent(HomeActivity.this,FavoriteActivity.class));
              				}
            				break;
            			case 1:	//问答区
            				 className =  new BbsActivity().getClass().getName();
             			    isOK = isModel(Def.MODEl_BBS_list, className,fromClassName,null);
             				if(isOK){
            				     startActivity(new Intent(HomeActivity.this,BbsActivity.class));
            				}
            				break;
            			case 2:	//名师
            			    className =  new TeacherActivity().getClass().getName();
            			    isOK = isModel(Def.MODEl_TE_LIST, className,fromClassName,null);
            				if(isOK){
            					startActivity(new Intent(HomeActivity.this,TeacherActivity.class));
            				}
            				break;
            			case 3:	//视频
            				className =  new VideoActivity().getClass().getName();
            			    isOK = isModel(Def.MODEl_VI_LIST, className,fromClassName,null);
            			    if(isOK){
            				    startActivity(new Intent(HomeActivity.this,VideoActivity.class));
            				}
            				break;
            			case 4:	//课程
            				className =  new SubjectActivity().getClass().getName();
            				 isOK = isModel(Def.MODEl_SU_LIST, className,fromClassName,null);
            				if(isOK){
            					startActivity(new Intent(HomeActivity.this,SubjectActivity.class));
            				}
            				break;
            			case 5:	//阅读
            				className =  new ReadActivity().getClass().getName();
              			    isOK = isModel(Def.MODEl_RE_LIST, className,fromClassName,null);
              				if(isOK){
              					startActivity(new Intent(HomeActivity.this,ReadActivity.class));
              				}
            				break;
            			case 6:	//在线测试
            				className =  new ExamActivity().getClass().getName();
              			    isOK = isModel(Def.MODEl_EX_LIST, className,fromClassName,null);
              				if(isOK){
              					startActivity(new Intent(HomeActivity.this,ExamActivity.class));
              				}
            				break;
            			case 7:	//校区
            				startActivity(new Intent(HomeActivity.this,RegionalActivity.class));
            				break;
            			case 8:	//个人专区
            				className =  new UserInfoActivity().getClass().getName();
              			    isOK = isModel(Def.MODEl_USER_INFO, className,fromClassName,null);
              				if(isOK){
            				      startActivity(new Intent(HomeActivity.this,UserInfoActivity.class));
              				}
            				break;            				
            			case 9:	//游戏
//            				Toast.makeText(HomeActivity.this, "游戏开发中，请稍后...", Toast.LENGTH_SHORT).show();
//            				 startActivity(new Intent(this,M.class));
//            				startActivity(new Intent(this,RegionalActivity.class));
            				break;
            			default:
            				break;
            			}
            			//Toast.makeText(HomeActivity.this, "ImageView" + j, Toast.LENGTH_SHORT).show();
            		} else {
            			tag = true;
            		}
            	}
        	}
    		//处理当底部菜单弹出状态下，点击屏幕除底部菜单之外的位置时退出底部弹出菜单
    		if(tag && isMenuState && eventY<DM.heightPixels-Linear_MenuView.getHeight()) {
    			isMenuState = false;
    			layout.removeView(Linear_MenuView);
    		}
    	}
    	return true;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		if(isMenuState) {	//弹出联通推荐业务栏
    			layout.removeView(Linear_MenuView);
    			isMenuState = false;
    		} else {
    			exitConirmDialog();
    		}
    	}
    	
    	return true;
    }
    
    LinearLayout iv  ;
    LinearLayout iv2  ;
    LinearLayout iv3  ;
    LinearLayout iv4  ;
    
	@Override
	public void onClick(View v) {
		String fromClassName =  this.getClass().getName();
		String className = "";
		boolean isOK = false;
		switch (v.getId()) {
		case R.id.layear1:
			Intent viewIntent = new 
		    Intent("android.intent.action.VIEW",Uri.parse(mLt));
			startActivity(viewIntent);
			break;
		case R.id.layear2:
			Intent viewIntent2 = new 
		    Intent("android.intent.action.VIEW",Uri.parse(mRes));
			startActivity(viewIntent2);
			break;
		case R.id.layear3:
			Intent viewIntent3 = new   Intent("android.intent.action.VIEW",Uri.parse(mWo17));
			startActivity(viewIntent3);
			break;
		case R.id.layear4:
			Intent viewIntent4 = new  Intent("android.intent.action.VIEW",Uri.parse(mWo));
			startActivity(viewIntent4);
			break;
		case R.id.unicom_view:
			setMenuLayout();
			layout.addView(Linear_MenuView);
			iv = (LinearLayout) Linear_MenuView.findViewById(R.id.layear1);
			iv.setOnClickListener(this);
			iv2 = (LinearLayout) Linear_MenuView.findViewById(R.id.layear2);
			iv2.setOnClickListener(this);
			iv3 = (LinearLayout) Linear_MenuView.findViewById(R.id.layear3);
			iv3.setOnClickListener(this);
			iv4 = (LinearLayout) Linear_MenuView.findViewById(R.id.layear4);
			iv4.setOnClickListener(this);
			isMenuState = true;
			break;	
		case R.id.setting_view:
			startActivity(new Intent(HomeActivity.this,SettingActivity.class));
			break;
		case R.id.message_view:
//			Toast.makeText(HomeActivity.this, "系统没有消息", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(HomeActivity.this,MsgActivity.class));
			
			break;
		case R.id.checkin_view:
			className =  new SignActivity().getClass().getName();
			 isOK = isModel(Def.MODEl_SIGN, className,fromClassName,null);
			if(isOK){
				startActivity(new Intent(HomeActivity.this,SignActivity.class));
			}
//			1101 
//			Toast.makeText(HomeActivity.this, "签到吗？嗯，很快就有了，稍等...", Toast.LENGTH_SHORT).show();
			break;			
		default:
			break;
		}
	}
	
	/**
	 * 检查是否可以联网
	 */
	private boolean CheckNetwork() {
		boolean bReturn = false;
		try {
			ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cwjManager.getActiveNetworkInfo() != null) {
				bReturn = cwjManager.getActiveNetworkInfo().isAvailable();
			}

			// 没有通信网络时，提示“网络信号不好或者没有网络”
			if (!bReturn) {
				new AlertDialog.Builder(this)
						.setTitle("提示")
						.setMessage("当前网络连接不正常，请检查！")
						.setCancelable(false)
						.setPositiveButton("设置网络",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										//以下这段代码不支持Android4.0+
//										Intent mIntent = new Intent("/");
//										ComponentName comp = new ComponentName(
//												"com.android.settings",
//												"com.android.settings.WirelessSettings");
//										mIntent.setComponent(comp);
//										mIntent.setAction("android.intent.action.VIEW");
//										startActivity(mIntent);
										startActivity(new Intent(Settings.ACTION_SETTINGS));
									}
								})
						.setNegativeButton("退出",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Toast.makeText(HomeActivity.this,
												"网络连接失败，应用将无法正常使用！",
												Toast.LENGTH_SHORT).show();
									}
								}).show();

			}
		} catch (Exception e) {
		}
		return bReturn;
	}
	
    /** 退出程序确认 */
	public void exitConirmDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("退出提示");
		builder.setMessage("确定要退出程序？");
		builder.setPositiveButton("确认", this);
		builder.setNegativeButton("取消", this);
		builder.create().show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which == DialogInterface.BUTTON_POSITIVE) {
			//退出程序，释放资源
			MyApplication.exitApp(0);
			
			
//			/*
//			 * BUG：当某个Activity运行中造成程序崩溃时，Activity关闭不彻底，下次进入时会反复退出提示。
//			 */
//			//方法一、用广播的方式进行退出
//			Intent intentBC = new Intent();
//			intentBC.setAction(Def.APP_EXIT_BROADCAST_MSG); // 退出动作
//			sendBroadcast(intentBC);// 发送广播
//			LogUtil.d(TAG,"Send Broadcast Msg-->"+Def.APP_EXIT_BROADCAST_MSG);
//			
//			//方法二、强制退出本App   -------------------BEGIN--------------------------------
//			//得到一个ActivityManager（这个Manager顾名思意就是管理Activity的）
//			ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//			//获取任务数
//			List<RunningTaskInfo> taskInfoList = manager.getRunningTasks(100);
//		    //topActivity表示当前正在运行的Activity，baseActivity表示系统后台有此进程在运行
//		    final String  MY_PKG_NAME = this.getPackageName();
//		    for (int i = 0; i < taskInfoList.size(); i++) {
//		    	LogUtil.i(TAG,"topActivity="+taskInfoList.get(i).topActivity.getPackageName() + "---和---baseActivity.getPackageName()="+taskInfoList.get(i).baseActivity.getPackageName());
//		    	
//		    	if (taskInfoList.get(i).topActivity.getPackageName().equals(MY_PKG_NAME) || taskInfoList.get(i).baseActivity.getPackageName().equals(MY_PKG_NAME)) {
//		    		//找到当前运行APP
//		    		if ( (i+1)<  taskInfoList.size() )
//		    		{
//		    			ComponentName toComponetActivity = taskInfoList.get(i+1).baseActivity;
//			    		Intent intent = new Intent();
//						intent.setComponent(toComponetActivity);
//						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(intent);	
//		    		}
//		    		LogUtil.i(TAG,"------find me at["+i+"]-------");
//		    	}
//		    	
//			}
//		    LogUtil.d(TAG, "-------------退出APP--------------");
//		    //方法二、强制退出本App   ------------------- END --------------------------------
		}
	}

	class download implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				if (isRun) {
					if (AppUpdateTask.isdownload == 1 && bShowUpdateDlg == true)// 有更新
					{
						mHandler.sendEmptyMessage(msgUpdate);
					} else if (AppUpdateTask.isdownload == 2)// 无更新
					{
						isRun = false;
						Thread.currentThread().interrupt();
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}

