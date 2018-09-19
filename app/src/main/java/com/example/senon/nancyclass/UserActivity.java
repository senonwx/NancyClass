package com.example.senon.nancyclass;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import com.example.senon.nancyclass.adapter.RecycleHolder;
import com.example.senon.nancyclass.adapter.RecyclerAdapter;
import com.example.senon.nancyclass.base.BaseActivity;
import com.example.senon.nancyclass.base.BaseResponse;
import com.example.senon.nancyclass.contract.UserContract;
import com.example.senon.nancyclass.greendaoentity.UserDetails;
import com.example.senon.nancyclass.greendaoentity.UserReview;
import com.example.senon.nancyclass.greendaoutil.UserDetailsDt;
import com.example.senon.nancyclass.greendaoutil.UserReviewDt;
import com.example.senon.nancyclass.presenter.UserPresenter;
import com.example.senon.nancyclass.util.BaseEvent;
import com.example.senon.nancyclass.util.ComUtil;
import com.example.senon.nancyclass.util.ToastUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
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
    @BindView(R.id.total_count_tv)
    TextView total_count_tv;
    @BindView(R.id.last_count_tv)
    TextView last_count_tv;
    @BindView(R.id.total_money_tv)
    TextView total_money_tv;
    @BindView(R.id.last_money_tv)
    TextView last_money_tv;

    private RecyclerAdapter<UserDetails> adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private boolean isLoadMore = false;//是否加载更多
    private boolean isDownRefesh = false;//是否下拉刷新
    private int currentPage = 0;//当前页数
    private List<UserDetails> mData = new ArrayList<>();//原始数据
    private List<UserDetails> tempData = new ArrayList<>();//间接数据
    private UserReviewDt userReviewDt = new UserReviewDt();
    private UserDetailsDt userDetailsDt = new UserDetailsDt();//学员详细信息操作类
    private String name;//学员姓名
    private UserReview userReview;//该学员概述信息
    private DialogRecharge dialogRecharge;


    @Override
    public int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        name = getIntent().getStringExtra("name");
        name_tv.setText(name+"的历史记录");

        initData();
        initLrv();
    }

    private void initData(){
        mData.clear();
        mData.addAll(userDetailsDt.getAllByName(name));
        userReview = userReviewDt.findByName(name);

        total_count_tv.setText(userReview.getTotal_count()+"");
        last_count_tv.setText(userReview.getLast_count()+"");
        total_money_tv.setText(userReview.getTotal_money()+"");
        last_money_tv.setText(userReview.getLast_money()+"");
    }
    private void initLrv() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        lrv.setLayoutManager(manager);
        lrv.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader); //设置下拉刷新Progress的样式
//        lrv.setArrowImageView(R.mipmap.news_renovate);  //设置下拉刷新箭头
        lrv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        adapter = new RecyclerAdapter<UserDetails>(this, mData, R.layout.item_user_lrv) {
            @Override
            public void convert(final RecycleHolder helper, final UserDetails item, final int position) {
                helper.setVisible(R.id.title_tv,position == 0);
                helper.setText(R.id.time_tv,item.getTime());

                if(item.getFlag() == 1){//签到
                    helper.setText(R.id.text2,"表现");
                    helper.setText(R.id.des_tv,"备注:"+item.getComments());
                    helper.setText(R.id.money_tv, ComUtil.getLevelStr(item.getLevel()));
                    helper.setText(R.id.type_tv,"签到");
                    helper.setTextColor(R.id.type_tv,R.color.txt_blue_color);
                }else if(item.getFlag() == 2){//充值
                    helper.setText(R.id.text2,"充值");
                    helper.setText(R.id.des_tv,"备注:"+item.getContent());
                    helper.setText(R.id.money_tv,item.getMoney());
                    helper.setText(R.id.type_tv,"充值");
                    helper.setTextColor(R.id.type_tv,R.color.txt_green_color);
                }

                if(item.getFlag() == 1){
                    helper.setOnClickListener(R.id.lay, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(UserActivity.this,SignActivity.class)
                                    .putExtra("name",name)
                                    .putExtra("state",0)
                                    .putExtra("time",item.getTime()));
                        }
                    });
                    helper.setOnLongClickListener(R.id.lay, new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            startActivity(new Intent(UserActivity.this,SignActivity.class)
                                            .putExtra("name",name)
                                            .putExtra("state",2)
                                            .putExtra("time",item.getTime()));
                            return false;
                        }
                    });
                }

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
                if(dialogRecharge == null){
                    dialogRecharge = new DialogRecharge(UserActivity.this,name);
                    dialogRecharge.setConfirmClickListener(new DialogRecharge.OnClickListener() {
                        @Override
                        public void setConfirmClickListener(String time, String money, String count, String des) {
                            UserDetails details = userDetailsDt.findByTime(time);//每天只能充值一次
                            if(details == null){
                                //生成当前充值记录，并插入数据库中
                                details = new UserDetails();
                                details.setName(name);
                                details.setFlag(2);
                                details.setTime(time);
                                details.setMoney(Integer.parseInt(money));
                                details.setCount(Integer.parseInt(count));
                                details.setContent(des);
                                userDetailsDt.insert(details);

                                //更新学员概述次数与金额等
                                UserReview review = userReviewDt.findByName(name);
                                review.setTotal_count(review.getTotal_count()+Integer.parseInt(count));
                                review.setLast_count(review.getLast_count()+Integer.parseInt(count));
                                review.setTotal_money(review.getTotal_money()+Integer.parseInt(money));
                                review.setLast_money(review.getLast_money()+Integer.parseInt(money));
                                userReviewDt.update(review);

                                BaseEvent event = new BaseEvent();
                                event.setCode(3);
                                EventBus.getDefault().post(event);

                                initData();
                                mLRecyclerViewAdapter.notifyDataSetChanged();
                                dialogRecharge.dismiss();

                                ToastUtil.showShortToast("充值成功！！！");
                            }else{
                                ToastUtil.showShortToast("每天只能充值一次哦");
                            }
                        }
                    });
                }
                dialogRecharge.show();
                break;
            case R.id.sign_btn:
                startActivity(new Intent(UserActivity.this,SignActivity.class)
                                .putExtra("name",name)
                                .putExtra("state",1));
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//在ui线程执行
    public void onDataSynEvent(BaseEvent event) {
        int code = event.getCode();
        if (code == 1 || code == 2) {//1签到  2签到修改
            initData();
            mLRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
