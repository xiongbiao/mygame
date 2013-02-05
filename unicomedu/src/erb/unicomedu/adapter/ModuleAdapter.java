package erb.unicomedu.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
/***
 * 主界面模块数据适配器
 * @author xiong
 *
 */
public class ModuleAdapter extends BaseAdapter {
	private Context mContent;
	private List<ResolveInfo> mModule;
	public ModuleAdapter(Context mContent  ) {
		this.mContent = mContent;
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mModule = mContent.getPackageManager().queryIntentActivities(mainIntent, 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i;

		if (convertView == null) {
			i = new ImageView(mContent);
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new GridView.LayoutParams(90, 90));
		} else {
			i = (ImageView) convertView;
		}
		ResolveInfo info = mModule.get(position);
		i.setImageDrawable(info.activityInfo.loadIcon(mContent.getPackageManager()));
		return i;
	}

	@Override
	public final int getCount() {
//		return mApps.size();
		return 12;
	}

	@Override
	public final Object getItem(int position) {
		return mModule.get(position);
	}

	@Override
	public final long getItemId(int position) {
		return position;
	}
}