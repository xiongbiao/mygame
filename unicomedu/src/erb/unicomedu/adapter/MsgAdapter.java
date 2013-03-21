package erb.unicomedu.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.vo.MsgTypeVo;
import erb.unicomedu.vo.MsgVo;

public class MsgAdapter extends BaseExpandableListAdapter {
	List<MsgTypeVo> group; // 组列表
	List<List<MsgVo>> child; // 子列表
	private Context mContext;

	public MsgAdapter(Context context, List<MsgTypeVo> group,
	List<List<MsgVo>> child) {
		this.mContext = context;
		this.group =group;
		this.child =child;
	}
 

	/***
	 *============Child==========
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 MsgVo vo = child.get(groupPosition).get(childPosition); 
		 return getChildView(vo);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if(child!=null&&child.get(groupPosition)!=null){
			return child.get(groupPosition).size();
		}else{
			return 0;
		}
	}

	/***
	 * ============Group==========
	 */
	
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		MsgTypeVo string = group.get(groupPosition); 
		int size = 0;
		if( group.get(groupPosition).getMsgList()!=null&& group.get(groupPosition).getMsgList().size()>0){
			size = group.get(groupPosition).getMsgList().size();
		}
		return getGenericView(string,isExpanded,  size);
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	

	 
    public RelativeLayout getGenericView(MsgTypeVo s,boolean isExpanded,int size) {
    	
    	RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.msg_list_parent_item, null);
    	TextView parent_title = (TextView)layout.findViewById(R.id.item_title);
        parent_title.setText(s.getMsgTypeName()+" ("+size+") " );
    	ImageView iv =(ImageView)layout.findViewById(R.id.item_left_img);
//    	if(isExpanded){
//    		iv.setBackgroundResource(R.drawable.jian);
//    	}else{
//    		iv.setBackgroundResource(R.drawable.jia);
//    	}
        return layout;  
    }  
   
    public LinearLayout getChildView(MsgVo s) {
    	LinearLayout layout = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.msg_list_item, null);
    	TextView item_title = (TextView)layout.findViewById(R.id.msg_title);
    	//item_title.setTextColor(Color.rgb(255, 149, 1));
    	item_title.setText(s.getTitle());
    	TextView theme_count = (TextView)layout.findViewById(R.id.msg_count);
    	//theme_count.setTextColor(Color.rgb(208, 207, 203));
    	theme_count.setText(String.valueOf("" + s.getSummary()));
    	TextView post_count = (TextView)layout.findViewById(R.id.msg_time);
    	//post_count.setTextColor(Color.rgb(208, 207, 203));
    	
    	 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date date = null;    
         String str = s.getPubtime();
         try {    
        	     date = format.parse(str);   
        	     Date dNow = new Date();  
        	     if(date.getDay()==dNow.getDay()){
        	    	 str = str.substring(10,str.length()); 
        	     } 
        	     
        } catch (ParseException e) {    
        	     e.printStackTrace();    
        } 
    	
    	post_count.setText(str);
        return layout;  
    }

}
