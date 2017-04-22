package ovh.olo.smok.smokwroclawski;


import android.os.Handler;

import ovh.olo.smok.smokwroclawski.Activity.ChatActivity;

/**
 * This class refresh request data to device
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class Refresher {
    private Handler handler;
    private ChatActivity chatActivity;

    private final static int MAX_COUNT = 10;
    private int counter = 0;

    public Refresher(ChatActivity chatActivity) {
        handler = new Handler();
        this.chatActivity = chatActivity;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            chatActivity.requestData();
            handler.postDelayed(runnable, 100);

            if(counter > MAX_COUNT) {
                stop();
            }
        }
    };

    public void start() {
        handler.post(runnable);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }
}
