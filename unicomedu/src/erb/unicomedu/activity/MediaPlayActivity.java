package erb.unicomedu.activity;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import erb.unicomedu.ui.MediaExpand;
import erb.unicomedu.ui.MediaExpandTop;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;

public class MediaPlayActivity extends PublicActivity implements SurfaceHolder.Callback,
	MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,
									MediaPlayer.OnErrorListener,OnClickListener,OnSeekBarChangeListener{
	
	public static final String EXTRA_NAME_PLAY_URL = "CHOOSE_URL";
	private static final int MSG_UPDATE_MEDIA_PROGRESS = 100;
	private static final int MSG_START_MEDIA = 101;
	private static final int MSG_UPDATE_PLAY_BUTTON = 102;
	private static final int MSG_UPDATE_PAUSE_BUTTON = 103;
	private static final int MSG_UPDATE_PROGRESS_MAX_SIZE = 104;
	private static final int MSG_UPDATE_DIALOG_PROGRESS = 105;
	private static final int MSG_UPDATE_PLAY_CONTROL = 106;
	private static final int MSG_START_HIDE_CONTROL_THREAD = 107;
	int mediaMaxLen = 0;
	MediaPlayer mMediaPlayer;
	SurfaceView mSurfaceView;
	SurfaceHolder mSurfaceHolder;
	MediaExpand mMediaExpand;
	MediaExpandTop mMediaExpandTop;
	String filePath = "http://114.112.41.154/pushhtml/1000/1.mp4";
	boolean isRun = false;
	boolean isPlay = false;
	boolean isProgressTouch = false;
	boolean isSoundTouch = false;
	boolean isHandPause = false;
	
	ImageButton playButton;
	Button backButton;
	SeekBar progressBar;
	SeekBar soundBar;
	TextView titleText;
	
	ProgressDialog mProgressDialog;
	
	AudioManager mAudioManager;
	
	private static final int MAX_HIDE_TIME = 5;
	private int currenthideTime = MAX_HIDE_TIME; 
	private Thread hideControlThread = new Thread(){
		@Override
		public void run() {
			while (isRun) {
				if(currenthideTime == 0){
					mHandler.sendEmptyMessage(MSG_UPDATE_PLAY_CONTROL);
				}else{
					currenthideTime--;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO 禁止屏幕进入锁屏状态
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		
		setContentView(R.layout.media_play_main);
		
		// TODO 获取播放地址
		// TODO 获取播放地址
		Intent intent = getIntent();
		if(intent == null){
			LogUtil.e(LogUtil.makeLogTag(getClass()), "intent is null");
			finish();
		}
		filePath = intent.getStringExtra(EXTRA_NAME_PLAY_URL);
		LogUtil.d(LogUtil.makeLogTag(getClass()), filePath);
		// TODO 播放地址检测
		if(TextUtils.isEmpty(filePath)){
			LogUtil.e(LogUtil.makeLogTag(getClass()), "the play url isn't analytic");
			finish();
		}
		
		mMediaExpand = (MediaExpand)findViewById(R.id.media_expand_bottom);
		mMediaExpandTop = (MediaExpandTop)findViewById(R.id.media_expand_top);
		mSurfaceView = (SurfaceView)findViewById(R.id.media_window);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		playButton = (ImageButton)findViewById(R.id.media_play);
		playButton.setOnClickListener(this);
		backButton = (Button)findViewById(R.id.media_back);
		backButton.setOnClickListener(this);
		
		progressBar = (SeekBar)findViewById(R.id.media_progress);
		progressBar.setProgress(0);
		progressBar.setOnSeekBarChangeListener(this);
		soundBar = (SeekBar)findViewById(R.id.media_sound);
		
		titleText = (TextView)findViewById(R.id.media_title);
		
		// TODO 音量控制
		mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		soundBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		soundBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		soundBar.setOnSeekBarChangeListener(this);
		
	}
	/**
	 * 弹出控制菜单
	 */
	private void showMediaControl(){
		mMediaExpand.openMoreListView();
		mMediaExpandTop.openMoreListView();
		currenthideTime = MAX_HIDE_TIME;
	}
	
	/**
	 * 隐藏控制菜单
	 */
	private void hideMediaControl(){
		mMediaExpand.hideMoreListView();
		mMediaExpandTop.hideMoreListView();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_PLAY_CONTROL, 1, 0));
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
			soundBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		}
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		onMediaPause();
		isHandPause = true;
		super.onStop();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 显示加载等待
		mProgressDialog = ProgressDialog.show(this, getString(R.string.media_loading_title), getString(R.string.media_loading_message),true,false);
		mHandler.sendEmptyMessage(MSG_START_MEDIA);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
	private void startMedia(final String path,int seekTo) throws IllegalStateException, IOException{
		if(mMediaPlayer == null){
			mMediaPlayer = new MediaPlayer();
		}else {
			mMediaPlayer.reset();
		}
		mMediaPlayer.setDataSource(path);
		mMediaPlayer.setDisplay(mSurfaceHolder);
		mMediaPlayer.setOnCompletionListener(MediaPlayActivity.this);
		mMediaPlayer.setOnBufferingUpdateListener(MediaPlayActivity.this);
		mMediaPlayer.setOnPreparedListener(MediaPlayActivity.this);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnErrorListener(MediaPlayActivity.this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mMediaPlayer.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mediaMaxLen = mMediaPlayer.getDuration();
				mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS_MAX_SIZE);
				onMediaStart();
				mHandler.sendEmptyMessage(MSG_UPDATE_DIALOG_PROGRESS);
				mHandler.sendEmptyMessage(MSG_START_HIDE_CONTROL_THREAD);
				isHandPause = false;
				while (isRun) {
					if(mMediaPlayer == null){
						break;
					}
					if(mMediaPlayer.isPlaying()){
						try {
							mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_MEDIA_PROGRESS, mMediaPlayer.getCurrentPosition(), 0));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case MSG_UPDATE_MEDIA_PROGRESS:
					progressBar.setProgress(msg.arg1);
					break;
				case MSG_START_MEDIA:
					try {
						if(!TextUtils.isEmpty(filePath)){
							startMedia(filePath,0);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case MSG_UPDATE_PLAY_BUTTON:
					if(playButton != null){
						playButton.setImageResource(android.R.drawable.ic_media_pause);
					}
					break;
				case MSG_UPDATE_PAUSE_BUTTON:
					if(playButton != null){
						playButton.setImageResource(android.R.drawable.ic_media_play);
					}
					break;
				case MSG_UPDATE_PROGRESS_MAX_SIZE:
					progressBar.setMax(mediaMaxLen);
					break;
				case MSG_UPDATE_DIALOG_PROGRESS:
					if(mProgressDialog != null){
						mProgressDialog.dismiss();
					}
					break;
				case MSG_START_HIDE_CONTROL_THREAD:
					hideControlThread.start();
					break;
				case MSG_UPDATE_PLAY_CONTROL:
					if(msg.arg1 == 1){
						showMediaControl();
					}else{
						hideMediaControl();
					}
					break;
			}
			
		};
	};
	
	/**
	 * 暂停
	 */
	public void onMediaPause(){
		if(mMediaPlayer != null){
			if(mMediaPlayer.isPlaying()){
				mMediaPlayer.pause();
				isPlay = false;
				mHandler.sendEmptyMessage(MSG_UPDATE_PAUSE_BUTTON);
			}
		}
	}
	
	/**
	 * 播放
	 */
	public void onMediaStart(){
		if(mMediaPlayer != null){
			if(!mMediaPlayer.isPlaying()){
				mMediaPlayer.start();
				isPlay = true;
				isRun = true;
				mHandler.sendEmptyMessage(MSG_UPDATE_PLAY_BUTTON);
			}
		}
	}
	
	/**
	 * 停止
	 */
	public void onMediaStop(){
		isRun = false;
		isPlay = false;
		if(mMediaPlayer != null){
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		onMediaStop();
		super.onDestroy();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		progressBar.setProgress(mediaMaxLen);
		onMediaPause();
		isHandPause = true;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		int playPercent = (int)((float)mMediaPlayer.getCurrentPosition() / mediaMaxLen * 100);
		progressBar.setSecondaryProgress(percent * mediaMaxLen / 100);
		if(isHandPause) return;
		if(percent != 100 && (percent - playPercent) < 10 && isPlay){
			onMediaPause();
		}else if((percent - playPercent) > 20){
			onMediaStart();
		}
	}

	@Override
	public void onClick(View v) {
		if(v == null) return;
		switch (v.getId()) {
		case R.id.media_back:
			onMediaStop();
			finish();
			break;
		case R.id.media_play:
			if(isPlay){
				isHandPause = true;
				onMediaPause();
			}else{
				isHandPause = false;
				onMediaStart();
			}
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(seekBar == null) return;
		if(isProgressTouch || isSoundTouch){
			showMediaControl();
		}
		switch (seekBar.getId()) {
			case R.id.media_progress:
				if(isProgressTouch) mMediaPlayer.seekTo(progress);
				break;
			case R.id.media_sound:
				if(isSoundTouch) {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
				}
				break;
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if(seekBar == null) return;
		switch (seekBar.getId()) {
			case R.id.media_progress:
				onMediaPause();
				isProgressTouch = true;
				break;
			case R.id.media_sound:
				isSoundTouch = true;
				break;
		}
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if(seekBar == null) return;
		switch (seekBar.getId()) {
			case R.id.media_progress:
				onMediaStart();
				isProgressTouch = false;
				break;
			case R.id.media_sound:
				isSoundTouch = false;
				break;
		}
	}
	
}
