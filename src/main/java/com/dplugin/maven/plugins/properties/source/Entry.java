package com.dplugin.maven.plugins.properties.source;

/**
 * 属性项 2016-07-23 22:44:01
 * @author nayuan
 */
public class Entry {

    /**
     * 属性ID
     */
    private Object id;

    /**
     * 描述/标题
     */
    private String title = "";

    /**
     * 属性key
     */
    private String key;

    /**
     * 属性值
     */
    private String value = "";

    public Entry(String key, String value) {
        this("", key, value);
    }

    public Entry(String title, String key, String value) {
        this(null, title, key, value);
    }

    public Entry(Object id, String title, String key, String value) {
        this.id = id;
        this.title = title;
        this.key = key;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        if(key == null) {
            return title;
        }else{
            return key + "=" + value;
        }
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
