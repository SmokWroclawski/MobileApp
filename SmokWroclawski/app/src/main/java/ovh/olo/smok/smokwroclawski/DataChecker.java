package ovh.olo.smok.smokwroclawski;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;

public class DataChecker {
    public static boolean isCorrect(WeatherData weatherData) {
        return !((weatherData.getTemperature() < -40 && weatherData.getTemperature() > 60) ||
                (weatherData.getHumidity() < 0) ||
                (weatherData.getPressure() < 0) ||
                (weatherData.getPm10() < 0) ||
                (weatherData.getPm25() < 0));
    }
}
