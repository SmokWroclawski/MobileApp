package ovh.olo.smok.smokwroclawski.ThingSpeak;

import android.app.ProgressDialog;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import ovh.olo.smok.smokwroclawski.Activity.SensorActivity;

/**
 * Created by Michal on 2017-05-07.
 */

public class ThingSpeakReceiver {
    private long channelId = 252240;
    private String readApiKey = "OJZ2HLXTJ2Y75OVN";
    private int numberOfPoints = 20;
    private OkHttpClient client;
    DataForChart data = null;

    private String URL = "https://api.thingspeak.com/channels/"
            + channelId + "/feeds.json?api_key=" ///fields/" + fieldNumber + "
            + readApiKey + "&results=" + numberOfPoints;

    public ThingSpeakReceiver(long channelId, String readApiKey) {
        client = new OkHttpClient();
        this.channelId = channelId;
        this.readApiKey = readApiKey;
    }

    private  void doGetRequest(final List<WebView> webViewArrayList) throws IOException {
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
                        System.out.println(res);
                        try {
                            data = getDataFromJson(res);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        drawAllCharts(webViewArrayList);
                    }
                });
    }

    private DataForChart getDataFromJson(String json) throws JSONException, IOException {

        DataForChart dataForChart = new DataForChart();


        JSONObject jsonObj = new JSONObject(json);
        JSONArray jArray = jsonObj.getJSONArray("feeds");
        for(int i = 0; i < jArray.length(); i++) {
            JSONObject json_data = jArray.getJSONObject(i);
            dataForChart.add(
                    Float.parseFloat(json_data.getString("field1")),
                    Float.parseFloat(json_data.getString("field2")),
                    Float.parseFloat(json_data.getString("field3")),
                    Float.parseFloat(json_data.getString("field4")),
                    Float.parseFloat(json_data.getString("field5")),
                    json_data.getString("created_at")
            );
        }

        return dataForChart;
    }

    private final ProgressDialog pd = ProgressDialog.show(SensorActivity.instance, "", "Loading...",true);

    private void setDataOnChart(final WebView webView,
                                String dataOnChart, String title, String color) {
        final String html = "<html>\n" +
                "  <head>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" />" +
                "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      google.charts.load('current', {'packages':['corechart']});\n" +
                "      google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "      function drawChart() {\n" +
                "        var data = google.visualization.arrayToDataTable(" +
                "" +
                dataOnChart +
                ");\n" +
                "\n" +
                "        var options = {\n" +
                "          title: '" + title + "',\n" +
                "          pointSize: 5,\n" +
                "          colors: ['"+ color +"']\n" +

                "        };\n" +
                "\n" +
                "        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));\n" +
                "\n" +
                "        chart.draw(data, options);\n" +
                "      }\n" +
                "    </script>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"curve_chart\" style=\"width: 900px; height: 400px\"></div>\n" +
                "  </body>\n" +
                "</html>\n";


        webView.post(new Runnable() {
            @Override
            public void run() {
            WebSettings webSettings = webView.getSettings();
            webSettings.setMinimumFontSize(18);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setJavaScriptEnabled(true);

            webSettings.setSupportZoom(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    if(pd!=null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                }
            });
            webView.requestFocusFromTouch();

            webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
            }
        });


    }

    private void drawAllCharts(List<WebView> webViewArrayList) {

        String forChart = "";
        String title = "";
        String color = "";
        for (int field = 1; field < webViewArrayList.size() + 1; field++) { //
            switch (field) {
                case 1:
                    forChart = data.getTemperature();
                    title = "Temperature";
                    color = "red"; //#e7711b
                    break;
                case 2:
                    forChart = data.getHumidity();
                    title = "Humidity";
                    color = "blue";
                    break;
                case 3:
                    forChart = data.getPressure();
                    title = "Pressure";
                    color = "green";
                    break;
                case 4:
                    forChart = data.getPm25();
                    title = "Pm 2.5";
                    color = "grey";
                    break;
                case 5:
                    forChart = data.getPm10();
                    title = "Pm 10";
                    color = "black";
                    break;
                default:
                    forChart = data.toString();
                    title = "Sensor data";
                    color = "red', 'blue', 'green', 'gray', 'black";
                    break;

            }
            setDataOnChart(webViewArrayList.get(field - 1),
                    forChart, title, color);
        }
    }

    public void run(List<WebView> webViewArrayList) {
        try {
            doGetRequest(webViewArrayList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
