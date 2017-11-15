package hucare.avstudy.mmp.contract;

import hucare.avstudy.mvp.IModel;
import hucare.avstudy.mvp.IPresenter;
import hucare.avstudy.mvp.IView;

/**
 * hot contract
 *
 * @author huzeliang
 * @version 1.0 2017-11-6 18:06:59
 * @see ***
 * @since ***
 */
public interface HotContract {

    /**
     * Model
     */
    interface Model extends IModel {
        /**
         * get Hot list bean
         *
         * @param getListCallback callback
         * @param pageNo          pageNo
         */
        void getListBean(GetDataCallback getListCallback, int pageNo);
    }

    /**
     * view
     */
    interface View extends IView<Presenter> {
        /**
         * refresh complete
         */
        void refreshComplete();
    }

    /**
     * presenter
     */
    interface Presenter extends IPresenter {
        /**
         * get hot list bean
         *
         * @param isShowDialog is shwo dialog
         */
        void getListBean(boolean isShowDialog);

        /**
         * load more data
         */
        void loadListBean();
    }
}
