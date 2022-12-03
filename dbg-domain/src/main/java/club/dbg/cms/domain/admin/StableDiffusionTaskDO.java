package club.dbg.cms.domain.admin;

public class StableDiffusionTaskDO {
    private Integer id;

    private String uuid;

    private Integer creationTime;

    private Integer completionTime;

    private Integer width;

    private Integer height;

    private Integer samplingSteps;

    private Float cfgScale;

    private String samplingMethod;

    private String params;

    private String imagePath;

    private Long seed;

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Integer creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Integer completionTime) {
        this.completionTime = completionTime;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getSamplingSteps() {
        return samplingSteps;
    }

    public void setSamplingSteps(Integer samplingSteps) {
        this.samplingSteps = samplingSteps;
    }

    public Float getCfgScale() {
        return cfgScale;
    }

    public void setCfgScale(Float cfgScale) {
        this.cfgScale = cfgScale;
    }

    public String getSamplingMethod() {
        return samplingMethod;
    }

    public void setSamplingMethod(String samplingMethod) {
        this.samplingMethod = samplingMethod;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
