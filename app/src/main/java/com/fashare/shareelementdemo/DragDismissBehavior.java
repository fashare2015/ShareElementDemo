package com.fashare.shareelementdemo;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-12-16
 * Time: 21:16
 * <br/><br/>
 */
public class DragDismissBehavior extends CoordinatorLayout.Behavior<View> {
    public static final String TAG = "DragDismissBehavior";

    int mDragToDismissRange = 200;

    private Context mContext;
    private CoordinatorLayout mParent;
    private View mNestedScrollingChild;
    private int mOriginTop;

    private ViewDragHelper mViewDragHelper;
    private static final float TOUCH_SLOP_SENSITIVITY = 1.f;
    private DragCallback mDragCallback = new DragCallback();


    private class DragCallback extends ViewDragHelper.Callback{
        // 仅捕获 mNestedScrollingChild
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.d(TAG, "tryCaptureView: " + (child == mNestedScrollingChild));
            return child == mNestedScrollingChild;
        }

        // 控制边界, 防止 mNestedScrollingChild 的头部超出边界
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.d(TAG, "clampViewPositionVertical: " + top);
            if(child == mNestedScrollingChild){
                int newTop = top;
                newTop = Math.max(newTop, mOriginTop - mDragToDismissRange);
                newTop = Math.min(newTop, mOriginTop + mDragToDismissRange);
                return newTop;
            }
            return top;
        }

        // 手指释放的时候回调
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.d(TAG, "onViewReleased");
            if (releasedChild == mNestedScrollingChild) {
                int curTop = releasedChild.getTop();
                Log.d(TAG, "top: " + curTop);


                int sign = curTop - mOriginTop > 0? 1: -1;
                if(Math.abs(curTop - mOriginTop) > mDragToDismissRange * 0.9){
                    smoothScrollTo(mOriginTop + mDragToDismissRange * sign);
                    // dissmiss
                    if(mContext instanceof Activity){
                        ((Activity)mContext).finishAfterTransition();
                    }
                }else
                    smoothScrollTo(mOriginTop);
            }
        }
    }

    public DragDismissBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        if(mViewDragHelper == null)
            mViewDragHelper = ViewDragHelper.create(parent, TOUCH_SLOP_SENSITIVITY, mDragCallback);

        if(mParent == null)
            mParent = parent;

        if(mNestedScrollingChild == null)
//            mNestedScrollingChild = findScrollingChild(child);
            mNestedScrollingChild = parent.findViewById(R.id.sv_detail);
        mOriginTop = mNestedScrollingChild.getTop();
        return false;
    }

    /**
     * copy from {@link BottomSheetBehavior#findScrollingChild(View)}
     * @return
     */
//    private View findScrollingChild(View view) {
//        if (view instanceof NestedScrollingChild) {
//            return view;
//        }
//        if (view instanceof ViewGroup) {
//            ViewGroup group = (ViewGroup) view;
//            for (int i = 0, count = group.getChildCount(); i < count; i++) {
//                View scrollingChild = findScrollingChild(group.getChildAt(i));
//                if (scrollingChild != null) {
//                    return scrollingChild;
//                }
//            }
//        }
//        return null;
//    }

    // ------ 滑动部分 begin ------
    private void smoothScrollTo(int finalTop){
        View child = mNestedScrollingChild;
        mViewDragHelper.smoothSlideViewTo(child, 0, finalTop);
        ViewCompat.postOnAnimation(child, new SettleRunnable(child));
    }

    /**
     * smoothScrollTo() 中用到 mScroller,
     * 以此 SettleRunnable 代替 @Override computeScroll().
     *
     * copy from {@link BottomSheetBehavior}
     */
    private class SettleRunnable implements Runnable {

        private final View mView;

        SettleRunnable(View view) {
            mView = view;
        }

        @Override
        public void run() {
            if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(mView, this);    // 递归调用
            }
        }
    }

    private void scrollTo(int finalTop){
        View child = mNestedScrollingChild;
        child.setTop(finalTop);
        ViewCompat.postInvalidateOnAnimation(child);
    }
    // ------ 滑动部分 end ------

    float downY, preY, curY, dy;

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
        boolean shouldScrollDown = false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = preY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                curY = event.getY();
                dy = curY - preY;
                preY = curY;

                shouldScrollDown = isScrolledAtTop() && dy > 0;
                break;

        }

        Log.d(TAG, "getScrollY: " + mNestedScrollingChild.getScrollY() + ", shouldScrollDown: " + shouldScrollDown + "dy: " + dy);

        boolean should = mViewDragHelper.shouldInterceptTouchEvent(event);
        if(shouldScrollDown)
            return true;
        else
            return should;
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
        Log.d(TAG, "onTouchEvent");
        mViewDragHelper.processTouchEvent(event);

        // TODO:
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(downY - event.getY()) > mViewDragHelper.getTouchSlop()) {
                mViewDragHelper.captureChildView(child, event.getPointerId(event.getActionIndex()));
            }
        }
        return true;
    }

    private boolean isScrolledAtTop() {
        return mNestedScrollingChild.getScrollY() <= 0;
    }
}