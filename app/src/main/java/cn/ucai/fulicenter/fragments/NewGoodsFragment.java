package cn.ucai.fulicenter.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;

import cn.ucai.fulicenter.utils.ImageLoader;

import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mtvRefresh;
    RecyclerView mrvNewGoods;
    ArrayList<NewGoodsBean> mNewGoodsList;


    int mPage = 10;
    int Page_id = 1;
    NewGoodsAdapter mAdapter;
    GridLayoutManager mgridLayoutManager;

    int mNewState;

    public NewGoodsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        mNewGoodsList = new ArrayList<>();
        mgridLayoutManager = new GridLayoutManager(getContext(),I.COLUM_NUM,LinearLayoutManager.VERTICAL,false);
        mAdapter = new NewGoodsAdapter(getActivity(),mNewGoodsList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        mtvRefresh = (TextView) view.findViewById(R.id.tvRefresh);
        mrvNewGoods = (RecyclerView) view.findViewById(R.id.rvNewGoods);
        mrvNewGoods.setAdapter(mAdapter);
        //设置是否自动修复
        mrvNewGoods.setHasFixedSize(true);
        mrvNewGoods.setLayoutManager(mgridLayoutManager);
        setListener();

        mgridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position==mAdapter.getItemCount()-1?2:1;
            }
        });
        //判断最后，网格布局两行合并变一行
        if (Page_id==1){
            downloadNewGoods(I.ACTION_DOWNLOAD,Page_id);
        }else {
            setPullUpListener();//上拉加载
        }
        return view;
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        mrvNewGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    Page_id+=1;
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.setEnabled(true);
                mtvRefresh.setVisibility(View.VISIBLE);
                Page_id = 1;
                downloadNewGoods(I.ACTION_PULL_DOWN, Page_id);

            }
        });
    }

    private void downloadNewGoods(final int action, int Page_id) {
        final OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>();
        utils.url(I.SERVER_ROOT + I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, I.CAT_ID + "")
                .addParam(I.PAGE_ID, Page_id + "")
                .addParam(I.PAGE_SIZE, mPage + "")
                .targetClass(NewGoodsBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        if (result!=null){
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
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mtvRefresh.setVisibility(View.GONE);
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

    class NewGoodsHolder extends RecyclerView.ViewHolder {
        ImageView mivGoodsPicture;
        TextView mtvGoodsName, mtvGoodsPrice;

        public NewGoodsHolder(View itemView) {
            super(itemView);
            mivGoodsPicture = (ImageView) itemView.findViewById(R.id.ivGoodsPicture);
            mtvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            mtvGoodsPrice = (TextView) itemView.findViewById(R.id.tvGoodsPrice);
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

            /*ImageLoader.build(I.SERVER_ROOT + I.REQUEST_DOWNLOAD_IMAGE)
                    .addParam(I.IMAGE_URL, goods.getGoodsImg())
                    .defaultPicture(R.drawable.nopic)
                    .width(150)
                    .height(150)
                    .imageView(newGoodsHolder.mivGoodsPicture)
                    .listener(parent)
                    .saveFileName(goods.getGoodsName())
                    .setDragging(mNewState!=RecyclerView.SCROLL_STATE_DRAGGING)
                    .showImage(context);*/
            ImageLoader.downloadImg(context,newGoodsHolder.mivGoodsPicture,goods.getGoodsThumb());
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
