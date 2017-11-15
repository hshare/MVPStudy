package hucare.avstudy.mmp.ui.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;
import android.widget.TextView;

import hucare.avstudy.R;
import hucare.avstudy.util.LogUtil;

/**
 * pull to refresh view
 *
 * @author huzeliang
 * @version 1.0 2017-11-8 09:44:03
 * @see ***
 * @since ***
 * 由于时间有限，下面几项没有添加，后期有时间在优化：
 * 1、没有处理margin、padding
 * 2、没有处理阻尼和加速度优化
 * 3、没有将footer、header进行封装
 * 4、没有自定义xml参数和java设置方法
 * 5、采用Scroller处理滑动，没有自定义滚动处理器，所以只有一周刷新模式
 * 6、没有针对各种View进行实践分发优化
 */
public class HucareSwipeToRefreshLayout extends ViewGroup {

    /**
     * header view
     */
    private TextView headerView;
    /**
     * footer view
     */
    private TextView footerView;
    /**
     * target view
     */
    private View targetView;
    /**
     * header height
     */
    private int headerHeight;
    /**
     * footer height
     */
    private int footerHeight;
    /**
     * finger down first for X
     */
    private float initX;
    /**
     * finger down first for Y
     */
    private float initY;
    /**
     * touch slop
     */
    private int touchSlop;
    /**
     * scroller
     */
    private Scroller scroller;
    /**
     * current statue
     */
    private int status = STATUS_NONE;
    /**
     * no status
     */
    private static final int STATUS_NONE = 0;
    /**
     * pull to refresh
     */
    private static final int STATUS_PULL_TO_REFRESH = 1;
    /**
     * refreshing
     */
    private static final int STATUS_REFRESH = 3;
    /**
     * pull to load more
     */
    private static final int STATUS_PULL_TO_LOADMORE = 4;
    /**
     * loading more
     */
    private static final int STATUS_LOADMORE = 6;
    /**
     * refresh listener
     */
    private OnRefreshListener onRefreshListener;
    /**
     * moved y
     */
    private float moveY;
    /**
     * last moved y
     */
    private float moveLastY;

    public HucareSwipeToRefreshLayout(Context context) {
        super(context);
    }

