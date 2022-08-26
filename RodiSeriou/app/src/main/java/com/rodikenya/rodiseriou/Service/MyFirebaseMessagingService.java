package com.rodikenya.rodiseriou.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.DashBoard;
import com.rodikenya.rodiseriou.MainActivity;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    IpService mService;
    String Title;
    String Messaage;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Title=remoteMessage.getData().get("title");
        Messaage=remoteMessage.getData().get("message");
        System.out.println("FireBaseMessage"+remoteMessage.getData().get("title"));
        if(remoteMessage.getData().isEmpty()) {
            showNotification(Title,Messaage);
            //showNotification(remoteMessage.getData());
        }else {
            //showNotification(remoteMessage.getData());
            showNotification(Title,Messaage);
        }
    }


    private void showNotification(Map<String, String> data) {
        String title=data.get("title").toString();
        String body=data.get("body").toString();
        NotificationManager notificationmanager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID="com.daimajia.fcmmessaging.test";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("this is mathew testing");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationmanager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");
        notificationmanager.notify(new Random().nextInt(),notificationBuilder.build());
    }

    private void showNotification(String title, String body)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationmanager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID="com.daimajia.fcmmessaging.test";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("this is mathew testing");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationmanager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
                .setContentIntent(pendingIntent);
        notificationmanager.notify(new Random().nextInt(),notificationBuilder.build());

    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        updateFireBaseToken(s);
        System.out.println("GetToken "+s);
    }

    private void updateFireBaseToken(String token) {
        mService= Common.getIpService();
        if(Common.CurrentUser!=null) {
            mService.UpdateFireBaseToken(Common.CurrentUser.getPhone(), token, "0")
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            System.out.println("Token Back " + response.body().toString());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            System.out.println("Failed " + t.getMessage());
                        }
                    });
        }else
        {
            //Toast.makeText(getApplicationContext(),"Firebase Failed",Toast.LENGTH_LONG).show();
        }
    }
}
