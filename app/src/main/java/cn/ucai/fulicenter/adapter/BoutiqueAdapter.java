package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/18.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter{
    Context mContext;
    ArrayList<BoutiqueBean> boutiquelist;

    RecyclerView parent;
    boolean isMore;
    String footer;

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
        notifyDataSetChanged();
    }

    public int getFooterString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> boutiquelist) {
        this.mContext = mContext;
        this.boutiquelist = boutiquelist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        RecyclerView.ViewHolder holder = null;
        View layout = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case I.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, parent, false);
                holder = new GoodsAdapter.FooterViewHolder(layout);
                break;
            case I.TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_boutique, parent, false);
                holder = new BoutiqueViewHolder(layout);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.tvFooter.setText(footer);
        } else {
            BoutiqueViewHolder boutiqueHolder = (BoutiqueViewHolder) holder;
            BoutiqueBean boutique = boutiquelist.get(position);
            boutiqueHolder.tvTitle.setText(boutique.getTitle());
            boutiqueHolder.tvDescription.setText(boutique.getDescription());
            boutiqueHolder.tvName.setText(boutique.getName());
            ImageLoader.downloadImg(mContext, boutiqueHolder.ivGoodsImage, boutique.getImageurl());
        }
        if (holder instanceof GoodsAdapter.FooterViewHolder){
            ((GoodsAdapter.FooterViewHolder)holder).tvFooter.setText(getFooterString());
        }
        if (holder instanceof BoutiqueViewHolder){
            BoutiqueBean boutique = boutiquelist.get(position);
            ((BoutiqueViewHolder)holder).tvTitle.setText(boutique.getTitle());
            ((BoutiqueViewHolder)holder).tvDescription.setText(boutique.getDescription());
            ((BoutiqueViewHolder)holder).tvName.setText(boutique.getName());
            ImageLoader.downloadImg(mContext, ((BoutiqueViewHolder)holder).ivGoodsImage, boutique.getImageurl());
        }

    }

    @Override
    public int getItemCount() {
        return boutiquelist == null ? 1 : boutiquelist.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }
    public void initData(ArrayList<BoutiqueBean> list){
        if (boutiquelist!=null){
            boutiquelist.clear();
        }
        boutiquelist.addAll(list);
        notifyDataSetChanged();
    }
    public void addData(ArrayList<BoutiqueBean> list) {
        boutiquelist.addAll(list);
        notifyDataSetChanged();
    }




    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvFooter)
        TextView tvFooter;

        public FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivGoodsImage)
        ImageView ivGoodsImage;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvDescription)
        TextView tvDescription;
        @Bind(R.id.tvName)
        TextView tvName;

        public BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
