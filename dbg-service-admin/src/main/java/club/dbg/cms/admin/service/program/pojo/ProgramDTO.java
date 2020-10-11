package club.dbg.cms.admin.service.program.pojo;

import club.dbg.cms.domain.admin.ProgramDO;

public class ProgramDTO {
    private Integer id;
    private String language;
    private String described;
    private String sourceCode;
    private Integer status;
    private Long createTime;
    private Long updateTime;

    public ProgramDTO() {
    }

    public ProgramDTO(ProgramDO programDO) {
        this.id = programDO.getId();
        this.language = programDO.getLanguage();
        this.described = programDO.getDescribed();
        this.status = programDO.getStatus();
        this.createTime = programDO.getCreateTime();
        this.updateTime = programDO.getUpdateTime();
        this.sourceCode = programDO.getSourceCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescribed() {
        return described;
    }

    public void setDescribed(String described) {
        this.described = described;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
