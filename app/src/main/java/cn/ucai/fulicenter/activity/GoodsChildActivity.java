package cn.ucai.fulicenter.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.PropertiesBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.view.FlowIndicator;

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
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.myView)
    FlowIndicator myView;
    @Bind(R.id.tvGoodsBrief)
    TextView tvGoodsBrief;
    @Bind(R.id.llGoodsDetails)
    LinearLayout llGoodsDetails;

    ArrayList<GoodsDetailsBean> goodDetailList;
    PropertiesBean[] properties;
    int goodsId;
    int mCount,mFocus;
    ArrayList<PropertiesBean> arraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_child);
        ButterKnife.bind(this);
        //商品的id
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details", "goodsid" + goodsId);
        mCount = myView.getCount();
        mFocus = myView.getmFocus();

        //通过商品的id下载商品详情
        downloadGoodsDetails();
    }

    private void downloadGoodsDetails() {
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(this);
        utils.url(I.SERVER_ROOT+I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Goods.KEY_GOODS_ID,String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
                    @Override
                    public void onSuccess(GoodsDetailsBean result) {
                        L.e("result:"+result);
                        tvGoodsEnglishName.setText(result.getGoodsEnglishName());
                        tvgoodsName.setText(result.getGoodsName());
                        tvShopPrice.setText(result.getCurrencyPrice());
                        tvGoodsBrief.setText(result.getGoodsBrief());
                        properties = result.getProperties();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });


    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.iamgeBack:
                finish();
                break;
        }
    }

    class MyAsyncTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            while (true){
                mFocus = mFocus<mCount-1?mFocus+1:0;
                publishProgress(mFocus);
                SystemClock.sleep(2000);
            }
        }

        @Override
        protected void onProgressUpdate(Integer...values){
            myView.setFocus(values[0]);
            viewPager.setCurrentItem(values[0]);
        }
    }

    class MyAdapter extends PagerAdapter{

        public MyAdapter(){
            for (int i = 0;i<properties.length;i++){
                arraylist.add(properties[i]);
            }
        }

        @Override
        public int getCount() {
            return properties.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

       /* @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView(arraylist.get(position));

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }*/
    }
}
