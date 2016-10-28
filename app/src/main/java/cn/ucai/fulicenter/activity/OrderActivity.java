package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;

public class OrderActivity extends AppCompatActivity {

    OrderActivity mContext;
    User user = null;
    String cartIds = "";
    ArrayList<CartBean> mlist = null;
    int savPrive = 0;
    String[] ids = new String[]{};

    @Bind(R.id.iv_order_back)
    ImageView ivOrderBack;
    @Bind(R.id.tv_consignee)
    TextView tvConsignee;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.bt_settlement)
    Button btSettlement;
    @Bind(R.id.tv_order_count_num)
    TextView tvOrderCountNum;
    @Bind(R.id.sp_area)
    Spinner spArea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        mContext = this;
        mlist = new ArrayList<>();
        initData();
    }

    private void initData() {
        cartIds = getIntent().getStringExtra(I.Cart.ID);
        L.e("cartIds======" + cartIds);
        user = FuLiCenterApplication.getUser();
        if (cartIds == null || cartIds.equals("") || user == null) {
            finish();
        }
        ids = cartIds.split(",");
        getOrderList();
    }

    @OnClick({R.id.iv_order_back, R.id.bt_settlement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_order_back:
                MFGT.finish(mContext);
                break;
            case R.id.bt_settlement:
                String receiveName = etName.getText().toString();
                if (TextUtils.isEmpty(receiveName)) {
                    etName.setError("收货人姓名不能为空");
                    etName.requestFocus();
                    return;
                }
                String mobile = etPhone.getText().toString();
                if (TextUtils.isEmpty(mobile)) {
                    etPhone.setError("手机号码不能为空");
                    etName.requestFocus();
                    return;
                }
                if (!mobile.matches("[\\d]{11}")) {
                    etPhone.setError("手机号码格式错误");
                    etName.requestFocus();
                    return;
                }
                String area = spArea.getSelectedItem().toString();
                if (TextUtils.isEmpty(area)) {
                    Toast.makeText(OrderActivity.this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String address = etAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    etAddress.setError("手机号码不能为空");
                    etAddress.requestFocus();
                    return;
                }
                gotoStateMent();
                break;
        }
    }

    private void gotoStateMent() {
        L.e("savPrice====="+savPrive);
    }

    private void getOrderList() {
        NetDao.findCarts(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                ArrayList<CartBean> list = ResultUtils.getCartFromJson(s);
                L.e("list-----------"+list.toString());
                if (list == null || list.size() == 0) {
                    finish();
                } else {
                    mlist.addAll(list);
                    sumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void sumPrice() {
        savPrive = 0;
        if (mlist != null && mlist.size() > 0) {
            for (CartBean c : mlist) {
                for (String id : ids) {
                    if (id.equals(String.valueOf(c.getId()))) {
                        savPrive += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                    }
                }

            }

            tvOrderCountNum.setText("￥" + Double.valueOf(savPrive));
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }
}
