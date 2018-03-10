package com.ianglei.jia.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;


public class NotificationUtil {
	private static NotificationManager notificationManager;
	private static Notification notification ;

	public static void showNotification(Context context, String musicName)
	{
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,  new Intent(context, DetailActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//
//        notification= new Notification.Builder(context)
//                .setSmallIcon(R.drawable.avatar) // 设置状态栏中的小图片，尺寸一般建议在24×24， 这里也可以设置大图标
//                .setTicker("begin to play")// 设置显示的提示文字
//                .setContentTitle(musicName)// 设置显示的标题
//                .setContentText(musicName)// 消息的详细内容
//                .setContentIntent(pendingIntent) // 关联PendingIntent
//                .setNumber(0) // 在TextView的右方显示的数字，可以在外部定义一个变量，点击累加setNumber(count),这时显示的和
//                .build();
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        int notificationId = 0;
//        notificationManager.notify(notificationId, notification);
    }

	public static void clearNotification(Context context){
        // 启动后删除之前我们定义的通知   
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);  
 
    }

}
