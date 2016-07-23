package com.dplugin.maven.plugins.properties.source;

import com.dplugin.maven.plugins.properties.log.Logger;
import com.dplugin.maven.plugins.properties.log.SimpleLogger;
import com.dplugin.maven.plugins.properties.util.OrderedProperties;
import com.dplugin.maven.plugins.properties.util.PropertyUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.shared.utils.io.DirectoryScanner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件 源 2016-07-23 22:41:53
 * @author nayuan
 */
public class DirectorySource implements Source {

    private static Logger logger = new SimpleLogger("[properties-maven-plugin DirectorySource] - ");

    /**
     * pack 分割符
     */
    private String pSplit;

    /**
     * pack和value的分割符
     */
    private String pkSplit;

    /**
     * 临时缓存
     */
    private Properties store = new OrderedProperties();

    public DirectorySource(String pSplit, String pkSplit) {
        this.pSplit = pSplit;
        this.pkSplit = pkSplit;
    }

    public Entry getValue(String pack, String key) throws MojoExecutionException {
        do{
            if(store.containsKey(pack + pkSplit + key)) {
                return new Entry(key, store.getProperty(pack + pkSplit + key));
            }

            if(!pack.contains(pSplit)) break;
            pack = pack.replaceFirst("[^\\" + pSplit + "]+$", "");
        }while(true);

        if(store.containsKey(key)) {
            return new Entry(key, store.getProperty(key));
        }

        return null;
    }

    public Collection<Entry> getProperties(String pack) throws MojoExecutionException {
        List<String> packs = new ArrayList();
        packs.add(pack + pkSplit);
        if(pack.contains(pSplit)) {
            String[] temps = pack.split("\\" + pSplit);
            for(int i = temps.length - 1; i > 0; i--) {
                pack = pack.replace(pSplit+temps[i], "");
                packs.add(pack + pkSplit);
            }
        }

        List<Entry> result = new ArrayList();
        Entry entry = null;
        for(Object key : store.keySet()) {
            for(String filter : packs) {
                if(key.toString().startsWith(filter)) {
                    result.add(new Entry(key.toString().substring(filter.length()), store.getProperty(key.toString())));
                    break;
                }
            }

        }

        return result;
    }

    public boolean init(String directory) throws IOException {
        if(StringUtils.isBlank(directory)) {
            logger.debug("directory is not set.");
            return false;
        }

        String include = null;
        if(directory.contains("*")) {
            Matcher matcher = Pattern.compile("^([^\\\\*]+/)([^/\\\\*]*\\*.*)$").matcher(directory);
            matcher.find();
            directory = matcher.group(1);
            include = matcher.group(2);
        } else {
            File dir = new File(directory);
            if(dir.isFile()) {
                logger.debug("load file: " + directory);
                store.putAll(PropertyUtils.loadPropertyFile(dir, false));
            }else{
                include = "*.*";
            }
        }

        if(include != null) {
            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(directory);
            scanner.setIncludes(include);
            scanner.scan();
            String[] files = scanner.getIncludedFiles();

            for(String file : files) {
                logger.debug("load file: " + file);
                store.putAll(PropertyUtils.loadPropertyFile(new File(scanner.getBasedir() + "/" + file), false));
            }
        }

        if(store.size() == 0) {
            logger.warn("not load attribute set.");
            return false;
        } else if(logger.isDebugEnabled()) {
            logger.debug("attribute list:");
            for(Object key : store.keySet()) {
                logger.debug(key + "=" + store.get(key));
            }
        }

        logger.info("directory source init succeed.");
        return true;
    }

}
