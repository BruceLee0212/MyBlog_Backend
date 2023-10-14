package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.BlogTagMapper;
import com.site.blog.my.core.dao.BlogTagRelationMapper;
import com.site.blog.my.core.entity.BlogTag;
import com.site.blog.my.core.service.TagService;
import com.site.blog.my.core.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private BlogTagMapper blogTagMapper;
    @Autowired
    private BlogTagRelationMapper relationMapper;

    @Override
    public List<BlogTag> getBlogTags(String tagOwner) {
        return blogTagMapper.getTagList(tagOwner);
    }

    @Override
    public int getTotalTags(String tagOwner) {
        return blogTagMapper.getTotalTags(tagOwner);
    }

    @Override
    public Boolean saveTag(String tagName, String tagOwner, Date createTime) {
        BlogTag temp = blogTagMapper.selectByTagName(tagName, tagOwner);
        if (temp == null) {
            BlogTag blogTag = new BlogTag();
            blogTag.setTagName(tagName);
            blogTag.setIsDeleted((byte)0);
            blogTag.setTagOwner(tagOwner);
            blogTag.setTagId(IDGenerator.generateID());
            blogTag.setCreateTime(createTime);
            return blogTagMapper.insertSelective(blogTag) > 0;
        }
        return false;
    }

    @Override
    public List<Integer> deleteBatch(Integer[] ids, String tagOwner) {
        //do not delete tags have blogs related to them
        List<Integer> relations = relationMapper.selectDistinctTagIds(ids, tagOwner);
        if (!CollectionUtils.isEmpty(relations)) {
            return relations;
        }
        //delete tag
        if(blogTagMapper.deleteBatch(ids, tagOwner) > 0){
            return new ArrayList<>();
        }
        else{
            List<Integer> res = new ArrayList<>();
            res.add(-1);
            return res;
        }
    }

    @Override
    public BlogTag getTagById(Integer id) {
        return blogTagMapper.selectByPrimaryKey(id);
    }

}
