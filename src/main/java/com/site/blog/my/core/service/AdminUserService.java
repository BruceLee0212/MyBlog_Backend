package com.site.blog.my.core.service;

import com.site.blog.my.core.entity.AdminUser;

public interface AdminUserService {

    AdminUser login(String account, String password);

    /**
     * get user info
     */
    AdminUser getUserDetailById(String account);

    /**
     * update current user's password
     */
    Boolean updatePassword(String account, String originalPassword, String newPassword);

    /**
     * update current user's name
     */
    Boolean updateName(String account, String fName, String lName, String pName);

    /**
     * register a new account
     */
    String register(AdminUser user);
    /**
     * get username by id
     */
    String getUserNameById(String account);
    /**
     * update user information
     */
    int updateUser(AdminUser adminUser);

}
