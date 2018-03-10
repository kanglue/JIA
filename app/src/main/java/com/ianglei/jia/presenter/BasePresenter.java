package com.ianglei.jia.presenter;

import android.os.Bundle;

import com.ianglei.jia.JApplication;
import com.ianglei.jia.mo.DaoSession;
import com.ianglei.jia.view.IPhraseView;
import com.ianglei.jia.view.IView;

/**
 * Created by ianglei on 2018/1/7.
 */

public class BasePresenter implements IPresenter{

    private IPhraseView view;
    private DaoSession daoSession;

    public BasePresenter(){}

    public BasePresenter(JApplication application)
    {
        //application.getApplicationComponent().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(IView v) {

    }
}
