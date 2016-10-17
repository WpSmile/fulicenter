package cn.ucai.fulicenter.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {


    @Bind(R.id.rvBoutique)
    RecyclerView rvBoutique;

    ArrayList<BoutiqueBean> mBoutiqueList;
    LinearLayoutManager MyManager;
    BoutiqueAdapter mAdapter;

    public BoutiqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, view);
        mBoutiqueList = new ArrayList<>();
        MyManager = new LinearLayoutManager(getContext());
        mAdapter = new BoutiqueAdapter(getContext(),mBoutiqueList);
        rvBoutique.setAdapter(mAdapter);
        rvBoutique.setLayoutManager(MyManager);
        setOnListener();
        return view;
    }

    private void setOnListener() {
        setOnPullUpListener();
    }

    private void setOnPullUpListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvFooter)
        TextView tvFooter;

        public FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    static class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ivGoodsImage)
        ImageView ivGoodsImage;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvDescription)
        TextView tvDescription;
        @Bind(R.id.tvName)
        TextView tvName;

        public BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    static class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;
        ArrayList<BoutiqueBean> boutiquelist;

        RecyclerView parent;
        boolean isMore;
        String footer;

        public String getFooter() {
            return footer;
        }

        public void setFooter(String footer) {
            this.footer = footer;
            notifyDataSetChanged();
        }

        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean more) {
            this.isMore = more;
            notifyDataSetChanged();
        }

        public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> boutiquelist) {
            this.mContext = mContext;
            this.boutiquelist = boutiquelist;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.parent = (RecyclerView) parent;
            RecyclerView.ViewHolder holder = null;
            View layout = null;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            switch (viewType) {
                case I.TYPE_FOOTER:
                    layout = inflater.inflate(R.layout.item_footer, parent, false);
                    holder = new FooterViewHolder(layout);
                    break;
                case I.TYPE_ITEM:
                    layout = inflater.inflate(R.layout.item_boutique, parent, false);
                    holder = new BoutiqueViewHolder(layout);
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position)==I.TYPE_FOOTER){
                FooterViewHolder footerHolder = (FooterViewHolder) holder;
                footerHolder.tvFooter.setText(footer);
            }else {
                BoutiqueViewHolder boutiqueHolder = (BoutiqueViewHolder) holder;
                BoutiqueBean boutique = boutiquelist.get(position);
                boutiqueHolder.tvTitle.setText(boutique.getTitle());
                boutiqueHolder.tvDescription.setText(boutique.getDescription());
                boutiqueHolder.tvName.setText(boutique.getName());

                ImageLoader.build(I.SERVER_ROOT+I.REQUEST_FIND_BOUTIQUES)
                        .width(200)
                        .height(200)
                        .defaultPicture(R.drawable.nopic)
                        .saveFileName(boutique.getName())
                        .listener(parent)
                        .imageView(boutiqueHolder.ivGoodsImage)
                        .showImage(mContext);
            }
        }

        @Override
        public int getItemCount() {
            return boutiquelist == null ? 1 : boutiquelist.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return I.TYPE_FOOTER;
            } else {
                return I.TYPE_ITEM;
            }
        }


    }
}
