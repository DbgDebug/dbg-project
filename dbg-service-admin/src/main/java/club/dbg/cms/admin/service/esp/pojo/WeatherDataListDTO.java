package club.dbg.cms.admin.service.esp.pojo;

import club.dbg.cms.domain.admin.WeatherDO;

import java.util.List;

public class WeatherDataListDTO {
    private List<WeatherDO> weatherList;

    private Integer total;

    public List<WeatherDO> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<WeatherDO> weatherList) {
        this.weatherList = weatherList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
