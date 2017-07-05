package com.clpstudio.bsocial.presentation.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class NotificationController {
    private static final int NOTIFICATION_LED_TURN_ON = 500;

    private Context context;
    private NotificationBuilder builder;
    private NotificationManager notificationManager;

    @Inject
    public NotificationController(Context context, NotificationBuilder notificationBuilder) {
        this.context = context;
        this.builder = notificationBuilder;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showGeneralNotification(Intent intentAction, String title, String subtitle, String message) {
        Notification notification = getNotificationWithCustomLed(
                builder.getGeneralNotificationBuilder(context, intentAction, title, subtitle, message)
                        .setVibrate(new long[]{100, 100, 100, 100, 100})
                        .setPriority(Notification.PRIORITY_HIGH)
                        .build()
        );

        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    private Notification getNotificationWithCustomLed(Notification notification) {
        notification.ledARGB = 0xffff00; //Yellow
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        notification.ledOnMS = NOTIFICATION_LED_TURN_ON;
        notification.ledOffMS = NOTIFICATION_LED_TURN_ON;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    public void clearNotification(int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}