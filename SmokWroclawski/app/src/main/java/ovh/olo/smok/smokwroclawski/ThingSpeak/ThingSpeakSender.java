package ovh.olo.smok.smokwroclawski.ThingSpeak;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;

/**
 * This class contains method to send data to Thingspeak
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class ThingSpeakSender {

//    https://api.thingspeak.com/update?api_key=PRKO5DA2XEW48J8K&field1=5&long=51.490664&location=true&lat=28.375852

    private String writeApiKey;// = "PRKO5DA2XEW48J8K";
    private final String BASE_ADDRESS_URL = "https://api.thingspeak.com/update?";
    private volatile boolean done = false;
    private final AtomicInteger returnCode = new AtomicInteger(-2);

    private OkHttpClient okHttpClient;
    private Request request;

    public ThingSpeakSender(String writeApiKey) {
        this.writeApiKey = writeApiKey;
    }

    /**
     * Send data from WeatherData object to Thingspeak
     *
     * @param weatherData   input data
     * @return              true if sent successfully, false otherwise
     */
    public int send(WeatherData weatherData) {
        okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        StringBuilder fields = new StringBuilder();

        for (int i = 0; i < WeatherData.fieldNames.size() - 3; i++) {
            fields.append("&")
                    .append("field")
                    .append(i + 1)
                    .append("=")
                    .append(weatherData.getValuesAsList().get(i));
        } //WeatherData.fieldNames.get(i)
        fields.append("&long=").append(weatherData.getLongtitude())
                .append("&lat=").append(weatherData.getLatitude())
                .append("&location=true");
        fields.append("&created_at=").append(weatherData.getTimeStamp());
        System.out.println(BASE_ADDRESS_URL + "api_key=" + writeApiKey + fields);
        request = builder.url( BASE_ADDRESS_URL + "api_key=" + writeApiKey + fields).build();

        setCallBack();

        while (true) {
            if (done) {
                break;
            }
        }

        done = false;
        return returnCode.get();
    }

    /**
     * Set callback for response ie. errors.
     */
    private void setCallBack() {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(this.getClass().toString(), "Error - " + e.getMessage());
                returnCode.getAndSet(-1);
                done = true;
            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBodyCode = response.body().string();
                        Log.e(this.getClass().toString() + "Success", responseBodyCode);

                        returnCode.getAndSet(Integer.parseInt(responseBodyCode));
                        done = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(this.getClass().toString(), "Error - " + e.getMessage());
                        returnCode.getAndSet(-1);
                        done = true;
                    }
                } else {
                    Log.e(this.getClass().toString(), "Not Success - code : " + response.code());
                    returnCode.getAndSet(response.code());
                    done = true;
                }
            }
        });
    }

}
