package com.ianglei.jia.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.ianglei.jia.R;
import com.ianglei.jia.di.ContextLifeCycle;
import com.ianglei.jia.mo.DaoSession;
import com.ianglei.jia.mo.Phrase;
import com.ianglei.jia.utils.L;
import com.ianglei.jia.utils.LJ;
import com.ianglei.jia.utils.ObservableUtils;
import com.ianglei.jia.utils.TimeUtils;
import com.ianglei.jia.view.IPhraseView;
import com.ianglei.jia.view.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ianglei on 2018/1/7.
 */

public class PhrasePresenter extends BasePresenter implements IPresenter
, View.OnFocusChangeListener,TextWatcher,DialogInterface.OnClickListener{

    public static final String TAG = "PhrasePresenter";

    public final static int VIEW_PHRASE_MODE = 0x00;
    public final static int EDIT_PHRASE_MODE = 0x01;
    public final static int CREATE_PHRASE_MODE = 0x02;
    public final static String OPERATE_PHRASE_TYPE_KEY = "OPERATE_PHRASE_TYPE_KEY";

    private IPhraseView phraseView;

    private MainPresenter.NotifyEvent<Phrase> event;

    /**
     * 操作类型
     */
    private int operateMode = 0;

    private Context context;

    private ObservableUtils observableUtils;

    private List<Phrase> phraseList;

    private DaoSession daoSession;

    private Phrase phrase;

    public PhrasePresenter(){}

    @Inject
    public PhrasePresenter(@ContextLifeCycle("Activity") Context context, ObservableUtils observableUtils, DaoSession daoSession) {
        this.context = context;
        this.observableUtils = observableUtils;
        this.daoSession = daoSession;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //在准备接收消息的页面注册EventBus，准备接受消息。
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
        phraseView.hideKeyBoard();
    }

    @Override
    public void onDestroy() {
        if(event != null){
            EventBus.getDefault().post(event);
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 通过Phrase参数类型匹配调用此方法
     * @param phrase
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(Phrase phrase){
        this.phrase = phrase;
        initToolbar();
        initEditText();
        initTimeTextView();
    }

    public void parseIntent(Intent intent){
        if(intent != null && intent.getExtras() != null){
            operateMode = intent.getExtras().getInt(OPERATE_PHRASE_TYPE_KEY, 0);
        }
    }

    /**
     * 初始化工具栏标题
     */
    private void initToolbar(){
        phraseView.setToolbarTitle(R.string.view_phrase);
        switch (operateMode){
            case CREATE_PHRASE_MODE: phraseView.setToolbarTitle(R.string.create_phrase);break;
            case EDIT_PHRASE_MODE: phraseView.setToolbarTitle(R.string.edit_phrase);break;
            case VIEW_PHRASE_MODE: phraseView.setToolbarTitle(R.string.view_phrase);break;
            default:break;
        }
    }

    private void initEditText(){
        switch (operateMode){
            case EDIT_PHRASE_MODE: phraseView.initViewWithEditMode(phrase);break;
            case VIEW_PHRASE_MODE: phraseView.initViewWithViewMode(phrase);break;
            default:phraseView.initViewWithCreateMode(phrase);break;
        }
    }

    private void initTimeTextView(){
        phraseView.setOperateTime(getOprTimeLineText(phrase));
    }

    private String getOprTimeLineText(Phrase phrase){
        if(phrase == null || phrase.getCreateTime() == 0){
            return "";
        }
        return "123";
    }

    private void savePhrase(){
        LJ.d(TAG, "Save Phrase");
        phraseView.hideKeyBoard();
        if(TextUtils.isEmpty(phraseView.getPhraseTitle())){
            phrase.setPhrase(context.getString(R.string.null_phrase));
        }else{
            phrase.setPhrase(phraseView.getPhraseTitle());
        }
        phrase.setSample(phraseView.getPhraseSample());
        phrase.setTranslate(phraseView.getPhraseTranslation());
        phrase.setTags(phraseView.getPhraseTag());
        event = new MainPresenter.NotifyEvent<>();
        switch (operateMode){
            case CREATE_PHRASE_MODE:
                phrase.setCreateTime(TimeUtils.getCurrentTimeInLong());
                phrase.setIsLearned(false);
                phrase.setLearnedCount(0);

                event.setType(MainPresenter.NotifyEvent.CREATE_PHRASE);
                daoSession.insert(phrase);
                break;
            default:
                event.setType(MainPresenter.NotifyEvent.UPDATE_PHRASE);
                daoSession.update(phrase);
                break;
        }

        event.setData(phrase);
        phraseView.finishView();
    }


    @Override
    public void attachView(IView v) {
        this.phraseView = (IPhraseView)v;
    }


    @Override
    /**
     * 弹出对话框
     */
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                savePhrase();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                phraseView.finishView();
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        L.i(TAG, "*********Text changed");
        if(phraseView.isDoneMenuItemNull()){
            return;
        }
        String titleText = phraseView.getPhraseTitle();
        String translationText = phraseView.getPhraseTranslation();
        String sampleText = phraseView.getPhraseSample();
        String tagsText = phraseView.getPhraseTag();
        //去除特殊字符
        String sampleFormatText = sampleText.replaceAll("\\s*|\t|\r|\n", "");
        //没有改就不用保存
        if(!TextUtils.isEmpty(sampleFormatText) || !TextUtils.isEmpty(titleText)
                || !TextUtils.isEmpty(translationText) || !TextUtils.isEmpty(tagsText)){
            if(TextUtils.equals(titleText, phrase.getPhrase())
                && TextUtils.equals(translationText, phrase.getTranslate())
                    && TextUtils.equals(sampleText, phrase.getSample())){
                phraseView.setDoneMenuItemVisible(false);
                return;
            }
            //显示保存
            phraseView.setDoneMenuItemVisible(true);
        }else{
            phraseView.setDoneMenuItemVisible(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus){
            phraseView.setToolbarTitle(R.string.edit_phrase);
        }
    }

    /**
     * 刚显示时不需要保存按钮
     */
    public void onPrepareOptionsMenu()
    {
        phraseView.setDoneMenuItemVisible(false);
    }

    /**
     * 选择按钮
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.done:
                LJ.d(TAG, "Done clicked");
                savePhrase();
                return true;
            case android.R.id.home:
                phraseView.hideKeyBoard();
                //有保存按钮代表没保存
                if(phraseView.isDoneMenuItemVisible()){
                    phraseView.showNotSavePhraseDialog();
                    return true;
                }
                phraseView.finishView();
                return true;
            default:
                return true;
        }
    }

    public void onToolbarClicked()
    {
        LJ.d(TAG, "Done clicked");
        savePhrase();
    }

    public boolean onKeyDown(int keycode){
        if(keycode == KeyEvent.KEYCODE_BACK){
            phraseView.hideKeyBoard();
            //如果有保存按钮，表示未保存
            if(phraseView.isDoneMenuItemVisible()){
                phraseView.showNotSavePhraseDialog();
                return true;
            }
            phraseView.finishView();
        }
        return false;
    }
}
