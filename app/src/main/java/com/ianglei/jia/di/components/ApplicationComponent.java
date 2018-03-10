package com.ianglei.jia.di.components;

import android.content.Context;

import com.ianglei.jia.JApplication;
import com.ianglei.jia.di.ContextLifeCycle;
import com.ianglei.jia.di.modules.ApplicationModule;
import com.ianglei.jia.di.modules.GreenDaoModule;
import com.ianglei.jia.mo.DaoSession;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ianglei on 2018/1/6.
 */

@Singleton
@Component(modules= {ApplicationModule.class, GreenDaoModule.class})
/**
 * Component注射器桥接module和inject
 * */
public interface ApplicationComponent {

    JApplication getJApplication();

    @ContextLifeCycle("Application")
    Context context();

    DaoSession getDaoSession();

    /**
     * BasePresenter是注射目标，也就是说想要在哪个类里面使用依赖注解，必须在component中注射。
     比如我们想在BasePresenter这个类里，使用依赖注入DaoSession实例，必须在component里插入。
     * @param basePresenter
     * @return
     */
    //BasePresenter inject(BasePresenter basePresenter);

}
