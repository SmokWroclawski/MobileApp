package ovh.olo.smok.smokwroclawski.ThingSpeak;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ovh.olo.smok.smokwroclawski.Activity.DeviceActivity;
import ovh.olo.smok.smokwroclawski.Activity.MainActivity;
import ovh.olo.smok.smokwroclawski.Validator.InternetValidator;
import ovh.olo.smok.smokwroclawski.Markers.MarkerManager;

public class ThingSpeakReceiver {
    private long channelId;
//    private String readApiKey = "OJZ2HLXTJ2Y75OVN";
    private int numberOfPoints = 100;
    private OkHttpClient client;

    private ChartData data;

    private String URL;
    public ThingSpeakReceiver(long channelId, String readApiKey) {
        client = new OkHttpClient();
        this.channelId = channelId;
        URL = "https://api.thingspeak.com/channels/"
                + channelId + "/feeds.json?api_key=" ///fields/" + fieldNumber + "
                + readApiKey + "&results=" + numberOfPoints + "&location=true";
    }

    public void run() {
        if(!InternetValidator.isOnline()) return;
        try {
            doGetRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void doGetRequest() throws IOException {
        Request request = new Request.Builder()
                .url(URL)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String res = response.body().string();
                        Log.i(ThingSpeakReceiver.class.getSimpleName(), "Data received from thingspeak");
                        try {
                            data = getDataFromJson(res);
                            MainActivity.instance.addData(data);

                            MarkerManager.setUpAllMarkers(data);
                            DeviceActivity.instance.getProgressDialog().dismiss();
//                            MarkerManager.setUpAllMarkers(data);

                            DeviceActivity.instance.done = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private ChartData getDataFromJson(String json) throws JSONException, IOException {

        ChartData chartData = new ChartData();


        JSONObject jsonObj = new JSONObject(json);
        JSONArray jArray = jsonObj.getJSONArray("feeds");
        for(int i = 0; i < jArray.length(); i++) {
            JSONObject json_data = jArray.getJSONObject(i);
            chartData.add(
                    Float.parseFloat(json_data.getString("field1")),
                    Float.parseFloat(json_data.getString("field2")),
                    Float.parseFloat(json_data.getString("field3")),
                    Float.parseFloat(json_data.getString("field4")),
                    Float.parseFloat(json_data.getString("field5")),
                    json_data.getString("created_at"),
                    Float.parseFloat(json_data.getString("latitude")),
                    Float.parseFloat(json_data.getString("longitude"))

            );
        }

        return chartData;
    }

    public ChartData getData() {
        if(MainActivity.instance.getData() == null) return new ChartData();
        return MainActivity.instance.getData();
    }
}
