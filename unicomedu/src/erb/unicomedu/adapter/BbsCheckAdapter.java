package erb.unicomedu.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import erb.unicomedu.activity.BbsReplyActivity;
import erb.unicomedu.activity.LoginActivity;
import erb.unicomedu.activity.PublicActivity;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsAskVo;
import erb.unicomedu.vo.BbsCheckVo;

public class BbsCheckAdapter extends BaseAdapter {
	private final String TAG = "BbsCheckAdapter";

	private Context context;
	//private int mResource;
	private List<BbsCheckVo> mData = new ArrayList<BbsCheckVo>();
	LayoutInflater inflater;
	private BbsAskVo mBbsAskVo;
	public BbsCheckAdapter(Context context, LayoutInflater inflater, List<BbsCheckVo> dataList){
		this.context = context;
		mData = dataList;
	}
	
	public void setAskVo(BbsAskVo bbsAskVo){
		mBbsAskVo = bbsAskVo;
	}

	@Override
	public int getCount() {
		if(mData == null)
			return 0;
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
//		if(mData == null || mData.size() == 0 || mData.get(position) == null){
//			LogUtil.i("TUDE==", "==================================NULL");
//			return null;
//		}
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout view;
		LinearLayout linear = new LinearLayout(context);
//			LinearLayout.LayoutParams lp_liner = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
//			linear.setLayoutParams(lp_liner);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setBackgroundResource(R.drawable.list_item_select_style);
		
		RelativeLayout relative_Top = new RelativeLayout(context);
		LinearLayout.LayoutParams lp_relative_top = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		relative_Top.setLayoutParams(lp_relative_top);
		
		TextView textView_editer = new TextView(context);
		RelativeLayout.LayoutParams lp_Tediter = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp_Tediter.leftMargin = dipToPixels(context, 20);
		lp_Tediter.topMargin = dipToPixels(context, 5);
		lp_Tediter.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp_Tediter.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		textView_editer.setLayoutParams(lp_Tediter);
//			textView_editer.setTextSize(10);
		if(!(mData.get(position).getAnswerid()==0)) {
			textView_editer.setText(mData.get(position).getAnswer());
		} else {
			textView_editer.setText("帖主："+mData.get(position).getAnswer());
		}
		textView_editer.setTextColor(Color.argb(255, 255, 149, 1));
		relative_Top.addView(textView_editer);
		
		TextView textView_Date = new TextView(context);
		RelativeLayout.LayoutParams lp_Date = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp_Date.rightMargin = dipToPixels(context, 60);
		lp_Date.topMargin = dipToPixels(context, 5);
		lp_Date.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp_Date.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		textView_Date.setLayoutParams(lp_Date);
//		textView_Date.setTextSize(10);
//		String time = mData.get(position).getPubtime().substring(5, mData.get(position).getPubtime().length());
//		textView_Date.setText(time);
		textView_Date.setTextSize(12); 
		textView_Date.setText(mData.get(position).getPubtime());
		textView_Date.setTextColor(Color.argb(255, 106, 108, 107));
		 
		relative_Top.addView(textView_Date);
		
		TextView textView_levels = new TextView(context);
		RelativeLayout.LayoutParams lp_Levels = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp_Levels.rightMargin = dipToPixels(context, 20);
		lp_Levels.topMargin = dipToPixels(context, 5);
		lp_Levels.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp_Levels.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		textView_levels.setLayoutParams(lp_Levels);
//			textView_Date.setTextSize(10);
//		textView_levels.setText((position+1)+" 楼");
		if(position!=0){
			textView_levels.setText((mData.get(position).getFloor() )+" 楼");
		}
		relative_Top.addView(textView_levels);
		
        linear.addView(relative_Top);
		if(mData.get(position).getBbsCheckVoList()!=null&&mData.get(position).getBbsCheckVoList().size() > 0)
			linear.addView(new ItemLayout(context, mData.get(position).getBbsCheckVoList()));
        
        TextView textView_info = new TextView(context);
		LinearLayout.LayoutParams lp_Info = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp_Info.leftMargin = dipToPixels(context, 20);
		lp_Info.topMargin = dipToPixels(context, 5);
		textView_info.setLayoutParams(lp_Info);
//			textView_info.setTextSize(10);
		textView_info.setText(mData.get(position).getContext());
        
		linear.addView(textView_info);
		
		RelativeLayout relative_bottom = new RelativeLayout(context);
		LinearLayout.LayoutParams lp_relative_bottom = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		relative_bottom.setLayoutParams(lp_relative_bottom);
		
		Button button_reply = new Button(context);
		RelativeLayout.LayoutParams lp_reply = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp_reply.rightMargin = dipToPixels(context, 10);
		lp_reply.topMargin = dipToPixels(context, 5);
		lp_reply.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp_reply.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		button_reply.setLayoutParams(lp_reply);
		button_reply.setText("回复");
		button_reply.setTextColor(Color.argb(255, 94, 173, 81));
		button_reply.setBackgroundColor(Color.argb(0, 248, 243, 247));
		
		final BbsCheckVo vo = mData.get(position);
		button_reply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtil.d(TAG,"onClick---回复 to 评论----");
				//这里
				String fromClassName =  context.getClass().getName();
				String className =  new BbsReplyActivity().getClass().getName();
				Bundle bbsPinglunBundle = new Bundle();
			     bbsPinglunBundle.putSerializable(Def.BBS_REPLY_PL,vo);  
                if(new PublicActivity().userInfo!=null){
                	 
    				//回复 to 用户评论
    				Intent mIntent = new Intent(context, BbsReplyActivity.class);  
    		        mIntent.putExtra(Def.OBJ_Bundle,bbsPinglunBundle);
    		        context.startActivity(mIntent);
					
				} else{
					Intent mIntent = new Intent(context,LoginActivity.class);  
					bbsPinglunBundle.putString(Def.TO_CLASS_NAME_TAG, className);
					bbsPinglunBundle.putString(Def.FROM_CLASS_NAME_TAG, fromClassName);
					bbsPinglunBundle.putSerializable(Def.OBJ,mBbsAskVo);
			        mIntent.putExtra(Def.OBJ_Bundle, bbsPinglunBundle); 
			        mIntent.putExtras(bbsPinglunBundle); 
			        context.startActivity(mIntent); 
				}
//				Bundle bundle = new Bundle();
//				mRBundle.putSerializable(Def.OBJ_bbs_ask,bbsCheckVo);  
//				mRBundle.putSerializable(Def.OBJ_bbs_ask,bbsAskVo);  
				
			}
		});
		relative_bottom.addView(button_reply);
		
		if(!(mData.get(position).getAnswerid()==0))linear.addView(relative_bottom);
		
        view= linear;
		return view;
	}
	
	class ItemLayout extends LinearLayout{
    	Context context;
    	private List<BbsCheckVo> bbsCheckVoList;
    	
		public ItemLayout(Context context, List<BbsCheckVo> bbsCheckVoList) {
			super(context);
			this.context = context;
			this.bbsCheckVoList = bbsCheckVoList;
//			TextView textView = new TextView(context);
			LinearLayout linear = new LinearLayout(context);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			linear.setLayoutParams(lp);
			linear.setOrientation(LinearLayout.VERTICAL);
			LogUtil.i("TUDE==", "SIZE"+bbsCheckVoList.size());
			addView(getLayout(null, linear, this.bbsCheckVoList.size()));
//			addView(getLayout(null, textView, i));
		}
    	
		private LinearLayout getLayout(LinearLayout upLayout, LinearLayout bottomView, int i){
			LinearLayout layout = new LinearLayout(context);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.setMargins(1, 0, 1, 0);
			layout.setLayoutParams(lp);
//			layout.setBackgroundColor(Colors[i - 1]);
			layout.setBackgroundResource(R.drawable.fold_bg);
			layout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout linear = new LinearLayout(context);
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			linear.setLayoutParams(lp1);
			linear.setOrientation(LinearLayout.VERTICAL);
			if(upLayout != null){
				layout.addView(upLayout);
			}
			if(bottomView != null){
				TextView t1 = new TextView(context);
				t1.setText("引用：");
				t1.setTextColor(Color.argb(255, 89, 89, 89));
				bottomView.addView(t1, new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				TextView t2 = new TextView(context);
				t2.setText("原帖由 "+bbsCheckVoList.get(i-1).getAnswer()+" 于 "+bbsCheckVoList.get(i-1).getPubtime());
				t2.setTextColor(Color.argb(255, 89, 89, 89));
				bottomView.addView(t2, new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				TextView t3 = new TextView(context);
				t3.setText("发表在  " +(bbsCheckVoList.get(i-1).getFloor() )+" 楼");
				t3.setTextColor(Color.argb(255, 89, 89, 89));
				bottomView.addView(t3, new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				TextView t4 = new TextView(context);
				t4.setText(bbsCheckVoList.get(i-1).getContext());
				t4.setTextColor(Color.argb(255, 89, 89, 89));
				bottomView.addView(t4, new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				layout.addView(bottomView,new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
//				bottomView.setGravity(Gravity.CENTER);
			}
			if(i == 1){
				lp.setMargins(30, 0, 30, 0);
				layout.setLayoutParams(lp);
				return layout;
			}
			return getLayout(layout, linear, --i);
		}
	}
	
	private int isexpires(String time){
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date createDate = null;
		try {
			createDate = format2.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}

		if(createDate == null) return 0;
		System.out.println("new Date()="+new Date());
		long ss=((new Date()).getTime()-createDate.getTime())/1000/3600/24;
		System.out.println("ss="+ss);
		return (int)ss;
	}
	
	class Holder{
		int tag;
		ImageView isrecommend;
		TextView title;
		TextView author;
		TextView replyCount;
		TextView pubtime;
	}
	
	public static int dipToPixels(Context context, float dip)
	{
		return (int)(context.getResources().getDisplayMetrics().density * dip);
	}

}
