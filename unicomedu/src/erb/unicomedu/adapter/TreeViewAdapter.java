package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.TreeVo;


public class TreeViewAdapter extends BaseExpandableListAdapter{
	public static   int ItemHeight=77;//每项的高度
	public static final int PaddingLeft=20;//每项的高度
	private int myPaddingLeft=0;//如果是由SuperTreeView调用，则作为子项需要往右移
	
	private LayoutInflater mInflater;
	
    private int color = new Color().rgb(131, 127, 98);
	static public class TreeNode{
		public Object parent;
		public List<Object> childs=new ArrayList<Object>();
	}
	List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	Context parentContext;
	
	public TreeViewAdapter(Context view,int myPaddingLeft)
	{
		parentContext=view;
		this.myPaddingLeft=myPaddingLeft;
		mInflater = LayoutInflater.from(view);
	}
	
	public List<TreeNode> GetTreeNode()
	{
		return treeNodes;
	}
	
	public void UpdateTreeNode(List<TreeNode> nodes)
	{
		treeNodes=nodes;
	}
	
	public void RemoveAll()
	{
		treeNodes.clear();
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return treeNodes.get(groupPosition).childs.get(childPosition);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return treeNodes.get(groupPosition).childs.size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		TextView textView;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.filter_list_items_item, null);
			textView = (TextView)convertView.findViewById(R.id.filter_list_item_item);
			convertView.setTag(textView);
		}
		textView = (TextView)convertView.getTag();
		TreeVo tv = ((TreeVo)getChild(groupPosition, childPosition));
		textView.setText(tv.getTreeName()+"   ( "+tv.getSubjectnumber()+" ) ");
		
		textView.setTag(tv);
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, TreeViewAdapter.ItemHeight);
		convertView.setLayoutParams(lp);
        LogUtil.d("$$$$", convertView.getWidth() + "---he :" +convertView.getHeight());
		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ListTitle listTitle; 
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.filter_list_item, null);
			listTitle = new ListTitle();
			listTitle.mListTitle = (TextView)convertView.findViewById(R.id.filter_list_item);
			listTitle.mListIcon = (ImageView)convertView.findViewById(R.id.filter_list_item_icon);
			convertView.setTag(listTitle);
		}
		listTitle = (ListTitle)convertView.getTag();
		listTitle.mListIcon.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
		TreeVo tv = (TreeVo)getGroup(groupPosition);
		listTitle.mListTitle.setText(tv.getTreeName()+"   ( "+tv.getSubjectnumber()+" ) ");
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,TreeViewAdapter.ItemHeight);
		convertView.setLayoutParams(lp);
		return convertView;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return treeNodes.get(groupPosition).parent;
	}

	@Override
	public int getGroupCount() {
		return treeNodes.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	private class ListTitle{
		TextView mListTitle;
		ImageView mListIcon;
	}
}
