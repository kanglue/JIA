package com.ianglei.jia.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by j00255628 on 2015/10/23.
 */
public class StorageUtil {

    private StorageUtil()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable()
    {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageDirectory().exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize()
    {
        if (isSDCardEnable())
        {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath)
    {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath()))
        {
            filePath = getSDCardPath();
        } else
        {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取当前程序路径
     */
    public static String getCurrentAppPath(Context context)
    {
        return context.getApplicationContext().getFilesDir().getAbsolutePath();
    }

    /**
     * 获取该程序的安装包路径
     */
    public static String getPackageResourcePath(Context context)
    {
        return context.getApplicationContext().getPackageResourcePath();
    }

    /**
     * 获取程序默认数据库路径
     * @return
     */
    public static String getDatabasePath(Context context)
    {
        return context.getApplicationContext().getDatabasePath("jmemo.db").getAbsolutePath();
    }

    public static void printAppPathInfo(Context context)
    {
        Log.i("CurrentAppPath", getCurrentAppPath(context));
        Log.i("PackageResourcePath", getPackageResourcePath(context));
        Log.i("DatabasePath", getDatabasePath(context));
        Log.i("RootDirectoryPath", getRootDirectoryPath());
        Log.i("SDCardPath()", getSDCardPath());
        Log.i("isSDExist", isSDCardEnable() + "");
    }
}
