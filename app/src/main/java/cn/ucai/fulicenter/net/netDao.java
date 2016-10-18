package cn.ucai.fulicenter.net;

import android.content.Context;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;

import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NetDao {
    public static void downloadNewGoods(Context context, int pageId,
                                        OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_ID_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
    public static void downloadGoodsDetails(Context context, int goodsId,
                                            OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }
}