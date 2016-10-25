package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.ResultUtils;

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
    @Bind(R.id.rlUserName)
    RelativeLayout rlUserName;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.ivRightArrowIcon)
    ImageView ivRightArrowIcon;
    @Bind(R.id.ivqrcode)
    ImageView ivqrcode;
    @Bind(R.id.rlMainData)
    RelativeLayout rlMainData;

    User user;
    EditText etChangeNick;
    PersonalDataActivity mContext;
    OnSetAvatarListener mOnSetAvatarListener;


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
        if (user == null) {
            finish();
        }
        showInfo();

    }

    @OnClick({R.id.ivBack, R.id.rlUserAvatar, R.id.rlNick, R.id.rlQrcode, R.id.btUnLogin, R.id.rlUserName})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rlUserAvatar:
                mOnSetAvatarListener = new OnSetAvatarListener(mContext,
                        R.id.rlMainData,user.getMuserName(),I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.rlNick:
                changeNick();
                break;
            case R.id.rlQrcode:
                MFGT.gotoQrcodeActivity(this);
                break;
            case R.id.btUnLogin:
                loginout();
                break;
            case R.id.rlUserName:
                CommonUtils.showLongToast(R.string.username_cannot_be_modify);
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
                        if (etChangeNick.getText() != null) {
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
        NetDao.updateNick(this, user.getMuserName(), etChangeNick.getText().toString(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                L.e("result==" + result);
                if (result == null) {
                    CommonUtils.showLongToast(R.string.update_fail);
                } else {
                    if (result.isRetMsg()) {
                        User u = (User) result.getRetData();
                        L.e("u==" + u);
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.updateUser(u);
                        if (isSuccess) {
                            FuLiCenterApplication.setUser(u);
                            tvNick.setText(etChangeNick.getText().toString());
                            CommonUtils.showLongToast("昵称修改成功");
                        } else {
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }
                    } else {
                        CommonUtils.showLongToast(R.string.update_fail);
                    }
                }

            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(R.string.update_fail);
            }
        });
    }

    private void loginout() {
        if (user != null) {
            SharePrefrenceUtils.getInstance(this).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLoginActivity(this);
        }
        MFGT.finish(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showInfo() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivAvatar);
            tvUserName.setText(user.getMuserName());
            tvNick.setText(user.getMuserNick());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("onActivityResult,requestCode="+requestCode+",resultCode="+resultCode);
        if (resultCode!=RESULT_OK){
            return;
        }
        mOnSetAvatarListener.setAvatar(requestCode,data,ivAvatar);
        if (requestCode== I.REQUEST_CODE_NICK){
            CommonUtils.showLongToast(R.string.update_user_nick_success);
        }
        if (requestCode==OnSetAvatarListener.REQUEST_CROP_PHOTO){
            updateAvatar();
        }
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext,user.getMavatarPath()
                +"/"+user.getMuserName()+I.AVATAR_SUFFIX_JPG));
        L.e("file="+file.exists());
        L.e("file="+file.getAbsolutePath());
        NetDao.updateAvatar(mContext, user.getMuserName(),file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                L.e("s="+s);
                Result result = ResultUtils.getResultFromJson(s,User.class);
                L.e("result="+result);
                if (result==null){
                    CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                }else {
                    User u = (User) result.getRetData();
                    if (result.isRetMsg()){
                        FuLiCenterApplication.setUser(u);
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u),mContext,ivAvatar);
                        CommonUtils.showLongToast(R.string.update_user_avatar_success);
                    }else {
                        CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e("error="+error);
                CommonUtils.showLongToast(R.string.update_user_avatar_fail);
            }
        });
    }


}
