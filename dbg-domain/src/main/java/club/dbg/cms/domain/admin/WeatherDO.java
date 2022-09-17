package club.dbg.cms.domain.admin;

public class WeatherDO {
    private Integer id;

    private Integer deviceId;

    private Integer creationTime;

    private Float temperature;

    private Float humidity;

    private Float atmosphericPressure;

    public Integer getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Integer creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getAtmosphericPressure() {
        return atmosphericPressure;
    }

    public void setAtmosphericPressure(Float atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }
}
