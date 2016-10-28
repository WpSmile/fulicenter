package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.MFGT;

public class OrderActivity extends AppCompatActivity {

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


    OrderActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.iv_order_back, R.id.bt_settlement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_order_back:
                MFGT.finish(mContext);
                break;
            case R.id.bt_settlement:

                break;
        }
    }
}
