package com.ianglei.jia.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.ianglei.jia.R;
import com.ianglei.jia.di.ContextLifeCycle;
import com.ianglei.jia.mo.DaoSession;
import com.ianglei.jia.mo.Phrase;
import com.ianglei.jia.utils.ObservableUtils;
import com.ianglei.jia.view.IMainView;
import com.ianglei.jia.view.IView;
import com.ianglei.jia.view.PhraseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ianglei on 2018/1/18.
 */

public class MainPresenter extends BasePresenter implements IPresenter, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        PopupMenu.OnMenuItemClickListener, MenuItemCompat.OnActionExpandListener
{
    private Context context;

    private ObservableUtils observableUtils;

    private List<Phrase> phraseList;

    private DaoSession daoSession;

    private IMainView mainView;

    @Inject
    public MainPresenter(@ContextLifeCycle("Activity") Context context, ObservableUtils observableUtils, DaoSession daoSession) {
        this.context = context;
        this.observableUtils = observableUtils;
        this.daoSession = daoSession;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainView.initToolbar();
        initItemLayoutManager();
        initRecyclerView();
        EventBus.getDefault().register(this);
    }

    private void initItemLayoutManager()
    {
        //TODO 读配置
        switchItemLayoutManager(false);
    }

    private void switchItemLayoutManager(boolean linear){
        if(linear){
            mainView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }else{
            mainView.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL));
        }
    }

    private void initRecyclerView()
    {
        mainView.showProgress(true);

        observableUtils.getLocalPhrase(daoSession)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((phraseList) -> {
                    mainView.initRecyclerView(phraseList);
                    mainView.showProgress(false);
                }, (e) ->{
                    e.printStackTrace();
                    mainView.showProgress(false);
                });
    }

    @Override
    public void attachView(IView v) {
        this.mainView = (IMainView)v;
    }


    public void newPhrase(){
        Phrase phrase = new Phrase();
        startPhraseActivity(PhrasePresenter.CREATE_PHRASE_MODE, phrase);
    }

    public void startPhraseActivity(int type, Phrase phrase){
        Intent intent = new Intent(context, PhraseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PhrasePresenter.OPERATE_PHRASE_TYPE_KEY, type);
        EventBus.getDefault().postSticky(phrase);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    @Override
    public void onRefresh() {
        mainView.stopRefresh();
    }

    @Override
    public void onClick(View v) {

    }

    public void showPopMenu(View view, int position, Phrase phrase){
        mainView.showNormalPopupMenu(view, phrase);
    }

    public boolean onPopupMenuClick(int id, Phrase phrase){
        switch(id){
            case R.id.edit:
                startPhraseActivity(PhrasePresenter.EDIT_PHRASE_MODE, phrase);
                break;
            case R.id.move_to_trash:
                del(phrase);
                break;
            case R.id.master:
                master(phrase);
                break;
            default:
                break;
        }
        return true;
    }

    private void master(Phrase phrase){
        if(phrase == null){return;}
        phrase.setIsLearned(true);
        daoSession.update(phrase);
    }

    private void del(Phrase phrase){
        if(phrase == null)
            return;
        daoSession.delete(phrase);
        mainView.delPhrase(phrase);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    /**
     * 接收Event发送的事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(NotifyEvent event){
        switch(event.getType()){
            case NotifyEvent.REFRESH_LIST:
                mainView.startRefresh();
                onRefresh();
                break;
            case NotifyEvent.CREATE_PHRASE:
                if(event.getData() instanceof Phrase){
                    Phrase phrase = (Phrase)event.getData();
                    mainView.addPhrase(phrase);
                    mainView.scrollRecyclerViewToTop();

                }
                break;
            case NotifyEvent.UPDATE_PHRASE:
                if(event.getData() instanceof Phrase){
                    Phrase phrase = (Phrase)event.getData();
                    mainView.updatePhrase(phrase);
                    mainView.scrollRecyclerViewToTop();

                }
                break;
            default:
                break;
        }
    }

    /**
     * 自定义通知事件类型
     * @param <T>
     */
    public static class NotifyEvent<T>{
        public static final int REFRESH_LIST = 0;
        public static final int CREATE_PHRASE = 1;
        public static final int UPDATE_PHRASE = 2;
        public static final int CHANGE_THEME = 3;
        public static final int CHANGE_ITEM_LAYOUT = 4;
        public static final int CHANGE_MENU_GRAVITY = 5;
        //类型
        private int type;
        //对象
        private T data;
        @IntDef({REFRESH_LIST, CREATE_PHRASE, UPDATE_PHRASE, CHANGE_THEME,
                CHANGE_ITEM_LAYOUT, CHANGE_MENU_GRAVITY})
        public @interface Type {
        }

        public @Type int getType() {
            return type;
        }

        public void setType(@Type int type) {
            this.type = type;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
