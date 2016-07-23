package com.dplugin.maven.plugins.properties.source;

import com.alibaba.druid.util.StringUtils;
import com.dplugin.maven.plugins.properties.log.Logger;
import com.dplugin.maven.plugins.properties.log.SimpleLogger;
import com.dplugin.maven.plugins.properties.model.DataSource;
import org.apache.maven.plugin.MojoExecutionException;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库 源 2016-07-23 21:34:15
 * @author nayuan
 */
public class DataBaseSource implements Source {

    private static Logger logger = new SimpleLogger("[properties-maven-plugin DataBaseSource] - ");

    /**
     * 临时缓存
     */
    private static Map<String, Entry> store = new HashMap();

    /**
     * pack的分割符
     */
    private String split;

    private JdbcTemplate template;

    private DataSource dataSource;

    public DataBaseSource(String split) {
        this.split = split;
    }

    public boolean init(DataSource dataSource) throws Exception {
        if(dataSource == null || StringUtils.isEmpty(dataSource.getUrl()) || StringUtils.isEmpty(dataSource.getUsername())) {
            return false;
        }
        if(StringUtils.isEmpty(dataSource.getRootId())) {
            dataSource.setRootId("0");
        }
        template = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
        logger.info("dataSource init succeed.");
        return true;
    }

    @Override
    public Entry getValue(String pack, String key) throws MojoExecutionException {

        if(store.containsKey(pack + "=" + key)) {
            return store.get(pack + "=" + key);
        }

        //获取pack下所有属性,并放入缓存中(一般情况下会大量获取同一pack下的多个属性值)
        Collection<Entry> list = getProperties(pack);
        for(Entry entry : list) {
            store.put(pack + "=" + entry.getKey(), entry);
        }

        if(store.containsKey(pack + "=" + key)) {
            return store.get(pack + "=" + key);
        }

        return null;
    }

    public Collection<Entry> getProperties(String pack) throws MojoExecutionException {
        Map<String, Entry> result = new LinkedHashMap();
        String[] packs = pack.split("\\" + split);
        try{
            //自动识别id的类型,和数据库尽量保持一致, 避免影响查询效率
            Object id = null;
            if(dataSource.getRootId().matches("\\d+")) {
                id = Long.parseLong(dataSource.getRootId());
            }else{
                id = dataSource.getRootId();
            }
            Map<Object, Byte> temp = new HashMap();
            for(String item : packs) {
                if(temp.containsKey(id)) { //避免反复获取同一ID
                    break;
                }else{
                    temp.put(id, null);
                }
                if((id = queryEntryList(id, item, result)) == null) {
                    break;
                }
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new MojoExecutionException(e.getMessage(), e);
        }

        return result.values();
    }

    private Object queryEntryList(Object id, String pack, Map<String, Entry> result) throws SQLException {
        id = template.getPackId(dataSource.getSqlGetIdByPidAndPack(), id, pack);
        if(id == null) {
            return null;
        }

        List<Entry> list = template.queryForList(dataSource.getSqlGetPropertiesByPid(), id);
        for(Entry entry : list) { //后来的会覆盖已存在的
            result.put(entry.getId().toString(), entry);
        }
        return id;
    }

}
