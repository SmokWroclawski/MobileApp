package ovh.olo.smok.smokwroclawski.ThingSpeak;

import android.os.Parcel;
import android.os.Parcelable;

public class ThingSpeakData implements Parcelable {
    private String date;
    private float temperature;
    private float pressure;
    private float humidity;
    private float pm25;
    private float pm10;
    private float latitude;
    private float longtitude;

    public ThingSpeakData(String date, float temperature, float pressure, float humidity,
                          float pm25, float pm10, float latitude, float longtitude) {
        this.date = date;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPm25() {
        return pm25;
    }

    public void setPm25(float pm25) {
        this.pm25 = pm25;
    }

    public float getPm10() {
        return pm10;
    }

    public void setPm10(float pm10) {
        this.pm10 = pm10;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public String toString() {
        return "['" + date +
                "', " + temperature +
                ", " + pressure +
                ", " + humidity +
                ", " + pm25 +
                ", " + pm10 +
                "]";
    }

    protected ThingSpeakData(Parcel in) {
        date = in.readString();
        temperature = in.readFloat();
        pressure = in.readFloat();
        humidity = in.readFloat();
        pm25 = in.readFloat();
        pm10 = in.readFloat();
        latitude = in.readFloat();
        longtitude = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeFloat(temperature);
        dest.writeFloat(pressure);
        dest.writeFloat(humidity);
        dest.writeFloat(pm25);
        dest.writeFloat(pm10);
        dest.writeFloat(latitude);
        dest.writeFloat(longtitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ThingSpeakData> CREATOR = new Parcelable.Creator<ThingSpeakData>() {
        @Override
        public ThingSpeakData createFromParcel(Parcel in) {
            return new ThingSpeakData(in);
        }

        @Override
        public ThingSpeakData[] newArray(int size) {
            return new ThingSpeakData[size];
        }
    };
}