package com.example.senon.nancyclass.presenter;

import android.content.Context;

import com.example.senon.nancyclass.contract.MainContract;
import java.util.HashMap;



public class MainPresenter extends MainContract.Presenter{

    private Context context;

    public MainPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void login(HashMap<String, String> map, boolean isDialog, boolean cancelable) {

    }
}
