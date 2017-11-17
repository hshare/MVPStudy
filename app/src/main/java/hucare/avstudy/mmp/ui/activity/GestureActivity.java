package hucare.avstudy.mmp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import hucare.avstudy.R;
import hucare.hucshare.gesture.GestureLockView;
import hucare.hucshare.gesture.base.OnGestureCompleteListener;
import hucare.hucshare.gesture.base.OnGestureVerifyListener;

/**
 * @author huzeliang
 *         目前有以下需求要实现：
 *         1、9个圆圈可以自定义，可以画线，可以使用图片，提供常用自定义View
 *         2、连接线可以自定义，可以画线，可以使用图片，提供常用自定义View
 *         3、可以隐藏轨迹
 *         4、
 */
public class GestureActivity extends Activity {

    GestureLockView gestureLockView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        gestureLockView = (GestureLockView) findViewById(R.id.guvMy);
        gestureLockView.setOnGestureCompleteListener(new OnGestureCompleteListener() {
            @Override
            public boolean onOutputPassword(String password) {
                Toast.makeText(GestureActivity.this, "" + password, Toast.LENGTH_SHORT).show();
                if (password.equals("1,2,3,6")) {
                    return true;
                }
                return false;
            }
        });
//        gestureLockView.setOnGestureVerifyListener("1,2,3,6", new OnGestureVerifyListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(GestureActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError() {
//                Toast.makeText(GestureActivity.this, "onError", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
