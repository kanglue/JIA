package com.ianglei.jia.mo;

/**
 * Created by ianglei on 2018/3/18.
 */

public enum DrawerItem {
    //常量之间逗号分割
    ENGLISH(0x00),
    CALCULATOR(0x11);

    private int mValue;

    DrawerItem(int value){
        this.mValue = value;
    }

    public static DrawerItem mapValueToStatus(final int value){
        for(DrawerItem item : DrawerItem.values()){
            if(value == item.getValue()){
                return item;
            }
        }
        return ENGLISH;
    }

    public static DrawerItem getDefault(){
        return ENGLISH;
    }

    public int getValue(){
        return mValue;
    }
}
