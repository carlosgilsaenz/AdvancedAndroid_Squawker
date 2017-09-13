package android.example.com.squawker.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class SquawkFirebaseMessagingService extends FirebaseMessagingService {

    private static final int SQUAWK_NOTIFICATION_ID = 1138;

    public SquawkFirebaseMessagingService() {
    }

    // TODO (1) Make a new Service in the fcm package that extends from FirebaseMessagingService.
    // TODO (2) As part of the new Service - Override onMessageReceived. This method will
    // be triggered whenever a squawk is received. You can get the data from the squawk
    // message using getData(). When you send a test message, this data will include the
    // following key/value pairs:
    // test: true
    // author: Ex. "TestAccount"
    // authorKey: Ex. "key_test"
    // message: Ex. "Hello world"
    // date: Ex. 1484358455343
    // TODO (3) As part of the new Service - If there is message data, get the data using
    // the keys and do two things with it :
    // 1. Display a notification with the first 30 character of the message
    // 2. Use the content provider to insert a new message into the local database
    // Hint: You shouldn't be doing content provider operations on the main thread.
    // If you don't know how to make notifications or interact with a content provider
    // look at the notes in the classroom for help.

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //  Get data from remoteMessage
        Map<String, String> data = remoteMessage.getData();

        //  Save Context
        Context context = getApplicationContext();

        //  Create Values to insert
        ContentValues values = new ContentValues();

        // Grab message for later
        String message = data.get(SquawkContract.COLUMN_MESSAGE);

        values.put(SquawkContract.COLUMN_ID, data.get(SquawkContract.COLUMN_ID));
        values.put(SquawkContract.COLUMN_AUTHOR, data.get(SquawkContract.COLUMN_AUTHOR));
        values.put(SquawkContract.COLUMN_AUTHOR_KEY,data.get(SquawkContract.COLUMN_AUTHOR_KEY));
        values.put(SquawkContract.COLUMN_MESSAGE,message);
        values.put(SquawkContract.COLUMN_DATE,data.get(SquawkContract.COLUMN_DATE));

        // Insert Data
        Uri uri = getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, values);

        // Grab first 30 characters of string
        if(message.length() > 30){
            message = message.substring(0,30);
        }

        // Checks if insert was successful
        if(ContentUris.parseId(uri) != -1){
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(data.get(SquawkContract.COLUMN_AUTHOR))
                    .setSmallIcon(R.drawable.ic_duck)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(contentIntent(context))
                    .setAutoCancel(true);

            //  Notification Manager set
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            //  SQUAWK_NOTIFICATION_ID Allows you to update or cancel the notification later on */
            notificationManager.notify(SQUAWK_NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                SQUAWK_NOTIFICATION_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}


