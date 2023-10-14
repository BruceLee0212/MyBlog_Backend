package com.site.blog.my.core.dao;

import com.site.blog.my.core.entity.ConfirmationToken;
import org.springframework.stereotype.Component;

@Component
public interface TokenMapper {

    /**
     * select token by primary key
     */
    ConfirmationToken selectByPrimaryKey(Long tokenId);

    /**
     * insert a token to database
     */
    void saveToken(ConfirmationToken token);

    /**
     * delete a token by primary key
     */
    void deleteByPrimaryKey(Long tokenId);
}
