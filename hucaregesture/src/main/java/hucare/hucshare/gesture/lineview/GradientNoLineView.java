package hucare.hucshare.gesture.lineview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
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
public class GradientNoLineView extends BaseLineView {


    public GradientNoLineView(Context context) {
        super(context);
    }

    @Override
    protected void setErrorPaint(Paint paint) {
        LinearGradient shader1 = new LinearGradient(0, 0, lineWidth, 0,
                new int[]{0xFFF1A8A8, 0xFFE23C3C, 0xFFFA0101}, null, Shader.TileMode.REPEAT);
        paint.setShader(shader1);
    }

    @Override
    protected int getErrorDelay() {
        return 2000;
    }

    @Override
    public void setMovePaint(Paint paint) {
        LinearGradient shader = new LinearGradient(0, 0, lineWidth, 0,
                new int[]{0xFFaaaaaa, 0xFF666666, 0xFF000505}, null, Shader.TileMode.REPEAT);
        paint.setShader(shader);
    }


}
