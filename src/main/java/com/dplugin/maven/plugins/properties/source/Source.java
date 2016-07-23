package com.dplugin.maven.plugins.properties.source;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.Collection;

/**
 * 源接口
 * @author nayuan
 * @time 2016-07-23 22:46:24
 */
public interface Source {

    /**
     * 获取单个属性的值
     * @param pack 包名
     * @param key 属性key
     * @return
     * @throws MojoExecutionException
     */
    public Entry getValue(String pack, String key) throws MojoExecutionException;

    /**
     * 获取pack下所有属性的值
     * @param pack 包名
     * @return
     * @throws MojoExecutionException
     */
    public Collection<Entry> getProperties(String pack) throws MojoExecutionException;

}
