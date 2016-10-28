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
import cn.ucai.fulicenter.activity.MyCollectActivity;
import cn.ucai.fulicenter.activity.OrderActivity;
import cn.ucai.fulicenter.activity.PersonalDataActivity;
import cn.ucai.fulicenter.activity.QrcodeActivity;
import cn.ucai.fulicenter.activity.RegisterActivity;
import cn.ucai.fulicenter.activity.SettingActivity;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.fragments.PersonalFragment;

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
        Intent intent = new Intent();
        intent.setClass(context,LoginActivity.class);
        startActivityForResult(context, intent,I.REQUEST_CODE_LOGIN);
    }

    public static void gotoRegisterActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context,RegisterActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_RESGITER);
    }

    public static void startActivityForResult(Activity context,Intent intent,int requestCode){
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoPersonalDataActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, PersonalDataActivity.class);
        startActivity(context,intent);
    }
    public static void gotoQrcodeActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, QrcodeActivity.class);
        startActivity(context,intent);
    }
    public static void gotoMyCollectActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, MyCollectActivity.class);
        startActivity(context,intent);
    }
    public static void gotoOrderActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, OrderActivity.class);
        startActivity(context,intent);
    }



}
