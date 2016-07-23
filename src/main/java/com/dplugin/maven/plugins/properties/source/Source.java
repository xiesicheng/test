package com.dplugin.maven.plugins.properties.source;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.Collection;

/**
 * 源接口 2016-07-23 22:46:24
 * @author nayuan
 */
public interface Source {

    /**
     * 获取单个属性的值
     * @param pack 包名
     * @param key 属性key
     * @return 项
     * @throws MojoExecutionException 失败
     */
    public Entry getValue(String pack, String key) throws MojoExecutionException;

    /**
     * 获取pack下所有属性的值
     * @param pack 包名
     * @return 属性集合
     * @throws MojoExecutionException 失败
     */
    public Collection<Entry> getProperties(String pack) throws MojoExecutionException;

}
