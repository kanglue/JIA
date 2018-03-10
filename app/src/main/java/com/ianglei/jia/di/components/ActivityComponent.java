package com.ianglei.jia.di.components;

import android.app.Activity;
import android.content.Context;

import com.ianglei.jia.di.ContextLifeCycle;
import com.ianglei.jia.di.PerActivity;
import com.ianglei.jia.di.modules.ActivityModule;
import com.ianglei.jia.mo.DaoSession;
import com.ianglei.jia.view.MainActivity;
import com.ianglei.jia.view.PhraseActivity;

import dagger.Component;

/**
 * Created by ianglei on 2018/1/14.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(PhraseActivity activity);
    
    Activity getActivity();
    DaoSession getDaoSession();

    @ContextLifeCycle("Activity")
    Context getActivityContext();
    @ContextLifeCycle("Application")
    Context getApplicationContext();
}
