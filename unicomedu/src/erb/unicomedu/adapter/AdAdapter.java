package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.adapter.VideoAdapter.Holder;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.vo.RecommendVo;

public class AdAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;
	private int mResource;
	private AsyncImageLoader asyncImageLoader;
	private List<RecommendVo> mData = new ArrayList<RecommendVo>();

	// private Integer[] mImageIds = { R.drawable.unicom_business,
	// R.drawable.wo_news,
	// R.drawable.unicom_17wo, R.drawable.wo_area };

	public AdAdapter(Context context, int resource, List<RecommendVo> dataList) {
		mContext = context;
		mResource = resource;
		mData = dataList;
		asyncImageLoader = new AsyncImageLoader();
	}

	@Override
	public int getCount() {
		if (mData == null)
			return 0;
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		if (mData == null || mData.size() == 0 || mData.get(position) == null)
			return null;
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(mResource, null);
			holder = new Holder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_avatar);
			holder.title = (TextView) convertView.findViewById(R.id.item_title);
			holder.tag = position;
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
			holder.imageView.setImageDrawable(null);
			holder.title.setText("");
			holder.tag = position;
		}

		Drawable cachedImage = asyncImageLoader.loadDrawable(mData
				.get(position).getLogo(), new ImageCallback() {
			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				ImageView imageViewByTag = holder.imageView;
				if (imageViewByTag != null && imageDrawable != null) {
					Bitmap bitmap = ((BitmapDrawable) imageDrawable)
							.getBitmap();
					imageViewByTag.setImageBitmap(bitmap);
				}
			}
		});
		if (cachedImage != null) {
			Bitmap bitmap = ((BitmapDrawable) cachedImage).getBitmap();
			holder.imageView.setImageBitmap(bitmap);
		} else {
			holder.imageView.setImageResource(R.drawable.avatar_icon);
		}

		holder.title.setText((mData.get(position).getAppname()));
		return convertView;
	}

	class Holder {
		int tag;
		ImageView imageView;
		TextView title;
	}

}
