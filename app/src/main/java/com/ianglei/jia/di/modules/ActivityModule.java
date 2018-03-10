package com.ianglei.jia.di.modules;

import android.app.Activity;
import android.content.Context;

import com.ianglei.jia.di.ContextLifeCycle;
import com.ianglei.jia.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ianglei on 2018/1/15.
 */

@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Activity provideActivity(){
        return activity;
    }

    @Provides
    @PerActivity
    @ContextLifeCycle("Activity")
    Context provideContext(){
        return activity;
    }
}
