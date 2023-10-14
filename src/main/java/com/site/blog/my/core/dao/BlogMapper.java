package com.site.blog.my.core.dao;

import com.site.blog.my.core.entity.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BlogMapper {
    int deleteByPrimaryKey(Long blogId);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Long blogId);

    int updateByPrimaryKeySelective(Blog record);


    int updateByPrimaryKey(Blog record);

    List<Blog> findBlogList(String blogOwner, String keyword);

    List<Blog> findBlogListWithContent(String blogOwner, String keyword);


    int getTotalBlogs(String blogOwner);

    int deleteBatch(String blogOwner, Integer[] ids);

    Blog selectBySubUrl(String blogOwner, String subUrl);

    void updateBlogCategories(@Param("categoryName") String categoryName, @Param("categoryId") Integer categoryId, @Param("ids")Integer[] ids);

    String getBlogOwnerById(Long blogId);

    String getBlogTitleById(Long blogId);
}