package cn.ucai.fulicenter.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;

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
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNick)
    TextView tvNick;
    User user;
    EditText etChangeNick;
    PersonalDataActivity mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);
        mContext = this;
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), this, ivAvatar);
            tvNick.setText(user.getMuserNick());
        }else {
            finish();
        }
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
                //CommonUtils.showLongToast("不能更改昵称!");
                changeNick();
                break;
            case R.id.rlQrcode:
                MFGT.gotoQrcodeActivity(this);
                break;
            case R.id.btUnLogin:
                loginout();
                break;
        }
    }

    private void changeNick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        etChangeNick = new EditText(this);
        builder.setTitle("设置昵称")
                .setView(etChangeNick)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (etChangeNick.getText()!=null){
                            updateUserNick();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonUtils.showLongToast("设置昵称已取消！");
            }
        }).create().show();
    }

    private void updateUserNick() {
        NetDao.updateNick(this, user.getMuserName(), etChangeNick.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result!=null){
                    SharePrefrenceUtils.getInstance(mContext).saveUser(etChangeNick.getText().toString());
                    if (user != null) {
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext, ivAvatar);
                        tvNick.setText(user.getMuserNick());
                    }
                }
                CommonUtils.showLongToast("昵称修改成功！");
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void loginout() {
        if (user!=null){
            SharePrefrenceUtils.getInstance(this).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLoginActivity(this);
        }
        MFGT.finish(this);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), this, ivAvatar);
            tvNick.setText(user.getMuserNick());
        }
    }*/
}
