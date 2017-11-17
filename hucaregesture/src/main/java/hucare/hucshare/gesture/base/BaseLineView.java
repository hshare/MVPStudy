package hucare.hucshare.gesture.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hucare.hucshare.gesture.GestureLockHelper;

/**
 * @author huzeliang
 *         2017-11-14 18:27:04
 */
public abstract class BaseLineView extends View implements IBaseLine {

    private List<ILockView> lockViews;
    private String verifyPassword;
    private OnGestureVerifyListener onGestureVerifyListener;
    private OnGestureCompleteListener onGestureCompleteListener;
    private int padding = 0;
    private int pointWidth = 0;

    private Path path;
    private Paint paint;

    private int lastPathX;
    private int lastPathY;
    private Point tmpTarget = new Point();
    private StringBuilder passWordSb;
    private boolean isShowLine = true;
    private List<Integer> chooseList = new ArrayList<Integer>();

    protected float lineWidth = 15;

    public BaseLineView(Context context) {
        super(context);
        initPaint();
    }

    @Override
    public void initLockViews(List<ILockView> lockViews, int width) {
        this.lockViews = lockViews;
        pointWidth = width;
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
        if (padding == 0) {
            padding = (int) (pointWidth * 0.15);
        }
        if (x >= child.getLeft() + padding && x <= child.getRight() - padding && y >= child.getTop() + padding && y <= child.getBottom() - padding) {
            return true;
        }
        return false;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setShowLine(boolean showLine) {
        isShowLine = showLine;
    }

    public void initPaint() {
        path = new Path();
        this.passWordSb = new StringBuilder();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(lineWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isShowLine) {
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
                setMovePaint(paint);
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
                            setAllError();
                        }
                        if (onGestureCompleteListener != null) {
                            onGestureCompleteListener.onOutputPassword(passWordSb.toString());
                        }
                    } else {
                        if (onGestureCompleteListener != null &&
                                !onGestureCompleteListener.onOutputPassword(passWordSb.toString())) {
                            setAllError();
                        } else {
                            reset();
                        }
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

    private void setAllError() {
        for (int i = 0; i < chooseList.size(); i++) {
            if (chooseList.get(i) > 0 && chooseList.get(i) <= lockViews.size()) {
                lockViews.get(chooseList.get(i) - 1).setState(ILockView.State.STATE_ERROR);
                setErrorPaint(paint);
            }
        }
        postDelayed(runnable, getErrorDelay());
    }

    protected abstract void setErrorPaint(Paint paint);

    protected abstract int getErrorDelay();

    public abstract void setMovePaint(Paint paint);

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            reset();
            invalidate();
        }
    };

}
