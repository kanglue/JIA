package com.ianglei.jia.di.modules;

import android.content.Context;

import com.ianglei.jia.JApplication;
import com.ianglei.jia.di.ContextLifeCycle;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ianglei on 2018/1/6.
 */

@Module
public class ApplicationModule {
    private final JApplication application;

    public ApplicationModule(JApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    JApplication provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    @ContextLifeCycle("Application")
    Context provideContext()
    {
        return application.getApplicationContext();
    }


}
