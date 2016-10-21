package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Bind(R.id.iamgeBack)
    ImageView iamgeBack;
    @Bind(R.id.etUserName)
    EditText etUserName;
    @Bind(R.id.etUserNick)
    EditText etUserNick;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @Bind(R.id.btRegisterFree)
    Button btRegisterFree;

    String username;
    String nickname;
    String password;
    String confirmPwd;
    RegisterActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        initData();

    }

    private void initData() {


    }

    @OnClick({R.id.iamgeBack, R.id.btRegisterFree})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iamgeBack:
                MFGT.finish(mContext);
                break;
            case R.id.btRegisterFree:
                method();
                break;
        }
    }

    private void method() {
        username = etUserName.getText().toString().trim();
        nickname = etUserNick.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPwd = etConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
            etUserName.requestFocus();
            return;
        } else if (!username.matches("[a-zA-Z]\\w{5,15}")) {
            CommonUtils.showShortToast(R.string.illegal_user_name);
            etUserName.requestFocus();
            return;

        } else if (TextUtils.isEmpty(nickname)) {
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            etUserNick.requestFocus();
            return;

        } else if (TextUtils.isEmpty(password)) {
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            etPassword.requestFocus();
            return;

        } else if (TextUtils.isEmpty(password)) {
            CommonUtils.showShortToast(R.string.two_input_password);
            etConfirmPassword.requestFocus();
            return;

        } else if (!password.equals(confirmPwd)) {
            CommonUtils.showShortToast(R.string.two_input_password);
            etConfirmPassword.requestFocus();
            return;
        }
        register();

    }

    private void register() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
        NetDao.register(this, username, nickname, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                if (result == null) {
                    CommonUtils.showShortToast(R.string.register_fail);
                } else {
                    if (result.isRetMsg()) {
                        CommonUtils.showShortToast(R.string.register_success);
                        MFGT.gotoLoginActivity(mContext,username,password);
                    } else {
                        CommonUtils.showLongToast(R.string.register_fail_exists);
                        etUserName.requestFocus();
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showShortToast(error);
                L.e(TAG, "register error=" + error);
            }
        });
    }
}
