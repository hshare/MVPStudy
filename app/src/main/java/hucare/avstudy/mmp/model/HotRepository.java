package hucare.avstudy.mmp.model;

import android.os.Handler;

import com.google.gson.Gson;

import java.util.Random;

import hucare.avstudy.mmp.contract.HotContract;
import hucare.avstudy.mmp.model.api.Api;
import hucare.avstudy.mmp.model.entity.HotBean;

/**
 * hot repository，simulate net data
 *
 * @author huzeliang
 * @version 1.0 2017-11-6 18:13:01
 * @see ***
 * @since ***
 */
public class HotRepository implements HotContract.Model {

    @Override
    public void getListBean(final GetDataCallback getListCallback, final int pageNo) {
        if (getListCallback == null) {
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                if (random.nextInt(100) < 98) {
                    HotBean hotBean = null;
                    if (pageNo > 1) {
                        hotBean = (new Gson()).fromJson(Api.HOT_STRING_P1, HotBean.class);
                    } else {
                        hotBean = (new Gson()).fromJson(Api.HOT_STRING_P2, HotBean.class);
                    }
                    if (hotBean != null &&
                            hotBean.getInfo_content_list() != null &&
                            hotBean.getInfo_content_list().size() > 0) {
                        getListCallback.onSuccess(hotBean.getInfo_content_list());
                        getListCallback.onComplete();
                    } else {
                        getListCallback.onError("网络异常....");
                        getListCallback.onComplete();
                    }
                } else {
                    getListCallback.onError("网络异常....");
                    getListCallback.onComplete();
                }
            }
        }, 1500);

    }
}
