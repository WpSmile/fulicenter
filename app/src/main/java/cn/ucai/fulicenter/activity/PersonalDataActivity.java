package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;

public class PersonalDataActivity extends AppCompatActivity {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.rlUserAvatar)
    RelativeLayout rlUserAvatar;
    @Bind(R.id.rlNick)
    RelativeLayout rlNick;
    @Bind(R.id.rlQrcode)
    RelativeLayout rlQrcode;
    @Bind(R.id.btUnLogin)
    Button btUnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivBack, R.id.rlUserAvatar, R.id.rlNick, R.id.rlQrcode, R.id.btUnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rlUserAvatar:
                break;
            case R.id.rlNick:
                break;
            case R.id.rlQrcode:
                break;
            case R.id.btUnLogin:
                break;
        }
    }
}
