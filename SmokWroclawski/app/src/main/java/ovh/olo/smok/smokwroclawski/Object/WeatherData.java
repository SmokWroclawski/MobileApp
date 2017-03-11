package ovh.olo.smok.smokwroclawski.Object;

/**
 * Created by Michal on 2017-03-10.
 */

public class WeatherData {
    private int temperature;
    private int pressure;
    private int humidity;
    private int smog;

    public WeatherData() {
    }

    public WeatherData(int temperature, int pressure, int humidity, int smog) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.smog = smog;
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

    public int getSmog() {
        return smog;
    }

    public void setSmog(int smog) {
        this.smog = smog;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", smog=" + smog +
                '}';
    }
}
