package ovh.olo.smok.smokwroclawski.Parser;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;

/**
 * Created by Michal on 2017-03-24.
 */

public class PacketParser {
//    uint8_t frame[] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
//    memcpy(&frame[0], &temperature, sizeof(temperature));
//    memcpy(&frame[2], &pressure, sizeof(pressure));
//    memcpy(&frame[4], &humidity, sizeof(humidity));
//    memcpy(&frame[6], &pm25, sizeof(pm25));
//    memcpy(&frame[8], &pm10, sizeof(pm10));

    public WeatherData parsePacket(byte[] frame) {
        short temperature = 0;
        short pressure = 0;
        short humidity = 0;
        short pm25 = 0;
        short pm10 = 0;
        try {
            temperature = ByteBuffer.wrap(Arrays.copyOfRange(frame, 0, 2)).order(ByteOrder.BIG_ENDIAN).getShort();
            pressure = ByteBuffer.wrap(Arrays.copyOfRange(frame, 2, 4)).order(ByteOrder.BIG_ENDIAN).getShort();
            humidity = ByteBuffer.wrap(Arrays.copyOfRange(frame, 4, 6)).order(ByteOrder.BIG_ENDIAN).getShort();
            pm25 = ByteBuffer.wrap(Arrays.copyOfRange(frame, 6, 8)).order(ByteOrder.BIG_ENDIAN).getShort();
            pm10 = ByteBuffer.wrap(Arrays.copyOfRange(frame, 8, 10)).order(ByteOrder.BIG_ENDIAN).getShort();
        } catch (Exception ignored) {}

        return new WeatherData(
                temperature,
                pressure,
                humidity,
                pm25,
                pm10
        );
    }

}
