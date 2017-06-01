package ovh.olo.smok.smokwroclawski.Github;

import ovh.olo.smok.smokwroclawski.Activity.MainActivity;
import ovh.olo.smok.smokwroclawski.Object.SensorConfigData;

/**
 * Created by Michal on 2017-05-31.
 */

public class GithubDataWorker {

    public static String getWriteApiKey(String macAddress) {
        for (SensorConfigData scd : MainActivity.instance.getSensorConfigDatas()) {
            if(scd.getMacAddress().equals(macAddress)) {
                return scd.getWriteAPIkey();
            }
        }
        return "";
    }

    public static String getReadApiKey(String macAddress) {
        for (SensorConfigData scd : MainActivity.instance.getSensorConfigDatas()) {
            if(scd.getMacAddress().equals(macAddress)) {
                return scd.getReadAPIkey();
            }
        }
        return "";
    }

    public static String getChannelId(String macAddress) {
        for (SensorConfigData scd : MainActivity.instance.getSensorConfigDatas()) {
            if(scd.getMacAddress().equals(macAddress)) {
                return scd.getChannelId();
            }
        }
        return "";
    }
}
