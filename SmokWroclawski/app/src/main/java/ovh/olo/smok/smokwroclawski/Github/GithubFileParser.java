package ovh.olo.smok.smokwroclawski.Github;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ovh.olo.smok.smokwroclawski.Object.SensorConfigData;

/**
 * Created by Michal on 2017-05-31.
 */

public class GithubFileParser {

    public List<SensorConfigData> parse(String fileContent) {
        List<String> lines = Arrays.asList(fileContent.split("\n"));
        List<SensorConfigData> sensorConfigDatas = new ArrayList<>();
        for (String line: lines) {
            if(!line.equals("")) {
                String[] data = line.split("\\|");
                if(data.length != 4) continue;
                sensorConfigDatas.add(
                        new SensorConfigData(
                                data[0],
                                data[1],
                                data[2],
                                data[3]
                        )
                );
            }
        }
        return sensorConfigDatas;
    }
}
