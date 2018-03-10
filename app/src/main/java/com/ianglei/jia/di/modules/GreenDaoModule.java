package com.ianglei.jia.di.modules;

import android.database.sqlite.SQLiteDatabase;

import com.ianglei.jia.JApplication;
import com.ianglei.jia.mo.DaoMaster;
import com.ianglei.jia.mo.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ianglei on 2018/1/6.
 */

@Module
public class GreenDaoModule {
    private final String DB_NAME = "jia-db";

    private final JApplication application;

    public GreenDaoModule(JApplication application)
    {
        this.application = application;
    }

    @Provides
    @Singleton
    public SQLiteDatabase provideSQLiteDatabase(JApplication application)
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, DB_NAME, null);
        SQLiteDatabase database = helper.getWritableDatabase();
        return database;
    }

    @Provides
    @Singleton
    public DaoMaster provideDaoMaster(SQLiteDatabase database)
    {
        DaoMaster daoMaster = new DaoMaster(database);
        return daoMaster;
    }

    @Provides
    @Singleton
    public DaoSession provideDaoSession(DaoMaster daoMaster)
    {
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }
}
