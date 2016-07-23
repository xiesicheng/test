package com.dplugin.maven.plugins.properties;

import com.dplugin.maven.plugins.properties.log.Logger;
import com.dplugin.maven.plugins.properties.log.SimpleLogger;
import com.dplugin.maven.plugins.properties.model.CreateRule;
import com.dplugin.maven.plugins.properties.source.Entry;
import com.dplugin.maven.plugins.properties.source.SourceStore;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.utils.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成文件 mojo 2016-07-23 23:02:12
 * @author nayuan
 * @version $Id$
 */
@Mojo(name = "create", defaultPhase = LifecyclePhase.INITIALIZE, threadSafe = true)
public class CreatePropertiesMojo extends AbstractPropertiesMojo {

    private static Logger logger = new SimpleLogger("[properties-maven-plugin create] - ");

    /**
     * Skip entire check.
     */
    @Parameter( property = "dplugin.properties.create.skip", defaultValue = "false" )
    protected boolean skipCreate;

    /**
     * 如果存在,是否覆盖或跳过
     */
    @Parameter( property = "dplugin.properties.isCover", defaultValue = "false")
    protected boolean isCover;

    /**
     * 生成文件 规则
     */
    @Parameter
    protected CreateRule[] createRules;

    public void update() throws MojoExecutionException, MojoFailureException {
        if(skipCreate) {
            logger.info("skip execute.");
            return;
        }

        logger.info("start execute.");

        if(createRules == null || createRules.length == 0) {
            logger.info("createRules is not set.");
            return;
        }

        int n = 0;
        for(CreateRule rule : createRules) {
            if(createFile(rule)) n++;
        }

        logger.info("A total of " + n + " files were created.");
    }

    private boolean createFile(CreateRule rule) throws MojoExecutionException {
        if(!rule.getFiltering()) return false;
        File file = new File(project.getBasedir() + File.separator + rule.getFile());

        File parent = file.getParentFile();
        if(!parent.isDirectory()) {
            parent.mkdirs();
        }

        Writer writer = null;
        try {
            if(file.exists()) {
                if(!isCover) {
                    return false;
                }
            }else{
                file.createNewFile();
            }

            Map<String, Integer> cache = new HashMap();
            Collection<Entry> list = null;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(rule.getFile())), encoding));
            for(String pack : rule.getIncludePackes()) {
                writer.append("########################################").append(lineSeparator);
                writer.append("## ").append(pack).append(lineSeparator);
                writer.append("########################################").append(lineSeparator);
                list = SourceStore.getProperties(pack);
                for(Entry entry : list) {
                    if(entry.getKey() == null || cache.containsKey(entry.getKey())) continue;
                    if(StringUtils.isNotBlank(entry.getTitle())) {
                        writer.append("##").append(entry.getTitle()).append(lineSeparator);
                    }
                    writer.append(entry.getKey()).append("=").append(entry.getValue()).append(lineSeparator);
                    cache.put(entry.getKey(), null);
                }
            }
            writer.flush();
            logger.info("created: " + rule.getFile());
        } catch (IOException e) {
            logger.error("create file fail.");
            throw new MojoExecutionException(e.getMessage(), e);
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {}
            }
        }
        return true;
    }



}
