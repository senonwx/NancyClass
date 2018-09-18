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
import com.example.senon.nancyclass.contract.UserContract;
import com.example.senon.nancyclass.greendaoentity.UserDetails;
import com.example.senon.nancyclass.greendaoentity.UserReview;
import com.example.senon.nancyclass.greendaoutil.UserDetailsDt;
import com.example.senon.nancyclass.greendaoutil.UserReviewDt;
import com.example.senon.nancyclass.presenter.MainPresenter;
import com.example.senon.nancyclass.presenter.UserPresenter;
import com.example.senon.nancyclass.util.ComUtil;
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

    private static final int REQUEST_CODE = 1000;
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
        name = getIntent().getStringExtra("name");
        mData.addAll(userDetailsDt.getAllByName(name));
        userReview = userReviewDt.findByName(name);

        name_tv.setText(userReview.getName());
        total_tv.setText(userReview.getTotal()+"");
        last_tv.setText(userReview.getLast()+"");

        initLrv();
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
                helper.setText(R.id.time_tv,item.getTime());
                if(item.getFlag() == 1){
                    helper.setText(R.id.type_tv,"签到");
                    helper.setText(R.id.des_tv, ComUtil.getLevelStr(item.getLevel()));
                }else if(item.getFlag() == 2){
                    helper.setText(R.id.type_tv,"充值"+item.getMoney());
                    helper.setText(R.id.des_tv,"充值"+item.getCount()+"次");
                    helper.setTextColor(R.id.type_tv,R.color.txt_green_color);
                    helper.setTextColor(R.id.des_tv,R.color.txt_green_color);
                }

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
                        startActivityForResult(new Intent(UserActivity.this,SignActivity.class)
                                .putExtra("name",name)
                                .putExtra("state",2)
                                .putExtra("time",item.getTime()),
                                REQUEST_CODE);
                        return false;
                    }
                });
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
                            UserDetails details = userDetailsDt.findByTime(time);
                            if(details == null){
                                details = new UserDetails();
                                details.setName(name);
                                details.setFlag(2);
                                details.setTime(time);
                                details.setMoney(Integer.parseInt(money));
                                details.setCount(Integer.parseInt(count));
                                details.setContent(des);
                                userDetailsDt.insert(details);

                                dialogRecharge.dismiss();
                                mData.clear();
                                mData.addAll(userDetailsDt.getAllByName(name));
                                mLRecyclerViewAdapter.notifyDataSetChanged();

                                ToastUtil.showShortToast("充值成功！！！");
                            }else{
//                                ToastUtil.showShortToast("当天已经签过到啦！！！");
                            }
                        }
                    });
                }
                dialogRecharge.show();
                break;
            case R.id.sign_btn:
                startActivityForResult(new Intent(UserActivity.this,SignActivity.class)
                                .putExtra("name",name)
                                .putExtra("state",1),
                                REQUEST_CODE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            mData.clear();
            mData.addAll(userDetailsDt.getAllByName(name));
            mLRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
