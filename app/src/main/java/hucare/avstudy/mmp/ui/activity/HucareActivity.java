package hucare.avstudy.mmp.ui.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import hucare.avstudy.R;
import hucare.avstudy.mmp.contract.HotContract;
import hucare.avstudy.mmp.model.HotRepository;
import hucare.avstudy.mmp.persenter.HotPresenter;
import hucare.avstudy.mmp.ui.view.HucareSwipeToRefreshLayout;
import hucare.avstudy.mvp.BaseActivity;

/**
 * main activity
 *
 * @author huzeliang
 * @version 1.0 2017-11-7 09:47:22
 * @see ***
 * @since ***
 * 本项目采用MVP架构，没有采用注解框架，简单易懂
 * Activity、Fragment作为view使用，时间有限，未对Fragment进行封装，未对细节进一步优化
 * model层采用仓库代理，屏蔽数据底层细节，时间有限，底层数据时模拟的，后期可修改成网络，数据库，file等，上层无需修改
 * 整体采用契约进行约束，如HotContract
 *
 *
 * 本项目采用毕数学的checkstyle进行check（除了bean无法修改）
 *
 *
 * 对Recyclerview的adapter进行简单封装，简化adapter，将业务逻辑移到holder中
 * 未对footer、header进行封装，原理同viewtype，后期添加
 *  * 由于时间有限，下面几项没有添加，后期有时间在优化：
 * 1、没有处理margin、padding
 * 2、没有处理阻尼和加速度优化
 * 3、没有将footer、header进行封装
 * 4、没有自定义xml参数和java设置方法
 * 5、采用Scroller处理滑动，没有自定义滚动处理器，所以只有一周刷新模式
 * 6、没有针对各种View进行实践分发优化
 */
public class HucareActivity extends BaseActivity<HotContract.Presenter> {

    /**
     * recyclerview
     */
    private RecyclerView rvHot;

    /**
     * 刷新layout
     */
    private HucareSwipeToRefreshLayout hucareSwipeToRefreshLayout;


    @Override
    protected void initPresenter() {
        presenter = new HotPresenter(new HotRepository(), this);
    }

    @Override
    protected void initData() {
        presenter.getListBean(true);
    }

    @Override
    protected void initView() {
        rvHot = (RecyclerView) findViewById(R.id.hucareTargetView);
        hucareSwipeToRefreshLayout = (HucareSwipeToRefreshLayout) findViewById(R.id.hucareSwipeToRefreshLayout);
        rvHot.setLayoutManager(new LinearLayoutManager(this));
        rvHot.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvHot.setItemAnimator(new DefaultItemAnimator());
        hucareSwipeToRefreshLayout.setOnRefreshListener(new HucareSwipeToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getListBean(false);
            }

            @Override
            public void onLoadMore() {
                presenter.loadListBean();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hucare;
    }


    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        rvHot.setAdapter(adapter);
    }

    @Override
    public void refreshComplete() {
        hucareSwipeToRefreshLayout.onRefreshComplete();
    }
}
