package ovh.olo.smok.smokwroclawski.ThingSpeak;

/**
 * Created by Michal on 2017-05-08.
 */

public class ThingSpeakData {
    private String date;
    private float temperature;
    private float pressure;
    private float humidity;
    private float pm25;
    private float pm10;

    public ThingSpeakData(String date, float temperature, float pressure, float humidity, float pm25, float pm10) {
        this.date = date;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.pm25 = pm25;
        this.pm10 = pm10;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPm25() {
        return pm25;
    }

    public void setPm25(float pm25) {
        this.pm25 = pm25;
    }

    public float getPm10() {
        return pm10;
    }

    public void setPm10(float pm10) {
        this.pm10 = pm10;
    }

    @Override
    public String toString() {
        return "['" + date +
                "', " + temperature +
                ", " + pressure +
                ", " + humidity +
                ", " + pm25 +
                ", " + pm10 +
                "]";
    }
}
