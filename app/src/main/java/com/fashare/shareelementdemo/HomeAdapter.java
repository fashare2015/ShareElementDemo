package com.fashare.shareelementdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jinliangshan on 16/12/14.
 */
class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context mContext;
    List<String> mDataList;

    public void setDataList(List<String> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public HomeAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_img)
        ImageView mIvImg;
        @BindView(R.id.tv_detail)
        TextView mTvDetail;

        public ViewHolder(Context context) {
            super(View.inflate(context, R.layout.item_home, null));
            ButterKnife.bind(this, itemView);
        }

        public void bind(final String data) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetail();
                }
            });
        }

        private void goToDetail() {
            mContext.startActivity(
                    new Intent(mContext, DetailActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            (Activity)mContext,
                            Pair.create((View)mIvImg, mContext.getString(R.string.iv_img_transitionName)),
                            Pair.create((View)mTvDetail, mContext.getString(R.string.scrollview_detail_transitionName))
                    ).toBundle()
            );
        }
    }
}
