package hucare.avstudy.mmp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import hucare.avstudy.R;

/**
 * @author huzeliang
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoGesture(View v) {
        startActivity(new Intent(this, GestureActivity.class));
    }

    public void gotoRecyclerView(View v) {
        startActivity(new Intent(this, HucareActivity.class));
    }
}
