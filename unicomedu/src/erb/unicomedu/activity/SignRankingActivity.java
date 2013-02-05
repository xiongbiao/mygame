package erb.unicomedu.activity;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import erb.unicomedu.adapter.SignRankingAdapter;
import erb.unicomedu.dao.SignDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.SignRankingVo;

public class SignRankingActivity extends PublicActivity implements OnClickListener {

	private String TAG = "SignRankingActivity";
	private ImageButton mSignBack;

	private PullToRefreshListView prlistView;
	private SignRankingAdapter mSignRankingBaseAdpter;
	private List<SignRankingVo> data;
	private LoadingView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_ranking);
		init();
		initData();
	}

	private void init() {
		lv = (LoadingView)findViewById(R.id.data_loading);
		mSignBack = (ImageButton) findViewById(R.id.sign_back);
		mSignBack.setOnClickListener(this);
		prlistView = (PullToRefreshListView) findViewById(R.id.obj_list);
	}

	private void initData() {
		prlistView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new GetDataTask().execute();
			}
		});
		new GetDataTask().execute();
	}

	/**
	 * 刷新数据
	 * 
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<SignRankingVo>> {
		int exType = 0;
		String erMsg = "";
		boolean isFoot = false;

		@Override
		protected void onPreExecute() {
			prlistView.setLoading();
			 lv.setVisibility(View.VISIBLE);
				lv.show(0);
		}

		@Override
		protected List<SignRankingVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("page", prlistView.getPage());
//				param.put("size", Def.M_LIST_SIZE);
				param.put("size", 50);
				List<SignRankingVo> tlist = SignDao.getSignRankingList(param);
				data = tlist;
//				if (prlistView.getPage() > 0) {
//					if (tlist != null) {
//						for (SignRankingVo tv : tlist) {
//							data.add(tv);
//						}
//					}
//				} else {
//					data = tlist;
//				}
//				if (tlist.size() < Def.M_LIST_SIZE) {
//					isFoot = false;
//				} else {
//					 if(tlist!=null&&tlist.size()>0) 
//					     isFoot = true;
//					 else
//						 isFoot = false;
//				}
			} catch (UnknownHostException ue) {
				data = null;
				isFoot = false;
				exType = 1;
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.d("Exception : ", "" + e.getClass().getName());
				data = null;
				isFoot = false;
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<SignRankingVo> data) {

			prlistView.setLoadingGone();
			if (mSignRankingBaseAdpter == null) {
				mSignRankingBaseAdpter = new SignRankingAdapter(
						SignRankingActivity.this, R.layout.sigin_ranking_item,
						data);
				prlistView.setAdapter(mSignRankingBaseAdpter);
			} else {
				if(prlistView.getAdapter()==null){
					prlistView.setAdapter(mSignRankingBaseAdpter);
				}
				mSignRankingBaseAdpter.setData(data);
			}
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			prlistView.setShowFooter(false);
			mSignRankingBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.sign_ranking_btn:

			break;
		case R.id.sign_back:
			finish();
			break;
		default:
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
