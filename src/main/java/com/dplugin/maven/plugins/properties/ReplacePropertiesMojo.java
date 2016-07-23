package com.dplugin.maven.plugins.properties;

import com.dplugin.maven.plugins.properties.log.Logger;
import com.dplugin.maven.plugins.properties.log.SimpleLogger;
import com.dplugin.maven.plugins.properties.model.ReplaceRule;
import com.dplugin.maven.plugins.properties.source.Entry;
import com.dplugin.maven.plugins.properties.source.SourceStore;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.utils.io.DirectoryScanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 替换文件 mojo 2016-07-23 22:58:00
 * @author nayuan
 * @version $Id$
 */
@Mojo(name = "replace", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true)
public class ReplacePropertiesMojo extends AbstractPropertiesMojo {

    private static Logger logger = new SimpleLogger("[properties-maven-plugin replace] - ");

    /**
     * Skip entire check.
     */
    @Parameter( property = "dplugin.properties.replace.skip", defaultValue = "false" )
    protected boolean skipReplace;

    /**
     * 替换文件 规则
     */
    @Parameter
    protected ReplaceRule[] replaceRules;

    @Override
    public void update() throws MojoExecutionException, MojoFailureException {
        if(skipReplace) {
            logger.info("skip replace.");
            return;
        }

        logger.info("replace execute.");

        if(replaceRules == null || replaceRules.length == 0) {
            getLog().info("replaceRules is not set.");
        }

        int n = 0;
        for(ReplaceRule rule : replaceRules) {
            n += replace(rule);
        }

        logger.info("A total of " + n + " files were replaced.");
    }

    private int replace(ReplaceRule rule) throws MojoExecutionException {
        if(!rule.getFiltering()) return 0;
        if(rule.getIncludes() == null || rule.getIncludes().length == 0) return 0;

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(project.getBasedir());
        scanner.setIncludes(rule.getIncludes());
        scanner.setExcludes(rule.getExcludes());
        scanner.scan();
        String[] files = scanner.getIncludedFiles();

        int n = 0;
        for(String item : files) {
            if(item.endsWith("." + FILE_PROPERTIES_SUFFIX)) {
                if(replaceProperties(rule.getPack(), item)) n++;
            }else{
                if(replaceOther(rule.getPack(), item)) n++;
            }
        }

        return n;
    }

    /**
     * 属性文件的替换方式
     * @param pack 包
     * @param file 文件
     * @return 是否有替换
     * @throws MojoExecutionException 失败
     */
    private boolean replaceProperties(String pack, String file) throws MojoExecutionException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), encoding));
        } catch (Exception e) {
            logger.error("load properties file fail - " + file);
            throw new MojoExecutionException(e.getMessage(), e);
        }

        logger.info("replace: " + file);
        StringBuilder content = new StringBuilder();
        int n = 0;
        String line = null;
        try {
            Entry value = null;
            while((line = reader.readLine()) != null) {
                if(!line.trim().startsWith("#") && line.contains("=")) {
                    String key = line.substring(0, line.indexOf("=")).trim();
                    value = SourceStore.getValue(pack, key);
                    if(value != null) {
                        n++;
                        logger.info(key + "=" + value.getValue());
                        line = line.replaceFirst("=.*$", "=" + value.getValue());
                    }
                }
                content.append(line).append(lineSeparator);
            }
        } catch (IOException e) {
            logger.error("load properties file fail - " + file);
            throw new MojoExecutionException(e.getMessage(), e);
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {}
            }
        }

        if(n == 0) {
            logger.info("No attribute is replaced");
            return false;
        }

        writeStringToFile(file, content.toString());

        return true;
    }

    /**
     * 其他文件的替换方式
     * @param pack 包
     * @param file 文件
     * @return 是否有替换
     * @throws MojoExecutionException 失败
     */
    private boolean replaceOther(String pack, String file) throws MojoExecutionException {
        Pattern pattern = Pattern.compile("\\$\\{([^\\}]+)\\}");
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(file)), this.encoding);
        } catch (IOException e) {
            logger.error("read file fail - " + file);
            throw new MojoExecutionException(e.getMessage(), e);
        }

        int n = 0;
        StringBuffer newContent = new StringBuffer();
        Matcher matcher = pattern.matcher(content);
        Entry value = null;
        while(matcher.find()) {
            value = SourceStore.getValue(pack, matcher.group(1));
            if(value != null) {
                n++;
                matcher.appendReplacement(newContent, value.getValue());
            }
        }

        if(n == 0) return false;

        matcher.appendTail(newContent);
        writeStringToFile(file, newContent.toString());

        return true;
    }

    private void writeStringToFile(String file, String content) throws MojoExecutionException {
        try {
            Files.write(Paths.get(file), content.getBytes(this.encoding));
            logger.info("replace: " + file);
        } catch (IOException e) {
            logger.error("replace file fail - " + file);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

}