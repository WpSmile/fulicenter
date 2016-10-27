package cn.ucai.fulicenter.fragments;


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
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
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
    }

    private void initData() {
        downloadCart();
    }


    private void setOnListener() {
        setOnPullDownListener();
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
            NetDao.findCarts(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    L.e("result====" + result);
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    if (result != null && result.length > 0) {
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        mAdapter.initData(list);
                    }

                }

                @Override
                public void onError(String error) {
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
    }

    @OnClick(R.id.btBuy)
    public void onClick() {
    }
}
