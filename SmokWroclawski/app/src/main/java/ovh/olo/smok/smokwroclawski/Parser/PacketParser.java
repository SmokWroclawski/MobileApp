package ovh.olo.smok.smokwroclawski.Parser;

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
        int temperature = ByteBuffer.wrap(Arrays.copyOfRange(frame, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        int pressure = ByteBuffer.wrap(Arrays.copyOfRange(frame, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        int humidity = ByteBuffer.wrap(Arrays.copyOfRange(frame, 4, 6)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        int pm25 = ByteBuffer.wrap(Arrays.copyOfRange(frame, 6, 8)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        int pm10 = ByteBuffer.wrap(Arrays.copyOfRange(frame, 8, 10)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        return new WeatherData(
                temperature,
                pressure,
                humidity,
                pm25,
                pm10
        );
    }

}
