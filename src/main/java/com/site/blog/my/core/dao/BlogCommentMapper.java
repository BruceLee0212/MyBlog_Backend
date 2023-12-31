package com.site.blog.my.core.dao;

import com.site.blog.my.core.entity.BlogComment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface BlogCommentMapper {
    int deleteByPrimaryKey(Long commentId);

    int insert(BlogComment record);

    int insertSelective(BlogComment record);

    BlogComment selectByPrimaryKey(Long commentId);

    int updateByPrimaryKeySelective(BlogComment record);

    int updateByPrimaryKey(BlogComment record);

    List<BlogComment> findBlogCommentList(String commentOwner);

    List<BlogComment> findBlogCommentForOneBlog(Long blogId);

    int getTotalBlogComments(String commentOwner);

    int checkDone(String commentOwner, Long[] ids);

    int deleteBatch(String commentOwner, Long[] ids);
}