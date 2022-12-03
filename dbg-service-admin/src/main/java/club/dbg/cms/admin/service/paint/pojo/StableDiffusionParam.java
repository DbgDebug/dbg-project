package club.dbg.cms.admin.service.paint.pojo;

import javax.validation.constraints.NotNull;

public class StableDiffusionParam {
    @NotNull
    private String prompt;

    @NotNull
    private String negativePrompt;

    @NotNull
    private Integer samplingSteps;

    @NotNull
    private String samplingMethod;

    @NotNull
    private Float cfgScale;

    @NotNull
    private Integer width;

    @NotNull
    private Integer height;

    @NotNull
    private Long seed;

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }

    public Integer getSamplingSteps() {
        return samplingSteps;
    }

    public void setSamplingSteps(Integer samplingSteps) {
        this.samplingSteps = samplingSteps;
    }

    public String getSamplingMethod() {
        return samplingMethod;
    }

    public void setSamplingMethod(String samplingMethod) {
        this.samplingMethod = samplingMethod;
    }

    public Float getCfgScale() {
        return cfgScale;
    }

    public void setCfgScale(Float cfgScale) {
        this.cfgScale = cfgScale;
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
}
