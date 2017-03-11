package ovh.olo.smok.smokwroclawski.Parser;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import ovh.olo.smok.smokwroclawski.Receiver.DataReceiver;
import ovh.olo.smok.smokwroclawski.MainActivity;
import ovh.olo.smok.smokwroclawski.Object.WeatherData;

/**
 * Created by Michal on 2017-03-10.
 */

public class JsonParser extends AsyncTask {
    private WeatherData weatherData;
    private MainActivity mainActivity;
    private DataReceiver dataReceiver;

    private final static String TEMPERATURE_STRING = "temperature";
    private final static String PRESSURE_STRING = "pressure";
    private final static String HUMIDITY_STRING = "humidity";
    private final static String SMOG_STRING = "smog";

    public JsonParser(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        dataReceiver = new DataReceiver();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            weatherData = getWeatherDataFromJson(dataReceiver.getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private WeatherData getWeatherDataFromJson(String in) throws JSONException {
        JSONObject jsonObject = new JSONObject(in);

        return new WeatherData(
                jsonObject.getInt(TEMPERATURE_STRING),
                jsonObject.getInt(PRESSURE_STRING),
                jsonObject.getInt(HUMIDITY_STRING),
                jsonObject.getInt(SMOG_STRING)
        );
    }

    @Override
    protected void onPostExecute(Object o) {
        myHandler.sendEmptyMessage(0);
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mainActivity.setWeatherData(weatherData);
                    break;
                default:
                    break;
            }
        }
    };
}
