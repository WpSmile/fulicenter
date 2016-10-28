package cn.ucai.fulicenter.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.GoodsChildActivity;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {

    ArrayList<NewGoodsBean> mNewGoodsList;


    int mPage = 10;
    int Page_id = 1;
    //GoodsAdapter adapter;
    GoodsAdapter mAdapter;
    GridLayoutManager mgridLayoutManager;

    int mNewState;
    @Bind(R.id.tvRefresh)
    TextView tvRefresh;
    @Bind(R.id.rvNewGoods)
    RecyclerView rvNewGoods;
    @Bind(R.id.Goodsrl)
    SwipeRefreshLayout Goodsrl;
    @Bind(R.id.newGoodsFragment)
    FrameLayout newGoodsFragment;

    public NewGoodsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, view);
        mNewGoodsList = new ArrayList<>();
        mAdapter = new GoodsAdapter(getContext(), mNewGoodsList);
        Goodsrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        mgridLayoutManager = new GridLayoutManager(getContext(), I.COLUM_NUM, LinearLayoutManager.VERTICAL, false);
        rvNewGoods.setLayoutManager(mgridLayoutManager);
        //设置是否自动修复
        rvNewGoods.setHasFixedSize(true);
        rvNewGoods.setAdapter(mAdapter);
        rvNewGoods.addItemDecoration(new SpaceItemDecoration(12));
        mgridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == mAdapter.getItemCount() - 1 ? 2 : 1;
            }
        });
        //判断最后，网格布局两行合并变一行
        if (Page_id == 1) {
            downloadNewGoods(I.ACTION_DOWNLOAD, Page_id);
        } else {
            //上拉加载
            setPullUpListener();
        }

        //initData();
        setListener();


        return view;
    }


    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rvNewGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mNewState = newState;
                lastPosition = mgridLayoutManager.findLastVisibleItemPosition();

                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.notifyDataSetChanged();
                }
                if (lastPosition >= mAdapter.getItemCount() - 1
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mAdapter.isMore()) {
                    Page_id += 1;
                    downloadNewGoods(I.ACTION_PULL_UP, Page_id);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition = mgridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void setPullDownListener() {
        Goodsrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Goodsrl.setRefreshing(true);
                Goodsrl.setEnabled(true);
                tvRefresh.setVisibility(View.VISIBLE);
                Page_id = 1;
                downloadNewGoods(I.ACTION_PULL_DOWN, Page_id);

            }
        });
    }

    private void downloadNewGoods(final int action, int Page_id) {
        final OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(getContext());
        utils.url(I.SERVER_ROOT + I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, I.CAT_ID + "")
                .addParam(I.PAGE_ID, Page_id + "")
                .addParam(I.PAGE_SIZE, mPage + "")
                .targetClass(NewGoodsBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        if (result != null) {
                            mAdapter.setMore(true);
                            ArrayList<NewGoodsBean> goodslist = utils.array2List(result);
                            switch (action) {
                                case I.ACTION_DOWNLOAD:
                                    mAdapter.initData(goodslist);
                                    break;
                                case I.ACTION_PULL_UP:
                                    mAdapter.addData(goodslist);
                                    break;
                                case I.ACTION_PULL_DOWN:
                                    mAdapter.initData(goodslist);
                                    Goodsrl.setRefreshing(false);
                                    tvRefresh.setVisibility(View.GONE);
                                    ImageLoader.release();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
