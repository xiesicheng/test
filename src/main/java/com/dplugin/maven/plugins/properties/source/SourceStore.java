package com.dplugin.maven.plugins.properties.source;

import com.dplugin.maven.plugins.properties.model.DataSource;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 源操作工具 2016-07-23 22:47:31
 * @author nayuan
 */
public class SourceStore {

    /**
     * 可用源列表
     */
    private static List<Source> sources = new ArrayList();

    public static boolean setDirectorySource(String directory, String pSplit, String pkSplit) throws IOException {
        DirectorySource source = new DirectorySource(pSplit, pkSplit);
        if(source.init(directory)) {
            sources.add(source);
            return true;
        }else{
            return false;
        }
    }

    public static boolean setDataBaseSource(DataSource dataSource, String pSplit) throws Exception {
        DataBaseSource source = new DataBaseSource(pSplit);
        if(source.init(dataSource)) {
            sources.add(source);
            return true;
        }else{
            return false;
        }
    }

    public static Entry getValue(String pack, String key) throws MojoExecutionException {
        Entry value = null;
        for(Source source : sources) {
            value = source.getValue(pack, key);
            if(value != null) {
                return value;
            }
        }

        return null;
    }

    public static Collection<Entry> getProperties(String pack) throws MojoExecutionException {
        Collection<Entry> result = null;
        for(Source source : sources) {
            result = source.getProperties(pack);
            if(result.size() > 0) {
                return result;
            }
        }

        return new ArrayList(0);
    }
}
