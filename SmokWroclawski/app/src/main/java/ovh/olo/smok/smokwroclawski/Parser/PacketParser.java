package ovh.olo.smok.smokwroclawski.Parser;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;

/**
 * This class contains method for manipulating packets.
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class PacketParser {

    /**
     * Convert byte array to WeatherData object. For every 2 bytes
     * there is another WeatherData field.
     *   uint8_t frame[] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
     *   memcpy(&frame[0], &temperature, sizeof(temperature));
     *   memcpy(&frame[2], &pressure, sizeof(pressure));
     *   memcpy(&frame[4], &humidity, sizeof(humidity));
     *   memcpy(&frame[6], &pm25, sizeof(pm25));
     *   memcpy(&frame[8], &pm10, sizeof(pm10));
     *
     * @param frame     Byte array packet
     * @return          WeatherData object with data from input byte array
     */
    public WeatherData parsePacket(byte[] frame) {
        if(frame.length == 0)
            return new WeatherData(0, 0, 0, 0, 0, 0);

        double temperature = 0;
        double pressure = 0;
        double humidity = 0;
        double pm25 = 0;
        double pm10 = 0;
        double date = 0;


        try {
            temperature = ByteBuffer.wrap(Arrays.copyOfRange(frame, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10.0;
            pressure = ByteBuffer.wrap(Arrays.copyOfRange(frame, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort();
            humidity = ByteBuffer.wrap(Arrays.copyOfRange(frame, 4, 6)).order(ByteOrder.LITTLE_ENDIAN).getShort();
            pm25 = ByteBuffer.wrap(Arrays.copyOfRange(frame, 6, 8)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10.0;
            pm10 = ByteBuffer.wrap(Arrays.copyOfRange(frame, 8, 10)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10.0;
            date = ByteBuffer.wrap(Arrays.copyOfRange(frame, 8, 10)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        } catch (Exception ignored) {}

        return new WeatherData(
                temperature,
                pressure,
                humidity,
                pm25,
                pm10,
                date
        );
    }

}
