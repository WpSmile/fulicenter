package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiquesActivity;
import cn.ucai.fulicenter.activity.CategoryActivity;
import cn.ucai.fulicenter.activity.GoodsChildActivity;
import cn.ucai.fulicenter.activity.LoginActivity;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.RegisterActivity;
import cn.ucai.fulicenter.bean.CategoryChildBean;

/**
 * 实现跳转
 */
public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);
    }
    public static void gotoGoodsChildActivity(Activity context,int goodsId){
        Intent intent = new Intent();
        intent.setClass(context, GoodsChildActivity.class);
        intent.putExtra(I.Goods.KEY_GOODS_ID,goodsId);
        startActivity(context,intent);
    }
    public static void gotoBoutiquesActivity(Activity context,int BoutiqueId){
        Intent intent = new Intent();
        intent.setClass(context, BoutiquesActivity.class);
        intent.putExtra(I.Boutique.ID,BoutiqueId);
        startActivity(context,intent);
    }
    public static void gotoCategoryActivity(Activity context, int categoryChildId, String groupName, ArrayList<CategoryChildBean> list){
        Intent intent = new Intent();
        intent.setClass(context, CategoryActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,categoryChildId);
        intent.putExtra(I.CategoryGroup.NAME,groupName);
        intent.putExtra(I.CategoryChild.ID,list);
        startActivity(context,intent);
    }

    public static void startActivity(Activity context,Intent intent){
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoLoginActivity(Activity context){
        startActivity(context, LoginActivity.class);
    }
    public static void gotoLoginActivity(Activity context,String username,String password){
        Intent intent = new Intent();
        intent.setClass(context,LoginActivity.class);
        intent.putExtra(I.User.USER_NAME,username);
        intent.putExtra(I.User.PASSWORD,password);
        startActivity(context, intent);
    }
    public static void gotoRegisterActivity(Activity context){
        startActivity(context, RegisterActivity.class);
    }


}
