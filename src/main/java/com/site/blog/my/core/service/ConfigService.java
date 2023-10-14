package com.site.blog.my.core.service;

import com.site.blog.my.core.entity.BlogConfig;

import java.util.Map;

public interface ConfigService {
    /**
     * update the config
     */
    int updateConfig(String configOwner, String configName, String configValue);

    /**
     * get all configs by blogger id
     */
    Map<String,String> getAllConfigs(String configOwner);
    /**
     * get footer config
     */
    Map<String, String> getFooterConfigs(String configOwner);
    /**
     * get site config
     */
    Map<String, String> getSiteConfigs(String configOwner);
    /**
     * get user's config by config name
     */
    String getConfigByName(String configOwner, String configName);
    /**
     * get config by config name and config value
     */
    BlogConfig getConfigByNameAndValue(String configName, String configValue);
    /**
     * insert a new config into database
     */
    void insert(BlogConfig config);
}
