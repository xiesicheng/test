package com.dplugin.maven.plugins.properties;

import com.dplugin.maven.plugins.properties.log.Logger;
import com.dplugin.maven.plugins.properties.log.SimpleLogger;
import com.dplugin.maven.plugins.properties.model.DataSource;
import com.dplugin.maven.plugins.properties.source.SourceStore;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.utils.StringUtils;

/**
 * 通用实现 mojo 2016-07-23 22:49:57
 * @author nayuan
 * @version $Id$
 */
public abstract class AbstractPropertiesMojo extends AbstractMojo{

    private static Logger logger = new SimpleLogger("[properties-maven-plugin] - ");

    protected static final String FILE_PROPERTIES_SUFFIX = "properties"; //属性文件后缀

    @Parameter( defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    /**
     * Skip entire check.
     */
    @Parameter( defaultValue = "false" )
    protected boolean skip;

    /**
     * The -encoding argument for the Java compiler.
     */
    @Parameter( property = "encoding", defaultValue = "${project.build.sourceEncoding}")
    protected String encoding;


    /**
     * 目录源 配置
     */
    @Parameter( property = "dplugin.properties.directory")
    protected String directory;

    /**
     * 数据库源 配置
     */
    @Parameter
    protected DataSource dataSource;

    /**
     * pack分割符
     */
    @Parameter( property = "dplugin.properties.packSeparator", defaultValue = "." )
    protected String packSeparator;

    /**
     * pack key分割符
     */
    @Parameter( property = "dplugin.properties.packKeySeparator", defaultValue = "@" )
    protected String packKeySeparator;

    /**
     * 换行符
     */
    @Parameter( property = "line.separator", defaultValue = "\n", readonly = true)
    protected String lineSeparator;

    public void execute() throws MojoExecutionException, MojoFailureException {
        SimpleLogger.initLogger(getLog());

        if(this.skip) {
            logger.info("skip.");
            return;
        }

        if(StringUtils.isEmpty(encoding)) {
            logger.warn("encoding is not set, default UTF-8.");
            this.encoding = "UTF-8";
        }

        try {
            this.initSource();
        } catch (Exception e) {
            logger.error("init source fail.");
            throw new MojoExecutionException(e.getMessage(), e);
        }

        if(! this.skip) {
            update();
        }
    }

    public abstract void update() throws MojoExecutionException, MojoFailureException;


    ///////////////////////////////////////////////////////////////////////
    ///// 数据源 初始化
    ///////////////////////////////////////////////////////////////////////
    private void initSource() throws Exception {
        boolean status = SourceStore.setDataBaseSource(dataSource, packSeparator)
            || SourceStore.setDirectorySource(directory, packSeparator, packKeySeparator);
        if(!status) {
            logger.warn("init source fail, skip.");
            this.skip = true;
        }
    }

}
