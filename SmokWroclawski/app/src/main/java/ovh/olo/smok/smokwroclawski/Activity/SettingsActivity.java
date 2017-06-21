package ovh.olo.smok.smokwroclawski.Activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import ovh.olo.smok.smokwroclawski.Refresher.ConnectionRefresher;
import ovh.olo.smok.smokwroclawski.R;

public class SettingsActivity extends Activity {
    private SeekBar seekBar;
    private TextView reconnectMinsCounterTv;
    private Button okButtonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        seekBar = (SeekBar) findViewById(R.id.seekBar2);
        reconnectMinsCounterTv = (TextView) findViewById(R.id.reconnectMinsCount);
        okButtonSettings = (Button) findViewById(R.id.okButtonSettings);


        setProgressByActualMinsCount();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        reconnectMinsCounterTv.setText("1 min");
                        MainActivity.instance.getReconnectCheckBox().setText("Auto reconnect every 1 min");
                        break;
                    case 1:
                        reconnectMinsCounterTv.setText("2 mins");
                        MainActivity.instance.getReconnectCheckBox().setText("Auto reconnect every 2 mins");
                        break;
                    case 2:
                        reconnectMinsCounterTv.setText("5 mins");
                        MainActivity.instance.getReconnectCheckBox().setText("Auto reconnect every 5 mins");
                        break;
                    case 3:
                        reconnectMinsCounterTv.setText("10 mins");
                        MainActivity.instance.getReconnectCheckBox().setText("Auto reconnect every 10 mins");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                switch (seekBar.getProgress()) {
                    case 0:
                        ConnectionRefresher.MINS = 60000;
                        break;
                    case 1:
                        ConnectionRefresher.MINS = 2*60000;
                        break;
                    case 2:
                        ConnectionRefresher.MINS = 5*60000;
                        break;
                    case 3:
                        ConnectionRefresher.MINS = 10*60000;
                        break;
                }
            }
        });

        okButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void setProgressByActualMinsCount() {
        int actualMinCount = ConnectionRefresher.MINS/60000;
        switch (actualMinCount) {
            default:
                seekBar.setProgress(0);
                reconnectMinsCounterTv.setText("1 min");
                break;
            case 2:
                seekBar.setProgress(1);
                reconnectMinsCounterTv.setText("2 mins");
                break;
            case 5:
                seekBar.setProgress(2);
                reconnectMinsCounterTv.setText("5 mins");
                break;
            case 10:
                seekBar.setProgress(3);
                reconnectMinsCounterTv.setText("10 mins");
                break;
        }
    }


    @Override
    public void onBackPressed() {
        this.finish();
    }
}
