package cn.ucai.fulicenter.fragments;


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
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
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
    NewGoodsAdapter mAdapter;
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
        //adapter = new GoodsAdapter(getActivity(), mNewGoodsList);
        mAdapter = new NewGoodsAdapter(getContext(), mNewGoodsList);
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

    /*private void initData() {
        NetDao.downloadNewGoods(getContext(), Page_id, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                Goodsrl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if (result!=null&&result.length>0){
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    adapter.initData(list);
                    if(list.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                    }
                }else{
                    mAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                Goodsrl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                CommonUtils.showShortToast(error);
                L.e("error"+error);
            }
        });
    }*/

    /*private void initView() {

    }*/

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
                                    mAdapter.initGoods(goodslist);
                                    mAdapter.setFooter("加载更多数据");
                                    break;
                                case I.ACTION_PULL_UP:
                                    mAdapter.addGoods(goodslist);
                                    break;
                                case I.ACTION_PULL_DOWN:
                                    mAdapter.initGoods(goodslist);
                                    mAdapter.setFooter("加载更多数据");
                                    //  mSwipeRefreshLayout.setEnabled(false);
                                    Goodsrl.setRefreshing(false);
                                    tvRefresh.setVisibility(View.GONE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class NewGoodsHolder extends RecyclerView.ViewHolder {
        ImageView mivGoodsPicture;
        TextView mtvGoodsName, mtvGoodsPrice;
        @Bind(R.id.llNewGoods)
        LinearLayout llNewGoods;

        public NewGoodsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mivGoodsPicture = (ImageView) itemView.findViewById(R.id.ivGoodsPicture);
            mtvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            mtvGoodsPrice = (TextView) itemView.findViewById(R.id.tvGoodsPrice);
        }
        @OnClick(R.id.llNewGoods)
        public void onClick() {
            int goodsId = (int) llNewGoods.getTag();
            getContext().startActivity(new Intent(getContext(), GoodsChildActivity.class)
            .putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId));
        }
    }


    class FooterHolder extends RecyclerView.ViewHolder {
        TextView mtvFooter;


        public FooterHolder(View itemView) {
            super(itemView);

            mtvFooter = (TextView) itemView.findViewById(R.id.tvFooter);
        }
    }

    class NewGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<NewGoodsBean> newGoodsList;


        boolean isMore;

        String footer;
        RecyclerView parent;


        public String getFooter() {
            return footer;
        }

        public void setFooter(String footer) {
            this.footer = footer;
            notifyDataSetChanged();
        }

        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean more) {
            this.isMore = more;
        }


        public void addGoods(ArrayList<NewGoodsBean> contastList) {
            this.newGoodsList.addAll(contastList);
            notifyDataSetChanged();


        }

        public void removeGoods(int i) {
            newGoodsList.remove(i);
            notifyDataSetChanged();
        }

        public void initGoods(ArrayList<NewGoodsBean> contastList) {
            this.newGoodsList.clear();
            this.newGoodsList.addAll(contastList);
            notifyDataSetChanged();

        }

        public NewGoodsAdapter(Context context, ArrayList<NewGoodsBean> newGoodsList) {
            this.context = context;
            this.newGoodsList = newGoodsList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            this.parent = (RecyclerView) parent;
            LayoutInflater inflater = LayoutInflater.from(context);

            View layout = null;
            switch (viewType) {
                case I.TYPE_ITEM:
                    layout = inflater.inflate(R.layout.item_goods, parent, false);
                    holder = new NewGoodsHolder(layout);
                    break;
                case I.TYPE_FOOTER:
                    layout = inflater.inflate(R.layout.item_footer, parent, false);
                    holder = new FooterHolder(layout);
                    break;
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (getItemViewType(position) == I.TYPE_FOOTER) {
                FooterHolder footerHolder = (FooterHolder) holder;
                footerHolder.mtvFooter.setText(footer);
                return;
            }

            NewGoodsHolder newGoodsHolder = (NewGoodsHolder) holder;
            NewGoodsBean goods = mNewGoodsList.get(position);
            newGoodsHolder.mtvGoodsName.setText(goods.getGoodsName());
            newGoodsHolder.mtvGoodsPrice.setText(goods.getCurrencyPrice());
            ImageLoader.downloadImg(context, newGoodsHolder.mivGoodsPicture, goods.getGoodsThumb());

            newGoodsHolder.llNewGoods.setTag(goods.getGoodsId());
        }

        @Override
        public int getItemCount() {
            return newGoodsList == null ? 1 : newGoodsList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return I.TYPE_FOOTER;
            }
            return I.TYPE_ITEM;
        }



    }

}
