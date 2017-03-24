package ovh.olo.smok.smokwroclawski.Object;

/**
 * Created by Michal on 2017-03-10.
 */

public class WeatherData {
    private int temperature;
    private int pressure;
    private int humidity;
    private int pm25;
    private int pm10;

    public WeatherData() {
    }

    public WeatherData(int temperature, int pressure, int humidity, int pm25, int pm10) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.pm25 = pm25;
        this.pm10 = pm10;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", pm25=" + pm25 +
                ", pm10=" + pm10 +
                '}';
    }
}
