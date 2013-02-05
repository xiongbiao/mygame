package erb.unicomedu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import erb.unicomedu.dao.TeacherDao;
import erb.unicomedu.ui.KeywordsFlow;
import erb.unicomedu.vo.KeyVo;

public class KeywordsFlowUtil implements SensorEventListener, OnTouchListener,
		OnGestureListener {

	private static KeywordsFlow keywordsFlow;
	private GestureDetector detector;
	private static int urlType = 5;
	private static List<KeyVo> kvl;
	private static List<KeyVo> sumKvl;
	private static boolean isBeing = false;
	private static int mSize = Def.KEY_NUM;
	private static int mIndex = 0;

	public void setUrlType(int urlType) {
		KeywordsFlowUtil.urlType = urlType;
		isBeing = true;
	}

	public static void addKeyVo(KeyVo kv) {
		if (sumKvl == null) {
			sumKvl = new ArrayList();
		}
		kv.setKeyId(sumKvl.size() + 1);
		kv.setTagId(sumKvl.size() + 1);
		sumKvl.add(kv);
	}

	public KeywordsFlowUtil(KeywordsFlow keywordsFlow) {
		KeywordsFlowUtil.keywordsFlow = keywordsFlow;
		keywordsFlow.setOnTouchListener(this);
		keywordsFlow.setLongClickable(true);
		detector = new GestureDetector(this);
	}

	private static void feedKeywordsFlow(KeywordsFlow keywordsFlow) {
		try {

			kvl = new ArrayList();
			if (isBeing) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("urlType", urlType);
				if (urlType == 5) {
					kvl = TeacherDao.getIntentionKey(param);
				} else {
					try {
						kvl = TeacherDao.getObjKey(param);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				sumKvl = kvl;
				mIndex++;
				isBeing = false;
				LogUtil.d("TAG-------", "size : " + sumKvl.size() + "");
			} else {
				if (sumKvl != null && sumKvl.size() > 0) {
					LogUtil.d("TAG-------", "size : " + sumKvl.size() + "");
					if (sumKvl.size() <= mSize) {
						for (int i = 0; i < sumKvl.size(); i++) {
							kvl.add(sumKvl.get(i));
						}
					} else {
						if (sumKvl.size() / mSize > mIndex) {
							for (int i = mIndex * mSize; i < (mIndex + 1)
									* mSize; i++) {
								kvl.add(sumKvl.get(i));
							}
							mIndex++;
						} else {

							for (int i = mIndex * mSize; i < sumKvl.size(); i++) {
								kvl.add(sumKvl.get(i));
							}
							mIndex = 0;
						}
					}
					LogUtil.d("&&&&&&",
							(kvl == null) + "------" + sumKvl.size());
				}
			}
			keywordsFlow.feedKeyword(kvl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void AnimationIn() {
		keywordsFlow.rubKeywords();
		feedKeywordsFlow(keywordsFlow);
		keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
	}

	public static void AnimationOut() {
		keywordsFlow.rubKeywords();
		feedKeywordsFlow(keywordsFlow);
		keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			if ((Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math
					.abs(values[2]) > 14)) {
				keywordsFlow.rubKeywords();
				feedKeywordsFlow(keywordsFlow);
				keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
			}
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 60) {
			AnimationIn();
			return true;
		} else if (e1.getX() - e2.getX() < -60) {
			AnimationOut();
			return true;
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		return detector.onTouchEvent(event);
	}

}
