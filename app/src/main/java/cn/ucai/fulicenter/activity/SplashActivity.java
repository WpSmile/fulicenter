package cn.ucai.fulicenter.activity;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class SplashActivity extends AppCompatActivity {
    private final long splashTime = 2000;
    SplashActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
    }
    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                //create db 耗时操作
                long costTiem = System.currentTimeMillis()-start;
                if (splashTime-costTiem>0){
                    try {
                        //如果耗时操作的时间小于闪屏设置的时间，线程延时
                        Thread.sleep(splashTime-costTiem);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                User user = FuLiCenterApplication.getUser();
                L.e("fulicenter,user="+user);
                String username = SharePrefrenceUtils.getInstance(mContext).getUser();
                L.e("fulicenter,username="+username);
                if (user==null&&username!=null){
                    UserDao dao = new UserDao(mContext);
                    user = dao.getUser(username);
                    L.e("database,user="+user);
                    if (user!=null){
                        FuLiCenterApplication.setUser(user);
                    }
                }
                MFGT.gotoMainActivity(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
            }
        },splashTime);
    }
}
