package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.BlogLinkMapper;
import com.site.blog.my.core.entity.BlogLink;
import com.site.blog.my.core.service.LinkService;
import com.site.blog.my.core.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private BlogLinkMapper blogLinkMapper;

    @Override
    public List<BlogLink> getBlogLinks(String linkOwner) {
        return blogLinkMapper.getLinkList(linkOwner);
    }

    @Override
    public int getTotalLinks(String linkOwner) {
        return blogLinkMapper.getTotalLinks(linkOwner);
    }

    @Override
    public Boolean saveLink(String linkOwner, Integer linkType, String linkName, String linkUrl, String linkDescription, Integer linkRank, Date createTime) {
        BlogLink link = new BlogLink();
        link.setLinkId(IDGenerator.generateID());
        link.setLinkOwner(linkOwner);
        link.setLinkType(linkType.byteValue());
        link.setLinkRank(linkRank);
        link.setLinkName(linkName);
        link.setLinkUrl(linkUrl);
        link.setCreateTime(createTime);
        link.setLinkDescription(linkDescription);
        link.setIsDeleted((byte)0);
        return blogLinkMapper.insertSelective(link) > 0;
    }

    @Override
    public BlogLink selectById(Integer id) {
        return blogLinkMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateLink(BlogLink tempLink) {
        return blogLinkMapper.updateByPrimaryKeySelective(tempLink) > 0;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids, String linkOwner) {
        return blogLinkMapper.deleteBatch(ids, linkOwner) > 0;
    }

    @Override
    public Map<Byte, List<BlogLink>> getLinksByType(String linkOwner) {
        //get all links
        List<BlogLink> links = blogLinkMapper.getLinkList(linkOwner);
        if (!CollectionUtils.isEmpty(links)) {
            return links.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));
        }
        return null;
    }
}
