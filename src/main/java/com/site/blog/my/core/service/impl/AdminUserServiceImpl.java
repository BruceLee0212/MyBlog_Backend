package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.AdminUserMapper;
import com.site.blog.my.core.entity.AdminUser;
import com.site.blog.my.core.service.AdminUserService;
import com.site.blog.my.core.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String account, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminUserMapper.login(account, passwordMd5);
    }

    @Override
    public AdminUser getUserDetailById(String account) {
        return adminUserMapper.selectByPrimaryKey(account);
    }

    @Override
    public Boolean updatePassword(String account, String originalPassword, String newPassword) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(account);
        //check if the current user exists
        if (adminUser != null) {
            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            //check the original password
            if (originalPasswordMd5.equals(adminUser.getPassword())) {
                //set the new password
                adminUser.setPassword(newPasswordMd5);
                //update the new password in database
                return adminUserMapper.updateByPrimaryKey(adminUser) > 0;
            }
        }
        return false;
    }

    @Override
    public Boolean updateName(String account, String fName, String lName, String pName) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(account);
        //check if the current user exists
        if (adminUser != null) {
            //update info
            if(fName.length() != 0) adminUser.setFName(fName);
            if(lName.length() != 0) adminUser.setLName(lName);
            if(pName.length() != 0) adminUser.setPName(pName);
            //update personal info into database
            return adminUserMapper.updateByPrimaryKey(adminUser) > 0;
        }
        return false;
    }

    @Override
    public String register(AdminUser user){
        if(user != null){
            if(adminUserMapper.selectByPrimaryKey(user.getAccount()) != null){
                return "Email address has been registered";
            }
            else {
                if(adminUserMapper.insert(user) > 0) return "success";
                else return "Register failed, try later";
            }
        }
        else return "Register failed, try later";
    }

    @Override
    public String getUserNameById(String account) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(account);
        return adminUser.getPName().trim().length() > 0 ?
                adminUser.getPName() :
                adminUser.getFName() + " " + adminUser.getLName();
    }

    @Override
    public int updateUser(AdminUser adminUser) {
        return adminUserMapper.updateByPrimaryKey(adminUser);
    }
}
