package hucare.hucshare.gesture.lineview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.MotionEvent;

import hucare.hucshare.gesture.base.BaseLineView;
import hucare.hucshare.gesture.base.ILockView;

/**
 * @author huzeliang
 *         2017-11-14 18:27:53
 */
public class NormalLineView extends BaseLineView {

    private Path path;
    private Paint paint;

    private int lastPathX;
    private int lastPathY;
    private Point tmpTarget = new Point();

    private int selectedLineColor = 0xffff9800;
    private int errorLineColor = 0xFFFF0000;
    private int errorDelay = 1000;
    private float lineWidth = 5;
    private int lineAlpha = 150;
    private boolean isShowLine = true;

    public void setShowLine(boolean showLine) {
        isShowLine = showLine;
    }

    public NormalLineView(Context context) {
        super(context);
        initPaint();
    }

    private void initPaint() {
        // 初始化画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(lineWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        path = new Path();
        this.passWordSb = new StringBuilder();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isShowLine){
            return;
        }
        // 绘制GestureLockView间的连线
        if (path != null) {
            canvas.drawPath(path, paint);
        }
        // 绘制指引线
        if (chooseList.size() > 0) {
            if (lastPathX != 0 && lastPathY != 0) {
                canvas.drawLine(lastPathX, lastPathY, tmpTarget.x, tmpTarget.y, paint);
            }
        }
    }

    private void reset() {
        this.removeCallbacks(runnable);
        chooseList.clear();
        path.reset();
        for (ILockView gestureLockView : lockViews) {
            gestureLockView.setState(ILockView.State.STATE_NORMAL);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                reset();
            case MotionEvent.ACTION_MOVE:
                paint.setColor(selectedLineColor);
                paint.setAlpha(lineAlpha);
                ILockView child = getChildIdByPos(x, y);
                if (child != null) {
                    int cId = child.getId();
                    if (!chooseList.contains(cId)) {
                        chooseList.add(cId);
                        child.setState(ILockView.State.STATE_SELECTED);
                        // 设置指引线的起点
                        lastPathX = child.getLeft() / 2 + child.getRight() / 2;
                        lastPathY = child.getTop() / 2 + child.getBottom() / 2;
                        tmpTarget.x = lastPathX;
                        tmpTarget.y = lastPathY;
                        if (chooseList.size() == 1) {
                            // 当前添加为第一个
                            path.moveTo(lastPathX, lastPathY);
                        } else {
                            // 非第一个，将两者使用线连上
                            path.lineTo(lastPathX, lastPathY);
                        }
                    }
                }
                // 指引线的终点
                tmpTarget.x = x;
                tmpTarget.y = y;
                break;
            case MotionEvent.ACTION_UP:
                if (chooseList.size() > 0) {
                    passWordSb = new StringBuilder();
                    for (int i = 0; i < chooseList.size(); i++) {
                        passWordSb.append((i == 0 ? "" : ",") + chooseList.get(i));
                    }
                    if (!TextUtils.isEmpty(verifyPassword)) {
                        if (verifyPassword.equals(passWordSb.toString())) {
                            reset();
                            if (onGestureVerifyListener != null) {
                                onGestureVerifyListener.onSuccess();
                            }
                        } else {
                            if (onGestureVerifyListener != null) {
                                onGestureVerifyListener.onError();
                            }
                            for (int i = 0; i < chooseList.size(); i++) {
                                if (chooseList.get(i) > 0 && chooseList.get(i) <= lockViews.size()) {
                                    lockViews.get(chooseList.get(i) - 1).setState(ILockView.State.STATE_ERROR);
                                    paint.setColor(errorLineColor);
                                    paint.setAlpha(lineAlpha);
                                }
                            }

                            postDelayed(runnable, errorDelay);
                        }
                    } else {
                        reset();
                    }
                    if (onGestureCompleteListener != null) {
                        onGestureCompleteListener.onOutputPassword(passWordSb.toString());
                    }
                } else {
                }

                // 将终点设置位置为起点，即取消指引线
                tmpTarget.x = lastPathX;
                tmpTarget.y = lastPathY;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            reset();
            invalidate();
        }
    };


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
