package com.site.blog.my.core.dao;

import com.site.blog.my.core.entity.BlogTag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BlogTagMapper {
    int deleteByPrimaryKey(Integer tagId);

    int insert(BlogTag record);

    int insertSelective(BlogTag record);

    BlogTag selectByPrimaryKey(Integer tagId);

    BlogTag selectByTagName(String tagName, String tagOwner);

    int updateByPrimaryKeySelective(BlogTag record);

    int updateByPrimaryKey(BlogTag record);

    List<BlogTag> getTagList(String tagOwner);

    int deleteBatch(Integer[] ids, String tagOwner);

    void batchInsertBlogTag(List<BlogTag> tagList);

    int getTotalTags(String tagOwner);
}