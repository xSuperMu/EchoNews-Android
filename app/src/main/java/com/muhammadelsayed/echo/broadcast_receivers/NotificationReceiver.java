package com.muhammadelsayed.echo.broadcast_receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.muhammadelsayed.echo.R;
import com.muhammadelsayed.echo.activities.MainActivity;
import com.muhammadelsayed.echo.database.DatabaseHelper;
import com.muhammadelsayed.echo.echo_utils.Constants;
import com.muhammadelsayed.echo.echo_utils.Utils;
import com.muhammadelsayed.echo.model.Article;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    public static Article article;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "onReceive: received !!");
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent targetIntent = new Intent(context, MainActivity.class);

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.settings_preferences), Context.MODE_PRIVATE);
        boolean internationalHeadline = sharedPreferences.getBoolean("international_headline", true);
        boolean usHeadline = sharedPreferences.getBoolean("us_headline", false);
        boolean ukHeadline = sharedPreferences.getBoolean("uk_headline", false);
        boolean australiaHeadline = sharedPreferences.getBoolean("australia_headline", false);

        String section;

        if (usHeadline) {
            section = "us-news";
        } else if (ukHeadline) {
            section = "uk-news";
        } else if (australiaHeadline) {
            section = "australia-news";
        } else {
            section = "news";
        }

        targetIntent.putExtra("from_notification", true);
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent saveButtonIntent = new Intent(context, saveButtonListener.class);
        final PendingIntent saveBtnPendingIntent = PendingIntent.getBroadcast(context, 101, saveButtonIntent, 0);


        Map<String, Object> options = new HashMap<>();
        options.put("section", section);
        options.put("order-by", "newest");
        options.put("show-tags", "contributor");
        options.put("show-fields", "thumbnail,showInRelatedContent,shortUrl");
        options.put("page", 1);
        options.put("page-size", 20);
        options.put("api-key", Constants.GUARDIAN_API_KEY);
        Utils.getNews(options, new Utils.retrofitCallback() {
            @Override
            public void onSuccess(List<Article> articles) {
                article = articles.get(0);
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                long[] vibrationPattern = {500, 1000};

                Bitmap image = null;

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    URL url = new URL(article.getFields().getThumbnail());
                    image = BitmapFactory.decodeStream((InputStream) url.getContent());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setBadgeIconType(R.mipmap.ic_launcher)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(image)
                        .setContentTitle(article.getSectionName())
                        .setSound(sound)
                        .setVibrate(vibrationPattern)
                        .setLights(Color.BLACK, 500, 1000)
                        .setContentText(article.getWebTitle())
                        .setSubText(article.getType())
                        .setAutoCancel(true)
                        .addAction(0, "Save", saveBtnPendingIntent)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(article.getWebTitle()));

//                notificationManager.notify(100, builder.build());

                if (intent.getAction().equals("MY_NOTIFICATION_MESSAGE")) {
                    notificationManager.notify(100, builder.build());
                    Log.d(TAG, "Alarm");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.wtf(TAG, "onFailure(): BUSINESS -> " + t.getMessage());
            }
        });

    }

    public static class saveButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Save Button Clicked");
            final DatabaseHelper db = new DatabaseHelper(context);
            long id = db.saveArticle(article);
            Log.d(TAG, "onReceive: SAVE ID = " + id);


//            Cancel the notification
//            final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.cancel(100);
        }
    }
}
