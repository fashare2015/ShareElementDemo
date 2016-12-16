package com.fashare.shareelementdemo;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by jinliangshan on 2016/7/26.
 * 设置 RecyclerView 的间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        int colCount = getSpanCount(parent);
        int rowCount = (itemCount-1)/colCount + 1;

        int rightSpace  = isLastCol(itemPosition, rowCount, colCount)? 0: space;  // 最后一列
        int bottomSpace = isLastRaw(itemPosition, rowCount, colCount)? 0: space;  // 最后一行

        outRect.set(0, 0, rightSpace, bottomSpace);    // 设置右边和底部 margin 间距
    }

    private boolean isLastRaw(int itemPosition, int rowCount, int colCount){
        return (itemPosition / colCount) == rowCount-1;
    }

    private boolean isLastCol(int itemPosition, int rowCount, int colCount){
        return (itemPosition % colCount) == colCount-1;
    }

    private int getSpanCount(RecyclerView parent){
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int spanCount = 1;  // 列数, LinearLayoutManger 默认为 1
        if(layoutManager instanceof GridLayoutManager)
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        else if(layoutManager instanceof StaggeredGridLayoutManager)
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        return spanCount;
    }
}
