package ovh.olo.smok.smokwroclawski;


import android.os.Handler;

import ovh.olo.smok.smokwroclawski.Activity.MainActivity;
import ovh.olo.smok.smokwroclawski.Service.ChatService;

/**
 * This class refresh request data to device
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class ConnectionRefresher {
    private Handler handler;
    private ChatService chatService;

    public ConnectionRefresher() {
        handler = new Handler();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            MainActivity.instance.findDevices();
            handler.postDelayed(runnable, 300000); //5min
        }
    };

    public void start() {
        handler.post(runnable);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }
}
