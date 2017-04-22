package ovh.olo.smok.smokwroclawski;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;

/**
 * This class contains method to send data to Thingspeak
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class ThingSpeakSender {
    private final static String API_KEY = "PRKO5DA2XEW48J8K";
    private final static String BASE_ADDRESS_URL = "https://api.thingspeak.com/update?";
    private final AtomicBoolean isNotError = new AtomicBoolean(true);

    private OkHttpClient okHttpClient;
    private Request request;

    /**
     * Send data from WeatherData object to Thingspeak
     *
     * @param weatherData   input data
     * @return              true if sent successfully, false otherwise
     */
    public boolean send(WeatherData weatherData) {
        okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        StringBuilder fields = new StringBuilder();

        for (int i = 0; i < WeatherData.fieldNames.size() - 1; i++) {
            fields.append("&")
                    .append("field")
                    .append(i + 1)
                    .append("=")
                    .append(weatherData.getValuesAsList().get(i));
        } //WeatherData.fieldNames.get(i)
        fields.append("&created_at=").append(weatherData.getTimeStamp());
        System.out.println(BASE_ADDRESS_URL + "api_key=" + API_KEY + fields);
        request = builder.url( BASE_ADDRESS_URL + "api_key=" + API_KEY + fields).build();

        setCallBack();

        return isNotError.get();
    }

    /**
     * Set callback for response ie. errors.
     */
    private void setCallBack() {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(this.getClass().toString(), "Error - " + e.getMessage());
                isNotError.getAndSet(false);
            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e(this.getClass().toString(), response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(this.getClass().toString(), "Error - " + e.getMessage());
                        isNotError.getAndSet(false);
                    }
                } else {
                    Log.e(this.getClass().toString(), "Not Success - code : " + response.code());
                    isNotError.getAndSet(false);
                }
            }
        });
    }

}
