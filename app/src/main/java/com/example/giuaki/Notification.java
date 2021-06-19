package com.example.giuaki;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.giuaki.Api.VanPhongPham;
import com.example.giuaki.Mainv2.ThongbaoLayout;

import java.util.List;

public class Notification {
    Context context = null;
    String CHANNEL_ID = "MYCHANNELID";
    String CHANNEL_NAME= "MYCHANNELNAME";
    int UNIQUE_INT = 0;
    int logo = R.drawable._xdlogo;
    int notiiconID = R.drawable.notiicon_active;

    public Notification( Context context ,String channelID, String channelName) {
        this.context = context;
        this.CHANNEL_ID = channelID;
        this.CHANNEL_NAME = channelName;
        createChannel();
    }
    public Notification( Context context , int unique_int) {
        this.context = context;
        this.UNIQUE_INT = unique_int;
        createChannel();
    }

    public void createChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID+"", CHANNEL_NAME+"", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(List<VanPhongPham> list, boolean all) {
            String textTitle = "Các VPP được giao hôm nay :";
            if(all == false) textTitle = list.get(0).getMaNcc()+" đã giao các VPP :";
            String textContent = "Giữ hoặc nhấp vào để xem!! ";
            if(all == false) textContent = "Giữ hoặc nhấp vào để xem!! ";
            String vppList = "";
            if( list != null ) {
                for( VanPhongPham vpp : list ){
                    vppList += vpp.toString()+"\n";
                }
            }

            Intent intent = new Intent(context, ThongbaoLayout.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            Bitmap notiicon = BitmapFactory.decodeResource(context.getResources(), notiiconID);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID+"")
                    .setLargeIcon(notiicon)
                    .setSmallIcon(logo)
                    .setContentTitle(textTitle)
                    .setContentText(textContent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(vppList))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(UNIQUE_INT, mBuilder.build());
    }
}
