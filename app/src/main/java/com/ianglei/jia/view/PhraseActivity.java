package com.ianglei.jia.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ianglei.jia.JApplication;
import com.ianglei.jia.R;
import com.ianglei.jia.di.components.DaggerActivityComponent;
import com.ianglei.jia.di.modules.ActivityModule;
import com.ianglei.jia.mo.Phrase;
import com.ianglei.jia.presenter.PhrasePresenter;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ianglei on 2018/1/7.
 */

public class PhraseActivity extends BaseActivity implements IPhraseView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phrase_edit_text)
    MaterialEditText phraseEditText;
    @BindView(R.id.phrase_translation_edit_text)
    MaterialEditText phraseTranslationText;
    @BindView(R.id.phrase_sample_edit_text)
    MaterialEditText phraseSampleText;
    @BindView(R.id.opr_time_line_text)
    TextView oprTimeLineText;
    @BindView(R.id.phrase_tag_edit_text)
    MaterialEditText phraseTagText;

    private MenuItem doneMenuItem;

    @Inject
    PhrasePresenter phrasePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        phrasePresenter.onCreate(savedInstanceState);

        toolbar.inflateMenu(R.menu.menu_phrase);
        getSupportActionBar().setHomeButtonEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.done:
                        phrasePresenter.onToolbarClicked();
                        break;
                    case android.R.id.home:
                        finishView();
                        break;
                }
                return true;
            }
        });
    }

    /**
     *
     */
    private void initPresenter(){
        //初始化时将View和Presenter进行关联
        phrasePresenter.attachView(this);
        //传递Intent
        phrasePresenter.parseIntent(getIntent());
    }

    @Override
    protected void initDI() {
        JApplication application = (JApplication)getApplication();
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(application.getApplicationComponent())
                .build();
        activityComponent.inject(this);
    }

    @Override
    protected void onStop() {
        phrasePresenter.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        phrasePresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_phrase;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
    }

    public static void startActivity(){

    }

    @Override
    /**
     * 只会调用一次，在Menu显示之前去调用一次，之后就不会在去调用
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phrase, menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finishView();
            }
        });
        return true;
    }

    @Override
    /**
     * 每次在display Menu之前，都会去调用，只要按一次Menu按鍵，就会调用一次。
     * 所以可以在这里动态的改变menu。
     */
    public boolean onPrepareOptionsMenu(Menu menu) {
        //取菜单第一个done按钮
        doneMenuItem = menu.getItem(0);
        phrasePresenter.onPrepareOptionsMenu();
        //TODO 这里只是规避
        //MenuItemCompat.getActionView(doneMenuItem).setOnClickListener(this);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSeleted(MenuItem item){
        if(phrasePresenter.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return phrasePresenter.onKeyDown(keyCode);
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void setToolbarTitle(int title) {
        if(toolbar != null){
            toolbar.setTitle(title);
        }
    }

    @Override
    public void showProgress(boolean isVisible) {

    }

    @Override
    public void showNotSavePhraseDialog() {

    }

    @Override
    /**
     * 以编辑模式打开
     */
    public void initViewWithEditMode(Phrase phrase) {
        showKeyBoard();
        phraseEditText.requestFocus();
        phraseEditText.setText(phrase.getPhrase());
        //将光标调整到最后
        phraseEditText.setSelection(phrase.getPhrase().length());
        phraseTranslationText.setText(phrase.getTranslate());
        phraseTranslationText.setSelection(phrase.getTranslate().length());
        phraseSampleText.setText(phrase.getSample());
        phraseSampleText.setSelection(phrase.getSample().length());
        phraseTagText.setText(phrase.getTags());
        phraseTagText.setSelection(phrase.getTags().length());

        phraseEditText.addTextChangedListener(phrasePresenter);
        phraseTranslationText.addTextChangedListener(phrasePresenter);
        phraseSampleText.addTextChangedListener(phrasePresenter);
        phraseTagText.addTextChangedListener(phrasePresenter);
    }

    @Override
    public void initViewWithViewMode(Phrase phrase) {
        hideKeyBoard();
        phraseEditText.setText(phrase.getPhrase());
        phraseEditText.setOnFocusChangeListener(phrasePresenter);
        phraseTranslationText.setText(phrase.getTranslate());
        phraseSampleText.setText(phrase.getSample());
        phraseTagText.setText(phrase.getTags());

        phraseEditText.addTextChangedListener(phrasePresenter);
        phraseTranslationText.addTextChangedListener(phrasePresenter);
        phraseSampleText.addTextChangedListener(phrasePresenter);
        phraseTagText.addTextChangedListener(phrasePresenter);
    }

    @Override
    public void initViewWithCreateMode(Phrase phrase) {
        phraseEditText.requestFocus();
        phraseEditText.addTextChangedListener(phrasePresenter);
        phraseSampleText.addTextChangedListener(phrasePresenter);
        phraseTranslationText.addTextChangedListener(phrasePresenter);
        phraseTagText.addTextChangedListener(phrasePresenter);
    }

    @Override
    public void setOperateTime(String text) {
        oprTimeLineText.setText(text);
    }

    @Override
    public void setMenuItemVisible(boolean visible) {

    }

    @Override
    public void showKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void hideKeyBoard() {
        hideKeyBoard(phraseEditText);
    }

    @Override
    public String getPhraseTitle() {
        return phraseEditText.getText().toString();
    }

    @Override
    public String getPhraseTranslation() {
        return phraseTranslationText.getText().toString();
    }

    @Override
    public String getPhraseSample() {
        return phraseSampleText.getText().toString();
    }

    @Override
    public String getPhraseTag() {
        return phraseTagText.getText().toString();
    }


    private void hideKeyBoard(EditText editText){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);
    }

    @Override
    /**
     * 设置显示保存按钮
     */
    public void setDoneMenuItemVisible(boolean visible)
    {
        if(doneMenuItem != null){
            doneMenuItem.setVisible(visible);
        }
    }

    @Override
    /**
     * 是否显示保存按钮
     */
    public boolean isDoneMenuItemVisible(){
        return doneMenuItem != null && doneMenuItem.isVisible();
    }

    @Override
    public boolean isDoneMenuItemNull(){
        return doneMenuItem == null;
    }

    @Override
    public void showNotSaveNoteDialog() {
//        AlertDialog.Builder builder = DialogUtils.makeDialogBuilder(this);
//        builder.setTitle(R.string.not_save_phrase_leave_tip);
//        builder.setPositiveButton(R.string.sure, notePresenter);
//        builder.setNegativeButton(R.string.cancel, notePresenter);
//        builder.show();
    }

}
