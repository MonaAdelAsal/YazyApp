package com.asc.yazy.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.asc.yazy.R;
import com.asc.yazy.activity.MainActivity;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.utils.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

public class ServiceFireBaseMessaging extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "10";
    private static final String TAG = "FIRE_BASE_BACKEND";
    private int enable = 1;
    private int random;


    @Override
    public void onCreate() {
        super.onCreate();
        checkNotificationSettings();
        //noinspection deprecation
        String theToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onCreate: theToken " + theToken);
        UtilsPreference.getInstance(getApplicationContext()).savePreference(Constants.USER_FIRE_BASE_TOKEN, theToken);
        BEUpdateFireBaseToken.updateServerToken(getApplicationContext(), theToken);

    }

    private void checkNotificationSettings() {
        ModelUser user = UtilsPreference.getInstance(getApplicationContext()).getUser();
        if (user != null && user.getAccess_token() != null && !user.getAccess_token().isEmpty()) {
            Log.d(TAG, "user token " + user.getAccess_token());
            if (user.getEnable_notification() == 1) {
                enable = 1;
            } else
                enable = 0;
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        BEUpdateFireBaseToken.updateServerToken(getApplicationContext(), token);
        Log.d(TAG, "onNewToken: token " + token);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        random = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        Log.d(TAG, "onMessageReceived: ");
        checkNotificationSettings();
        if (remoteMessage.getData().size() > 0) {
            String id = remoteMessage.getData().get("id");
            String target_id = remoteMessage.getData().get("target_id");
            String type = remoteMessage.getData().get("type");
            String title_ar = remoteMessage.getData().get("title_ar");
            String title_en = remoteMessage.getData().get("title_en");
            String body_ar = remoteMessage.getData().get("body_ar");
            String body_en = remoteMessage.getData().get("body_en");
            String created_at = remoteMessage.getData().get("created_at");
            String updated_at = remoteMessage.getData().get("updated_at");
            String title, body;
            if (Constants.getLANGUAGE().equals("ar")) {
                title = title_ar;
                body = body_ar;
            } else {
                title = title_en;
                body = body_en;
            }
            Log.d(TAG, "onMessageReceived: id " + id);
            Log.d(TAG, "onMessageReceived: target_id " + target_id);
            Log.d(TAG, "onMessageReceived: type " + type);
            Log.d(TAG, "onMessageReceived: title " + title);
            Log.d(TAG, "onMessageReceived: body " + body);
            Log.d(TAG, "onMessageReceived: created_at " + created_at);
            Log.d(TAG, "onMessageReceived: updated_at " + updated_at);


            /*MyServiceSyncAdapter.initializeSyncAdapter(getApplicationContext());
            MyServiceSyncAdapter.syncImmediately(getApplicationContext());
            */
            Constants.newNotification++;
            Log.e("notificationCount", Constants.newNotification + "");
            Intent broadcast = new Intent(Constants.TRANSACTION_NOTIFICATION_ID);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);

            if (enable == 1) {
                handleNotification(title, body, target_id, type, id);

            }
/*
            NotificationRoomModel notificationRoomModel = new NotificationRoomModel();
            notificationRoomModel.setTitle(title);
            notificationRoomModel.setDetails(body);
            notificationRoomModel.setOpened(false);
            assert action != null;
            notificationRoomModel.setType(Integer.parseInt(action));
            notificationRoomModel.setOffer_id(actionId);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            String date = df.format(Calendar.getInstance().getTime());
            notificationRoomModel.setCreated_at(date);
            notificationRoomModel.setUpdated_at(date);
            NotificationsRepository repository = new NotificationsRepository(getApplication());
            repository.insertNotification(notificationRoomModel);
*/

            /*
            if (listener != null) {
                ModelNotification notification = new ModelNotification();
                notification.setBody(body);
                notification.setTitle(title);
                notification.setOffer_id(actionId);
                listener.onNotification(notification);
            }

             */


        }

    }

    @SuppressLint("SetTextI18n")
    private void handleNotification(String title, String body, String target_id, String type, String id) {


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra(Constants.TRANSACTION_TARGET_ID, target_id);
        intent.putExtra(Constants.TRANSACTION_TYPE, type);
        intent.putExtra(Constants.TRANSACTION_NOTIFICATION_ID, id);
        intent.putExtra(Constants.NOTIFICATION_TITLE, title);
        intent.putExtra(Constants.NOTIFICATION_BODY, body);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_icon_rounded_notif)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(random, builder.build());


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }


}
