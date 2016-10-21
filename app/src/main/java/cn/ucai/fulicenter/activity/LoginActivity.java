package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
                break;
            case R.id.btRegisterFree:
                MFGT.gotoRegisterActivity(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==I.REQUEST_CODE_RESGITER){
            String name = data.getStringExtra(I.User.USER_NAME);
            etUserName.setText(name);
        }
    }
}
