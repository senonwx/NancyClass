package com.example.senon.nancyclass;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.senon.nancyclass.adapter.RecycleHolder;
import com.example.senon.nancyclass.adapter.RecyclerAdapter;
import com.example.senon.nancyclass.base.BaseActivity;
import com.example.senon.nancyclass.base.BaseResponse;
import com.example.senon.nancyclass.contract.MainContract;
import com.example.senon.nancyclass.greendaoentity.UserReview;
import com.example.senon.nancyclass.greendaoutil.UserReviewDt;
import com.example.senon.nancyclass.presenter.MainPresenter;
import com.example.senon.nancyclass.util.ToastUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.senon.nancyclass.base.BaseApplication.getContext;


/**
 * 主页
 */
public class MainActivity extends BaseActivity<MainContract.View, MainContract.Presenter> implements MainContract.View{

    @BindView(R.id.lrv)
    LRecyclerView lrv;
    
    private RecyclerAdapter<UserReview> adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private boolean isLoadMore = false;//是否加载更多
    private boolean isDownRefesh = false;//是否下拉刷新
    private int currentPage = 0;//当前页数
    private List<UserReview> mData = new ArrayList<>();//原始数据
    private List<UserReview> tempData = new ArrayList<>();//间接数据
    private UserReviewDt userBeanDt = new UserReviewDt();



    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mData = userBeanDt.getAll();
        initLrv();
    }

    private void initLrv() {

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        lrv.setLayoutManager(manager);
        lrv.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader); //设置下拉刷新Progress的样式
//        lrv.setArrowImageView(R.mipmap.news_renovate);  //设置下拉刷新箭头
        lrv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        adapter = new RecyclerAdapter<UserReview>(this, mData, R.layout.item_main_lrv) {
            @Override
            public void convert(final RecycleHolder helper, final UserReview item, final int position) {
                helper.setText(R.id.name_tv,item.getName());
                helper.setText(R.id.count_tv,item.getTotal());
                helper.setText(R.id.time_tv,item.getSignTime());
            }
        };
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lrv.setAdapter(mLRecyclerViewAdapter);
        lrv.setLoadMoreEnabled(false);
        lrv.setPullRefreshEnabled(false);
        //设置底部加载颜色
        lrv.setFooterViewColor(R.color.color_blue, R.color.text_gray, R.color.elegant_bg);
        lrv.setHeaderViewColor(R.color.color_blue, R.color.text_gray, R.color.elegant_bg);
        lrv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getFirstPageData();
            }
        });
        lrv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                isLoadMore = true;
//                currentPage++;
//                getOrderList();
            }
        });

    }
    @OnClick({R.id.add_igv,R.id.detele_igv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_igv:
                UserReview user = new UserReview();
                user.setName("张三");
                user.setSignTime("2018-9-18");
                user.setTotal(12);
                userBeanDt.insert(user);

                mData = userBeanDt.getAll();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                break;
            case R.id.detele_igv:
                break;
        }
    }


    @Override
    public MainContract.Presenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public MainContract.View createView() {
        return this;
    }

    @Override
    public void result(BaseResponse data) {
    }

    @Override
    public void setMsg(String msg) {
        ToastUtil.showShortToast(msg);
    }


}
