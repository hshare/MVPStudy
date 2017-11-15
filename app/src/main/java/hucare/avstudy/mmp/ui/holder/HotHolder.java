package hucare.avstudy.mmp.ui.holder;

import android.view.View;
import android.widget.TextView;

import hucare.avstudy.R;
import hucare.avstudy.mmp.model.entity.HotItemBean;
import hucare.avstudy.mvp.BaseHolder;

/**
 * hot holder
 *
 * @author huzeliang
 * @version 1.0 2017-11-7 10:31:23
 * @see ***
 * @since ***
 */
public class HotHolder extends BaseHolder<HotItemBean> {

    /**
     * title
     */
    private TextView tvTitle;


    public HotHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
    }

    @Override
    public void updateData(HotItemBean infoContentListBean, int position) {
        tvTitle.setText(infoContentListBean.getTitle());
    }
}
