package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.AdminUserMapper;
import com.site.blog.my.core.dao.BlogConfigMapper;
import com.site.blog.my.core.entity.AdminUser;
import com.site.blog.my.core.entity.BlogConfig;
import com.site.blog.my.core.service.ConfigService;
import com.site.blog.my.core.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private BlogConfigMapper configMapper;
    @Autowired
    private AdminUserMapper adminUserMapper;

    //default site info
    public static final String siteTitle = "Personal Blog";
    public static final String siteDescription = "Your Personal Blog";
    // default personal info
    public static final String profilePic = "default";

    // default footer info
    public static final String footerAbout = "your personal blog. have fun.";
    public static final String footerCopyRight = "Enter your copyright here";
    public static final String footerPoweredBy = "Enter your powered by here";
    public static final String footerPoweredByUrl = "Enter your powered by url here";

    @Override
    public int updateConfig(String configOwner, String configName, String configValue) {
        BlogConfig blogConfig = configMapper.selectByConfigName(configOwner, configName);
        if (blogConfig != null) {
            blogConfig.setConfigValue(configValue);
            return configMapper.updateByPrimaryKeySelective(blogConfig);
        }
        else{
            BlogConfig newBlogConfig = new BlogConfig();
            newBlogConfig.setConfigId(IDGenerator.generateID());
            newBlogConfig.setConfigOwner(configOwner);
            newBlogConfig.setConfigName(configName);
            newBlogConfig.setConfigValue(configValue);
            Date currentTime = new Date();
            newBlogConfig.setCreateTime(currentTime);
            return configMapper.insert(newBlogConfig);
        }
    }

    @Override
    public Map<String, String> getAllConfigs(String configOwner) {
        //get all config entries and make them a map
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(configOwner);
        List<BlogConfig> blogConfigs = configMapper.selectAll(configOwner);
        Map<String, String> configMap = blogConfigs.stream().collect(Collectors.toMap(BlogConfig::getConfigName, BlogConfig::getConfigValue));
        String fName = adminUser.getFName();
        String lName = adminUser.getLName();
        String pName = adminUser.getPName();
        configMap.putIfAbsent("siteTitle", siteTitle);
        configMap.putIfAbsent("siteDescription", siteDescription);
        configMap.putIfAbsent("siteSubUrl", pName.length() != 0 ? pName.toLowerCase() : fName.toLowerCase() + lName.toLowerCase());
        configMap.putIfAbsent("profilePic", profilePic);
        configMap.putIfAbsent("fName", fName);
        configMap.putIfAbsent("lName", lName);
        configMap.putIfAbsent("pName", pName);
        configMap.putIfAbsent("footerAbout", footerAbout);
        configMap.putIfAbsent("footerCopyRight", footerCopyRight);
        configMap.putIfAbsent("footerPoweredBy", footerPoweredBy);
        configMap.putIfAbsent("footerPoweredByUrl", footerPoweredByUrl);
        return configMap;
    }

    @Override
    public Map<String, String> getFooterConfigs(String configOwner) {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("footerAbout", configMapper.selectByConfigName(configOwner, "footerAbout").getConfigValue());
        configMap.put("footerCopyRight", configMapper.selectByConfigName(configOwner, "footerCopyRight").getConfigValue() );
        configMap.put("footerPoweredBy", configMapper.selectByConfigName(configOwner, "footerPoweredBy").getConfigValue() );
        configMap.put("footerPoweredByUrl", configMapper.selectByConfigName(configOwner, "footerPoweredByUrl").getConfigValue());
        return configMap;
    }

    @Override
    public Map<String, String> getSiteConfigs(String configOwner) {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("websiteTitle", configMapper.selectByConfigName(configOwner, "websiteTitle").getConfigValue());
        configMap.put("webSiteDescription", configMapper.selectByConfigName(configOwner, "websiteDescription").getConfigValue());
        configMap.put("websiteIcon", configMapper.selectByConfigName(configOwner, "websiteIcon").getConfigValue());
        return configMap;
    }

    @Override
    public Map<String, String> getWebsiteConfigs(String configOwner){
        Map<String, String> configMap = new HashMap<>();
        String github = configMapper.selectByConfigName(configOwner, "github").getConfigValue();
        String facebook = configMapper.selectByConfigName(configOwner, "facebook").getConfigValue();
        String x = configMapper.selectByConfigName(configOwner, "x").getConfigValue();
        String instagram = configMapper.selectByConfigName(configOwner, "instagram").getConfigValue();
        configMap.put("github", github);
        configMap.put("facebook", facebook);
        configMap.put("x", x);
        configMap.put("instagram", instagram);
        return configMap;
    }

    @Override
    public String getConfigByName(String configOwner, String configName) {
        BlogConfig blogConfig = configMapper.selectByConfigName(configOwner, configName);
        return blogConfig == null ? "" : blogConfig.getConfigValue();
    }

    @Override
    public BlogConfig getConfigByNameAndValue(String configName, String configValue) {
        return configMapper.selectByConfigNameAndValue(configName, configValue);
    }

    @Override
    public void insert(BlogConfig config) {
        configMapper.insert(config);
    }
}
