package club.dbg.cms.admin.service.esp.pojo;

public class EspWeatherDataDTO {
    //  温度 temperature 摄氏度 xx.xx
    private float t;

    // 湿度 humidity xx.xx
    private float h;

    // 大气压强 atmospheric pressure
    private Float ap;

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public Float getAp() {
        return ap;
    }

    public void setAp(Float ap) {
        this.ap = ap;
    }
}
