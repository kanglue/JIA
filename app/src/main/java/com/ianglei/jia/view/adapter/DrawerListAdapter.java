package com.ianglei.jia.view.adapter;

import android.content.Context;

import com.ianglei.jia.R;

import java.util.List;

/**
 * Created by ianglei on 2018/3/11.
 */

public class DrawerListAdapter extends SimpleListAdapter {

    public DrawerListAdapter(Context context, List<String> list){
        super(context, list);
    }

    @Override
    protected int getLayout() {
        return R.layout.drawer_list_item_layout;
    }
}
