package com.fashare.shareelementdemo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-12-16
 * Time: 21:16
 * <br/><br/>
 */
public class FollowBehavior extends CoordinatorLayout.Behavior<View> {
    public static final String TAG = "FollowBehavior";

    private Context mContext;
    NestedScrollView mDependency;

    public FollowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {

//        boolean isValidDependency = dependency instanceof NestedScrollView;
//        if(isValidDependency)
//            mDependency = (NestedScrollView)dependency;
        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        int height = child.getHeight();
        child.setTop(dependency.getTop() - height);
        child.setBottom(dependency.getTop());
        Log.d(TAG, "onDependentViewChanged: " + dependency.getTop() + ", " + child.getTop() + ", " + height);

        return true;
    }
}