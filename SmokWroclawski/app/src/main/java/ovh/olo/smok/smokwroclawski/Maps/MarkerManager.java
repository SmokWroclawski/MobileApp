package ovh.olo.smok.smokwroclawski.Maps;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import ovh.olo.smok.smokwroclawski.Activity.DeviceActivity;
import ovh.olo.smok.smokwroclawski.Activity.MainActivity;
import ovh.olo.smok.smokwroclawski.Activity.SensorActivity;

/**
 * Created by Michal on 2017-06-01.
 */

public class MarkerManager {

    public final static String DATA_FROM_SENSOR = "DATA_FROM_SENSOR";
    public final static String DATA_LATITUDE = "DATA_LATITUDE";
    public final static String DATA_LONGTITUDE = "DATA_LONGTITUDE";

    public static void setUpMarkersListeners() {

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
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : MainActivity.instance.getMarkers()) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        MainActivity.instance.getmMap().animateCamera(cu);
    }
}
