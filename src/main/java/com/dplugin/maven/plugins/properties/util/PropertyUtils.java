package com.dplugin.maven.plugins.properties.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * properties工具类 2016-07-23 22:48:51
 * @author nayuan
 */
public class PropertyUtils {

    public static Properties loadPropertyFile(File propfile, boolean useSystemProps ) throws IOException {

        Properties result = new OrderedProperties();
        result.load(new FileInputStream(propfile));

        if ( useSystemProps ) {
            result.putAll( System.getProperties() );
        }

        return result;
    }

}
