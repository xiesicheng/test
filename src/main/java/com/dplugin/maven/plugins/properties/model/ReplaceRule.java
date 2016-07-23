package com.dplugin.maven.plugins.properties.model;

/**
 * 替换文件内容规则配置项 2016-07-23 21:31:40
 * @author nayuan
 */
public class ReplaceRule {

    /**
     * 是否启用 true-启用 false-不启用
     */
    private Boolean filtering;

    /**
     * 源属性所在的 pack 名称
     */
    private String pack;

    /**
     * 包含要替换的文件或路径列表
     */
    public String[] includes;

    /**
     * 要排除的文件或路径列表
     */
    public String[] excludes;

    public Boolean getFiltering() {
        return filtering;
    }

    public void setFiltering(Boolean filtering) {
        this.filtering = filtering;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String[] getIncludes() {
        return includes;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

    public String[] getExcludes() {
        return excludes;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }
}
