package cn.ucai.fulicenter.activity;

import android.os.Bundle;
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
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.fragments.NewGoodsFragment;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class BoutiquesActivity extends AppCompatActivity {

    @Bind(R.id.iamgeBack)
    ImageView iamgeBack;
    @Bind(R.id.tvTitles)
    TextView tvTitles;
    @Bind(R.id.rlGoodsDetails)
    RelativeLayout rlGoodsDetails;
    @Bind(R.id.rvGoods)
    RecyclerView rvGoods;

    int boutiqueId;
    GoodsAdapter mAdapter;
    GridLayoutManager Mymanager;
    ArrayList<NewGoodsBean> mList;
    int mPage = 10;
    int Page_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique);
        ButterKnife.bind(this);
        boutiqueId = getIntent().getIntExtra(I.Boutique.ID,0);
        L.e("boutiqueId:"+boutiqueId);
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(this,mList);
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setOnPullUpListener();
    }

    private void setOnPullUpListener() {
        rvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                lastPosition = Mymanager.findLastVisibleItemPosition();

                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.notifyDataSetChanged();
                }
                if (lastPosition >= mAdapter.getItemCount() - 1
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mAdapter.isMore()) {
                    Page_id += 1;
                    downloadBoutiques(I.ACTION_PULL_UP, Page_id);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition = Mymanager.findLastVisibleItemPosition();
            }
        });
    }

    private void initData() {
        downloadBoutiques(I.ACTION_DOWNLOAD,Page_id);
    }

    private void downloadBoutiques(final int action, int page_id) {
        final OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(this);
        utils.url(I.SERVER_ROOT + I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, boutiqueId + "")
                .addParam(I.PAGE_ID, page_id + "")
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
                                    mAdapter.setFooter("加载更多数据");
                                    break;
                                case I.ACTION_PULL_UP:
                                    mAdapter.addData(goodslist);
                                    break;
                                case I.ACTION_PULL_DOWN:
                                    mAdapter.initData(goodslist);
                                    mAdapter.setFooter("加载更多数据");
                                    ImageLoader.release();
                                    break;
                            }
                        }
                        if (action == I.ACTION_PULL_UP) {
                            mAdapter.setFooter("没有更多数据可加载");
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }


    private void initView() {
        Mymanager = new GridLayoutManager(this,I.COLUM_NUM, LinearLayoutManager.VERTICAL,false);
        rvGoods.setLayoutManager(Mymanager);
        rvGoods.setHasFixedSize(true);
        rvGoods.setAdapter(mAdapter);
        rvGoods.addItemDecoration(new SpaceItemDecoration(12));


    }

    @OnClick(R.id.iamgeBack)
    public void onClick() {
        finish();
    }
}
