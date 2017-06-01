package ovh.olo.smok.smokwroclawski.Object;

/**
 * Created by Michal on 2017-05-31.
 */

public class SensorConfigData {
    private String macAddress;
    private String channelId;
    private String writeAPIkey;
    private String readAPIkey;

    public SensorConfigData(String macAddress, String channelId, String writeAPIkey, String readAPIkey) {
        this.macAddress = macAddress;
        this.channelId = channelId;
        this.writeAPIkey = writeAPIkey;
        this.readAPIkey = readAPIkey;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getWriteAPIkey() {
        return writeAPIkey;
    }

    public void setWriteAPIkey(String writeAPIkey) {
        this.writeAPIkey = writeAPIkey;
    }

    public String getReadAPIkey() {
        return readAPIkey;
    }

    public void setReadAPIkey(String readAPIkey) {
        this.readAPIkey = readAPIkey;
    }

    @Override
    public String toString() {
        return "SensorConfigData{" +
                "macAddress='" + macAddress + '\'' +
                ", channelId='" + channelId + '\'' +
                ", writeAPIkey='" + writeAPIkey + '\'' +
                ", readAPIkey='" + readAPIkey + '\'' +
                '}';
    }
}
