package ovh.olo.smok.smokwroclawski;


import android.os.Handler;

import ovh.olo.smok.smokwroclawski.Service.ChatService;

/**
 * This class refresh request data to device
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class Refresher {
    private Handler handler;
    private ChatService chatService;

    private final static int MAX_COUNT = 10;
    private int counter = 0;

    public Refresher(ChatService chatService) {
        handler = new Handler();
        this.chatService = chatService;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            chatService.requestData();
            handler.postDelayed(runnable, 100);
            counter++;

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
