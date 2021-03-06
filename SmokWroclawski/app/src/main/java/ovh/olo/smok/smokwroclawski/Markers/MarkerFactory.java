package ovh.olo.smok.smokwroclawski.Markers;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import ovh.olo.smok.smokwroclawski.Activity.MainActivity;

/**
 * Created by Michal on 2017-05-10.
 */

public class MarkerFactory {
    private GoogleMap map;

    public MarkerFactory() {
        map = MainActivity.instance.getmMap();
    }

    public void add(LatLng point, String title, String description, int pm) {
        Marker existingMarker = getMarker(title);
        if(existingMarker != null && existingMarker.getPosition() == point) return;

        Marker marker = map.addMarker(
                new MarkerOptions()
                        .position(point)
                        .title(title)
                        .snippet(description)
                        .icon(BitmapDescriptorFactory.defaultMarker(getColorByValue(pm)))
        );
        MainActivity.instance.getMarkers().add(marker);
    }

    private float getColorByValue(int val) {
        if(val >= 0 && val <= 24)
            return BitmapDescriptorFactory.HUE_GREEN;
        else if (val >= 25 && val <= 49)
            return BitmapDescriptorFactory.HUE_ORANGE;
        else if (val >= 50 && val <= 199)
            return BitmapDescriptorFactory.HUE_RED;
        else if (val >= 200)
            return BitmapDescriptorFactory.HUE_VIOLET;
        else
            return BitmapDescriptorFactory.HUE_AZURE;
    }

    private Marker getMarker(String title) {
        for (Marker marker:
                MainActivity.instance.getMarkers()) {
            if(marker.getTitle().equals(title)) {
                return marker;
            }
        }
        return null;
    }
}
