package erb.unicomedu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import erb.unicomedu.ui.VideoView;
import erb.unicomedu.ui.VideoView.MySizeChangeLinstener;
import erb.unicomedu.util.LogUtil;

public class VideoPlayerActivity extends PublicActivity {

	private final static String TAG = "VideoPlayerActivity";
	private boolean isChangedVideo = false;

	// private Uri videoListUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	// private static int position ;
	// private static boolean backFromAD = false;
	private int playedTime;
	public static final String EXTRA_NAME_PLAY_URL = "CHOOSE_URL";

	private VideoView vv = null;
	private SeekBar playSeekBar = null;
	private SeekBar soundSeekBar = null;
	private TextView durationTextView = null;
	private TextView playedTextView = null;
	private GestureDetector mGestureDetector = null;
	private AudioManager mAudioManager = null;

	private int maxVolume = 0;
	private int currentVolume = 0;

	private Button controlPlay = null;
	private ImageView soundIcon = null;

	private View controlView = null;
	private PopupWindow controler = null;

	private View extralView = null;
	private PopupWindow extralWindow = null;

	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static int controlHeight = 0;

	private final static int TIME = 6868;

	private boolean isControllerShow = true;
	private boolean isPaused = false;
	private boolean isFullScreen = true;
	private ProgressBar pb ;
    private int topHeight = 35;
	ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_player);
		LogUtil.d(TAG, "top : "+topHeight);
		Looper.myQueue().addIdleHandler(new IdleHandler() {

			@Override
			public boolean queueIdle() {

				// TODO Auto-generated method stub
				if (controler != null && vv.isShown()) {
					controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
					controler.update(0, 0, screenWidth, controlHeight);
				}

				if (extralWindow != null && vv.isShown()) {
					extralWindow.showAtLocation(vv, Gravity.TOP, 0, 0);
					extralWindow.update(0, topHeight, screenWidth, 60);
				}

				// myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
				return false;
			}
		});
		pb = (ProgressBar)findViewById(R.id.data_progressBar);
		controlView = getLayoutInflater().inflate(R.layout.controler, null);
		controler = new PopupWindow(controlView);
		durationTextView = (TextView) controlView.findViewById(R.id.duration);
		playedTextView = (TextView) controlView.findViewById(R.id.has_played);

		extralView = getLayoutInflater().inflate(R.layout.extral, null);
		extralWindow = new PopupWindow(extralView);

		Button backButton = (Button) extralView.findViewById(R.id.back);
		TextView mediaName = (TextView) extralView
				.findViewById(R.id.media_title);

		// position = -1;

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				VideoPlayerActivity.this.finish();
			}

		});

		controlPlay = (Button) controlView
				.findViewById(R.id.controler_play_pause);
		soundIcon = (ImageView) controlView.findViewById(R.id.controler_sound);

		// int screenWidth;//屏幕宽度
		// int screenHeight;//屏幕高度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
        
		if(screenHeight>500)
			topHeight = 60;
		 
		vv = (VideoView) findViewById(R.id.vv);
		vv.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				vv.stopPlayback();
				new AlertDialog.Builder(VideoPlayerActivity.this)
						.setTitle("对不起")
						.setMessage("您所播的视频出现异常，播放已停止。")
						.setPositiveButton("知道了",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										vv.stopPlayback();

									}

								}).setCancelable(false).show();

				return false;
			}

		});

		// Uri uri = (Uri)this.getIntent().getParcelableExtra
		// (EXTRA_NAME_PLAY_URL);
		String mMediaName = getIntent().getStringExtra("MediaName");

		mediaName.setText(mMediaName);

		String filePath = getIntent().getStringExtra(EXTRA_NAME_PLAY_URL);
		LogUtil.d(LogUtil.makeLogTag(getClass()), filePath);
		// TODO 播放地址检测
		if (!TextUtils.isEmpty(filePath)) {
			vv.stopPlayback();
			vv.setVideoPath(filePath);
			controlPlay.setBackgroundResource(R.drawable.button_background_control_pause);
		} else {
			controlPlay.setBackgroundResource(R.drawable.button_background_control_play);
		}
         /**
          * 监听改变大小   
          */
		vv.setMySizeChangeLinstener(new MySizeChangeLinstener() {
			@Override
			public void doMyThings() {
				// TODO Auto-generated method stub
				
				setVideoScale(SCREEN_FULL);
			}

		});

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		soundSeekBar = (SeekBar) controlView.findViewById(R.id.control_sound_seekbar);
		soundSeekBar.setMax(maxVolume);
		soundSeekBar.setProgress(currentVolume);
		soundSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				updateVolume(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				cancelDelayHide();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				hideControllerDelay();
			}
		});

		controlPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelDelayHide();
				if (isPaused) {
					vv.start();
					controlPlay.setBackgroundResource(R.drawable.button_background_control_pause);
					hideControllerDelay();
				} else {
					vv.pause();
					controlPlay
							.setBackgroundResource(R.drawable.button_background_control_play);
				}
				isPaused = !isPaused;

			}

		});

		playSeekBar = (SeekBar) controlView.findViewById(R.id.control_seekbar_play);
		playSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

				if (fromUser) {
					// if(!isOnline){
					vv.seekTo(progress);
					// }
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				myHandler.removeMessages(HIDE_CONTROLER);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
			}
		});

		getScreenSize();

		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// TODO Auto-generated method stub
				if (isFullScreen) {
					setVideoScale(SCREEN_DEFAULT);
				} else {
					setVideoScale(SCREEN_FULL);
				}
				isFullScreen = !isFullScreen;
				LogUtil.d(TAG, "onDoubleTap");

				if (isControllerShow) {
					showController();
				}
				// return super.onDoubleTap(e);
				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				if (!isControllerShow) {
					showController();
					hideControllerDelay();
				} else {
					cancelDelayHide();
					hideController();
				}
				// return super.onSingleTapConfirmed(e);
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				if (isPaused) {
					vv.start();
					controlPlay.setBackgroundResource(R.drawable.button_background_control_pause);
					cancelDelayHide();
					hideControllerDelay();
				} else {
					vv.pause();
					controlPlay
							.setBackgroundResource(R.drawable.button_background_control_play);
					cancelDelayHide();
					showController();
				}
				isPaused = !isPaused;
				// super.onLongPress(e);
			}
		});

		// vv.setVideoPath("http://202.108.16.171/cctv/video/A7/E8/69/27/A7E86927D2BF4D2FA63471D1C5F97D36/gphone/480_320/200/0.mp4");

		vv.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub

				setVideoScale(SCREEN_FULL);
				isFullScreen = false;
				if (isControllerShow) {
					showController();
				}

				int i = vv.getDuration();
				LogUtil.d("onCompletion", "" + i);
				playSeekBar.setMax(i);
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				durationTextView.setText(String.format("%02d:%02d:%02d", hour, minute, second));

				/*
				 * controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
				 * controler.update(screenWidth, controlHeight);
				 * myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
				 */
				pb.setVisibility(View.GONE);
				vv.start();
				controlPlay.setBackgroundResource(R.drawable.button_background_control_pause);
				hideControllerDelay();
				myHandler.sendEmptyMessage(PROGRESS_CHANGED);
			}
		});

		vv.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				vv.stopPlayback();
				VideoPlayerActivity.this.finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

			vv.stopPlayback();

			int result = data.getIntExtra("CHOOSE", -1);
			LogUtil.d("RESULT", "" + result);
			if (result != -1) {
				// isOnline = false;
				// isChangedVideo = true;
				// vv.setVideoPath(playList.get(result).path);
				// position = result;
			} else {
				String url = data.getStringExtra("CHOOSE_URL");
				LogUtil.e(TAG, "url 地址: " + url);
				if (url != null) {
					vv.setVideoPath(url);
					isChangedVideo = true;
				}
			}

			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private final static int PROGRESS_CHANGED = 0;
	private final static int HIDE_CONTROLER = 1;

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PROGRESS_CHANGED:
				int i = vv.getCurrentPosition();
				playSeekBar.setProgress(i);
				// if(isOnline){
				// int j = vv.getBufferPercentage();
				// seekBar.setSecondaryProgress(j * seekBar.getMax() / 100);
				// }else{
				playSeekBar.setSecondaryProgress(0);
				// }
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				playedTextView.setText(String.format("%02d:%02d:%02d", hour,
						minute, second));
				sendEmptyMessageDelayed(PROGRESS_CHANGED, 100);
				break;
			case HIDE_CONTROLER:
				hideController();
				break;
			}

			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		boolean result = mGestureDetector.onTouchEvent(event);
		if (!result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				/*
				 * if(!isControllerShow){ showController();
				 * hideControllerDelay(); }else { cancelDelayHide();
				 * hideController(); }
				 */
			}
			result = super.onTouchEvent(event);
		}

		return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub

		getScreenSize();
		if (isControllerShow) {

			cancelDelayHide();
			hideController();
			showController();
			hideControllerDelay();
		}

		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		playedTime = vv.getCurrentPosition();
		vv.pause();
		controlPlay
				.setBackgroundResource(R.drawable.button_background_control_play);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (!isChangedVideo) {
			vv.seekTo(playedTime);
			vv.start();
		} else {
			isChangedVideo = false;
		}

		// if(vv.getVideoHeight()!=0){
		if (vv.isPlaying()) {
			controlPlay.setBackgroundResource(R.drawable.button_background_control_pause);
			hideControllerDelay();
		}
		LogUtil.d("REQUEST", "NEW AD !");

		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (controler.isShowing()) {
			controler.dismiss();
			extralWindow.dismiss();
		}
		myHandler.removeMessages(PROGRESS_CHANGED);
		myHandler.removeMessages(HIDE_CONTROLER);

		if (vv.isPlaying()) {
			vv.stopPlayback();
		}
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (vv.isPlaying()) {
			vv.pause();
		}
		super.onStop();
	}

	private void getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		controlHeight = screenHeight / 4;
	}

	private void hideController() {
		if (controler.isShowing()) {
			controler.update(0, 0, 0, 0);
			extralWindow.update(0, 0, screenWidth, 0);
			isControllerShow = false;
		}
	}

	private void hideControllerDelay() {
		myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	private void showController() {
		controler.update(0, 0, screenWidth, controlHeight);
		if (isFullScreen) {
			extralWindow.update(0, 0, screenWidth, 60);
		} else {
			extralWindow.update(0, topHeight, screenWidth, 60);
		}

		isControllerShow = true;
	}

	private void cancelDelayHide() {
		myHandler.removeMessages(HIDE_CONTROLER);
	}

	/**
	 * 全屏 0
	 */
	private final static int SCREEN_FULL = 0;
	/**
	 * 非全屏 1
	 */
	private final static int SCREEN_DEFAULT = 1;

	private void setVideoScale(int flag) {

		LayoutParams lp = vv.getLayoutParams();

		switch (flag) {
		case SCREEN_FULL:
			LogUtil.d(TAG, "screenWidth: " + screenWidth + " screenHeight: " + screenHeight);
			vv.setVideoScale(screenWidth, screenHeight);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			break;

		case SCREEN_DEFAULT:
			int videoWidth = vv.getVideoWidth();
			int videoHeight = vv.getVideoHeight();
			int mWidth = screenWidth;
			int mHeight = screenHeight - topHeight;

			if (videoWidth > 0 && videoHeight > 0) {
				if (videoWidth * mHeight > mWidth * videoHeight) {
					// LogUtil.i("@@@", "image too tall, correcting");
					mHeight = mWidth * videoHeight / videoWidth;
				} else if (videoWidth * mHeight < mWidth * videoHeight) {
					// LogUtil.i("@@@", "image too wide, correcting");
					mWidth = mHeight * videoWidth / videoHeight;
				} else {

				}
			}

			vv.setVideoScale(mWidth, mHeight);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

			break;
		}
	}

	private void updateVolume(int index) {
		if (mAudioManager != null) {
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
			soundIcon.setImageResource(R.drawable.control_sound);
			if (index == 0) {
				soundIcon.setImageResource(R.drawable.control_sound_mute);
			}
			currentVolume = index;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	};
}