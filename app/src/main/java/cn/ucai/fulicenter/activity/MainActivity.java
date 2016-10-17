package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragments.BoutiqueFragment;
import cn.ucai.fulicenter.fragments.CartFragment;
import cn.ucai.fulicenter.fragments.CategoryFragment;
import cn.ucai.fulicenter.fragments.NewGoodsFragment;
import cn.ucai.fulicenter.fragments.PersonalFragment;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity {

    NewGoodsFragment newGoodsFragment;
    BoutiqueFragment boutiqueFragment;
    CategoryFragment categoryFragment;
    CartFragment cartFragment;
    PersonalFragment personalFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity onCreate()");
        initView();
    }


    private void initView() {
        rbs = new RadioButton[5];
        rbs[0] = newGoods;
        rbs[1] = boutique;
        rbs[2] = itemCategory;
        rbs[3] = itemCart;
        rbs[4] = person;

        //设置默认的主页
        setDefaultFragment();
    }
    private void setDefaultFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        newGoodsFragment = new NewGoodsFragment();
        transaction.replace(R.id.fragment_container,newGoodsFragment);
        transaction.commit();
    }

    public void onCheckedChange(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (view.getId()) {
            case R.id.new_goods:
                index = 0;
                if (newGoodsFragment == null) {
                    newGoodsFragment = new NewGoodsFragment();
                }
                transaction.replace(R.id.fragment_container, newGoodsFragment);
                transaction.addToBackStack("stack");
                break;
            case R.id.boutique:
                index = 1;
                if (boutiqueFragment == null) {
                    boutiqueFragment = new BoutiqueFragment();
                }
                transaction.replace(R.id.fragment_container, boutiqueFragment);
                transaction.addToBackStack("stack");
                break;
            case R.id.item_category:
                index = 2;
                if (categoryFragment == null){
                    categoryFragment = new CategoryFragment();
                }
                transaction.replace(R.id.fragment_container,categoryFragment);
                transaction.addToBackStack("stack");
                break;
            case R.id.item_cart:
                if (cartFragment == null){
                    cartFragment = new CartFragment();
                }
                transaction.replace(R.id.fragment_container,cartFragment);
                transaction.addToBackStack("stack");
                index = 3;
                break;
            case R.id.person:
                index = 4;
                if (personalFragment == null){
                    personalFragment = new PersonalFragment();
                }
                transaction.replace(R.id.fragment_container,personalFragment);
                transaction.addToBackStack("stack");
                break;
        }
        setRadioButtonStatus();
        transaction.commit();
    }

    private void setRadioButtonStatus() {
        for (int i = 0; i < rbs.length; i++) {
            if (index == i) {
                rbs[i].setChecked(true);
            } else {
                rbs[i].setChecked(false);
            }
        }
    }

}
