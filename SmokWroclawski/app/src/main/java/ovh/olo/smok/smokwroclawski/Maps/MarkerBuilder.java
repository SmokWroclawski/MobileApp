package ovh.olo.smok.smokwroclawski.Maps;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
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

public class MarkerBuilder {
    private GoogleMap map;

    public MarkerBuilder() {
        map = MainActivity.instance.getmMap();
    }

    public void add(LatLng point, String title) {
        Marker existingMarker = getMarker(title);
        if(existingMarker != null) existingMarker.remove();
        Marker marker = map.addMarker(
                new MarkerOptions()
                        .position(point)
                        .title(title)
        );
        MainActivity.instance.getMarkers().add(marker);
    }

    public void addLine(LatLng startPoint,
                        LatLng goalPoint) {
        addLine(startPoint, goalPoint, Color.RED);
    }

    public void addLine(LatLng startPoint, LatLng goalPoint,
                        int color) {
        Polyline polyline = map.addPolyline(
                new PolylineOptions()
                        .add(startPoint, goalPoint)
                        .width(10)
                        .color(color)
                        .clickable(true)
        );

        MainActivity.instance.getPolylines().add(polyline);
    }

    public void addCurveLine(List<LatLng> pointsList) {
        addCurveLine(pointsList, Color.RED);
    }

    public void addCurveLine(List<LatLng> pointsList,
                             int color) {
        PolylineOptions options = new PolylineOptions();
        for (LatLng point: pointsList) {
            options.add(point);
        }
        options.width(10).color(color).clickable(true);

        Polyline polyline = map.addPolyline(options);

        MainActivity.instance.getPolylines().add(polyline);
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
