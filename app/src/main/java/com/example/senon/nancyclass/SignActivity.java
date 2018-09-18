package com.example.senon.nancyclass;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.senon.nancyclass.adapter.RecycleHolder;
import com.example.senon.nancyclass.adapter.RecyclerAdapter;
import com.example.senon.nancyclass.base.BaseActivity;
import com.example.senon.nancyclass.base.BasePresenter;
import com.example.senon.nancyclass.base.BaseView;
import com.example.senon.nancyclass.entity.ClassLevel;
import com.example.senon.nancyclass.greendaoentity.UserDetails;
import com.example.senon.nancyclass.greendaoutil.UserDetailsDt;
import com.example.senon.nancyclass.util.SelectorTimeUtil;
import com.example.senon.nancyclass.util.ToastUtil;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 学员签到页面
 */
public class SignActivity extends BaseActivity<BaseView, BasePresenter<BaseView>> implements BaseView {

    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.level_lrv)
    LRecyclerView lrv;
    @BindView(R.id.content_edt)
    EditText content_edt;
    @BindView(R.id.comments_edt)
    EditText comments_edt;
    private List<ClassLevel> levels;
    private RecyclerAdapter<ClassLevel> adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private UserDetailsDt userDetailsDt = new UserDetailsDt();//学员详细信息操作类
    private String name;//当前学员名字
    private boolean isChange;//是否是修改

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    public void init() {
        name = getIntent().getStringExtra("name");
        isChange = getIntent().getBooleanExtra("isChange",false);
        if(isChange){
            name_tv.setText("签到修改:"+name);
        }else{
            name_tv.setText("签到:"+name);
        }

        initLevelLrv();
    }

    //初始化类型Recyclerview
    private void initLevelLrv(){
        levels = new ClassLevel().getTypeTop();
        lrv.setLayoutManager(new GridLayoutManager(this,5));
        adapter = new RecyclerAdapter<ClassLevel>(this, levels, R.layout.item_sign_level) {
            @Override
            public void convert(final RecycleHolder helper, final ClassLevel item, final int position) {
                helper.setImageResource(R.id.item_igv,item.isCheck() ? item.getImageCheck() : item.getImageNoCheck());
                helper.setTextColor(R.id.item_tv,item.isCheck() ? R.color.group_tablayout_tv : R.color.tablayout_tv_gray);
                helper.setText(R.id.item_tv,item.getDes());

                helper.setOnClickListener(R.id.item_lay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < levels.size(); i++) {
                            levels.get(i).setCheck( i == position ? true : false);
                        }

                        lrv.refreshComplete(0);
                        mLRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lrv.setAdapter(mLRecyclerViewAdapter);
        lrv.setLoadMoreEnabled(false);
        lrv.setPullRefreshEnabled(false);
    }
    
    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseView createView() {
        return null;
    }
    

    @OnClick({R.id.back_igv, R.id.commit_tv, R.id.time_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.commit_tv:
                String time = time_tv.getText().toString().trim();
                int level = 1;
                String content = content_edt.getText().toString().trim();
                String comments = comments_edt.getText().toString().trim();
                if(time.isEmpty()){
                    ToastUtil.showShortToast("请选择签到时间");
                    return;
                }else if(content.isEmpty()){
                    ToastUtil.showShortToast("请填写上课内容");
                    return;
                }else if(comments.isEmpty()){
                    ToastUtil.showShortToast("请对学生该堂课表现进行评价");
                    return;
                }
                for (int i = 0; i < levels.size(); i++) {
                    if(levels.get(i).isCheck()){
                        level = levels.get(i).getLevel();
                        break;
                    }
                    if(i == levels.size() - 1){
                        ToastUtil.showShortToast("请选择听课等级");
                        return;
                    }
                }

                UserDetails details = new UserDetails();
                details.setName(name);
                details.setTime(time);
                details.setFlag(1);
                details.setLevel(level);
                details.setContent(content);
                details.setComments(comments);
                if(userDetailsDt.findByTime(time) != null){
                    ToastUtil.showShortToast("当天已经签过到啦");
                }else{
                    userDetailsDt.insertByTime(details);
                    ToastUtil.showShortToast("签到提交成功");
                }
                break;
            case R.id.time_lay:
                SelectorTimeUtil.choseDateTime(time_tv, null, SignActivity.this);
                break;
        }
    }
}
