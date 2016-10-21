package cn.ucai.fulicenter.net;

import android.content.Context;
import android.database.CursorJoiner;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;

import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MD5;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NetDao {
    public static void downloadNewGoods(Context context, int pageId,
                                        OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    public static void downloadGoodsDetails(Context context, int goodsId,
                                            OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener) {
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID, String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    public static void downloadBoutiques(Context context, OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener) {
        OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }

    public static void downloadCategoryGroup(Context context, OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener) {
        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    public static void downloadCategoryChild(Context context, int pageId,
                                             OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener) {
        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, String.valueOf(pageId))
                .addParam(I.PAGE_ID, String.valueOf(I.CATEGORY_GOOD))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }

    public static void register(Context context, String username, String usernick, String password
            , OkHttpUtils.OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.NICK,usernick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .post()
                .execute(listener);

    }
}
