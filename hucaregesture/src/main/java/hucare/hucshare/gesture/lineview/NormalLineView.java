package hucare.hucshare.gesture.lineview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import hucare.hucshare.gesture.base.BaseLineView;
import hucare.hucshare.gesture.base.ILockView;

/**
 * @author huzeliang
 *         2017-11-14 18:27:53
 */
public class NormalLineView extends BaseLineView {

    private int selectedLineColor = 0xFF000000;
    private int errorLineColor = 0xFFFF0000;
    private int errorDelay = 1000;
    private int lineAlpha = 150;

    public NormalLineView(Context context) {
        super(context);
    }


    @Override
    protected void setErrorPaint(Paint paint) {
        paint.setColor(errorLineColor);
        paint.setAlpha(lineAlpha);
    }

    @Override
    protected int getErrorDelay() {
        return errorDelay;
    }

    @Override
    public void setMovePaint(Paint paint) {
        paint.setColor(selectedLineColor);
        paint.setAlpha(lineAlpha);
    }

    public void setSelectedLineColor(int selectedLineColor) {
        this.selectedLineColor = selectedLineColor;
    }

    public void setErrorLineColor(int errorLineColor) {
        this.errorLineColor = errorLineColor;
    }

    public void setErrorDelay(int errorDelay) {
        this.errorDelay = errorDelay;
    }

    public void setLineWidthDp(float lineWidth) {
        this.lineWidth = dip2px(lineWidth);
    }

    public void setLineAlpha(int lineAlpha) {
        this.lineAlpha = lineAlpha;
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
