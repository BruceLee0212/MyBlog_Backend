package com.site.blog.my.core.service;

import com.site.blog.my.core.entity.Blog;

import java.util.List;
import java.util.Map;

public interface BlogService {

    /**
     * save the blog into database
     */
    String saveBlog(Blog blog);

    /**
     * update the blog into database
     */
    String updateBlog(Blog blog);

    /**
     * get all blogs
     */
    List<Blog> getBlogs(String blogOwner);

    /**
     * get blog without content
     */
    List<Blog> getBlogsWithoutContent(String blogOwner);

    /**
     * delete a batch of blogs by their ids
     */
    Boolean deleteBatch(String blogOwner, Integer[] ids);

    /**
     * get the number of the blogs
     */
    int getTotalBlogs(String blogOwner);

    /**
     * get the blog detail by ID
     */
    Blog getBlogById(Long blogId);

    /**
     * get blog by url
     */
    Blog getBlogDetailBySubUrl(String blogOwner, String subUrl);

    /**
     * get blogOwner by blogId
     */
    String getOwnerById(Long blogId);

    /**
     * get blog title list by ids
     */
    Map<Long, String> getTitleByIds(long[] ids);

}
