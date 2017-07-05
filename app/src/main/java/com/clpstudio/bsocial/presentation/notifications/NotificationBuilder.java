package com.clpstudio.bsocial.presentation.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.clpstudio.bsocial.R;

import javax.inject.Inject;

public class NotificationBuilder {

    @Inject
    public NotificationBuilder() {
    }

    public NotificationCompat.Builder getGeneralNotificationBuilder(Context context, Intent resultIntent, String title,
                                                                    String subtitle, String message) {
        int uniqueId = (int) (System.currentTimeMillis() / 1000);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, uniqueId, resultIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setTicker(message)
                .setContentText(subtitle)
                .setSubText(message)
                .setSound(soundUri);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        return builder;
    }

}
