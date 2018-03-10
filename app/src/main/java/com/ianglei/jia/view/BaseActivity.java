package com.ianglei.jia.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.WindowManager;

import com.ianglei.jia.R;
import com.ianglei.jia.di.components.ActivityComponent;
import com.ianglei.jia.utils.ToolbarUtils;
import com.itheima.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;

/**
 * Created by ianglei on 2018/1/6.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected ActivityComponent activityComponent;

    public final static String IS_START_ANIM = "IS_START_ANIM";
    public final static String IS_CLOSE_ANIM = "IS_CLOSE_ANIM";

    protected boolean isStartAnim = true;
    protected boolean isCloseAnim = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //解析意图先
        parseIntent(getIntent());
        showActivityInAnim();
        super.onCreate(savedInstanceState);
        //沉浸式窗口
        initWindow();
        initDI();
        setContentView(getViewLayout());
        ButterKnife.bind(this);
        initToolbar();
    }

    /**
     * 意图中是否打开动画
     * @param intent
     */
    private void parseIntent(Intent intent) {
        if(intent != null){
            isStartAnim = intent.getBooleanExtra(IS_START_ANIM, true);
            isCloseAnim = intent.getBooleanExtra(IS_CLOSE_ANIM, true);
        }
    }

    /**
     * 进入动画
     */
    protected void showActivityInAnim(){
        if (isStartAnim) {
            //第一个参数是进入：从下向上出现，第二个参数退出时渐隐动画
            overridePendingTransition(R.anim.activity_down_up_anim, R.anim.activity_exit_anim);
        }
    }

    /**
     * 退出动画
     */
    protected void showActivityExitAnim(){
        if (isCloseAnim) {
            overridePendingTransition(R.anim.activity_exit_anim, R.anim.activity_up_down_anim);
        }
    }

    protected void initDI(){};

    protected abstract int getViewLayout();

    @TargetApi(19)
    private void initWindow(){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //沉浸式状态栏
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getColorPrimary());
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    /**
     * 初始化各子View的Toolbar
     * @param toolbar
     */
    protected void initToolbar(Toolbar toolbar){
        ToolbarUtils.initToolbar(toolbar, this);
    }

    protected void initToolbar(){}

    /**
     * 获取主色值
     * @return
     */
    public int getColorPrimary(){
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }



    /**
     * 增加了默认的返回finish事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ActivityComponent getActivityComponent(){
        return activityComponent;
    }

    /**
     * Setting中设置后调用
     * @param anim
     */
    public void reload(boolean anim){

    }

    @Override
    public void finish() {
        super.finish();
        showActivityExitAnim();
    }
}
