package cn.ucai.fulicenter.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;


/**
 * Created by Administrator on 2016/10/19.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    static Context mContext;
    ArrayList<CategoryGroupBean> mgroupList;
    ArrayList<ArrayList<CategoryChildBean>> mchildList;

    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }
    public int getFooterString(){
        return isMore?R.string.load_more:R.string.no_more;
    }

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> mgroupList, ArrayList<ArrayList<CategoryChildBean>> mchildList) {
        this.mContext = mContext;
        this.mgroupList = mgroupList;
        this.mchildList = mchildList;
    }

    @Override
    public int getGroupCount() {

        return mgroupList != null ? mgroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return mchildList != null && mchildList.get(groupPosition) != null ?
                mchildList.get(groupPosition).size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mgroupList != null ? mgroupList.get(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mchildList != null && mchildList.get(groupPosition) != null ?
                mchildList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {

        return mgroupList.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mchildList.get(groupPosition).get(childPosition).getId();
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
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        ImageLoader.downloadImg(mContext, holder.ivGroupImage, mgroupList.get(groupPosition).getImageUrl());
        holder.tvGroupName.setText(mgroupList.get(groupPosition).getName());


        //holder.tvGroupName.setTag(mgroupList.get(groupPosition).getId());

        if (isExpanded) {//若是展开状态
            holder.ivExpandImage.setImageResource(R.mipmap.expand_off);
        } else {
            holder.ivExpandImage.setImageResource(R.mipmap.expand_on);
        }
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_child, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        ImageLoader.downloadImg(mContext, holder.ivChildIamge, mchildList.get(groupPosition).get(childPosition).getImageUrl());
        holder.tvChildName.setText(mchildList.get(groupPosition).get(childPosition).getName());

        holder.tvChildName.setTag(mchildList.get(groupPosition).get(childPosition).getId());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    public void initData1(ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        if (mgroupList!=null){
            mgroupList.clear();
        }
        mgroupList.addAll(groupList);
        if (mchildList!=null){
            mchildList.clear();
        }
        mchildList.addAll(childList);
        notifyDataSetChanged();
    }


    static class ChildViewHolder {
        @Bind(R.id.ivChildIamge)
        ImageView ivChildIamge;
        @Bind(R.id.tvChildName)
        TextView tvChildName;

        ChildViewHolder(View view) {

            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.tvChildName)
        public void onClick() {
            int child_Id = (int) tvChildName.getTag();
            MFGT.gotoCategoryActivity((Activity) mContext,child_Id);
        }

    }

    static class GroupViewHolder {
        @Bind(R.id.ivGroupImage)
        ImageView ivGroupImage;
        @Bind(R.id.tvGroupName)
        TextView tvGroupName;
        @Bind(R.id.ivExpandImage)
        ImageView ivExpandImage;


        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
/*
        @OnClick(R.id.tvGroupName)
        public void onClick() {
            int group_Id = (int) tvGroupName.getTag();
            L.e("mama_Id:"+group_Id);
            MFGT.gotoCategoryActivity((Activity) mContext, group_Id);
        }*/
    }
}