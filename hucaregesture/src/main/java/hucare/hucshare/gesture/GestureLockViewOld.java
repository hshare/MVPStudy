package hucare.hucshare.gesture;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hucare.hucshare.gesture.base.ILockView;

/**
 * gesture lock view
 *
 * @author huzeliang
 *         2017-11-14 10:37:14
 * @deprecated
 */
public class GestureLockViewOld extends ViewGroup {

    private List<ILockView> lockViews;
    private List<Integer> chooseList = new ArrayList<Integer>();
    private Path path;
    private Paint paint;
    private int pointWidth;
    private float innerMargin = 0;
    private float outerMargin = 0;
    private int lastPathX;
    private int lastPathY;
    private Point tmpTarget = new Point();
    private int selectedLineColor = 0xffff9800;
    private int errorLineColor = 0xFFFF0000;
    private StringBuilder passWordSb;
    private String verifyPassword;
    private OnGestureVerifyListener onGestureVerifyListener;
    private OnGestureCompleteListener onGestureCompleteListener;
    private int errorDelay = 2000;
    private float lineWidth = 0;
    private int lineAlpha = 150;

    public GestureLockViewOld(Context context) {
        this(context, null, 0);
    }

    public GestureLockViewOld(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockViewOld(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GestureLockView);
//        errorDelay = a.getInteger(R.styleable.GestureLockView_errorLineDelay, errorDelay);
//        selectedLineColor = a.getColor(R.styleable.GestureLockView_selectedLineColor, selectedLineColor);
//        errorLineColor = a.getColor(R.styleable.GestureLockView_errorLineColor, errorLineColor);
//        lineWidth = a.getDimension(R.styleable.GestureLockView_lineWidth, lineWidth);
//        lineAlpha = a.getInteger(R.styleable.GestureLockView_lineAlpha, lineAlpha);
        innerMargin = a.getDimension(R.styleable.GestureLockView_innerMargin, innerMargin);
        outerMargin = a.getDimension(R.styleable.GestureLockView_outerMargin, outerMargin);
        if (lineAlpha < 15) {
            lineAlpha = 15;
        }
        if (lineAlpha > 255) {
            lineAlpha = 255;
        }
        a.recycle();
        initPaint();
    }

    private void initPaint() {
        // 初始化画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        path = new Path();
        this.passWordSb = new StringBuilder();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        width = height = width > height ? height : width;
        setMeasuredDimension(width, height);

        pointWidth = (int) ((width - innerMargin * 2 - outerMargin * 2) / (3.0));
        if (lineWidth == 0) {
            lineWidth = (float) (pointWidth * 0.15);
        }
        paint.setStrokeWidth(lineWidth);

        if (lockViews == null) {
            lockViews = new ArrayList<ILockView>();

            for (int i = 0; i < 9; i++) {
                ILockView lockPointView = GestureLockHelper.getInstance().getLockView(getContext());
                lockPointView.setId(i + 1);
                lockViews.add(lockPointView);
                this.addView(lockPointView.getView());
            }
        }
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getId() > 0) {
                    child.measure(MeasureSpec.makeMeasureSpec(pointWidth, MeasureSpec.EXACTLY)
                            , MeasureSpec.makeMeasureSpec(pointWidth, MeasureSpec.EXACTLY));
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childcount = getChildCount();
        for (int i = 0; i < childcount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE && childView.getId() > 0) {
                int hang = i / 3 + 1;
                int lie = i % 3 + 1;
                int left = (int) (outerMargin + (lie - 1) * (innerMargin + pointWidth));
                int top = (int) (outerMargin + (hang - 1) * (innerMargin + pointWidth));
                int right = left + pointWidth;
                int bottom = top + pointWidth;
                childView.layout(left, top, right, bottom);
            }
        }
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
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
        getHandler().removeCallbacks(runnable);
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

    private ILockView getChildIdByPos(int x, int y) {
        for (ILockView gestureLockView : lockViews) {
            if (checkPositionInChild(gestureLockView, x, y)) {
                return gestureLockView;
            }
        }
        return null;
    }

    private boolean checkPositionInChild(ILockView child, int x, int y) {
        // 设置了内边距，即x,y必须落入下GestureLockView的内部中间的小区域中，可以通过调整padding使得x,y落入范围不变大，或者不设置padding
        int padding = (int) (pointWidth * 0.15);
        if (x >= child.getLeft() + padding && x <= child.getRight() - padding && y >= child.getTop() + padding && y <= child.getBottom() - padding) {
            return true;
        }
        return false;
    }

    public void setOnGestureVerifyListener(String password, OnGestureVerifyListener onGestureVerifyListener) {
        this.onGestureVerifyListener = onGestureVerifyListener;
        this.verifyPassword = password;
    }

    public void setOnGestureCompleteListener(OnGestureCompleteListener onGestureCompleteListener) {
        this.onGestureCompleteListener = onGestureCompleteListener;
    }

    public interface OnGestureVerifyListener {
        void onSuccess();

        void onError();
    }

    public interface OnGestureCompleteListener {

        void onOutputPassword(String password);
    }

}
