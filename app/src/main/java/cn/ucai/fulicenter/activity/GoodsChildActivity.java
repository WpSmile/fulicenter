package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsChildActivity extends AppCompatActivity {

    @Bind(R.id.iamgeBack)
    ImageView iamgeBack;
    @Bind(R.id.imageShare)
    ImageView imageShare;
    @Bind(R.id.imageCollect)
    ImageView imageCollect;
    @Bind(R.id.imageCartSelect)
    ImageView imageCartSelect;
    @Bind(R.id.rlGoodsDetails)
    RelativeLayout rlGoodsDetails;
    @Bind(R.id.tvGoodsEnglishName)
    TextView tvGoodsEnglishName;
    @Bind(R.id.tvgoodsName)
    TextView tvgoodsName;
    @Bind(R.id.tvShopPrice)
    TextView tvShopPrice;
    @Bind(R.id.slideAutoLoopView)
    SlideAutoLoopView slideAutoLoopView;
    @Bind(R.id.myView)
    FlowIndicator myView;
    @Bind(R.id.tvGoodsBrief)
    TextView tvGoodsBrief;


    int goodsId;

    GoodsChildActivity mContext;
    boolean isCollected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_child);
        ButterKnife.bind(this);
        //商品的id
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details", "goodsid" + goodsId);

        if (goodsId == 0) {
            finish();
        }
        mContext = this;
        initView();
        initData();
        setListener();

    }

    private void setListener() {

    }

    private void initData() {
        NetDao.downloadGoodsDetails(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    showGoodDetails(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("details,error=" + error);
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean details) {
        tvGoodsEnglishName.setText(details.getGoodsEnglishName());
        tvgoodsName.setText(details.getGoodsName());
        tvShopPrice.setText(details.getCurrencyPrice());
        tvGoodsBrief.setText(details.getGoodsBrief());
        slideAutoLoopView.startPlayLoop(myView, getAlbumImgUrl(details), getAlbumImgCount(details));
    }

    private int getAlbumImgCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] url = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            url = new String[albums.length];
            for (int i = 0; i < albums.length; i++) {
                url[i] = albums[i].getImgUrl();
            }
        }
        return url;

    }

    private void initView() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iamgeBack:
                finish();
                break;
            case R.id.imageCollect:
                //isCollected();
                User user = FuLiCenterApplication.getUser();
                if (user == null) {
                    MFGT.gotoLoginActivity(mContext);
                } else {
                    if (isCollected) {
                        NetDao.deleteCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null && result.isSuccess()) {
                                    isCollected = !isCollected;
                                    updateGoodsCollectedStatus();
                                    CommonUtils.showLongToast(result.getMsg());
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    } else {
                        NetDao.addCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                isCollected = !isCollected;
                                updateGoodsCollectedStatus();
                                CommonUtils.showLongToast(result.getMsg());
                            }

                            @Override
                            public void onError(String error) {
                                CommonUtils.showLongToast(error);
                            }
                        });
                    }
                }
                break;
            case R.id.imageShare:
                ShareSDK.initSDK(this);
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

                // title标题：微信、QQ（新浪微博不需要标题）
                oks.setTitle("我是分享标题");  //最多30个字符

                // text是分享文本：所有平台都需要这个字段
                oks.setText("我是分享文本，啦啦啦~http://uestcbmi.com/");  //最多40个字符

                // imagePath是图片的本地路径：除Linked-In以外的平台都支持此参数
                //oks.setImagePath(Environment.getExternalStorageDirectory() + "/meinv.jpg");//确保SDcard下面存在此张图片

                //网络图片的url：所有平台
                oks.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul

                // url：仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情

                // Url：仅在QQ空间使用
                oks.setTitleUrl("http://www.baidu.com");  //网友点进链接后，可以看到分享的详情

                // 启动分享GUI
                oks.show(this);
                break;
            case R.id.imageCartSelect:
                addCart();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollected();
    }

    private void isCollected() {
        String name = FuLiCenterApplication.getUser().getMuserName();
        if (name != null) {
            NetDao.isCollected(mContext, name, goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollected = true;
                    } else {
                        isCollected = false;
                    }
                    updateGoodsCollectedStatus();
                }

                @Override
                public void onError(String error) {
                    isCollected = false;
                    updateGoodsCollectedStatus();
                }
            });
        }
        updateGoodsCollectedStatus();
    }

    private void updateGoodsCollectedStatus() {
        if (isCollected) {
            imageCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            imageCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    private void addCart() {
        User u = FuLiCenterApplication.getUser();
        if (u!=null) {
            NetDao.addCart(mContext, goodsId, u.getMuserName(), 1,true, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        CommonUtils.showLongToast(R.string.add_cart_success);
                        //imageCartSelect.setImageResource(R.mipmap.menu_item_cart_selected);
                    } else {
                        CommonUtils.showLongToast(R.string.add_cart_fail);
                    }
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showLongToast(R.string.add_cart_fail);
                }
            });
        }else {
            MFGT.gotoLoginActivity(mContext);
        }
    }

}
