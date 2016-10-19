package cn.ucai.fulicenter.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryGroupBean;


/**
 * Created by Administrator on 2016/10/19.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mgroupList;
    ArrayList<ArrayList<CategoryGroupBean>> mchildList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> mgroupList, ArrayList<ArrayList<CategoryGroupBean>> mchildList) {
        this.mContext = mContext;
        this.mgroupList = mgroupList;
        this.mchildList = mchildList;
    }

    @Override
    public int getGroupCount() {
        return mgroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mchildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mgroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mchildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_group, null);
            holder = new GroupViewHolder();
            holder.ivExpandImage = (ImageView) convertView.findViewById(R.id.ivExpandImage);
            holder.ivGroupImage = (ImageView) convertView.findViewById(R.id.ivGroupImage);
            convertView.setTag(holder);
        }else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.ivGroupImage.setImageResource(mgroupList.get(groupPosition).getId());
        if(isExpanded){//若是展开状态
            holder.ivExpandImage.setImageResource(R.mipmap.expand_off);
        }else{
            holder.ivExpandImage.setImageResource(R.mipmap.expand_on);
        }
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        convertView = View.inflate(mContext,R.layout.item_child,null);
        holder.ivChildIamge = (ImageView) convertView.findViewById(R.id.ivChildIamge);

        holder.ivChildIamge.setImageResource(mchildList.get(groupPosition).get(childPosition).getId());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder{
        ImageView ivGroupImage,ivExpandImage;
    }

    class ChildViewHolder{
        ImageView ivChildIamge;
    }
}
