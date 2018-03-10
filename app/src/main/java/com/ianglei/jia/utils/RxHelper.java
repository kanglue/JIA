package com.ianglei.jia.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ianglei on 2018/1/1.
 */

public class RxHelper {

    public static Observable<Integer> countdown(int time)
    {
        if(time < 0)
        {
            time = 0;
        }
        final int countTime = time;

        return Observable.interval(0,1, TimeUnit.SECONDS)
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                }).take(countTime + 1) //take自动结束
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
