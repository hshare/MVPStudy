package hucare.hucshare.gesture;

import android.content.Context;

import hucare.hucshare.gesture.base.IBaseLine;
import hucare.hucshare.gesture.base.ILockView;
import hucare.hucshare.gesture.lineview.NormalLineView;
import hucare.hucshare.gesture.lockview.NormalLockView;

/**
 * lock view helper
 *
 * @author huzeliang
 *         2017-11-14 10:48:04
 */
public class GestureLockHelper {

    private static GestureLockHelper LOCKVIEWHELPER;
    private OnGestureLockNewListener onLockViewNewListener;

    public void setOnLockViewNewListener(OnGestureLockNewListener onLockViewNewListener) {
        this.onLockViewNewListener = onLockViewNewListener;
    }

    public interface OnGestureLockNewListener {
        ILockView onLockViewNew(Context context);

        IBaseLine onLineViewNew(Context context);
    }

    public static GestureLockHelper getInstance() {
        if (LOCKVIEWHELPER == null) {
            LOCKVIEWHELPER = new GestureLockHelper();
        }
        return LOCKVIEWHELPER;
    }

    public ILockView getLockView(Context context) {
        if (onLockViewNewListener != null) {
            ILockView lockView = onLockViewNewListener.onLockViewNew(context);
            if (lockView == null) {
                return new NormalLockView(context);
            }
            return lockView;
        }
        return new NormalLockView(context);
    }

    public IBaseLine getLineView(Context context) {
        if (onLockViewNewListener != null) {
            IBaseLine baseLine = onLockViewNewListener.onLineViewNew(context);
            if (baseLine == null) {
                return new NormalLineView(context);
            }
            return baseLine;
        }
        return new NormalLineView(context);
    }

}
