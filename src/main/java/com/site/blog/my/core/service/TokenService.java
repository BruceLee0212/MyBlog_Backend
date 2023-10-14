package com.site.blog.my.core.service;

import com.site.blog.my.core.entity.ConfirmationToken;

public interface TokenService {
    /**
     * save the token into database
     */
    void saveToken(ConfirmationToken token);
    /**
     * get token by id
     */
    ConfirmationToken getToken(Long tokenId);
    /**
     * delete a token by id
     */
    void deleteToken(Long tokenId);
}
