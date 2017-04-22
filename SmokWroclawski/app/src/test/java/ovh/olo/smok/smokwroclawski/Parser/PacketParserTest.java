package ovh.olo.smok.smokwroclawski.Parser;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;

import static org.junit.Assert.*;

/**
 * Created by Michal on 2017-03-29.
 */
public class PacketParserTest {
    private static final double DELTA = 1e-15;
    private final static byte[] bytes = {0x31, (byte) 0xfc};
    private PacketParser packetParser;

    @Before
    public void setUp() {
        packetParser = new PacketParser();
    }

    @Test
    public void shouldBeLessThan0Temperature() throws Exception {
        WeatherData weatherData = packetParser.parsePacket(bytes);

//        System.out.printf(weatherData.toString());

        assertEquals(-97.5, weatherData.getTemperature(), DELTA);
    }



}