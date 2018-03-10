package com.ianglei.jia.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by ianglei on 2018/1/14.
 */

public class LJ {

    public static void d(String tag, String content){
        Logger.d(tag, content);
    }

    public static void d(String tag, String content, Throwable tr){
        Logger.d(tag, content, tr);
    }

    public static void e(String tag, String content){
        Logger.e(tag, content);
    }

    public static void e(String tag, String content, Throwable tr){
        Logger.e(tag, content, tr);
    }

    public static void i(String tag, String content){
        Logger.i(tag, content);
    }

    public static void i(String tag, String content, Throwable tr){
        Logger.i(tag, content, tr);
    }

    public static void v(String tag, String content){
        Logger.v(tag, content);
    }

    public static void v(String tag, String content, Throwable tr){
        Logger.v(tag, content, tr);
    }

    public static void w(String tag, String content){
        Logger.w(tag, content);
    }

    public static void w(String tag, String content, Throwable tr){
        Logger.w(tag, content, tr);
    }

    public static void w(String tag, Throwable tr){
        Logger.w(tag, tr);
    }
}
