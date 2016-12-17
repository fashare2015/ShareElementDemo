package com.fashare.shareelementdemo.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.fashare.shareelementdemo.R;
import com.fashare.shareelementdemo.widget.SpaceItemDecoration;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_img)
    ImageView mIvImg;
    @BindView(R.id.rv_detail)
    RecyclerView mRvDetail;
    private DetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mRvDetail.setAdapter(mAdapter = new DetailAdapter(this));
        mRvDetail.addItemDecoration(new SpaceItemDecoration(30));

        mAdapter.setDataList(Arrays.asList(
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20"
        ));
    }
}
