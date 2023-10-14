package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.BlogMapper;
import com.site.blog.my.core.dao.TokenMapper;
import com.site.blog.my.core.entity.ConfirmationToken;
import com.site.blog.my.core.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenMapper tokenMapper;
    @Override
    public void saveToken(ConfirmationToken token) {
        tokenMapper.saveToken(token);
    }

    @Override
    public ConfirmationToken getToken(Long tokenId) {
        return tokenMapper.selectByPrimaryKey(tokenId);
    }

    @Override
    public void deleteToken(Long tokenId) {
        tokenMapper.deleteByPrimaryKey(tokenId);
    }
}
