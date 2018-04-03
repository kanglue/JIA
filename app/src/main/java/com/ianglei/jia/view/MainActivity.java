package com.ianglei.jia.view;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ianglei.jia.JApplication;
import com.ianglei.jia.R;
import com.ianglei.jia.di.components.DaggerActivityComponent;
import com.ianglei.jia.di.modules.ActivityModule;
import com.ianglei.jia.mo.Phrase;
import com.ianglei.jia.presenter.MainPresenter;
import com.ianglei.jia.presenter.PhrasePresenter;
import com.ianglei.jia.utils.SnackbarUtils;
import com.ianglei.jia.utils.ToolbarUtils;
import com.ianglei.jia.view.adapter.BaseRecyclerViewAdapter;
import com.ianglei.jia.view.adapter.DrawerListAdapter;
import com.ianglei.jia.view.adapter.PhraseAdapter;
import com.ianglei.jia.view.adapter.SimpleListAdapter;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements IMainView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refresher)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.left_drawer_listview)
    ListView drawerListView;
    @BindView(R.id.left_drawer)
    View leftDrawerView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    BetterFab fab;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private ActionBarDrawerToggle drawerToggle;
    private PhraseAdapter phraseAdapter;

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这句害了4h，相当于布局先被赋了
        //setContentView(R.layout.activity_main);
        //必须写在setContentView之后，父类已经调用过
        //ButterKnife.bind(this);
        //init();
        initPresenter();
        //逻辑全在presenter中做
        mainPresenter.onCreate(savedInstanceState);
    }

    @Override
    protected int getViewLayout(){
        return R.layout.activity_main;
    }

    @Override
    public void initToolbar(){
        ToolbarUtils.initToolbar(toolbar, this);
    }

    @Override
    /**
     * 初始化列表时添加事件
     */
    public void initRecyclerView(List<Phrase> list){
        phraseAdapter = new PhraseAdapter(list, this);
        recyclerView.setHasFixedSize(true);
        phraseAdapter.setOnInViewClickListener(R.id.notes_item_root,
                new BaseRecyclerViewAdapter.onInternalClickListenerImpl<Phrase>() {
                    @Override
                    public void onClickListener(View parentView, View v, Integer position, Phrase phrase) {
                        super.onClickListener(parentView, v, position, phrase);
                        mainPresenter.startPhraseActivity(PhrasePresenter.VIEW_PHRASE_MODE, phrase);
                    }

                    @Override
                    public void onLongClickListener(View parentView, View v, Integer position, Phrase phrase) {

                    }
                });
        phraseAdapter.setOnInViewClickListener(R.id.phrase_more,
                new BaseRecyclerViewAdapter.onInternalClickListenerImpl<Phrase>(){
                    @Override
                    public void onClickListener(View parentView, View v, Integer position, Phrase phrase) {
                        super.onClickListener(parentView, v, position, phrase);
                        mainPresenter.showPopMenu(v, position, phrase);
                    }
        });
        phraseAdapter.setOnlyFirst(false);
        phraseAdapter.setDuration(300);
        recyclerView.setAdapter(phraseAdapter);
        swipeRefreshLayout.setColorSchemeColors(getColorPrimary());
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(mainPresenter);
    }

    @Override
    public void initDrawerView(List<String> list){
        SimpleListAdapter adapter = new DrawerListAdapter(this, list);
        drawerListView.setAdapter(adapter);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainPresenter.onDrawerItemSelect(position);
            }
        });


    }


    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }

    /**
     * 把当前activity实例和presenter关联
     */
    private void initPresenter(){
        mainPresenter.attachView(this);
    }

    @Override
    protected void initDI() {
        JApplication application = (JApplication)getApplication();
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                //TODO 这一步是为何？
                .applicationComponent(application.getApplicationComponent()).build();
        activityComponent.inject(this);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    //初始化Toolbar的菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem item = menu.findItem(R.id.action_add);
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                mainPresenter.newPhrase();
//                return true;
//            }
//        });
        return true;
    }

    @Override
    /**
     * 菜单点击时的作用
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_add:
                mainPresenter.newPhrase();
                break;
            case android.R.id.home:
                openOrCloseDrawer();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initToolbar(Toolbar toolbar){
        ToolbarUtils.initToolbar(toolbar, this);
    }


    @OnClick(R.id.fab)
    public void newPhrase(View view){
        mainPresenter.newPhrase();
    }

    @Override
    public void showProgress(boolean isVisible) {
        progressWheel.setBarColor(getColorPrimary());
        if (isVisible){
            if (!progressWheel.isSpinning()) {
                progressWheel.spin();
            }
        }else{
            progressWheel.postDelayed(() -> {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                }
            }, 300);
        }
    }

    @Override
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void addPhrase(Phrase phrase) {
        phraseAdapter.add(phrase);
    }

    @Override
    public void updatePhrase(Phrase phrase) {
        phraseAdapter.update(phrase);
    }

    @Override
    public void delPhrase(Phrase phrase) {
        phraseAdapter.remove(phrase);
    }

    @Override
    public void showFab(boolean visible) {
        fab.setForceHide(!visible);
    }

    @Override
    public void scrollRecyclerViewToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void startRefresh() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    @Override
    public void showSnackbar(int message) {
        SnackbarUtils.show(fab, message);
    }

    @Override
    public void showNormalPopupMenu(View view, Phrase phrase){
        PopupMenu menu = new PopupMenu(this, view);
        menu.getMenuInflater().inflate(R.menu.menu_phrase_more, menu.getMenu());
        menu.setOnMenuItemClickListener((item -> mainPresenter.onPopupMenuClick(item.getItemId(), phrase)));
        menu.show();
    }

    @Override
    public void setDrawerGravity(int gravity) {
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams)leftDrawerView.getLayoutParams();
        params.gravity = gravity;
        leftDrawerView.setLayoutParams(params);
    }

    @Override
    public void setDrawerItemChecked(int position) {
        drawerListView.setItemChecked(position, true);
    }

    @Override
    public void closeDrawer()
    {
        if(drawerLayout.isDrawerOpen(leftDrawerView)){
            drawerLayout.closeDrawer(leftDrawerView);
        }
    }

    @Override
    public boolean isDrawerOpen(){
        return drawerLayout.isDrawerOpen(leftDrawerView);
    }

    @Override
    public void openOrCloseDrawer(){
        if(drawerLayout.isDrawerOpen(leftDrawerView)){
            drawerLayout.closeDrawer(leftDrawerView);
        }else{
            drawerLayout.openDrawer(leftDrawerView);
        }
    }
}
