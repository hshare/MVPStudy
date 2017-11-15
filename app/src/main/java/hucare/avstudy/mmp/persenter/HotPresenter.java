package hucare.avstudy.mmp.persenter;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hucare.avstudy.mmp.contract.HotContract;
import hucare.avstudy.mmp.model.entity.HotItemBean;
import hucare.avstudy.mmp.ui.adapter.HotAdapter;
import hucare.avstudy.mvp.BaseAdapter;
import hucare.avstudy.mvp.IModel;
import hucare.avstudy.util.LogUtil;

/**
 * hot presenter
 *
 * @author huzeliang
 * @version 1.0 2017-11-7 09:49:09
 * @see ***
 * @since ***
 */
public class HotPresenter implements HotContract.Presenter {
    /**
     * model
     */
    private HotContract.Model mmModel;
    /**
     * view
     */
    private HotContract.View mmView;

    /**
     * hot adapter
     */
    private HotAdapter hotAdapter;

    /**
     * hot list bean
     */
    private List<HotItemBean> infoContentListBeanList;

    public HotPresenter(HotContract.Model model, HotContract.View view) {
        this.mmModel = model;
        this.mmView = view;
        //unnecessary if activity!
        mmView.setPresenter(this);
        infoContentListBeanList = new ArrayList<HotItemBean>();
    }

    @Override
    public void onStart() {
        if (hotAdapter == null) {
            hotAdapter = new HotAdapter(infoContentListBeanList);
            mmView.setAdapter(hotAdapter);
            hotAdapter.setOnItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener<HotItemBean>() {
                @Override
                public void onItemClick(View view, int viewType, HotItemBean data, int position) {
                    if (infoContentListBeanList != null && infoContentListBeanList.size() > 0) {
                        mmView.showToast("" + data.getTitle());
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void getListBean(final boolean isShowDialog) {
        LogUtil.iLog("getListBean");
        if (isShowDialog) {
            mmView.showDialog("正在加载...");
        }
        mmModel.getListBean(new IModel.GetDataCallback<HotItemBean>() {

            @Override
            public void onSuccess(List beans) {
                if (infoContentListBeanList != null) {
                    infoContentListBeanList.clear();
                    infoContentListBeanList.addAll(beans);
                    hotAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(String s) {
                mmView.showToast(s);
            }

            @Override
            public void onComplete() {
                if (isShowDialog) {
                    mmView.hideDialog();
                } else {
                    mmView.refreshComplete();
                }
            }
        }, 1);
    }

    @Override
    public void loadListBean() {
        LogUtil.iLog("loadListBean");
        mmModel.getListBean(new IModel.GetDataCallback<HotItemBean>() {

            @Override
            public void onSuccess(List beans) {
                if (infoContentListBeanList != null) {
                    infoContentListBeanList.addAll(beans);
                    hotAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String s) {
                mmView.showToast(s);
            }

            @Override
            public void onComplete() {
                mmView.refreshComplete();
            }
        }, 2);
    }
}
