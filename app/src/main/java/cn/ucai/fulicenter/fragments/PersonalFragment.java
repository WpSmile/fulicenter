package cn.ucai.fulicenter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {


    @Bind(R.id.tvSetting)
    TextView tvSetting;
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvName)
    TextView tvName;
    MainActivity mContext;

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
        User user = FuLiCenterApplication.getUser();
        if (user==null){
            MFGT.gotoLoginActivity(mContext);
        }else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivAvatar);
            tvName.setText(user.getMuserNick());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tvSetting, R.id.ivAvatar, R.id.tvName})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSetting:
                MFGT.gotoPersonalDataActivity(mContext);
                break;
            case R.id.ivAvatar:
                break;
            case R.id.tvName:
                break;
        }
    }
}
