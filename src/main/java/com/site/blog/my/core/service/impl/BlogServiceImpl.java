package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.*;
import com.site.blog.my.core.entity.Blog;
import com.site.blog.my.core.entity.BlogCategory;
import com.site.blog.my.core.entity.BlogTag;
import com.site.blog.my.core.entity.BlogTagRelation;
import com.site.blog.my.core.service.BlogService;
import com.site.blog.my.core.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogCategoryMapper categoryMapper;
    @Autowired
    private BlogTagMapper tagMapper;
    @Autowired
    private BlogTagRelationMapper blogTagRelationMapper;
    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        String owner = blog.getBlogOwner();
        //handle tags
        String getTagsFromBlog = blog.getBlogTags();
        String[] tags = new String[0];
        if(getTagsFromBlog != null){
            tags = getTagsFromBlog.split(",");
        }

        if (tags.length > 6) {
            return "The limitation of tags is 6";
        }
        //save the blog into database
        if (blogMapper.insertSelective(blog) > 0) {
            if(blog.getBlogCategoryId() != null){
                // update category rank
                BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
                blogCategory.setCategoryRank(blogCategory.getCategoryRank()+1);
                categoryMapper.updateByPrimaryKeySelective(blogCategory);
            }
            else {
                BlogCategory blogCategory = categoryMapper.selectByCategoryName("Default", blog.getBlogOwner());
                blogCategory.setCategoryRank(blogCategory.getCategoryRank()+1);
                categoryMapper.updateByPrimaryKeySelective(blogCategory);
                blog.setBlogCategoryName(blogCategory.getCategoryName());
                blog.setBlogCategoryId(blogCategory.getCategoryId());
            }
            if(tags.length > 0){
                //create tag list
                List<BlogTag> tagListForInsert = new ArrayList<>();
                //list for all tags
                List<BlogTag> allTagsList = new ArrayList<>();
                for (String s : tags) {
                    BlogTag tag = tagMapper.selectByTagName(s, owner);
                    if (tag == null) {
                        //insert when tags do not exist
                        BlogTag tempTag = new BlogTag();
                        tempTag.setTagName(s);
                        tempTag.setTagId(IDGenerator.generateID());
                        tempTag.setTagOwner(owner);
                        tempTag.setCreateTime(new Date());
                        tempTag.setIsDeleted((byte)0);
                        tagListForInsert.add(tempTag);
                    } else {
                        allTagsList.add(tag);
                    }
                }
                //update into database
                if (!CollectionUtils.isEmpty(tagListForInsert)) {
                    tagMapper.batchInsertBlogTag(tagListForInsert);
                }
                List<BlogTagRelation> blogTagRelations = new ArrayList<>();
                //add blog&tag relation into database
                allTagsList.addAll(tagListForInsert);
                for (BlogTag tag : allTagsList) {
                    BlogTagRelation blogTagRelation = new BlogTagRelation();
                    blogTagRelation.setRelationOwner(owner);
                    blogTagRelation.setRelationId((long)IDGenerator.generateID());
                    blogTagRelation.setBlogId(blog.getBlogId());
                    blogTagRelation.setTagId(tag.getTagId());
                    blogTagRelation.setCreateTime(new Date());
                    blogTagRelations.add(blogTagRelation);
                }
                if (blogTagRelationMapper.batchInsert(blogTagRelations) > 0) {
                    return "success";
                }
            }
            return "success";
        }
        return "failed";
    }

    @Override
    @Transactional
    public String updateBlog(Blog blog) {
        blog.setUpdateTime(new Date());
        String owner = blog.getBlogOwner();
        Blog originalBlog = blogMapper.selectByPrimaryKey(blog.getBlogId());
        if(blog.getBlogCategoryId() != null){
            // update category info
            BlogCategory newBlogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
            BlogCategory originalBlogCategory = categoryMapper.selectByPrimaryKey(originalBlog.getBlogCategoryId());
            if(!originalBlogCategory.getCategoryId().equals(newBlogCategory.getCategoryId())){
                originalBlogCategory.setCategoryRank(originalBlogCategory.getCategoryRank() - 1);
                newBlogCategory.setCategoryRank(newBlogCategory.getCategoryRank() + 1);
                categoryMapper.updateByPrimaryKeySelective(originalBlogCategory);
                categoryMapper.updateByPrimaryKeySelective(newBlogCategory);
            }
        }else{
            BlogCategory blogCategory = categoryMapper.selectByCategoryName("Default", blog.getBlogOwner());
            blogCategory.setCategoryRank(blogCategory.getCategoryRank()+1);
            categoryMapper.updateByPrimaryKeySelective(blogCategory);
            blog.setBlogCategoryName(blogCategory.getCategoryName());
            blog.setBlogCategoryId(blogCategory.getCategoryId());
        }
        // update tag info
        String getTagsFromBlog = blog.getBlogTags();
        if(getTagsFromBlog != null){
            String[] tags = getTagsFromBlog.split(",");
            if (tags.length > 6) {
                return "You can not add more than 6 tags";
            }
            //create new tag list for insert
            List<BlogTag> tagListForInsert = new ArrayList<>();
            //list for all tags
            List<BlogTag> allTagsList = new ArrayList<>();
            for (String s : tags) {
                BlogTag tag = tagMapper.selectByTagName(s, owner);
                if (tag == null) {
                    //insert when tags do not exist
                    BlogTag tempTag = new BlogTag();
                    tempTag.setTagName(s);
                    tempTag.setTagId(IDGenerator.generateID());
                    tempTag.setTagOwner(owner);
                    tempTag.setCreateTime(new Date());
                    tempTag.setIsDeleted((byte)0);
                    tagListForInsert.add(tempTag);
                } else {
                    allTagsList.add(tag);
                }
            }
            //update new tags into database
            if (!CollectionUtils.isEmpty(tagListForInsert)) {
                tagMapper.batchInsertBlogTag(tagListForInsert);
            }
            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            // add new tags into allTagsList
            allTagsList.addAll(tagListForInsert);
            for (BlogTag tag : allTagsList) {
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                blogTagRelation.setRelationOwner(owner);
                blogTagRelation.setRelationId((long)IDGenerator.generateID());
                blogTagRelation.setBlogId(blog.getBlogId());
                blogTagRelation.setTagId(tag.getTagId());
                blogTagRelation.setCreateTime(new Date());
                blogTagRelations.add(blogTagRelation);
            }
            // delete original tag blog relations and add new relations
            blogTagRelationMapper.deleteByBlogId(originalBlog.getBlogId(), owner);
            blogTagRelationMapper.batchInsert(blogTagRelations);
        }
        if (blogMapper.updateByPrimaryKeySelective(blog) > 0) {
            return "success";
        }
        return "Update failed";
    }

    @Override
    public List<Blog> getBlogs(String blogOwner) {
        return blogMapper.findBlogListWithContent(blogOwner, null);
    }

    @Override
    public List<Blog> getBlogsWithoutContent(String blogOwner) {
        return blogMapper.findBlogList(blogOwner, null);
    }

    @Override
    public Boolean deleteBatch(String blogOwner, Integer[] ids) {
        return blogMapper.deleteBatch(blogOwner, ids) > 0;
    }

    @Override
    public int getTotalBlogs(String blogOwner) {
        return blogMapper.getTotalBlogs(blogOwner);
    }

    @Override
    public Blog getBlogById(Long blogId) {
        return blogMapper.selectByPrimaryKey(blogId);
    }

    @Override
    public Blog getBlogDetailBySubUrl(String blogOwner, String subUrl) {
        return blogMapper.selectBySubUrl(blogOwner, subUrl);
    }

    @Override
    public String getOwnerById(Long blogId) {
        return blogMapper.getBlogOwnerById(blogId);
    }

    @Override
    public Map<Long, String> getTitleByIds(long[] ids) {
        Map<Long, String> titleMap = new HashMap<>();
        for(long id : ids){
            titleMap.put(id, blogMapper.getBlogTitleById(id));
        }
        return titleMap;
    }

}
