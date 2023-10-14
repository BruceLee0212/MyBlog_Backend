package com.site.blog.my.core.dao;

import com.site.blog.my.core.entity.BlogConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BlogConfigMapper {
    List<BlogConfig> selectAll(String configOwner);

    BlogConfig selectByConfigName(String configOwner, String configName);

    int updateByPrimaryKeySelective(BlogConfig record);

    int insert(BlogConfig record);

    BlogConfig selectByConfigNameAndValue(String configName, String configValue);

}