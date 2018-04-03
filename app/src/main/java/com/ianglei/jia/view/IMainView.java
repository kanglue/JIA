package com.ianglei.jia.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ianglei.jia.mo.Phrase;

import java.util.List;

/**
 * Created by ianglei on 2018/1/14.
 */

public interface IMainView extends IView {
    void showProgress(boolean isVisible);

    void initToolbar();
    void initDrawerView(List<String> list);
    void initRecyclerView(List<Phrase> phrase);
    void setLayoutManager(RecyclerView.LayoutManager manager);

    void addPhrase(Phrase phrase);
    void updatePhrase(Phrase phrase);
    void delPhrase(Phrase phrase);

    void showFab(boolean visible);

    void scrollRecyclerViewToTop();
    void startRefresh();
    void stopRefresh();
    boolean isRefreshing();

    void showSnackbar(int message);

    void showNormalPopupMenu(View view, Phrase phrase);

    void setDrawerGravity(int gravity);
    void setDrawerItemChecked(int position);
    boolean isDrawerOpen();
    void closeDrawer();
    void openOrCloseDrawer();
}
