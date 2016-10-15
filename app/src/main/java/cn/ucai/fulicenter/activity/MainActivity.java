package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity {

    RadioButton mNewGoods,mBoutique,mCategory,mCart,mPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.i("MainActivity onCreate()");
        initView();
    }


    private void initView() {
        mNewGoods = (RadioButton) findViewById(R.id.new_goods);
        mBoutique = (RadioButton) findViewById(R.id.boutique);
        mCategory = (RadioButton) findViewById(R.id.item_category);
        mCart = (RadioButton) findViewById(R.id.item_cart);
        mPersonal = (RadioButton) findViewById(R.id.person);
    }

    public void onCheckedChange(View view){
        switch (view.getId()){
            case R.id.new_goods:
                MysetCheck((RadioButton) view);
                break;
            case R.id.boutique:
                MysetCheck((RadioButton) view);
                break;
            case R.id.item_category:
                MysetCheck((RadioButton) view);
                break;
            case R.id.item_cart:
                MysetCheck((RadioButton) view);
                break;
            case R.id.person:
                MysetCheck((RadioButton) view);
                break;
        }
    }

    public void MysetCheck(RadioButton v){
        if (v!=mNewGoods){
            mNewGoods.setChecked(false);
        }
        if (v!=mBoutique){
            mBoutique.setChecked(false);
        }
        if (v!=mCategory){
            mCategory.setChecked(false);
        }
        if (v!=mCart){
            mCart.setChecked(false);
        }
        if (v!=mPersonal){
            mPersonal.setChecked(false);
        }


    }
}
