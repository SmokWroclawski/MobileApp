package ovh.olo.smok.smokwroclawski.Parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;

/**
 * This class contains method for manipulating JSON.
 *
 * @author Michal Popek
 * @author Jakub Obacz
 * @see WeatherData
 */
public class JsonParser {
    private NumberParser numberParser;

    public JsonParser() {
        numberParser = new NumberParser();
    }

    /**
     * Returns an list of WeatherData objects from JSON input string.
     * <p>
     * Returns and empty list if input string is wrong.
     * @param in    Input string to parse from JSON
     * @return      List of WeatherData from input string
     * @throws      JSONException
     */
    public List<WeatherData> getWeatherDatasFromJson(String in) throws JSONException {
        List<WeatherData> weatherDatas = new ArrayList<>();
        List<String> lines = Arrays.asList(in.split("\\r?\\n"));
        try {
            for (String line : lines) {
                JSONObject json = new JSONObject(line);
                weatherDatas.add(
                        new WeatherData(
                                numberParser.parseDouble(json.getString(
                                        WeatherData.fieldNames.get(0)
                                )),
                                numberParser.parseDouble(json.getString(
                                        WeatherData.fieldNames.get(1)
                                )),
                                numberParser.parseDouble(json.getString(
                                        WeatherData.fieldNames.get(2)
                                )),
                                numberParser.parseDouble(json.getString(
                                        WeatherData.fieldNames.get(3)
                                )),
                                numberParser.parseDouble(json.getString(
                                        WeatherData.fieldNames.get(4)
                                )),
                                numberParser.parseDouble(json.getString(
                                        WeatherData.fieldNames.get(5)
                                ))
                        )
                );
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return weatherDatas;
    }
}
