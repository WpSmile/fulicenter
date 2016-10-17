package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends Adapter {
    Context mContext;
    List<NewGoodsBean> mlist;
    String footer;

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
            footerHolder.tvFooter.setText(footer);
            return;
        }else {
            GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
            NewGoodsBean goods = mlist.get(position);
            goodsViewHolder.tvGoodsName.setText(goods.getGoodsName());
            goodsViewHolder.tvGoodsPrice.setText(goods.getCurrencyPrice());
            ImageLoader.downloadImg(mContext,goodsViewHolder.ivGoodsPicture,goods.getGoodsThumb());
        }
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
    static class GoodsViewHolder extends ViewHolder{
        @Bind(R.id.ivGoodsPicture)
        ImageView ivGoodsPicture;
        @Bind(R.id.tvGoodsName)
        TextView tvGoodsName;
        @Bind(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;

        public GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void initData(ArrayList<NewGoodsBean> list){
        if (mlist!=null){
            mlist.clear();
        }
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
}
