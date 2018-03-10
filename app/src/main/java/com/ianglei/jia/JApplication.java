package com.ianglei.jia;

import android.app.Application;

import com.ianglei.jia.di.components.ApplicationComponent;
import com.ianglei.jia.di.components.DaggerApplicationComponent;
import com.ianglei.jia.di.modules.ApplicationModule;
import com.ianglei.jia.di.modules.GreenDaoModule;
import com.ianglei.jia.mo.DaoSession;
import com.ianglei.jia.utils.LJ;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by ianglei on 2018/1/5.
 */

public class JApplication extends Application {
    private static final String TAG = JApplication.class.getSimpleName();

    private ApplicationComponent applicationComponent;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
        //初始化Logger类
        //Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }


    private void initInjector()
    {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .greenDaoModule(new GreenDaoModule(this))
                .build();
    }

    /**
     * 可以利用get方法获取创建的component，然后调用inject()方法将BasePresenter
     * 作为参数传进去，这样就完成了绑定BasePresenter依赖
     */
    public ApplicationComponent getApplicationComponent()
    {
        return applicationComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LJ.d(TAG,"我挂了");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LJ.d(TAG,"地方不够住了");
    }
}
