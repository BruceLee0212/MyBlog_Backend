package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.entity.AdminUser;
import com.site.blog.my.core.service.*;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Controller
@CrossOrigin
public class AdminController {

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private FileStoreService fileStoreService;
    @Resource ConfigService configService;
    @Resource
    private BlogService blogService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LinkService linkService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> profile(@RequestParam("user") String user) {
        AdminUser adminUser = adminUserService.getUserDetailById(user);
        Map<String, Object> responseMap = new HashMap<>();
        if (adminUser == null) {
            responseMap.put("status", "failed");
            responseMap.put("message", "Can not find your information, log in again");
            return ResponseEntity.ok(responseMap);
        }
        responseMap.put("status", "succeeded");
        responseMap.put("firstName", adminUser.getFName());
        responseMap.put("lastName", adminUser.getLName());
        responseMap.put("preferredName", adminUser.getPName());
        String path = configService.getConfigByName(user, "profilePic");
        if(path.length() > 0){
            byte[] profilePic = fileStoreService.downloadFile(path);
            responseMap.put("profilePic", profilePic);
        }
        String iconPath = configService.getConfigByName(user, "websiteIcon");
        if(iconPath.length() > 0){
            byte[] siteIcon = fileStoreService.downloadFile(iconPath);
            responseMap.put("websiteIcon", siteIcon);
        }
        responseMap.put("blogUrl", configService.getConfigByName(user, "websiteUrl"));
        responseMap.put("categoryCount", categoryService.getTotalCategories(user));
        responseMap.put("blogCount", blogService.getTotalBlogs(user));
        responseMap.put("linkCount", linkService.getTotalLinks(user));
        responseMap.put("tagCount", tagService.getTotalTags(user));
        responseMap.put("commentCount", commentService.getTotalComments(user));
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(@RequestParam("user") String user) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("categoryCount", categoryService.getTotalCategories(user));
        responseMap.put("blogCount", blogService.getTotalBlogs(user));
        responseMap.put("linkCount", linkService.getTotalLinks(user));
        responseMap.put("tagCount", tagService.getTotalTags(user));
        responseMap.put("commentCount", commentService.getTotalComments(user));
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public ResponseEntity<Object> passwordUpdate(@RequestParam("user") String user,
                                         @RequestParam("originalPassword") String originalPassword,
                                         @RequestParam("newPassword") String newPassword) {
        JSONObject response = new JSONObject();
        if (!StringUtils.hasText(originalPassword)) {
            response.put("originalPwdError", "Original Password can not be null");
            response.put("status", "failed");
        }
        if (!StringUtils.hasText(newPassword)) {
            response.put("newPwdError", "New Password can not be null");
            if(!response.has("status")) response.put("status", "failed");
        }
        if(response.has("status")) return ResponseEntity.ok(response.toString());
        if (adminUserService.updatePassword(user, originalPassword, newPassword)) {
            response.put("status", "succeeded");
            response.put("message", "Password has been updated successfully");
        } else {
            response.put("status", "failed");
            response.put("message", "Invalid original password");
        }
        return ResponseEntity.ok(response.toString());
    }
}
