package erb.unicomedu.util;

import java.util.ArrayList;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Message;
import erb.unicomedu.dao.TeacherDao;

public class ObjDataTask extends
		AsyncTask<Map<String, Object>, Void, ArrayList<?>> {

	private final AsyncTaskListener<ArrayList<?>> callback;


	public ObjDataTask(AsyncTaskListener<ArrayList<?>> asyncTaskListener) {
		this.callback = asyncTaskListener;
	}

	@Override
	protected void onPreExecute() {
		callback.onPreExecute();
	}

	@Override
	protected ArrayList<?> doInBackground(Map<String, Object>... params) {
		ArrayList<?> result = null;
		Map<String, Object> param = params[0];
		String eventID = param.get("eventId") + "";
		int page = Integer.valueOf(param.get("page") + "");
		if ("teacherList".equals(eventID)) {
			try {
				result = TeacherDao.getTeacherList(param);
			} catch (EuException ex) {
				ex.printStackTrace();
				callback.onExclption(ex);
			} catch (Exception e) {
				e.printStackTrace();
//				callback.onExclption(e);
			}
		} else {

		}
		showFoot(result, page);
		return result;
	}

	private void showFoot(ArrayList<?> result, int page) {
		boolean isFoot = true;
		if (page > 0) {
			if (result != null) {
				if (result.size() < Def.M_LIST_SIZE) {
					isFoot = false;
				} else {
					isFoot = true;
				}
			}
		} else {
			if (result != null && result.size() > 0)
				isFoot = true;
			else
				isFoot = false;
		}
		callback.showFoot(isFoot);
	}

	@Override
	protected void onPostExecute(ArrayList<?> data) {
		callback.onTaskComplete(data);
	}
}
