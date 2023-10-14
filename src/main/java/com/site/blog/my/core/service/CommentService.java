package com.site.blog.my.core.service;

import com.site.blog.my.core.entity.BlogComment;

import java.util.List;

public interface CommentService {
    /**
     * add a comment for viewers
     */
    Boolean addComment(BlogComment blogComment);


    /**
     * get the number of all comments
     */
    int getTotalComments(String commentOwner);

    /**
     * check batch of comments
     */
    Boolean checkDone(String commentOwner, Long[] ids);

    /**
     * delete batch of comments
     */
    Boolean deleteBatch(String commentOwner, Long[] ids);

    /**
     * add a reply
     */
    Boolean addReply(Long commentId, String reply);

    /**
     * get all blog comments of one blog
     */
    List<BlogComment> getAllCommentForOneBlog(Long blogId);

    /**
     * get all blog comments for management
     */
    List<BlogComment> getCommentList(String commentOwner);

}
