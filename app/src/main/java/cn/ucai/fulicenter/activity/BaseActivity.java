package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BaseActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        iniView();
        initData();
        setListener();
        super.onCreate(savedInstanceState, persistentState);

    }

    private void setListener() {

    }

    private void initData() {

    }

    private void iniView() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MFGT.finish(this);
    }
}
