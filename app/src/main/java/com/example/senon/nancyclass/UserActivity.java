package com.example.senon.nancyclass;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.example.senon.nancyclass.adapter.RecycleHolder;
import com.example.senon.nancyclass.adapter.RecyclerAdapter;
import com.example.senon.nancyclass.base.BaseActivity;
import com.example.senon.nancyclass.base.BaseResponse;
import com.example.senon.nancyclass.contract.UserContract;
import com.example.senon.nancyclass.contract.UserContract;
import com.example.senon.nancyclass.greendaoentity.UserReview;
import com.example.senon.nancyclass.greendaoutil.UserReviewDt;
import com.example.senon.nancyclass.presenter.MainPresenter;
import com.example.senon.nancyclass.presenter.UserPresenter;
import com.example.senon.nancyclass.util.ToastUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.senon.nancyclass.base.BaseApplication.getContext;

/**
 * 学员详细 页面
 */
public class UserActivity extends BaseActivity<UserContract.View, UserContract.Presenter> implements UserContract.View{

    @BindView(R.id.lrv)
    LRecyclerView lrv;
    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.total_tv)
    TextView total_tv;
    @BindView(R.id.last_tv)
    TextView last_tv;

    private RecyclerAdapter<UserReview> adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private boolean isLoadMore = false;//是否加载更多
    private boolean isDownRefesh = false;//是否下拉刷新
    private int currentPage = 0;//当前页数
    private ArrayList<UserReview> mData = new ArrayList<>();//原始数据
    private ArrayList<UserReview> tempData = new ArrayList<>();//间接数据
    private UserReviewDt userBeanDt = new UserReviewDt();

    @Override
    public int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    public void init() {
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

    @OnClick({R.id.back_igv,R.id.recharge_btn,R.id.sign_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.recharge_btn:
                break;
            case R.id.sign_btn:
                break;
        }
    }

    @Override
    public UserContract.Presenter createPresenter() {
        return new UserPresenter(this);
    }

    @Override
    public UserContract.View createView() {
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
