package d.pintoptech.liftnetwork.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import d.pintoptech.liftnetwork.ChatActivity;
import d.pintoptech.liftnetwork.MainActivity;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.beneficiary.ProfileActivity;
import d.pintoptech.liftnetwork.beneficiary.ViewSponsor;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.sponsor.MainPageSponsor;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    NotificationCompat.Builder notificationBuilder;

    Bitmap image;
    Intent intent;
    UserInfo userInfo;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        String tag = remoteMessage.getData().get("tag");
        String msg = remoteMessage.getData().get("message");
        String title = remoteMessage.getData().get("title");
        String img = remoteMessage.getData().get("image");

        image = getBitmapFromURL(img);

        sendNotification(tag, msg, title, image);
    }
    // [END receive_message]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String tag, String messageBody, String title, Bitmap img) {
        try {
            JSONObject fullTag = new JSONObject(tag);
            String type = fullTag.getString("type");
            if(type.equalsIgnoreCase("chat")){
                JSONObject data = fullTag.getJSONObject("data");
                String id = data.getString("id");
                String name = data.getString("name");
                String email = data.getString("email");
                String image_1 = data.getString("image_1");
                String about = data.getString("about");
                String image_2 = data.getString("image_2");
                String image_3 = data.getString("image_3");
                String location = data.getString("location");
                String age = data.getString("age");
                String phone = data.getString("phone");
                String help_with = data.getString("help_with");
                String category = data.getString("category");
                String occupation = data.getString("occupation");
                String gender = data.getString("gender");

                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("image_1", image_1);
                intent.putExtra("name", name);
                intent.putExtra("about", about);
                intent.putExtra("email", email);
                intent.putExtra("image_2", image_2);
                intent.putExtra("image_3", image_3);
                intent.putExtra("location", location);
                intent.putExtra("age", age);
                intent.putExtra("phone", phone);
                intent.putExtra("help_with", help_with);
                intent.putExtra("gender", gender);
                intent.putExtra("category",category);
                intent.putExtra("occupation", occupation);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                String channelID = getString(R.string.channel_id);

                notificationBuilder = new NotificationCompat.Builder(this, channelID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelID,
                            "LiftChannel",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(0 , notificationBuilder.build());
            }else if(type.equalsIgnoreCase("sponsor requested")){
                JSONObject data = fullTag.getJSONObject("data");
                String id = data.getString("id");
                String name = data.getString("name");
                String email = data.getString("email");
                String image_1 = data.getString("image_1");
                String about = data.getString("about");
                String image_2 = data.getString("image_2");
                String image_3 = data.getString("image_3");
                String location = data.getString("location");
                String age = data.getString("age");
                String phone = data.getString("phone");
                String help_with = data.getString("help_with");
                String category = data.getString("category");
                String occupation = data.getString("occupation");
                String gender = data.getString("gender");
                //new
                String from = data.getString("from");
                String helping_with = data.getString("helping_with");
                String status = data.getString("status");

                Intent intent = new Intent(this, ViewSponsor.class);
                intent.putExtra("id", id);
                intent.putExtra("image_1", image_1);
                intent.putExtra("name", name);
                intent.putExtra("about", about);
                intent.putExtra("email", email);
                intent.putExtra("image_2", image_2);
                intent.putExtra("image_3", image_3);
                intent.putExtra("location", location);
                intent.putExtra("age", age);
                intent.putExtra("phone", phone);
                intent.putExtra("help_with", help_with);
                intent.putExtra("gender", gender);
                intent.putExtra("category",category);
                intent.putExtra("occupation", occupation);

                intent.putExtra("from", from);
                intent.putExtra("helping_with", helping_with);
                intent.putExtra("status", status);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                String channelID = getString(R.string.channel_id);

                notificationBuilder = new NotificationCompat.Builder(this, channelID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelID,
                            "LiftChannel",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(0 , notificationBuilder.build());
            }else if(type.equalsIgnoreCase("beneficiary accepted")){
                JSONObject data = fullTag.getJSONObject("data");
                String id = data.getString("id");
                String name = data.getString("name");
                String email = data.getString("email");
                String image_1 = data.getString("image_1");
                String about = data.getString("about");
                String image_2 = data.getString("image_2");
                String image_3 = data.getString("image_3");
                String location = data.getString("location");
                String age = data.getString("age");
                String phone = data.getString("phone");
                String help_with = data.getString("help_with");
                String category = data.getString("category");
                String occupation = data.getString("occupation");
                String gender = data.getString("gender");
                //new
                String from = data.getString("from");
                String helping_with = data.getString("helping_with");
                String status = data.getString("status");

                Intent intent = new Intent(this, MainPageSponsor.class);
                intent.putExtra("id", id);
                intent.putExtra("image_1", image_1);
                intent.putExtra("name", name);
                intent.putExtra("about", about);
                intent.putExtra("email", email);
                intent.putExtra("image_2", image_2);
                intent.putExtra("image_3", image_3);
                intent.putExtra("location", location);
                intent.putExtra("age", age);
                intent.putExtra("phone", phone);
                intent.putExtra("help_with", help_with);
                intent.putExtra("gender", gender);
                intent.putExtra("category",category);
                intent.putExtra("occupation", occupation);

                intent.putExtra("from", from);
                intent.putExtra("helping_with", helping_with);
                intent.putExtra("status", status);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                String channelID = getString(R.string.channel_id);

                notificationBuilder = new NotificationCompat.Builder(this, channelID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelID,
                            "LiftChannel",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(0 , notificationBuilder.build());
            }else if(type.equalsIgnoreCase("register")){

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                String channelID = getString(R.string.channel_id);

                notificationBuilder = new NotificationCompat.Builder(this, channelID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelID,
                            "LiftChannel",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(0 , notificationBuilder.build());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}