package com.fashare.shareelementdemo.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.fashare.shareelementdemo.R;
import com.fashare.shareelementdemo.widget.SpaceItemDecoration;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mRv.setAdapter(mAdapter = new HomeAdapter(this));
        mRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRv.addItemDecoration(new SpaceItemDecoration(30));

        mAdapter.setDataList(Arrays.asList("1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3"));
    }
}
