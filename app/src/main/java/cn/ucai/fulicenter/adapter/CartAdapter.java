package cn.ucai.fulicenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/27.
 */
public class CartAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mlist;
    RecyclerView parent;

    public CartAdapter(Context mContext, ArrayList<CartBean> list) {
        this.mContext = mContext;
        this.mlist = list;

    }

    public void initData(ArrayList<CartBean> list) {
        if (mlist != null) {
            mlist.clear();
        }
        mlist.addAll(list);
        //mlist = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        RecyclerView.ViewHolder holder = null;
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
        holder = new CartViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final CartBean cartBean = mlist.get(position);
        L.e("cartBean=====" + cartBean.toString());
        GoodsDetailsBean goods = new GoodsDetailsBean();

        L.e("名字：====" + cartBean.getGoods().getGoodsName());
        ((CartViewHolder) holder).tvGoodsNum.setText("(" + cartBean.getCount() + ")");

        ((CartViewHolder) holder).tvGoodsName.setText(goods.getGoodsName());
        ((CartViewHolder) holder).tvprice.setText(goods.getCurrencyPrice());
        ((CartViewHolder) holder).idCheckbox1.setChecked(cartBean.isChecked());
        ImageLoader.downloadImg(mContext, ((CartViewHolder) holder).ivGoodsPicture, goods.getGoodsThumb());
        ((CartViewHolder) holder).idCheckbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                cartBean.setChecked(b);
                mContext.sendBroadcast(new Intent(I.BROADCASE_UPDATE_CART));
            }
        });

        ((CartViewHolder) holder).ivAddCart.setTag(position);


    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }


    class CartViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.id_checkbox1)
        CheckBox idCheckbox1;
        @Bind(R.id.iv_Goods_picture)
        ImageView ivGoodsPicture;
        @Bind(R.id.tv_goods_name)
        TextView tvGoodsName;
        @Bind(R.id.iv_add_cart)
        ImageView ivAddCart;
        @Bind(R.id.tv_goods_num)
        TextView tvGoodsNum;
        @Bind(R.id.iv_delete_cart)
        ImageView ivDeleteCart;
        @Bind(R.id.tv_price)
        TextView tvprice;

        public CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.id_checkbox1, R.id.iv_add_cart, R.id.iv_delete_cart})
        public void onClick(View view) {
            final int position = (int) ivAddCart.getTag();
            CartBean cart = mlist.get(position);

            switch (view.getId()) {
                case R.id.iv_add_cart:

                    NetDao.updateCart(mContext, cart.getId(), cart.getCount() + 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                mlist.get(position).setCount(mlist.get(position).getCount() + 1);
                                mContext.sendBroadcast(new Intent(I.BROADCASE_UPDATE_CART));
                                tvGoodsNum.setText("(" + mlist.get(position).getCount() + ")");
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });

                    break;
                case R.id.iv_delete_cart:

                    if (cart.getCount() > 1) {
                        NetDao.updateCart(mContext, cart.getId(), cart.getCount() - 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null && result.isSuccess()) {
                                    mlist.get(position).setCount(mlist.get(position).getCount() - 1);
                                    mContext.sendBroadcast(new Intent(I.BROADCASE_UPDATE_CART));
                                    tvGoodsNum.setText("(" + mlist.get(position).getCount() + ")");
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    } else {
                        NetDao.delCart(mContext, cart.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null && result.isSuccess()) {
                                    mlist.remove(position);
                                    mContext.sendBroadcast(new Intent(I.BROADCASE_UPDATE_CART));
                                    notifyDataSetChanged();

                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    }

                    break;

            }
        }

        @OnClick(R.id.iv_Goods_picture)
        public void goDetails() {
            final int position = (int) ivAddCart.getTag();
            CartBean cart = mlist.get(position);
            MFGT.gotoGoodsChildActivity((Activity) mContext, cart.getGoodsId());
        }
    }
}
