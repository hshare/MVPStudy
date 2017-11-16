package hucare.hucshare.gesture.lockview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import hucare.hucshare.gesture.R;
import hucare.hucshare.gesture.base.BaseLockView;

/**
 * 功能/模块 ：***
 *
 * @author huzeliang
 * @version 1.0 ${date} ${time}
 * @see ***
 * @since ***
 */
public class PicLockView extends BaseLockView {

    private Bitmap bmp1;
    private Bitmap bmp2;
    private Bitmap bmp3;
    private int childWidth;
    private int childHeight;

    public PicLockView(Context context) {
        super(context);
        bmp1 = BitmapFactory.decodeResource(getResources(), R.mipmap.gesture_node_normal);
        bmp2 = BitmapFactory.decodeResource(getResources(), R.mipmap.gesture_node_pressed);
        bmp3 = BitmapFactory.decodeResource(getResources(), R.mipmap.gesture_node_wrong);
    }

    @Override
    public void onStateError(Canvas canvas) {
        canvas.drawBitmap(bmp3, null, new Rect(0, 0, childWidth, childHeight), null);
    }

    @Override
    public void onStateSelect(Canvas canvas) {
        canvas.drawBitmap(bmp2, null, new Rect(0, 0, childWidth, childHeight), null);
    }

    @Override
    public void onStateNormal(Canvas canvas) {
        canvas.drawBitmap(bmp1, null, new Rect(0, 0, childWidth, childHeight), null);
    }

    @Override
    public void afterMeasure(int childWidth, int childHeight) {
        this.childHeight = childHeight;
        this.childWidth = childWidth;
    }

    @Override
    public void onDestroy() {
        bmp1.recycle();
        bmp2.recycle();
        bmp3.recycle();
        bmp1 = null;
        bmp2 = null;
        bmp3 = null;
    }

}
