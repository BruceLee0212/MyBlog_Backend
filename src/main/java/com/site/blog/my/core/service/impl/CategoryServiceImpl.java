package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.BlogCategoryMapper;
import com.site.blog.my.core.dao.BlogMapper;
import com.site.blog.my.core.entity.BlogCategory;
import com.site.blog.my.core.service.CategoryService;
import com.site.blog.my.core.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private BlogCategoryMapper blogCategoryMapper;
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<BlogCategory> getBlogCategories(String categoryOwner) {
        return blogCategoryMapper.getCategoryList(categoryOwner);
    }


    @Override
    public int getTotalCategories(String categoryOwner) {
        return blogCategoryMapper.getTotalCategories(categoryOwner);
    }

    @Override
    public Boolean saveCategory(String categoryName, String categoryIcon, String categoryOwner, Date createTime) {
        BlogCategory temp = blogCategoryMapper.selectByCategoryName(categoryName, categoryOwner);
        if (temp == null) {
            BlogCategory blogCategory = new BlogCategory();
            blogCategory.setCategoryId(IDGenerator.generateID());
            blogCategory.setCategoryOwner(categoryOwner);
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            blogCategory.setIsDeleted((byte)0);
            blogCategory.setCategoryRank(0);
            blogCategory.setCreateTime(createTime);
            return blogCategoryMapper.insertSelective(blogCategory) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        BlogCategory blogCategory = blogCategoryMapper.selectByPrimaryKey(categoryId);
        if (blogCategory != null) {
            blogCategory.setCategoryIcon(categoryIcon);
            blogCategory.setCategoryName(categoryName);
            blogMapper.updateBlogCategories(categoryName, blogCategory.getCategoryId(), new Integer[]{categoryId});
            return blogCategoryMapper.updateByPrimaryKeySelective(blogCategory) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean deleteBatch(Integer[] ids, String categoryOwner) {
        blogMapper.updateBlogCategories("Default Category", 0, ids);
        return blogCategoryMapper.deleteBatch(ids) > 0;
    }

    @Override
    public BlogCategory selectById(Integer id) {
        return blogCategoryMapper.selectByPrimaryKey(id);
    }
}
