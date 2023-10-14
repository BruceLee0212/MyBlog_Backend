package com.site.blog.my.core.service;

import com.site.blog.my.core.entity.BlogTag;

import java.util.Date;
import java.util.List;

public interface TagService {

    /**
     * get all tags
     */
    List<BlogTag> getBlogTags(String tagOwner);
    /**
     * get the number of tags
     */
    int getTotalTags(String tagOwner);
    /**
     * save a new tag to database
     */
    Boolean saveTag(String tagName, String tagOwner, Date createTime);

    /**
     * delete a batch of tags
     */
    List<Integer> deleteBatch(Integer[] ids, String tagOwner);

    /**
     * get a tag by Id
     */
    BlogTag getTagById(Integer id);
}
