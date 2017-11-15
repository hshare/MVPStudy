package hucare.hucshare.gesture;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import hucare.hucshare.gesture.base.IBaseLine;
import hucare.hucshare.gesture.base.OnGestureCompleteListener;
import hucare.hucshare.gesture.base.OnGestureVerifyListener;

/**
 * gesture lock view
 *
 * @author huzeliang
 *         2017-11-14 10:37:14
 */
public class GestureLockView extends ViewGroup {

    private int pointWidth;
    private float innerMargin = 0;
    private float outerMargin = 0;
    private IBaseLine baseLineView;

    public GestureLockView(Context context) {
        this(context, null, 0);
    }

    public GestureLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GestureLockView);
        innerMargin = a.getDimension(R.styleable.GestureLockView_innerMargin, innerMargin);
        outerMargin = a.getDimension(R.styleable.GestureLockView_outerMargin, outerMargin);
        a.recycle();
        baseLineView = GestureLockHelper.getInstance().getLineView(getContext());
    }

    private boolean isInit = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (baseLineView != null && !isInit) {
            isInit = true;
            baseLineView.initLockViews(this);
            this.addView((View) baseLineView);
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        width = height = width > height ? height : width;
        setMeasuredDimension(width, height);

        pointWidth = (int) ((width - innerMargin * 2 - outerMargin * 2) / (3.0));

        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getId() > 0) {
                    child.measure(MeasureSpec.makeMeasureSpec(pointWidth, MeasureSpec.EXACTLY)
                            , MeasureSpec.makeMeasureSpec(pointWidth, MeasureSpec.EXACTLY));
                } else {
                    child.measure(widthMeasureSpec, heightMeasureSpec);
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
            } else {
                childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            }
        }
    }

    public void setOnGestureVerifyListener(String password, OnGestureVerifyListener onGestureVerifyListener) {
        baseLineView.setOnGestureVerifyListener(password, onGestureVerifyListener);
    }

    public void setOnGestureCompleteListener(OnGestureCompleteListener onGestureCompleteListener) {
        baseLineView.setOnGestureCompleteListener(onGestureCompleteListener);
    }
}
