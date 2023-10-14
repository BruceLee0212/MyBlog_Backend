package com.site.blog.my.core.service;

import com.site.blog.my.core.entity.BlogLink;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LinkService {

    /**
     * get all links
     */
    List<BlogLink> getBlogLinks(String linkOwner);

    /**
     * get the number of total links
     */
    int getTotalLinks(String linkOwner);

    /**
     * save a new link to database
     */
    Boolean saveLink(String linkOwner, Integer linkType, String linkName, String linkUrl, String linkDescription, Integer linkRank, Date createTime);

    /**
     * select link by its id
     */
    BlogLink selectById(Integer id);

    /**
     * update a link in database
     */
    Boolean updateLink(BlogLink tempLink);

    /**
     * delete a batch of links
     */
    Boolean deleteBatch(Integer[] ids, String linkOwner);

    /**
     * get all links for link page
     */
    Map<Byte, List<BlogLink>> getLinksByType(String linkOwner);
}
