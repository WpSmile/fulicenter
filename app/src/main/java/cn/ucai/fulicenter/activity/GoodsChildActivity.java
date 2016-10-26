package cn.ucai.fulicenter.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.PropertiesBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
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

        if (goodsId==0){
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
                if (result!=null){
                    showGoodDetails(result);
                }else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("details,error="+error);
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean details) {
        tvGoodsEnglishName.setText(details.getGoodsEnglishName());
        tvgoodsName.setText(details.getGoodsName());
        tvShopPrice.setText(details.getCurrencyPrice());
        tvGoodsBrief.setText(details.getGoodsBrief());
        slideAutoLoopView.startPlayLoop(myView,getAlbumImgUrl(details),getAlbumImgCount(details));
    }

    private int getAlbumImgCount(GoodsDetailsBean details) {
        if (details.getProperties()!=null&&details.getProperties().length>0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] url = new String[]{};
        if (details.getProperties()!=null&&details.getProperties().length>0){
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            url = new String[albums.length];
            for (int i=0;i<albums.length;i++){
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
                if (user==null){
                    MFGT.gotoLoginActivity(mContext);
                }else {
                    if (isCollected){
                        NetDao.deleteCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result!=null&&result.isSuccess()){
                                    isCollected=!isCollected;
                                    updateGoodsCollectedStatus();
                                    CommonUtils.showLongToast(result.getMsg());
                                }
                            }

                            @Override
                            public void onError(String error) {
                                CommonUtils.showLongToast(error);
                            }
                        });
                    }else {
                        NetDao.addCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                isCollected=!isCollected;
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollected();
    }

    private void isCollected() {
        String name = FuLiCenterApplication.getUser().getMuserName();
        if (name!=null) {
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
        if (isCollected){
            imageCollect.setImageResource(R.mipmap.bg_collect_out);
        }else {
            imageCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }


}
