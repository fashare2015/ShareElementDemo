# 下拉回退 Activity，layout_behavior 还能这么玩？
---
熟悉`layout_behavior`的朋友都知道，它是`CoordinatorLayout`的重要属性。不过，你该不会以为它只能用在`NestedScrollingChild`上吧？如果你真这么觉得，它可会很伤心的：人家才没有这么弱呢。
其实它的能力远远不止这些。这里和大家分享一个 Demo 和心得。
<br/>

#效果图
---
这次仿的是“下拉回退”效果，来自 https://github.com/nickbutcher/plaid，
这个项目有必要学习一下，堪称神交互。

![这里写图片描述](http://img.blog.csdn.net/20161217160751998?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYTE1MzYxNDEzMQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

<br/>
我的效果:

![这里写图片描述](http://img.blog.csdn.net/20161217171054773?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYTE1MzYxNDEzMQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

<br/>

#源码
---
本来想研究“过场动画”，所以名字取成了`ShareElement`，其实是`Behavior`的Demo，不要在意这些细节。

https://github.com/fashare2015/ShareElementDemo
<br/>

#灵感来源
---
##BottomSheetBehavior
>之前在掘金上发了处女作——[Android 简易悬停抽屉控件 —— 仿知乎收藏夹](https://gold.xitu.io/entry/5852342f128fe100697fa9b4/detail)。结果分分钟被打脸，这分明就有官方控件啊orz。百度了才知道`support`包里有这玩意——`BottomSheetBehavior`。也正感谢它，有了今天这篇博客。

它的用法是这样的：
在需要的控件上加上一行`app:layout_behavior="@string/bottom_sheet_behavior"`。搞定了，你的`LinearLayout`已经变成一个底部`抽屉控件`了。。。这也太屌了吧。。。
那我们的demo呢，也跟这个类似。
```xml
<android.support.design.widget.CoordinatorLayout>
    
    <LinearLayout
        app:layout_behavior="@string/bottom_sheet_behavior"/>
        ...
    </LinearLayout>

</android.support.design.widget.CoordinatorLayout>
```
参考：
[使用Bottom Sheet实现底部菜单](http://www.jianshu.com/p/1024ad202683)

##Plaid 的“下拉回退”效果
如前面效果图所示，我主要模仿了，`下拉回退`和`过场动画`两个效果。
<br/>

＃实现
---
`Plaid`里面是通过`自定义View`来实现，那我这里呢用`Behavior`试一下，能更好的复用。

##Behavior 的接口
从接口看，它更像是一个轻量级的 View, 有一些类似 View 的接口：

- View 的绘制
  - onMeasureChild
  - onLayoutChild
- View 的事件分发
  - onInterceptTouchEvent
  - onTouchEvent
- child 间的依赖
  - layoutDependsOn
  - onDependentViewChanged
- child 的嵌套滑动响应
  - onStartNestedScroll
  - onNestedPreScroll
  - onNestedScroll
  - onStopNestedScroll
  - ...

网上看到的资料中所介绍的更多的为`NestedScroll`这一块。而对于`绘制`和`事件分发`避而不谈，使得`Behavior`少了许多应用场景。事实上，`NestedScroll`使用起来更有局限性，它要求应用该`Behavior`的`View`必须`implement NestedScrollingChild`。反观`绘制`和`分发`，基于它们的`Behavior`可以应用在所有的`View`上，是`自定义View`的一种更轻量的实现。
<br/>

##behavior 定义与使用
定义两个`Behavior`:

 - FollowBehavior：一个 "紧贴联动" 的 Behavior，使得 RecyclerView 移动时，ImageView 紧贴着它。这是一个典型的`child 间的依赖`的场景。
 - DragDismissBehavior：拖动时拦截事件，使得 RecyclerView 本体移动（而非内部 scroll）。移动到一定距离时，finish Activity。这里用到了`事件分发`和`NestedScroll`两类回调。

**注意**：`RecyclerView`为`NestedScrollingChild`，因此`下拉回退`很流畅。但是，事实上，`DragDismissBehavior`还可以用在`非NestedScrollingChild`上，例如`ListView`、`ScrollView`等，仍然可以正常的`下拉回退`。只不过`NestedScroll`的回调不在响应，流畅度有些下降。亲们可以自己试一下。

```xml
<!-- activity_detail -->
<android.support.design.widget.CoordinatorLayout>

    <ImageView
        app:layout_behavior=".behavior.FollowBehavior"/>

    <android.support.v7.widget.RecyclerView
        app:layout_behavior=".behavior.DragDismissBehavior"/>

</android.support.design.widget.CoordinatorLayout>
```
<br/>
##代码实现
---
主要有这几块：

- 事件分发：用`mViewDragHelper`处理拖动事件，参考：
  - 鸿神的 [Android ViewDragHelper完全解析 自定义ViewGroup神器](http://blog.csdn.net/lmj623565791/article/details/46858663)
- 滑动：smoothScrollTo（），涉及 Scroller 类的使用

- Behavior 的 NestedScroll 相关回调：也不复杂，需要理解`NestedScrollingParent`和`NestedScrollingChild`。参考：
  - [关于CoordinatorLayout与Behavior的一点分析](http://www.jianshu.com/p/a506ee4afecb)
  - [自定义Behavior](http://www.jianshu.com/p/ebca9f6096f5)
  - [CoordinatorLayout 自定义Behavior并不难，由简到难手把手带你撸三款！](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0824/6565.html)

代码就不贴了，主要是把接口理清楚，像`onMeasure()->onLayout()->onDraw()`一样，挨个往里填实现就是了。
有兴趣可以看看，自认为可读性还是可以的，嘿嘿。

