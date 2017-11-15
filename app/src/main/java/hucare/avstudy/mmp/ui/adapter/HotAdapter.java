package hucare.avstudy.mmp.ui.adapter;

import android.view.View;

import java.util.List;

import hucare.avstudy.R;
import hucare.avstudy.mmp.model.entity.HotItemBean;
import hucare.avstudy.mmp.ui.holder.HotHolder;
import hucare.avstudy.mvp.BaseAdapter;
import hucare.avstudy.mvp.BaseHolder;

/**
 * hot adapter
 *
 * @author huzeliang
 * @version 1.0 2017-11-6 18:19:01
 * @see ***
 * @since ***
 */
public class HotAdapter extends BaseAdapter<HotItemBean> {


    /**
     * none
     *
     * @param beans beans
     */
    public HotAdapter(List<HotItemBean> beans) {
        super(beans);
    }

    @Override
    protected BaseHolder<HotItemBean> getBaseHolder(View v, int viewType) {
        return new HotHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_hot;
    }
}
