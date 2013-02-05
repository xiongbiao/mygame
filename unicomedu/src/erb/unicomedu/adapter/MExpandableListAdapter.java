package erb.unicomedu.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsVo;

public class MExpandableListAdapter extends BaseExpandableListAdapter{
	List<BbsVo> group;           //组列表
	List<List<BbsVo>> child;     //子列表
	LayoutInflater inflater;
	private Context context;
	
	public MExpandableListAdapter(Context context, List<BbsVo> group, List<List<BbsVo>> child, LayoutInflater inflater) {
		this.context = context;
		this.inflater = inflater;
		this.group = group;
		this.child = child;
	}
	
	//-----------------Child----------------//
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child.get(groupPosition).get(childPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return child.get(groupPosition).size();
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		BbsVo vo = child.get(groupPosition).get(childPosition); 
		return getChildView(vo);
	}
	
	//----------------Group----------------//
	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}				

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}	
	
	@Override
	public int getGroupCount() {
		return group.size();
	}	
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
		View convertView, ViewGroup parent) {
		BbsVo string = group.get(groupPosition);  
		return getGenericView(string,isExpanded);
	}

	//创建组视图  
    public RelativeLayout getGenericView(BbsVo s,boolean isExpanded) {
    	
    	RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.list_bbs_parent_item, null);
    	TextView parent_title = (TextView)layout.findViewById(R.id.BBS_parent_title);
    	parent_title.setTextColor(Color.rgb(9, 86, 143));
    	parent_title.setText(s.getTopic());
    	ImageView iv =(ImageView)layout.findViewById(R.id.imageView1);
    	if(isExpanded){
    		LogUtil.d("ssssssssssss", "ture");
    		iv.setBackgroundResource(R.drawable.jian);
    	}else{
    		LogUtil.d("ssssssssssss", "false");
    		iv.setBackgroundResource(R.drawable.jia);
    	}
        return layout;  
    }  
    
    //创建子视图  
    public LinearLayout getChildView(BbsVo s) {
    	
    	LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.list_bbs_item, null);
    	TextView item_title = (TextView)layout.findViewById(R.id.BBS_title);
    	item_title.setTextColor(Color.rgb(255, 149, 1));
    	item_title.setText(s.getTopic());
    	TextView theme_count = (TextView)layout.findViewById(R.id.theme_count);
    	theme_count.setTextColor(Color.rgb(208, 207, 203));
    	theme_count.setText(String.valueOf("主题 ：" + s.getAsknumber()));
    	TextView post_count = (TextView)layout.findViewById(R.id.post_count);
    	post_count.setTextColor(Color.rgb(208, 207, 203));
//    	post_count.setText(String.valueOf("贴数 ：" + s.getReplynumber()));
    	post_count.setText("");
        return layout;  
    }
	
	
	@Override
	public boolean hasStableIds() {
		return false;
	}		

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
//		LogUtil.i("TUDE==", "groupPosition:"+groupPosition+"childPosition:"+childPosition);
		return true;
	}
	
}

