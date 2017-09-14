package android.example.com.squawker;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import timber.log.Timber;

/**
 * Created by Kuma-Licious on 9/11/2017.
 */

public class fcm extends FirebaseInstanceIdService {

    private static String LOG_TAG = fcm.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(LOG_TAG, refreshedToken);
        Timber.d(refreshedToken);
    }
}
