package cn.ucai.fulicenter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {


    @Bind(R.id.tvSetting)
    TextView tvSetting;
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNick)
    TextView tvNick;
    MainActivity mContext;
    @Bind(R.id.llAvatarAndNick)
    LinearLayout llAvatarAndNick;

    User user;
    @Bind(R.id.tvCategoryGoods)
    TextView tvCategoryGoods;
    @Bind(R.id.tvtvMyStore)
    TextView tvtvMyStore;
    @Bind(R.id.tvPersonal)
    TextView tvPersonal;
    @Bind(R.id.tvCategoryNum)
    TextView tvCategoryNum;
    @Bind(R.id.tvMyStoreNum)
    TextView tvMyStoreNum;
    @Bind(R.id.tvPersonalNum)
    TextView tvPersonalNum;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        mContext = (MainActivity) getActivity();
        initData();

        return view;
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivAvatar);
            tvNick.setText(user.getMuserNick());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tvSetting, R.id.ivAvatar, R.id.tvNick, R.id.llAvatarAndNick, R.id.tvCategoryGoods, R.id.tvtvMyStore, R.id.tvPersonal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSetting:
                MFGT.gotoPersonalDataActivity(mContext);
                break;
            case R.id.ivAvatar:
                break;
            case R.id.tvNick:
                break;
            case R.id.llAvatarAndNick:
                MFGT.gotoPersonalDataActivity(mContext);
                break;
            case R.id.tvCategoryGoods:

                break;
            case R.id.tvtvMyStore:
                break;
            case R.id.tvPersonal:
                break;
        }
    }

    private void downloadCollectCount() {
        NetDao.downloadCollectCount(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result!=null&&result.isSuccess()){
                    tvCategoryNum.setText(result.getMsg());
                    L.e("result.getMsg()======"+result.getMsg());
                }else {
                    tvCategoryNum.setText(String.valueOf(0));
                }
            }

            @Override
            public void onError(String error) {
                tvCategoryNum.setText(String.valueOf(0));
                CommonUtils.showLongToast(error);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivAvatar);
            tvNick.setText(user.getMuserNick());
            syncUserInfo();
            downloadCollectCount();
        }
    }

    private void syncUserInfo() {
        NetDao.syncUserInfo(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                if (result != null) {
                    User u = (User) result.getRetData();
                    if (!user.equals(u)) {
                        UserDao dao = new UserDao(mContext);
                        boolean b = dao.saveUser(u);
                        if (b) {
                            FuLiCenterApplication.setUser(u);
                            user = u;
                            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u), mContext, ivAvatar);
                            tvNick.setText(user.getMuserNick());
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


}
