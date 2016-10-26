package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CollectAdapter;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class MyCollectActivity extends AppCompatActivity {


    MyCollectActivity mContext;
    User user;
    int pageId = 1;
    ArrayList<CollectBean> mlist;
    GridLayoutManager MyManager;
    CollectAdapter mAdapter;
    @Bind(R.id.iv_Back)
    ImageView ivBack;
    @Bind(R.id.rl_Collects_goods)
    RelativeLayout rlCollectsGoods;
    @Bind(R.id.tv_Refresh)
    TextView tvRefresh;
    @Bind(R.id.rv_Collects)
    RecyclerView rvCollects;
    @Bind(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        ButterKnife.bind(this);
        mContext = this;
        mlist = new ArrayList<>();
        initView();
        initData();
        setListener();
    }

    private void initView() {
        MyManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false);
        mAdapter = new CollectAdapter(mContext, mlist);
        rvCollects.setAdapter(mAdapter);
        rvCollects.setLayoutManager(MyManager);
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        //设置是否自动修复
        rvCollects.setHasFixedSize(true);
        rvCollects.addItemDecoration(new SpaceItemDecoration(12));
    }

    private void setListener() {
        setOnPullUpListener();
        setOnPullDownListener();
    }

    private void setOnPullDownListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.setEnabled(true);
                tvRefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                downloadCollect(I.ACTION_PULL_DOWN);
            }
        });
    }


    private void setOnPullUpListener() {
        rvCollects.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastposition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastposition = MyManager.findLastVisibleItemPosition();
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.notifyDataSetChanged();
                }
                if (lastposition >= mAdapter.getItemCount() - 1
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mAdapter.isMore()) {
                    pageId++;
                    downloadCollect(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastposition = MyManager.findLastVisibleItemPosition();
            }
        });
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        }
        downloadCollect(I.ACTION_DOWNLOAD);
    }

    private void downloadCollect(final int action) {
        NetDao.downloadCollect(mContext, user.getMuserName(), pageId, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                mSwipeRefreshLayout.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if (result != null && result.length > 0) {
                    ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(list);
                    } else {
                        mAdapter.addData(list);
                    }
                    if (list.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                    }
                } else {
                    mAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                mSwipeRefreshLayout.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                CommonUtils.showShortToast(error);
                L.e("error" + error);
            }
        });
    }


    @OnClick(R.id.iv_Back)
    public void onClick() {
        finish();
    }
}
