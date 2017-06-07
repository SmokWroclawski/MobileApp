package ovh.olo.smok.smokwroclawski.ThingSpeak;

import android.app.ProgressDialog;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ovh.olo.smok.smokwroclawski.Activity.SensorActivity;

public class ChartDrawer {
    public ChartDrawer() {
    }

    public void drawAllCharts(ChartData data, List<WebView> webViewArrayList, int count, LatLng latLng) {
        String forChart = "";
        String title = "";
        String color = "";
        for (int field = 1; field < webViewArrayList.size() + 1; field++) {
            switch (field) {
                case 1:
                    forChart = data.getTemperature(count, latLng);
                    title = "Temperature";
                    color = "red"; //#e7711b
                    break;
                case 2:
                    forChart = data.getHumidity(count, latLng);
                    title = "Humidity";
                    color = "blue";
                    break;
                case 3:
                    forChart = data.getPressure(count, latLng);
                    title = "Pressure";
                    color = "green";
                    break;
                case 4:
                    forChart = data.getPm25(count, latLng);
                    title = "Pm 2.5";
                    color = "grey";
                    break;
                case 5:
                    forChart = data.getPm10(count, latLng);
                    title = "Pm 10";
                    color = "black";
                    break;
                default:
                    forChart = data.toString(count, latLng);
                    title = "Sensor data";
                    color = "red', 'blue', 'green', 'gray', 'black";
                    break;

            }
            setDataOnChart(webViewArrayList.get(field - 1),
                    forChart, title, color);
        }
    }


    private void setDataOnChart(final WebView webView,
                                String dataOnChart, String title, String color) {

        final String html =
                "<html><head>\n" +
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                        "\n" +
                        "    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                        "  <script type=\"text/javascript\">\n" +
                        "    google.charts.load('current', {packages:['corechart']});\n" +
                        "    google.charts.setOnLoadCallback(drawExample3);\n" +
                        "\n" +
                        "function drawExample3() {\n" +
                        "  var options = {\n" +
                        "    hAxis: {direction: -1}," +
                        "    title: '" + title + "',\n" +
                        "    pointSize: 5,\n" +
                        "    height: 240,\n" +
                        "    colors: ['"+ color +"'],\n" +
                        "    legend: { position: 'bottom' }," +
                        "    animation: {\n" +
                        "      duration: 400,\n" +
                        "      easing: 'in'\n" +
                        "    }\n" +
                        "  };\n" +
                        "\n" +
                        "  var chart = new google.visualization.LineChart(\n" +
                        "      document.getElementById('example3-visualization'));\n" +
                        "  \n" +
                        "  var data = google.visualization.arrayToDataTable(" +
                        dataOnChart +
                        ");\n" +
                        "  function drawChart() {\n" +
                        "    chart.draw(data, options);\n" +
                        "  }\n" +
                        "" +

                        "\n" +
                        "drawChart();" +
                        "}" +
                        "</script>\n" +
                        "<div id=\"example3-visualization\" style=\"height:auto\">Unable to load chart, please reload!</div>\n" +
                        "\n" +
                        "   \n" +
                        "</body></html>";
        final AtomicInteger webViewDoneCounter = new AtomicInteger(0);
        webView.post(new Runnable() {
            @Override
            public void run() {
                final ProgressDialog pd = ProgressDialog.show(SensorActivity.instance, "", "Drawing charts...",true);

                WebSettings webSettings = webView.getSettings();


                webView.setWebChromeClient(new WebChromeClient() {
                                        @Override
                    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                        Log.i("JSMessage", consoleMessage.message());
                        return super.onConsoleMessage(consoleMessage);
                    }

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        if(newProgress == 100) {
                            webViewDoneCounter.incrementAndGet();
                        }
                        if(webViewDoneCounter.get() == 2) { //1 raz kiedy wczyta sie strona, drugi raz kiedy wczyta sie js

                            if(pd !=null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                        }
                    }
                });

                webSettings.setMinimumFontSize(18);
                webSettings.setLoadWithOverviewMode(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setBuiltInZoomControls(true);
                webSettings.setDisplayZoomControls(false);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setDomStorageEnabled(true);
                webSettings.setSupportZoom(true);


                webView.requestFocusFromTouch();

                webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
            }
        });
    }

}
