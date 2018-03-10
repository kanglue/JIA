package com.ianglei.jia.presenter;

import android.os.Bundle;

import com.ianglei.jia.view.IView;

/**
 * Created by ianglei on 2018/1/14.
 */

public interface IPresenter {
    void onCreate (Bundle savedInstanceState);

    void onResume();

    void onStart ();

    void onPause();

    void onStop ();

    void onDestroy();

    void attachView (IView v);
}
