package ovh.olo.smok.smokwroclawski;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;
import ovh.olo.smok.smokwroclawski.Parser.JsonParser;

public class MainActivity extends AppCompatActivity {
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = (TextView) findViewById(R.id.output);

        JsonParser jsonParser = new JsonParser(MainActivity.this);
        jsonParser.execute();

    }

    public void setWeatherData(WeatherData weatherData) {
        String textToShow = weatherData.toString() + "\n";
        output.setText(textToShow);
    }
}
