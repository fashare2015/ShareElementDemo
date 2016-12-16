package com.fashare.shareelementdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_img)
    ImageView mIvImg;
    @BindView(R.id.sv_detail)
    ViewGroup mSvDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

//        initView();
    }

    private void initView() {
//        mSvDetail.setOnTouchListener(new View.OnTouchListener() {
//            float preY, curY;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        preY = event.getY();
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//                        curY = event.getY();
//                        float dy = curY - preY;
//                        preY = curY;
//
//                        boolean shouldScrollDown = Math.abs(dy) > 0 && isScrolledAtTop() && dy > 0;
//                        if(shouldScrollDown)
//                            mSvDetail.setY(mSvDetail.getY() + dy);
//                        return shouldScrollDown;
//                }
//                return false;
//            }
//
//            private boolean isScrolledAtTop() {
//                return mSvDetail.getScrollY() <= 0;
//            }
//        });
    }
}
