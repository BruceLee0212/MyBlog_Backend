package com.site.blog.my.core.dao;

import com.site.blog.my.core.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface AdminUserMapper {
    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser login(@Param("account") String account, @Param("password") String password);

    AdminUser selectByPrimaryKey(String account);

    int updateByPrimaryKey(AdminUser record);

    int delete(AdminUser record);
}