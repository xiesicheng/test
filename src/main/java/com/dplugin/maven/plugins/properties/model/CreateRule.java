package com.dplugin.maven.plugins.properties.model;

/**
 * 生成文件规则类 2016-07-23 21:26:51
 * @author nayuan
 */
public class CreateRule {

    /**
     * 是否启用 true-启用 false-不启用
     */
    private Boolean filtering;

    /**
     * 要生成的文件(相对工程的路径)
     */
    private String file;

    /**
     * 包含的pack列表
     */
    private String[] includePackes;

    public Boolean getFiltering() {
        return filtering;
    }

    public void setFiltering(Boolean filtering) {
        this.filtering = filtering;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String[] getIncludePackes() {
        return includePackes;
    }

    public void setIncludePackes(String[] includePackes) {
        this.includePackes = includePackes;
    }

}
