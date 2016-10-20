package cn.ucai.fulicenter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    @Bind(R.id.elvCategory)
    ExpandableListView elvCategory;

    ArrayList<CategoryGroupBean> groupList;
    ArrayList<ArrayList<CategoryChildBean>> childList;
    CategoryAdapter mAdapter;
    int parent_id;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        groupList = new ArrayList<>();
        childList = new ArrayList<>();

        initData();
        initView();
        return view;
    }

    private void initData() {

        NetDao.downloadCategoryGroup(getContext(), new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                ArrayList<CategoryGroupBean> been = ConvertUtils.array2List(result);
                if (result != null && result.length > 0) {
                    groupList=been;
                    for (int i = 0; i < result.length; i++) {
                        parent_id = result[i].getId();
                        L.e("parent_id:" + parent_id);
                        downloadChildlist(parent_id);
                    }
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(error);
            }
        });





    }

    private void downloadChildlist(int parent_id) {
        NetDao.downloadCategoryChild(getContext(), parent_id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                ArrayList<CategoryChildBean> bean = ConvertUtils.array2List(result);
                childList.add(bean);
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(error);
            }
        });
    }


    private void initView() {
        mAdapter = new CategoryAdapter(getContext(),groupList,childList);
        elvCategory.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



}
