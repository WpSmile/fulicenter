package cn.ucai.fulicenter.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    ArrayList<CartBean> mlist;
    LinearLayoutManager MyManager;
    CartAdapter mAdapter;
    MainActivity mContext;
    User user;
    updateCartReceiver myReceiver;

    String cartIds = "";

    @Bind(R.id.tv_count_num)
    TextView tvCountNum;
    @Bind(R.id.ll_count_money)
    LinearLayout llCountMoney;
    @Bind(R.id.tv_save_num)
    TextView tvSaveNum;
    @Bind(R.id.ll_save_money)
    LinearLayout llSaveMoney;
    @Bind(R.id.btBuy)
    Button btBuy;

    @Bind(R.id.rvCart)
    RecyclerView rvCart;
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;
    @Bind(R.id.tv_refresh)
    TextView tvRefresh;
    @Bind(R.id.tvNull)
    TextView tvNull;
    @Bind(R.id.layout_cart)
    RelativeLayout layoutCart;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mContext = (MainActivity) getContext();
        mlist = new ArrayList<>();
        ButterKnife.bind(this, view);
        initView();
        initData();
        setOnListener();
        return view;
    }


    private void initView() {
        MyManager = new LinearLayoutManager(mContext);
        mAdapter = new CartAdapter(mContext, mlist);
        rvCart.setAdapter(mAdapter);
        rvCart.setLayoutManager(MyManager);
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        //设置是否自动修复
        rvCart.setHasFixedSize(true);
        rvCart.addItemDecoration(new SpaceItemDecoration(12));
        setCartLayout(false);

        myReceiver = new updateCartReceiver();
    }

    private void setCartLayout(boolean hasCart) {
        layoutCart.setVisibility(hasCart?View.VISIBLE:View.GONE);
        tvNull.setVisibility(hasCart?View.GONE:View.VISIBLE);
        rvCart.setVisibility(hasCart?View.VISIBLE:View.GONE);
        sumPrice();
    }

    private void initData() {
        downloadCart();
    }


    private void setOnListener() {
        setOnPullDownListener();
        IntentFilter intentFilter = new IntentFilter(I.BROADCASE_UPDATE_CART);
        mContext.registerReceiver(myReceiver,intentFilter);

    }

    private void setOnPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                srl.setEnabled(true);
                tvRefresh.setVisibility(View.VISIBLE);
                downloadCart();
            }
        });
    }

    private void downloadCart() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.findCarts(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    ArrayList<CartBean> list = ResultUtils.getCartFromJson(s);
                    L.e("result====" + list);
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    if (list != null && list.size() > 0) {
                        L.e("list[0]=" + list.get(0));
                        //mlist.addAll(list);
                        mAdapter.initData(list);
                        setCartLayout(true);
                    }else {
                        setCartLayout(false);

                    }

                }

                @Override
                public void onError(String error) {
                    setCartLayout(false);
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    CommonUtils.showShortToast(error);
                    L.e("error" + error);
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (myReceiver!=null){
            mContext.unregisterReceiver(myReceiver);
        }
    }

    @OnClick(R.id.btBuy)
    public void onClick() {
        if (cartIds!=null&&cartIds.length()>0){
            MFGT.gotoOrderActivity(mContext,cartIds);
        }else {
            CommonUtils.showLongToast(R.string.order_nothing);
        }

    }

    private void sumPrice(){
        cartIds = "";

        int sumPrice = 0;
        int savPrive = 0;
        if (mlist!=null&&mlist.size()>0){
            for (CartBean c:mlist){
                if (c.isChecked()){
                    cartIds += c.getId()+",";

                    sumPrice+=getPrice(c.getGoods().getCurrencyPrice())*c.getCount();
                    savPrive+=getPrice(c.getGoods().getRankPrice())*c.getCount();
                }
            }

            tvCountNum.setText("￥"+Double.valueOf(savPrive));
            tvSaveNum.setText("￥"+Double.valueOf(sumPrice-savPrive));
        }else {
            cartIds = "";
            tvCountNum.setText("￥0");
            tvSaveNum.setText("￥0");
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥")+1);
        return Integer.valueOf(price);
    }

    class updateCartReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            sumPrice();
            setCartLayout(mlist!=null&&mlist.size()>0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
