package ovh.olo.smok.smokwroclawski.ThingSpeak;

import android.os.Handler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ovh.olo.smok.smokwroclawski.Activity.MainActivity;
import ovh.olo.smok.smokwroclawski.DataChecker;
import ovh.olo.smok.smokwroclawski.Github.GithubDataWorker;
import ovh.olo.smok.smokwroclawski.InternetChecker;
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
    private static String FILENAME;
    private final static int SEND_DELAY = 16000;

    private ThingSpeakSender thingSpeakSender;
    private FileWorker fileWorker;

    private int lineCount;

    public SendingQueue(String macAddress) {
        thingSpeakSender = new ThingSpeakSender(GithubDataWorker.getWriteApiKey(macAddress));
        fileWorker = new FileWorker();
        FILENAME = "SW_WeatherData_" + macAddress + ".txt";

    }


    /**
     * Writes input WeatherData to file and sends it to Thingspeak.
     *
     * @param weatherData   Input data
     */
    public void send(WeatherData weatherData) {
        try {
            weatherData.setTimeStamp(System.currentTimeMillis()/1000);
            weatherData.setLongtitude(Math.round(
                    MainActivity.instance.getMyLocation().getLongitude() * 1000d
                )/1000d);
            weatherData.setLatitude(Math.round(
                    MainActivity.instance.getMyLocation().getLatitude() * 1000d
                )/1000d);
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
        if(!InternetChecker.isOnline()) {
            System.out.println("No Internet Connection!");
            return;
        }
        final AtomicInteger counter = new AtomicInteger(0);
        List<WeatherData> weatherDatas = null;

        try {
            lineCount = fileWorker.getLineCount(FILENAME);

            weatherDatas = fileWorker.readFile(FILENAME);
            if(weatherDatas.isEmpty()) return;

            int delay = 0;
            MainActivity.instance.updateMaxProgressBar(lineCount);
            for (WeatherData weatherData : weatherDatas) {
                System.out.println(delay);
                sendWithDelay(weatherData, delay, counter);
                delay += SEND_DELAY;
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
    private void sendWithDelay(final WeatherData weatherData, final int delay,
                               final AtomicInteger counter) {

        final Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
            @Override
            public void run() {
                MainActivity.instance.updateProgressBar();
                int responseBodyCode = 0;
                boolean isCorrectData = DataChecker.isCorrect(weatherData);
                if(isCorrectData) {
                    responseBodyCode = thingSpeakSender.send(weatherData);
                    System.out.println("Kod:" + responseBodyCode);
                }
                if(responseBodyCode > 0 || !isCorrectData) {
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

    private void sendListWithDelay(final List<WeatherData> weatherDatas,
                               final AtomicInteger counter) {
        final AtomicInteger weatherDataCounter = new AtomicInteger(0);
        final Handler handler = new Handler();
        handler.postDelayed(
//        scheduler.schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        if(weatherDataCounter.get() >= weatherDatas.size()) return;
                        MainActivity.instance.updateProgressBar();
                        int responseBodyCode = 0;
                        boolean isCorrectData = DataChecker.isCorrect(weatherDatas.get(weatherDataCounter.get()));
                        if(isCorrectData) {
                            responseBodyCode = thingSpeakSender.send(weatherDatas.get(weatherDataCounter.get()));
                            System.out.println("Kod:" + responseBodyCode);
                        }
                        if(responseBodyCode > 0 || !isCorrectData) {
                            try {
                                fileWorker.removeLineFromFile(FILENAME, counter.get());
                            } catch (IOException e) {
                                //todo: nie mozna usunac danych z pliku!
                                e.printStackTrace();
                            }
                        } else {
                            counter.incrementAndGet();
                        }
                        weatherDataCounter.incrementAndGet();
                        handler.postDelayed(this, SEND_DELAY);
                    }
                }, SEND_DELAY); //);//, TimeUnit.MILLISECONDS
    }
}
