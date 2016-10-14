package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.MFGT;

public class SplashActivity extends AppCompatActivity {

    private final long splashTime = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
                MFGT.gotoMainActivity(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
                /*startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();*/
            }
        },splashTime);
    }
}
