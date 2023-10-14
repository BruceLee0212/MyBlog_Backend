package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.BlogCommentMapper;
import com.site.blog.my.core.entity.BlogComment;
import com.site.blog.my.core.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Override
    public Boolean addComment(BlogComment blogComment) {
        return blogCommentMapper.insertSelective(blogComment) > 0;
    }

    @Override
    public int getTotalComments(String commentOwner) {
        return blogCommentMapper.getTotalBlogComments(commentOwner);
    }

    @Override
    public Boolean checkDone(String commentOwner, Long[] ids) {
        return blogCommentMapper.checkDone(commentOwner, ids) > 0;
    }

    @Override
    public Boolean deleteBatch(String commentOwner, Long[] ids) {
        return blogCommentMapper.deleteBatch(commentOwner, ids) > 0;
    }

    @Override
    public Boolean addReply(Long commentId, String reply) {
        BlogComment blogComment = blogCommentMapper.selectByPrimaryKey(commentId);
        if(blogComment != null){
            blogComment.setCommentStatus((byte)1);
            blogComment.setReplyBody(reply);
            blogComment.setReplyCreateTime(new Date());
            blogCommentMapper.updateByPrimaryKeySelective(blogComment);
            return true;
        }
        return false;
    }

    @Override
    public List<BlogComment> getAllCommentForOneBlog(Long blogId) {
        return blogCommentMapper.findBlogCommentForOneBlog(blogId);
    }

    @Override
    public List<BlogComment> getCommentList(String commentOwner) {
        return blogCommentMapper.findBlogCommentList(commentOwner);
    }

}
