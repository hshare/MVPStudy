package hucare.hucshare.gesture.base;

import android.view.ViewGroup;

/**
 * @author huzeliang
 */
public interface IBaseLine {
    void initLockViews(ViewGroup viewGroup);

    void setOnGestureVerifyListener(String password, OnGestureVerifyListener onGestureVerifyListener);

    void setOnGestureCompleteListener(OnGestureCompleteListener onGestureCompleteListener);
}
