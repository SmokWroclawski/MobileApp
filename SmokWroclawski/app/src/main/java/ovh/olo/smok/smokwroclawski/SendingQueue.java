package ovh.olo.smok.smokwroclawski;

import android.os.Handler;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;
import ovh.olo.smok.smokwroclawski.Worker.FileWorker;

/*
todo: usuwanie linii z pliku
 */

/**
 * This class contains method to send, save or delete WeatherData
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class SendingQueue {
    private final static String FILENAME = "SW_WeatherData.log";
    private final static int SEND_DELAY = 15000;

    private ThingSpeakSender thingSpeakSender;
    private FileWorker fileWorker;
    private InternetChecker internetChecker;

    public SendingQueue() {
        thingSpeakSender = new ThingSpeakSender();
        fileWorker = new FileWorker();
        internetChecker = new InternetChecker();
    }


    /**
     * Writes input WeatherData to file and sends it to Thingspeak.
     *
     * @param weatherData   Input data
     */
    public void send(WeatherData weatherData) {
        try {

            weatherData.setTimeStamp(System.currentTimeMillis()/1000);

            /*RANDOM BLOK dla testow (te same dane bo brak czujnikow)*/
            Random rand = new Random();
            int a = rand.nextInt(5);
            weatherData.setHumidity(weatherData.getHumidity() + a);
            a = rand.nextInt(10);
            weatherData.setPm10(weatherData.getPm10() + a);
            a = rand.nextInt(7);
            weatherData.setPm25(weatherData.getPm25() + a);
            a = rand.nextInt(50);
            weatherData.setPressure(weatherData.getPressure() + a);
            a = rand.nextInt(10);
            weatherData.setTemperature(weatherData.getTemperature() + a);
            /*koniec RANDOM BLOK*/

            fileWorker.appendToFile(FILENAME, weatherData.toString());

            sendAndDelete();

        } catch (Exception e) {
            //todo: blad!
            e.printStackTrace();
        }
    }

    /**
     * Reads file and sends data from file
     */
    private void sendAndDelete() {
        if(!internetChecker.isOnline()) return;

        final AtomicInteger counter = new AtomicInteger(0);
        List<WeatherData> weatherDatas = null;

        try {
            weatherDatas = fileWorker.readFile(FILENAME);
            if(weatherDatas.isEmpty()) return;

            int delay = 0;
            for (final WeatherData weatherData : weatherDatas) {
                delay += SEND_DELAY;
                sendWithDelay(weatherData, delay, counter);
            }
        } catch (IOException e) {
            //todo: nie mozna przeczytac pliku!
            e.printStackTrace();
        }
    }

    /**
     * Sends data to Thingspeak with delay and remove line from file
     *
     * @param weatherData   input data to send
     * @param delay         delay value
     * @param counter       line of file to remove
     */
    private void sendWithDelay(final WeatherData weatherData, int delay,
                                final AtomicInteger counter) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(thingSpeakSender.send(weatherData)) {
                    try {
                        fileWorker.removeLineFromFile(FILENAME, counter.get());
                    } catch (IOException e) {
                        //todo: nie mozna usunac danych z pliku!
                        e.printStackTrace();
                    }
                } else {
                    counter.incrementAndGet();
                }
            }
        }, delay);
    }
}
