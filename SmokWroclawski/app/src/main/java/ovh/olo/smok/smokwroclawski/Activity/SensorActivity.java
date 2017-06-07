package ovh.olo.smok.smokwroclawski.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

import ovh.olo.smok.smokwroclawski.InternetChecker;
import ovh.olo.smok.smokwroclawski.Markers.MarkerManager;
import ovh.olo.smok.smokwroclawski.R;
import ovh.olo.smok.smokwroclawski.ThingSpeak.ChartData;
import ovh.olo.smok.smokwroclawski.ThingSpeak.ChartDrawer;

public class SensorActivity extends Activity {

    private TextView sensorName;
    private TextView measureCount;
    private SeekBar measureCountSeekBar;
    private WebView chart1;
    private WebView chart2;
    private WebView chart3;
    private WebView chart4;
    private WebView chart5;
    private Button okButton;

    private List<WebView> webViewList;

    private int numberOfPoints = 20;

    private LatLng latLngOfSensor;

    public static SensorActivity instance = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sensor);
        instance = SensorActivity.this;


        chart1 = (WebView) findViewById(R.id.chart1);
        chart2 = (WebView) findViewById(R.id.chart2);
        chart3 = (WebView) findViewById(R.id.chart3);
        chart4 = (WebView) findViewById(R.id.chart4);
        chart5 = (WebView) findViewById(R.id.chart5);
        sensorName = (TextView) findViewById(R.id.sensorName);
        measureCount = (TextView) findViewById(R.id.measureCount);
        measureCountSeekBar = (SeekBar) findViewById(R.id.measureCountSeekBar);
        okButton = (Button) findViewById(R.id.okButton);

//        measureCountSeekBar.setProgress(numberOfPoints);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webViewList = Arrays.asList(chart1, chart2, chart3, chart4, chart5);

        latLngOfSensor = new LatLng(
                getIntent().getDoubleExtra(MarkerManager.DATA_LATITUDE, 0),
                getIntent().getDoubleExtra(MarkerManager.DATA_LONGTITUDE, 0)
                );

        if(!InternetChecker.isOnline()) {
            Toast.makeText(MainActivity.instance, "Required internet access!", Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            setListeners();
            receiveDataAndDrawChart();
        }

    }

    public void setMeasureCount(int pointCount) {
        measureCountSeekBar.setMax(pointCount);
        if(numberOfPoints > pointCount) numberOfPoints = pointCount;
        measureCountSeekBar.setProgress(numberOfPoints);
        measureCount.setText("Measure count: " + numberOfPoints);
    }

    private void setListeners() {
        measureCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int MIN = 2;
            Intent intent = getIntent();
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= MIN) {
                    numberOfPoints = progress;
                    measureCount.setText("Measure count: " + numberOfPoints);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new ChartDrawer().drawAllCharts(
                        (ChartData)intent.getParcelableExtra(MarkerManager.DATA_FROM_SENSOR),
                        webViewList,
                        numberOfPoints,
                        latLngOfSensor
                );
            }
        });
    }

    private void receiveDataAndDrawChart() {
        Intent intent = getIntent();
        ChartData chartData = intent.getParcelableExtra(MarkerManager.DATA_FROM_SENSOR);
        int count = chartData.getMeasureCount(latLngOfSensor);
        setMeasureCount(count);

        new ChartDrawer().drawAllCharts(
                chartData,
                webViewList,
                numberOfPoints,
                latLngOfSensor
        );
        sensorName.setText(intent.getStringExtra(MainActivity.SENSOR_NAME));
//        measureCount.setText("Measure count: " + numberOfPoints);

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
