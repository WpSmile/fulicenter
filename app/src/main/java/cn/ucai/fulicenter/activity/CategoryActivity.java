package cn.ucai.fulicenter.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CategoryActivity extends AppCompatActivity {

    @Bind(R.id.iamgeBack)
    ImageView iamgeBack;
    @Bind(R.id.tvTitles)
    TextView tvTitles;
    @Bind(R.id.rlGoodsDetails)
    RelativeLayout rlGoodsDetails;
    @Bind(R.id.tvPrice)
    Button tvPrice;
    @Bind(R.id.tvTime)
    Button tvTime;
    @Bind(R.id.rvCategory)
    RecyclerView rvCategory;

    boolean addTimeAsc = false;
    boolean priceAsc = false;
    int sortBy = I.SORT_BY_ADDTIME_DESC;
    int child_id;
    int pageId = 1;
    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager Mymanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        child_id = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        L.e("group_id:" + child_id);
        mList = new ArrayList<>();
        initView();
        initData();

    }

    private void initData() {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(this);
        utils.url(I.SERVER_ROOT + I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, String.valueOf(child_id))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(10))
                .targetClass(NewGoodsBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        if (result != null && result.length > 0) {
                            ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                            mAdapter.initData(list);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showShortToast(error);
                    }
                });

    }

    private void initView() {
        mAdapter = new GoodsAdapter(this, mList);
        Mymanager = new GridLayoutManager(this, I.COLUM_NUM, LinearLayoutManager.VERTICAL, false);
        rvCategory.setAdapter(mAdapter);
        rvCategory.setLayoutManager(Mymanager);
        rvCategory.setHasFixedSize(true);
        rvCategory.addItemDecoration(new SpaceItemDecoration(12));
    }

    @OnClick(R.id.iamgeBack)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.tvPrice, R.id.tvTime})
    public void onClick(View view) {
        Drawable right;
        switch (view.getId()) {
            case R.id.tvPrice:
                if (priceAsc){
                    sortBy = I.SORT_BY_PRICE_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                }else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0,0,right.getIntrinsicWidth(),right.getIntrinsicHeight());
                //tvPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,right,null);
                priceAsc = ! priceAsc;
                break;
            case R.id.tvTime:
                if (addTimeAsc){
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                }else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                }
                addTimeAsc = !addTimeAsc;
                break;
        }
        mAdapter.setSortBy(sortBy);
    }
}
