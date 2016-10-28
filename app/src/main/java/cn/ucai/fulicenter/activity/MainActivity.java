package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragments.BoutiquesFragment;
import cn.ucai.fulicenter.fragments.CartFragment;
import cn.ucai.fulicenter.fragments.CategoryFragment;
import cn.ucai.fulicenter.fragments.NewGoodsFragment;
import cn.ucai.fulicenter.fragments.PersonalFragment;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.new_goods)
    RadioButton newGoods;
    @Bind(R.id.boutique)
    RadioButton boutique;
    @Bind(R.id.item_category)
    RadioButton itemCategory;
    @Bind(R.id.item_cart)
    RadioButton itemCart;
    @Bind(R.id.person)
    RadioButton person;

    int index;
    RadioButton[] rbs;
    MainActivity mContext;
    int currentIndex;

    Fragment[] mFragment;
    NewGoodsFragment mNewGoodsFragment;
    BoutiquesFragment mBoutiquesFragment;
    CategoryFragment mCategoryFragment;
    CartFragment mCartFragment;
    PersonalFragment mPersonalFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(this);
        ButterKnife.bind(this);
        mContext = this;
        L.i("MainActivity onCreate()");
        initView();
        initFragment();
    }


    private void initView() {
        rbs = new RadioButton[5];
        rbs[0] = newGoods;
        rbs[1] = boutique;
        rbs[2] = itemCategory;
        rbs[3] = itemCart;
        rbs[4] = person;

    }

    private void initFragment() {
        mFragment = new Fragment[5];
        mNewGoodsFragment = new NewGoodsFragment();
        mBoutiquesFragment = new BoutiquesFragment();
        mCategoryFragment = new CategoryFragment();
        mCartFragment = new CartFragment();
        mPersonalFragment = new PersonalFragment();

        mFragment[0] = mNewGoodsFragment;
        mFragment[1] = mBoutiquesFragment;
        mFragment[2] = mCategoryFragment;
        mFragment[3] = mCartFragment;
        mFragment[4] = mPersonalFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mNewGoodsFragment)
                .add(R.id.fragment_container, mBoutiquesFragment)
                .add(R.id.fragment_container, mCategoryFragment)
                .hide(mBoutiquesFragment)
                .hide(mCategoryFragment)
                .show(mNewGoodsFragment)
                .commit();
    }


    public void onCheckedChange(View view) {

        switch (view.getId()) {
            case R.id.new_goods:
                index = 0;
                break;
            case R.id.boutique:
                index = 1;
                break;
            case R.id.item_category:
                index = 2;
                break;
            case R.id.item_cart:
                index = 3;
                break;
            case R.id.person:
                if (FuLiCenterApplication.getUser() == null) {
                    MFGT.gotoLoginActivity(mContext);
                } else {
                    index = 4;
                }
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if (index != currentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragment[currentIndex]);
            if (!mFragment[index].isAdded()) {
                ft.add(R.id.fragment_container, mFragment[index]);
            }
            ft.show(mFragment[index]).commit();
        }
        setRadioButtonStatus();
        currentIndex = index;
    }

    private void setRadioButtonStatus() {
        L.e("index=" + index);
        for (int i = 0; i < rbs.length; i++) {
            if (i == index) {
                rbs[i].setChecked(true);
            } else {
                rbs[i].setChecked(false);
            }
        }
    }

    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (index==4&&FuLiCenterApplication.getUser()==null){
            index=0;
        }
    setFragment();
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e(TAG, "onActivityResult,requestCode=" + requestCode);
        if (requestCode == I.REQUEST_CODE_LOGIN && FuLiCenterApplication.getUser() != null) {
            index = 4;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }
}
