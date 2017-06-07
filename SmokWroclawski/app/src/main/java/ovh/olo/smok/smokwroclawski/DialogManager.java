package ovh.olo.smok.smokwroclawski;

import android.app.Dialog;
import android.os.Handler;

/**
 * Created by Michal on 2017-06-05.
 */

public class DialogManager {
    public static void timerDelayRemoveDialog(long time, final Dialog d){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time);
    }
}
