package cn.ucai.fulicenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends Adapter {
    Context mContext;
    List<NewGoodsBean> mlist;
    boolean isMore;
    String footer;
    int sortBy = I.SORT_BY_ADDTIME_DESC;

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        sortBy();
        notifyDataSetChanged();
    }

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
        isMore = more;
        notifyDataSetChanged();
    }

    public GoodsAdapter(Context mContext, List<NewGoodsBean> list) {
        this.mContext = mContext;
        this.mlist = new ArrayList<>();
        mlist.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new GoodsViewHolder(View.inflate(mContext, R.layout.item_goods, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position)==I.TYPE_FOOTER){
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.tvFooter.setText(getFooterString());
            return;
        }else {
            GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
            NewGoodsBean goods = mlist.get(position);
            goodsViewHolder.tvGoodsName.setText(goods.getGoodsName());
            goodsViewHolder.tvGoodsPrice.setText(goods.getCurrencyPrice());
            ImageLoader.downloadImg(mContext,goodsViewHolder.ivGoodsPicture,goods.getGoodsThumb());

            goodsViewHolder.llNewGoods.setTag(goods.getGoodsId());
        }
    }

    private int getFooterString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 1 : mlist.size() + 1;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }
    class GoodsViewHolder extends ViewHolder{
        @Bind(R.id.ivGoodsPicture)
        ImageView ivGoodsPicture;
        @Bind(R.id.tvGoodsName)
        TextView tvGoodsName;
        @Bind(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @Bind(R.id.llNewGoods)
        LinearLayout llNewGoods;

        public GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.llNewGoods)
        public void onClick() {
            int goodsId = (int) llNewGoods.getTag();
            MFGT.gotoGoodsChildActivity((Activity) mContext,goodsId);
        }
    }

    public void initData(ArrayList<NewGoodsBean> list){
        if (mlist!=null){
            mlist.clear();
        }
        mlist.addAll(list);
        notifyDataSetChanged();
    }


    public void addData(ArrayList<NewGoodsBean> list) {
        mlist.addAll(list);
        notifyDataSetChanged();
    }
    static class FooterViewHolder extends ViewHolder{
        @Bind(R.id.tvFooter)
        TextView tvFooter;

        public FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //商品价格排序方法
    private void sortBy(){
        Collections.sort(mlist, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                int result=0;
                switch (sortBy){
                    case I.SORT_BY_ADDTIME_ASC:
                        result= (int) (Long.valueOf(left.getAddTime())-Long.valueOf(right.getAddTime()));
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result= (int) (Long.valueOf(right.getAddTime())-Long.valueOf(left.getAddTime()));
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(left.getCurrencyPrice())-getPrice(right.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(right.getCurrencyPrice())-getPrice(left.getCurrencyPrice());
                        break;
                }
                return result;
            }
            private int getPrice(String prive){
                prive = prive.substring(prive.indexOf("￥")+1);
                return Integer.valueOf(prive);
            }
        });
    }
}
