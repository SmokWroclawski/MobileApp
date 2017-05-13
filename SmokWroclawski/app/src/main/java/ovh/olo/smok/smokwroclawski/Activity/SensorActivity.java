package ovh.olo.smok.smokwroclawski.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import ovh.olo.smok.smokwroclawski.InternetChecker;
import ovh.olo.smok.smokwroclawski.R;
import ovh.olo.smok.smokwroclawski.ThingSpeak.ThingSpeakReceiver;

/**
 * Created by Michal on 2017-05-08.
 */

public class SensorActivity extends Activity {

    private TextView sensorName;
    private WebView chart1;
    private WebView chart2;
    private WebView chart3;
    private WebView chart4;
    private WebView chart5;
    private Button okButton;


    public static SensorActivity instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sensor);
        instance = SensorActivity.this;

        Intent intent = getIntent();

        chart1 = (WebView) findViewById(R.id.chart1);
        chart2 = (WebView) findViewById(R.id.chart2);
        chart3 = (WebView) findViewById(R.id.chart3);
        chart4 = (WebView) findViewById(R.id.chart4);
        chart5 = (WebView) findViewById(R.id.chart5);
        sensorName = (TextView) findViewById(R.id.sensorName);
        okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        InternetChecker internetChecker = new InternetChecker();
        if(!internetChecker.isOnline()) {
            Toast.makeText(MainActivity.instance, "Required internet access!", Toast.LENGTH_LONG).show();
            this.finish();
        } else {

            ThingSpeakReceiver receiver = new ThingSpeakReceiver(
                    Long.parseLong(intent.getStringExtra(MainActivity.CHANNEL_ID_NAME)),
                    intent.getStringExtra(MainActivity.API_KEY_NAME));
            receiver.run(Arrays.asList(chart1, chart2, chart3, chart4, chart5));

            sensorName.setText(intent.getStringExtra(MainActivity.SENSOR_NAME));
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
//        super.onBackPressed();
    }
}
