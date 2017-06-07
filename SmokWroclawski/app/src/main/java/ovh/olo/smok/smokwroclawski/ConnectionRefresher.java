package ovh.olo.smok.smokwroclawski;


import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;

import ovh.olo.smok.smokwroclawski.Activity.MainActivity;
import ovh.olo.smok.smokwroclawski.Github.GithubReader;

/**
 * This class refresh request data to device
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class ConnectionRefresher {
    private Handler handler;
    public static int MINS = 60000;
    private CountDownTimer countDownTimer;

    public ConnectionRefresher() {
        handler = new Handler();
    }

    private void setUpTimer() {
        MainActivity.instance.getTimer().setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(MINS, 1000) {

            public void onTick(long millisUntilFinished) {
                MainActivity.instance.getTimer().setText("Autoreconnect in: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                MainActivity.instance.getTimer().setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            setUpTimer();

            MainActivity.instance.clearAllDatas();
            if(InternetChecker.isOnline())
                new GithubReader().execute();
            else
                MainActivity.instance.checkPermissionsAndStartSearching();

            handler.postDelayed(runnable, MINS); //5min - 300000 ; 1min - 60000
        }
    };

    public void start() {
        handler.post(runnable);
    }

    public void stop() {
        MainActivity.instance.getTimer().setVisibility(View.INVISIBLE);
        countDownTimer.cancel();
        handler.removeCallbacks(runnable);
    }
}
