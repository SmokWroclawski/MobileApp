package ovh.olo.smok.smokwroclawski.ThingSpeak;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ovh.olo.smok.smokwroclawski.Parser.ParserISO8601;

/**
 * Created by Michal on 2017-05-08.
 */
public class ChartData implements Parcelable {
    private List<ThingSpeakData> thingSpeakDataList;

    public ChartData() {
        thingSpeakDataList = new ArrayList<>();
    }

    public List<ThingSpeakData> getThingSpeakDataList() {
        return thingSpeakDataList;
    }

    public void setThingSpeakDataList(List<ThingSpeakData> thingSpeakDataList) {
        this.thingSpeakDataList = thingSpeakDataList;
    }

    public void add(Float temperature, Float pressure, Float humidity, Float pm25, Float pm10,
                    String date, Float latitude, Float longtitude) {
        thingSpeakDataList.add(new ThingSpeakData(date, temperature, pressure, humidity, pm25, pm10,
                latitude, longtitude));
    }

    public String getTemperature(int count, LatLng latLng) {
        if(count > thingSpeakDataList.size() || count < 0) count = thingSpeakDataList.size();
        String temperature = "[['ThingSpeakData', 'Temperature'],";
        int counter = 0;
        for (int i = thingSpeakDataList.size() - 1; (i >=0 ) && (counter < count); i--) {
            if(thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {
                temperature += "['" + ParserISO8601.toDate(thingSpeakDataList.get(i).getDate()) +
                        "', " + thingSpeakDataList.get(i).getTemperature() + "],";
                counter++;

            }
        }

        temperature += "]";
        return temperature;
    }

    public String getLastTemeprature(LatLng latLng) {
        String temperature = "";
        for (int i = thingSpeakDataList.size() - 1; i >= 0; i--) {
            if(thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {
                temperature += thingSpeakDataList.get(i).getTemperature();
                break;
            }
        }
        return temperature;
    }

    public String getPressure(int count, LatLng latLng) {
        if(count > thingSpeakDataList.size() || count < 0) count = thingSpeakDataList.size();
        String pressure = "[['ThingSpeakData', 'Pressure'],";
        int counter = 0;
        for (int i = thingSpeakDataList.size() - 1; (i >=0 ) && (counter < count); i--) {
            if(thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {
                pressure += "['" + ParserISO8601.toDate(thingSpeakDataList.get(i).getDate()) +
                        "', " + thingSpeakDataList.get(i).getPressure() + "],";
                counter++;
            }
        }
        pressure += "]";
        return pressure;
    }

    public String getLastPressure(LatLng latLng) {
        String pressure = "";
        for (int i = thingSpeakDataList.size() - 1; i >= 0; i--) {
            if (thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {

                pressure += thingSpeakDataList.get(i).getPressure();
                break;
            }
        }
        return pressure;
    }

    public String getHumidity(int count, LatLng latLng) {
        if(count > thingSpeakDataList.size() || count < 0) count = thingSpeakDataList.size();
        String humidity = "[['ThingSpeakData', 'Humidity'],";
        int counter = 0;
        for (int i = thingSpeakDataList.size() - 1; (i >=0 ) && (counter < count); i--) {
            if(thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {
                humidity += "['" + ParserISO8601.toDate(thingSpeakDataList.get(i).getDate()) +
                        "', " + thingSpeakDataList.get(i).getHumidity() + "],";
                counter++;
            }
        }
        humidity += "]";
        return humidity;
    }

    public String getLastHumidity(LatLng latLng) {
        String humidity = "";
        for (int i = thingSpeakDataList.size() - 1; i >= 0; i--) {
            if (thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {

                humidity += thingSpeakDataList.get(i).getHumidity();
                break;
            }
        }
        return humidity;
    }

    public String getPm25(int count, LatLng latLng) {
        if(count > thingSpeakDataList.size() || count < 0) count = thingSpeakDataList.size();
        String pm25 = "[['ThingSpeakData', 'PM2.5'],";
        int counter = 0;
        for (int i = thingSpeakDataList.size() - 1; (i >=0 ) && (counter < count); i--) {
            if(thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {
                pm25 += "['" + ParserISO8601.toDate(thingSpeakDataList.get(i).getDate()) +
                        "', " + thingSpeakDataList.get(i).getPm25() + "],";
                counter++;
            }
        }
        pm25 += "]";
        return pm25;
    }

    public String getLastPM25(LatLng latLng) {
        String pm25 = "";
        for (int i = thingSpeakDataList.size() - 1; i >= 0; i--) {
            if (thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {

                pm25 += thingSpeakDataList.get(i).getPm25();
                break;
            }
        }
        return pm25;
    }

    public String getPm10(int count, LatLng latLng) {
        if(count > thingSpeakDataList.size() || count < 0) count = thingSpeakDataList.size();
        String pm10 = "[['ThingSpeakData', 'PM10'],";
        int counter = 0;
        for (int i = thingSpeakDataList.size() - 1; (i >=0 ) && (counter < count); i--) {
            if(thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {
                pm10 += "['" + ParserISO8601.toDate(thingSpeakDataList.get(i).getDate()) +
                        "', " + thingSpeakDataList.get(i).getPm10() + "],";
                counter++;
            }
        }
        pm10 += "]";
        return pm10;
    }


    public String getLastPM10(LatLng latLng) {
        String pm10 = "";
        for (int i = thingSpeakDataList.size() - 1; i >= 0; i--) {
            if (thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {

                pm10 += thingSpeakDataList.get(i).getPm10();
                break;
            }
        }
        return pm10;
    }

    public String getLastDate(LatLng latLng) {
        String date = "";
        if(thingSpeakDataList.size() == 1) date = ParserISO8601.toDate(thingSpeakDataList.get(0).getDate());
        for (int i = thingSpeakDataList.size() - 1; i > 0; i--) {
            if (thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {

                date += thingSpeakDataList.get(i).getDate();
                break;
            }
        }
        return date;
    }

    public String toString(int count, LatLng latLng) {
        if(count > thingSpeakDataList.size() || count < 0) count = thingSpeakDataList.size();
        String toReturn = "[['ThingSpeakData', 'Temperature', 'Pressure', 'Humidity', 'PM2.5', 'PM10'],";

        for (int i = thingSpeakDataList.size() - count; i < thingSpeakDataList.size(); i++) {
            if(thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {
                toReturn += thingSpeakDataList.get(i).toString() + ",";
            }
        }

        toReturn += "]";
        return toReturn;
    }

    public int getMeasureCount(LatLng latLng) {
        int counter = 0;
        System.out.println(thingSpeakDataList.size());
        for (int i = thingSpeakDataList.size() - 1; i >= 0; i--) {
            if (thingSpeakDataList.get(i).getLongtitude() == latLng.longitude
                    && thingSpeakDataList.get(i).getLatitude() == latLng.latitude) {
                counter++;
            }
        }
        return counter;
    }

    public float getAvgPms(LatLng latLng) {
        float pm = 0;
        for (ThingSpeakData data :
                thingSpeakDataList) {
            if (data.getLongtitude() == latLng.longitude
                    && data.getLatitude() == latLng.latitude) {
                pm += (data.getPm10() + data.getPm25())/2;
            }
        }
        return pm / thingSpeakDataList.size();
    }

    protected ChartData(Parcel in) {
        if (in.readByte() == 0x01) {
            thingSpeakDataList = new ArrayList<ThingSpeakData>();
            in.readList(thingSpeakDataList, ThingSpeakData.class.getClassLoader());
        } else {
            thingSpeakDataList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (thingSpeakDataList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(thingSpeakDataList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChartData> CREATOR = new Parcelable.Creator<ChartData>() {
        @Override
        public ChartData createFromParcel(Parcel in) {
            return new ChartData(in);
        }

        @Override
        public ChartData[] newArray(int size) {
            return new ChartData[size];
        }
    };
}