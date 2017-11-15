package hucare.avstudy.mmp.ui.view.HucareRefreshLayout;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;

/**
 * @author huzeliang
 * @version 1.0 ${date} ${time}
 * @see ***
 * @since ***
 */
public class RefreshUtil {

    /**
     * refresh
     *
     * @return is scroll up
     */
    public static boolean canChildScrollUp(View targetView) {
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(targetView, -1);
        } else if (targetView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) targetView;
            return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
        } else {
            return ViewCompat.canScrollVertically(targetView, -1) || targetView.getScrollY() > 0;
        }
    }

    /**
     * load more
     *
     * @return is scroll down
     */
    protected boolean canChildScrollDown(View targetView) {
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(targetView, 1);
        } else if (targetView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) targetView;
            return absListView.getChildCount() > 0
                    && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                    || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() >
                    absListView.getPaddingBottom());
        } else {
            return ViewCompat.canScrollVertically(targetView, 1) || targetView.getScrollY() < 0;
        }
    }

    public static int getScaledTouchSlop(Context context) {
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }


}