    public HucareSwipeToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public HucareSwipeToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(headerView, widthMeasureSpec, heightMeasureSpec);
        measureChild(targetView, widthMeasureSpec, heightMeasureSpec);
        measureChild(footerView, widthMeasureSpec, heightMeasureSpec);
        headerHeight = headerView.getMeasuredHeight();
        footerHeight = footerView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean b1, int l, int t, int r, int b) {
        headerView.layout(l, -headerHeight, headerView.getMeasuredWidth(), 0);
        footerView.layout(l, targetView.getMeasuredHeight(),
                footerView.getMeasuredWidth(), targetView.getMeasuredHeight() + footerHeight);
        targetView.layout(l, t, r, b);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            headerView = (TextView) this.findViewById(R.id.hucareHeaderView);
            targetView = this.findViewById(R.id.hucareTargetView);
            footerView = (TextView) this.findViewById(R.id.hucareFooterView);
        } else {
            throw new IllegalStateException("chich num error");
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://0
                break;
            case MotionEvent.ACTION_MOVE://2
                break;
            case MotionEvent.ACTION_UP://1
            case MotionEvent.ACTION_CANCEL://3
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.iLog("onInterceptTouchEvent:" + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://0
                initX = ev.getX();
                initY = ev.getY();
                moveLastY = moveY = ev.getY();
                if (status == STATUS_REFRESH || status == STATUS_REFRESH) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE://2
                float diffX = ev.getX() - initX;
                float diffY = ev.getY() - initY;

                boolean moved = Math.abs(diffY) > Math.abs(diffX) && Math.abs(diffY) > (float) this.touchSlop;

                if ((!canChildScrollUp() && diffY > 0.0F) && moved) {
                    LogUtil.iLog("滚到顶部了" + status);
                    if (status == STATUS_NONE) {
                        status = STATUS_PULL_TO_REFRESH;
                        headerView.setText("下拉可以刷新");
                    }
                    return true;
                }
                if (!canChildScrollDown() && diffY < 0.0F && moved) {
                    LogUtil.iLog("滚到底部了");
                    if (status == STATUS_NONE) {
                        status = STATUS_PULL_TO_LOADMORE;
                        footerView.setText("上拉加载");
                    }
                    return true;
                }
                status = STATUS_NONE;
                break;
            case MotionEvent.ACTION_UP://1
                break;
            case MotionEvent.ACTION_CANCEL://3
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.iLog("onTouchEvent:" + event.getAction() + "  " + status);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://0
                break;
            case MotionEvent.ACTION_MOVE://2
                moveY = event.getY();
                if (status == STATUS_PULL_TO_REFRESH) {
                    scrollBy(0, (int) (moveLastY - moveY));
                    LogUtil.iLog("scrollBy:" + (int) (moveLastY - moveY));
                    LogUtil.iLog("getScrollY():" + getScrollY());
                    if (Math.abs(getScrollY()) > headerHeight) {
                        headerView.setText("松开立即刷新");
                    }
                }
                if (status == STATUS_PULL_TO_LOADMORE) {
                    scrollBy(0, (int) (moveLastY - moveY));
                    LogUtil.iLog("scrollBy:" + (int) (moveLastY - moveY));
                    LogUtil.iLog("getScrollY():" + getScrollY());
                    if (Math.abs(getScrollY()) > footerHeight) {
                        footerView.setText("松开立即加载");
                    }
                }
                moveLastY = moveY;
                break;
            case MotionEvent.ACTION_UP://1
            case MotionEvent.ACTION_CANCEL://3
                if (status == STATUS_PULL_TO_REFRESH) {
                    status = STATUS_REFRESH;
                    int y = getScrollY();
                    scroller.startScroll(0, y, 0, -y - headerHeight, 1000);
                    invalidate();
                    headerView.setText("正在刷新数据");
                    LogUtil.iLog("startScroll----" + y);
                    if (onRefreshListener != null) {
                        onRefreshListener.onRefresh();
                    }
                }
                if (status == STATUS_PULL_TO_LOADMORE) {
                    status = STATUS_LOADMORE;
                    int y = getScrollY();
                    scroller.startScroll(0, y, 0, -y + footerHeight, 1000);
                    invalidate();
                    footerView.setText("正在加载数据");
                    if (onRefreshListener != null) {
                        onRefreshListener.onLoadMore();
                    }
                    LogUtil.iLog("startScroll----" + y);
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);

    }

    /**
     * refresh complete
     */
    public void onRefreshComplete() {
        if (status == STATUS_LOADMORE) {
            status = STATUS_NONE;
            footerView.setText("加载完成");
            scroller.startScroll(0, getScrollY(), 0, -getScrollY(), 1000);
            invalidate();
        }
        if (status == STATUS_REFRESH) {
            status = STATUS_NONE;
            headerView.setText("刷新完成");
            scroller.startScroll(0, getScrollY(), 0, -getScrollY(), 1000);
            invalidate();
        }
    }

    /**
     * refresh
     *
     * @return is scroll up
     */
    protected boolean canChildScrollUp() {
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(this.targetView, -1);
        } else if (this.targetView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) this.targetView;
            return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
        } else {
            return ViewCompat.canScrollVertically(this.targetView, -1) || this.targetView.getScrollY() > 0;
        }
    }

    /**
     * load more
     *
     * @return is scroll down
     */
    protected boolean canChildScrollDown() {
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(this.targetView, 1);
        } else if (this.targetView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) this.targetView;
            return absListView.getChildCount() > 0
                    && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                    || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() >
                    absListView.getPaddingBottom());
        } else {
            return ViewCompat.canScrollVertically(this.targetView, 1) || this.targetView.getScrollY() < 0;
        }
    }

    /**
     * refresh listener
     */
    public interface OnRefreshListener {
        /**
         * 下拉刷新回调
         */
        void onRefresh();

        /**
         * 上拉加载更多回调
         */
        void onLoadMore();
    }
}
