package cn.ucai.fulicenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CollectAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CollectBean> mlist;
    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public CollectAdapter(Context mContext, ArrayList<CollectBean> list) {
        this.mContext = mContext;
        this.mlist = new ArrayList<>();
        mlist.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new CollectViewHolder(View.inflate(mContext, R.layout.item_collects, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.tvFooter.setText(getFooterString());
            return;
        } else {
            CollectViewHolder collectHolder = (CollectViewHolder) holder;
            CollectBean goods = mlist.get(position);
            collectHolder.tvGoodsName.setText(goods.getGoodsName());
            ImageLoader.downloadImg(mContext, collectHolder.ivGoodsPicture, goods.getGoodsThumb());

            collectHolder.llCollect.setTag(goods);
        }
    }

    private int getFooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
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


    public void initData(ArrayList<CollectBean> list) {
        if (mlist != null) {
            mlist.clear();
        }
        mlist.addAll(list);
        notifyDataSetChanged();
    }


    public void addData(ArrayList<CollectBean> list) {
        mlist.addAll(list);
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

    class CollectViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivGoodsPicture)
        ImageView ivGoodsPicture;
        @Bind(R.id.tvGoodsName)
        TextView tvGoodsName;
        @Bind(R.id.iv_collect_del)
        ImageView ivCollectDel;
        @Bind(R.id.llCollect)
        RelativeLayout llCollect;

        public CollectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.iv_collect_del, R.id.llCollect})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_collect_del:
                    final CollectBean goods1 = (CollectBean) llCollect.getTag();
                    String name = FuLiCenterApplication.getUser().getMuserName();
                    NetDao.deleteCollect(mContext, name, goods1.getGoodsId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result!=null&&result.isSuccess()){
                                mlist.remove(goods1);
                                notifyDataSetChanged();
                            }else {
                                CommonUtils.showLongToast(result!=null?result.getMsg():
                                        mContext.getResources().getString(R.string.delete_collect_fail));
                            }
                        }

                        @Override
                        public void onError(String error) {
                            L.e("error===="+error);
                            CommonUtils.showLongToast(R.string.delete_collect_fail);
                        }
                    });
                    break;
                case R.id.llCollect:
                    CollectBean goods = (CollectBean) llCollect.getTag();
                    MFGT.gotoGoodsChildActivity((Activity) mContext,goods.getGoodsId());
                    break;
            }
        }
    }


}
