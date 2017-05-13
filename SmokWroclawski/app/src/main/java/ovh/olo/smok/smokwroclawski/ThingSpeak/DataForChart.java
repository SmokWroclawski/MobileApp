package ovh.olo.smok.smokwroclawski.ThingSpeak;

import java.util.ArrayList;
import java.util.List;

import ovh.olo.smok.smokwroclawski.ThingSpeak.ThingSpeakData;

/**
 * Created by Michal on 2017-05-08.
 */

public class DataForChart {
    private List<ThingSpeakData> thingSpeakDataList;

    public DataForChart() {
        thingSpeakDataList = new ArrayList<>();
    }

    public void add(Float temperature, Float pressure, Float humidity, Float pm25, Float pm10, String date) {
        thingSpeakDataList.add(new ThingSpeakData(date, temperature, pressure, humidity, pm25, pm10));
    }

    public String getTemperature() {
        String temperature = "[['ThingSpeakData', 'Temperature'],";
        for (ThingSpeakData data: thingSpeakDataList) {
            temperature += "['" + data.getDate() +
                    "', " + data.getTemperature() + "],";
        }
        temperature += "]";
        return temperature;
    }

    public String getPressure() {
        String pressure = "[['ThingSpeakData', 'Pressure'],";
        for (ThingSpeakData data: thingSpeakDataList) {
            pressure += "['" + data.getDate() +
                    "', " + data.getPressure() + "],";
        }
        pressure += "]";
        return pressure;
    }

    public String getHumidity() {
        String humidity = "[['ThingSpeakData', 'Humidity'],";
        for (ThingSpeakData data: thingSpeakDataList) {
            humidity += "['" + data.getDate() +
                    "', " + data.getHumidity() + "],";
        }
        humidity += "]";
        return humidity;
    }

    public String getPm25() {
        String pm25 = "[['ThingSpeakData', 'PM2.5'],";
        for (ThingSpeakData data: thingSpeakDataList) {
            pm25 += "['" + data.getDate() +
                    "', " + data.getPm25() + "],";
        }
        pm25 += "]";
        return pm25;
    }

    public String getPm10() {
        String pm10 = "[['ThingSpeakData', 'PM10'],";
        for (ThingSpeakData data: thingSpeakDataList) {
            pm10 += "['" + data.getDate() +
                    "', " + data.getPm10() + "],";
        }
        pm10 += "]";
        return pm10;
    }

    @Override
    public String toString() {
        String toReturn = "[['ThingSpeakData', 'Temperature', 'Pressure', 'Humidity', 'PM2.5', 'PM10'],";

        for (ThingSpeakData data: thingSpeakDataList) {
            toReturn += data.toString() + ",";
        }

        toReturn += "]";
        return toReturn;
    }
}
