package hucare.avstudy.mvp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import hucare.avstudy.mmp.contract.HotContract;

/**
 * base activity
 *
 * @author huzeliang
 * @version 1.0 2017-11-7 09:47:09
 * @param <P> p
 * @see ***
 * @since ***
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements HotContract.View {

    /**
     * progress dialog
     */
    private ProgressDialog progressDialog;

    /**
     * present
     */
    protected P presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initPresenter();
        initView();
        initViewAfter();
        initData();
    }

    /**
     * init presenter
     */
    protected abstract void initPresenter();

    /**
     * init data
     */
    protected abstract void initData();

    /**
     * after init view
     */
    private void initViewAfter() {
        presenter.onStart();
    }

    /**
     * init view
     */
    protected abstract void initView();

    /**
     * get layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();

    @Override
    public void showToast(String s) {
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(HotContract.Presenter iPresenter) {
        //unnecessary if activity!
    }

    @Override
    public void showDialog(String s) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(s + "");
        }
        progressDialog.show();
    }

    @Override
    public void hideDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
