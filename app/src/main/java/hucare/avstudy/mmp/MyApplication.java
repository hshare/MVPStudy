package hucare.avstudy.mmp;

import android.app.Application;
import android.content.Context;

import hucare.hucshare.gesture.base.BaseLineView;
import hucare.hucshare.gesture.GestureLockHelper;
import hucare.hucshare.gesture.base.ILockView;
import hucare.hucshare.gesture.lineview.NormalLineView;
import hucare.hucshare.gesture.lockview.AnimatorLockView;

/**
 * 功能/模块 ：***
 *
 * @author huzeliang
 * @version 1.0 ${date} ${time}
 * @see ***
 * @since ***
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        GestureLockHelper.getInstance().setOnLockViewNewListener(new GestureLockHelper.OnGestureLockNewListener() {
            @Override
            public ILockView onLockViewNew(Context context) {
//                ZhiFuBaoLockView lockView = new ZhiFuBaoLockView(context);
//                lockView.setColorNormal(Color.parseColor("#1B94EA"));
//                lockView.setColorSelected(Color.parseColor("#108EE9"));
//                lockView.setColorError(Color.parseColor("#F84545"));
//                return lockView;
//                return new PicLockView(context);
                return new AnimatorLockView(context);
            }

            @Override
            public BaseLineView onLineViewNew(Context context) {
//                NormalLineView normalLineView = new NormalLineView(context);
//                normalLineView.setErrorDelay(3000);
//                normalLineView.setSelectedLineColor(Color.parseColor("#108EE9"));
//                normalLineView.setErrorLineColor(Color.parseColor("#F84545"));
//                normalLineView.setLineWidthDp(2);
//                normalLineView.setLineAlpha(255);
//                normalLineView.setPadding(20);
                return new NormalLineView(context);
//                return null;
            }
        });
    }
}
