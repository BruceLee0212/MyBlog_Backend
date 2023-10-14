package com.site.blog.my.core.service;

import com.site.blog.my.core.entity.BlogCategory;

import java.util.Date;
import java.util.List;

public interface CategoryService {

    /**
     * get the configuration of category page
     */
    List<BlogCategory> getBlogCategories(String categoryOwner);

    /**
     * get the number of categories
     */
    int getTotalCategories(String categoryOwner);

    /**
     * push the new category to database
     */
    Boolean saveCategory(String categoryName,String categoryIcon, String categoryOwner, Date createTime);

    /**
     * update the category
     */
    Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon);

    /**
     * delete a batch of categories
     */
    Boolean deleteBatch(Integer[] ids, String categoryOwner);

    /**
     * select a blog category by id and owner
     */
    BlogCategory selectById(Integer id);

}
