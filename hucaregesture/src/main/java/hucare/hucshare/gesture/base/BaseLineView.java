package hucare.hucshare.gesture.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hucare.hucshare.gesture.GestureLockHelper;

/**
 * @author huzeliang
 * 2017-11-14 18:27:04
 */
public abstract class BaseLineView extends View implements IBaseLine {

    protected List<ILockView> lockViews;
    protected StringBuilder passWordSb;
    protected String verifyPassword;
    protected OnGestureVerifyListener onGestureVerifyListener;
    protected OnGestureCompleteListener onGestureCompleteListener;
    protected List<Integer> chooseList = new ArrayList<Integer>();
    private int padding = 20;

    public BaseLineView(Context context) {
        super(context);
    }

    @Override
    public void initLockViews(ViewGroup viewGroup) {
        if (lockViews == null) {
            lockViews = new ArrayList<ILockView>();

            for (int i = 0; i < 9; i++) {
                ILockView lockPointView = GestureLockHelper.getInstance().getLockView(getContext());
                lockPointView.setId(i + 1);
                lockViews.add(lockPointView);
                if (viewGroup != null) {
                    viewGroup.addView(lockPointView.getView());
                }
            }
        }
    }

    @Override
    public void setOnGestureVerifyListener(String password, OnGestureVerifyListener onGestureVerifyListener) {
        this.onGestureVerifyListener = onGestureVerifyListener;
        this.verifyPassword = password;
    }

    @Override
    public void setOnGestureCompleteListener(OnGestureCompleteListener onGestureCompleteListener) {
        this.onGestureCompleteListener = onGestureCompleteListener;
    }

    protected ILockView getChildIdByPos(int x, int y) {
        for (ILockView gestureLockView : lockViews) {
            if (checkPositionInChild(gestureLockView, x, y)) {
                return gestureLockView;
            }
        }
        return null;
    }

    private boolean checkPositionInChild(ILockView child, int x, int y) {
        if (x >= child.getLeft() + padding && x <= child.getRight() - padding && y >= child.getTop() + padding && y <= child.getBottom() - padding) {
            return true;
        }
        return false;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}
