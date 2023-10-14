package com.site.blog.my.core.dao;

import com.site.blog.my.core.entity.BlogLink;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BlogLinkMapper {
    int deleteByPrimaryKey(Integer linkId);

    int insert(BlogLink record);

    int insertSelective(BlogLink record);

    BlogLink selectByPrimaryKey(Integer linkId);

    int updateByPrimaryKeySelective(BlogLink record);

    int updateByPrimaryKey(BlogLink record);

    List<BlogLink> getLinkList(String linkOwner);

    int getTotalLinks(String linkOwner);

    int deleteBatch(Integer[] ids, String linkOwner);
}