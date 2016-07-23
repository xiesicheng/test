package com.dplugin.maven.plugins.properties.model;

/**
 * 数据库资源配置项 2016-07-23 21:28:26
 * @author nayuan
 */
public class DataSource implements java.io.Serializable {

    /**
     * 数据库连接URL
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password = "";

    /**
     * pack的根ID
     */
    private String rootId = "0";

    /**
     * 根据上级pack ID和当前pack名称查询当前pack ID 的SQL
     */
    private String sqlGetIdByPidAndPack;

    /**
     * 根据上级pack ID获取所有属性列表 的SQL
     */
    private String sqlGetPropertiesByPid;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getSqlGetIdByPidAndPack() {
        return sqlGetIdByPidAndPack;
    }

    public void setSqlGetIdByPidAndPack(String sqlGetIdByPidAndPack) {
        this.sqlGetIdByPidAndPack = sqlGetIdByPidAndPack;
    }

    public String getSqlGetPropertiesByPid() {
        return sqlGetPropertiesByPid;
    }

    public void setSqlGetPropertiesByPid(String sqlGetPropertiesByPid) {
        this.sqlGetPropertiesByPid = sqlGetPropertiesByPid;
    }
}
