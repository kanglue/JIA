package com.ianglei.jia.utils;

import com.ianglei.jia.mo.DaoSession;
import com.ianglei.jia.mo.Phrase;
import com.ianglei.jia.mo.PhraseDao;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ianglei on 2018/1/14.
 */

public class ObservableUtils {

    @Inject
    public ObservableUtils(){}

    public interface CallFunc<T>
    {
        T call() throws Exception;
    }

    /**
     * 根据传入的不同类型返回对应的Observable对象
     * @param callFunc
     * @param <T>
     * @return
     */
    private <T> Observable<T> create(final CallFunc<T> callFunc)
    {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try{
                    T t = callFunc.call();
                    subscriber.onNext(t);
                }
                catch (Exception e)
                {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Phrase>> getLocalPhrase(DaoSession daoSession)
    {
        return create(new GetLocalPhrase(daoSession));
    }

    private class GetLocalPhrase implements CallFunc<List<Phrase>>
    {
        private DaoSession daoSession;
        public GetLocalPhrase(DaoSession daoSession){
            this.daoSession = daoSession;
        }
        @Override
        public List<Phrase> call() throws Exception {
            PhraseDao phraseDao = daoSession.getPhraseDao();
            return phraseDao.loadAll();
        }
    }


    private class AddLocalPhrase implements CallFunc<Long>{
        private DaoSession daoSession;
        private Phrase phrase;

        public AddLocalPhrase(DaoSession daoSession, Phrase phrase){
            this.daoSession = daoSession;
            this.phrase = phrase;
        }
        @Override
        public Long call() throws Exception {
            return daoSession.getPhraseDao().insert(phrase);
        }
    }
}
