package ovh.olo.smok.smokwroclawski;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ovh.olo.smok.smokwroclawski.Activity.MainActivity;

/**
 * This class contain method to check Internet state.
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class InternetChecker {
    /**
     * This method checks if Internet connection is available.
     *
     * @return  true if Internet connection is available, false otherwise
     */
    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity
                .instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
