package com.example.senon.nancyclass.contract;

import com.example.senon.nancyclass.base.BasePresenter;
import com.example.senon.nancyclass.base.BaseResponse;
import com.example.senon.nancyclass.base.BaseView;

import java.util.HashMap;


public interface UserContract {

    interface View extends BaseView {

        void result(BaseResponse data);

        void setMsg(String msg);

    }

    abstract class Presenter extends BasePresenter<View> {

        //请求1
        public abstract void login(HashMap<String, String> map, boolean isDialog, boolean cancelable);

    }
}
