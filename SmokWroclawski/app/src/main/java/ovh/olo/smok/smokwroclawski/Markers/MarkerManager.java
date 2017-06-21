package ovh.olo.smok.smokwroclawski.Markers;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import ovh.olo.smok.smokwroclawski.Activity.MainActivity;
import ovh.olo.smok.smokwroclawski.Activity.SensorActivity;
import ovh.olo.smok.smokwroclawski.Parser.ParserISO8601;
import ovh.olo.smok.smokwroclawski.ThingSpeak.ChartData;
import ovh.olo.smok.smokwroclawski.ThingSpeak.ThingSpeakData;

/**
 * Created by Michal on 2017-06-01.
 */

public class MarkerManager {

    public final static String DATA_FROM_SENSOR = "DATA_FROM_SENSOR";
    public final static String DATA_LATITUDE = "DATA_LATITUDE";
    public final static String DATA_LONGTITUDE = "DATA_LONGTITUDE";

    public static void setUpAllMarkers(final ChartData data) {

        MainActivity.instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                for (ThingSpeakData tsData: data.getThingSpeakDataList()) {
                    MarkerFactory markerFactory = new MarkerFactory();

                    tsData.setLatitude(Math.round(
                            tsData.getLatitude() * 1000f
                    )/1000f);
                    tsData.setLongtitude(Math.round(
                            tsData.getLongtitude() * 1000f
                    )/1000f);

                    LatLng latLng = new LatLng(
                            tsData.getLatitude(),
                            tsData.getLongtitude()
                    );
                    markerFactory.add(latLng,
                            "(" + Math.round(latLng.latitude * 1000d ) / 1000d + ", "
                            + Math.round(latLng.longitude * 1000d ) / 1000d + ")",

                            "Last measure ( "
                            + ParserISO8601.toDate(data.getLastDate(latLng)) +
                            " ): \n" +
                            "Temperature: " + data.getLastTemeprature(latLng) + "\n" +
                            "Humidity: " + data.getLastHumidity(latLng) + "\n" +
                            "Pressure: " + data.getLastPressure(latLng) + "\n" +
                            "PM 2.5: " + data.getLastPM25(latLng) + "\n" +
                            "PM 10: " + data.getLastPM10(latLng),

                            (int) data.getAvgPms(latLng)
                    );
                }
                setUpMarkersListeners();
                animate();
            }

        });
    }

    public static void setUpMarkersListeners() {
        if(MainActivity.instance.getMarkers().isEmpty()) return;

        MainActivity.instance.getmMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MainActivity.instance, "Tap info window to show charts!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        MainActivity.instance.getmMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MainActivity.instance, SensorActivity.class);
                intent.putExtra(DATA_LATITUDE, marker.getPosition().latitude);
                intent.putExtra(DATA_LONGTITUDE, marker.getPosition().longitude);

                intent.putExtra(DATA_FROM_SENSOR, MainActivity.instance.getData());
                intent.putExtra(MainActivity.SENSOR_NAME, marker.getTitle());
                MainActivity.instance.startActivity(intent);
                //                                        return false;

            }
        });

    }

    public static void animate() {
        if(MainActivity.instance.getMarkers().isEmpty()) return;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : MainActivity.instance.getMarkers()) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        MainActivity.instance.getmMap().animateCamera(cu);
    }

    public static void clearMarkers() {
        MainActivity.instance.setMarkers(new ArrayList<Marker>());
        MainActivity.instance.getmMap().clear();
    }
}
