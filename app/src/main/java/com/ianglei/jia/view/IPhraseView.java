package com.ianglei.jia.view;

import android.view.MenuItem;

import com.ianglei.jia.mo.Phrase;

/**
 * Created by ianglei on 2018/1/14.
 */

public interface IPhraseView extends IView{

    void finishView();

    void setToolbarTitle(int title);

    void initViewWithEditMode(Phrase phrase);
    void initViewWithViewMode(Phrase phrase);
    void initViewWithCreateMode(Phrase phrase);

    void setOperateTime(String text);

    void setMenuItemVisible(boolean visible);

    void showKeyBoard();
    void hideKeyBoard();

    String getPhraseTitle();
    String getPhraseTranslation();
    String getPhraseSample();
    String getPhraseTag();

    void showProgress(boolean isVisible);

    void showNotSavePhraseDialog();

    boolean isDoneMenuItemVisible();
    boolean isDoneMenuItemNull();
    void setDoneMenuItemVisible(boolean visible);

    boolean onOptionsItemSeleted(MenuItem item);

    void showNotSaveNoteDialog();

}
