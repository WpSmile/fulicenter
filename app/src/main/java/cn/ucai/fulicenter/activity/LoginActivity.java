package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.iamgeBack)
    ImageView iamgeBack;
    @Bind(R.id.etUserName)
    EditText etUserName;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btLogin)
    Button btLogin;
    @Bind(R.id.btRegisterFree)
    Button btRegisterFree;
    String username ;
    String password ;
    LoginActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {

    }

    @OnClick({R.id.iamgeBack, R.id.btLogin, R.id.btRegisterFree})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iamgeBack:
                MFGT.finish(this);
                break;
            case R.id.btLogin:
                checkedInput();
                break;
            case R.id.btRegisterFree:
                MFGT.gotoRegisterActivity(this);
                break;
        }
    }

    private void checkedInput() {
        username = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            CommonUtils.showLongToast(R.string.user_name_connot_be_empty);
            etUserName.requestFocus();
            return;
        }else if (TextUtils.isEmpty(password)){
            CommonUtils.showLongToast(R.string.password_connot_be_empty);
            etPassword.requestFocus();
            return;
        }
        login();
    }

    private void login() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.logining));
        NetDao.login(mContext, username, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                L.e("result="+result);
                if (result==null){
                    CommonUtils.showLongToast(R.string.login_fail);
                }else {
                    if (result.isRetMsg()){
                        String json = result.getRetData().toString();
                        L.e("json="+json);
                        Result resultFromJson = ResultUtils.getResultFromJson(json, User.class);
                        L.e("resultFromJson="+resultFromJson);
                        String str = resultFromJson.getRetData().toString();
                        L.e("str="+str);
                        if (str!=null&&str.length()>0){
                            CommonUtils.showShortToast("登陆成功");
                        }
                        //MFGT.finish(mContext);
                    }else {
                        if (result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                            CommonUtils.showLongToast(R.string.login_fail_unknow_user);
                        }else if (result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                            CommonUtils.showLongToast(R.string.login_fail_error_password);
                        }else {
                            CommonUtils.showLongToast(R.string.login_fail);
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
                L.e("error="+error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_RESGITER) {
            String name = data.getStringExtra(I.User.USER_NAME);
            etUserName.setText(name);
        }
    }
}
