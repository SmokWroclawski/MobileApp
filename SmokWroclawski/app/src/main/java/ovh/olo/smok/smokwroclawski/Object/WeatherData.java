package ovh.olo.smok.smokwroclawski.Object;

import java.util.Arrays;
import java.util.List;

public class WeatherData {
    private double temperature;
    private double pressure;
    private double humidity;
    private double pm25;
    private double pm10;
    private double timeStamp;

    public final static List<String> fieldNames =
            Arrays.asList("temperature", "pressure", "humidity", "pm25", "pm10", "timeStamp");



    public WeatherData() {
    }

    public WeatherData(double temperature, double pressure, double humidity, double pm25, double pm10, double timeStamp) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.timeStamp = timeStamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getPm10() {
        return pm10;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }

    public double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Double> getValuesAsList() {
        return Arrays.asList(getTemperature(), getPressure(), getHumidity(), getPm25(), getPm10(), getTimeStamp());
    }

    @Override
    public String toString() {
        return "{" +
                "\"temperature\":\"" + temperature +
                "\", \"pressure\":\"" + pressure +
                "\", \"humidity\":\"" + humidity +
                "\", \"pm25\":\"" + pm25 +
                "\", \"pm10\":\"" + pm10 +
                "\", \"timeStamp\":\"" + timeStamp +
                "\"}";
    }
}
