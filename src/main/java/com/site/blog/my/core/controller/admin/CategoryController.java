package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.entity.BlogCategory;
import com.site.blog.my.core.service.CategoryService;
import com.site.blog.my.core.service.FileStoreService;
import com.site.blog.my.core.util.IDGenerator;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;

@Controller
@CrossOrigin
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    @Resource
    private FileStoreService fileStoreService;
    private static final List<String> defaultIconPath = new ArrayList<>(Arrays.asList("default-resources/category-icons/00.png",
                                                                                "default-resources/category-icons/01.png",
                                                                                "default-resources/category-icons/02.png",
                                                                                "default-resources/category-icons/03.png",
                                                                                "default-resources/category-icons/04.png",
                                                                                "default-resources/category-icons/05.png",
                                                                                "default-resources/category-icons/06.png",
                                                                                "default-resources/category-icons/07.png",
                                                                                "default-resources/category-icons/08.png",
                                                                                "default-resources/category-icons/09.png",
                                                                                "default-resources/category-icons/10.png",
                                                                                "default-resources/category-icons/11.png",
                                                                                "default-resources/category-icons/12.png",
                                                                                "default-resources/category-icons/13.png",
                                                                                "default-resources/category-icons/14.png",
                                                                                "default-resources/category-icons/15.png",
                                                                                "default-resources/category-icons/16.png",
                                                                                "default-resources/category-icons/camera.png"
                                                                                ));

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> categoryPage(@RequestParam("categoryOwner") String categoryOwner) {
        Map<String, Object> responseMap = new HashMap<>();
        List<BlogCategory> blogCategoryList = categoryService.getBlogCategories(categoryOwner);
        for(int i = blogCategoryList.size() - 1; i >= 0; --i){
            if(blogCategoryList.get(i).getCategoryName().equals("Default")){
                blogCategoryList.remove(i);
                break;
            }
        }
        List<byte[]> categoryDefaultIcons = fileStoreService.downloadMultipleFiles(defaultIconPath);
        List<byte[]> userIcons = new ArrayList<>();
        for(BlogCategory category : blogCategoryList){
            userIcons.add(fileStoreService.downloadFile(category.getCategoryIcon()));
        }
        responseMap.put("blogCategoryList", blogCategoryList);
        responseMap.put("defaultIcons", categoryDefaultIcons);
        responseMap.put("userIcons", userIcons);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping(
            path = "/categories/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<Object> save(@RequestParam("categoryName") String categoryName,
                                       @RequestParam("categoryOwner") String categoryOwner,
                                       @RequestParam(name = "categoryIcon", required = false) MultipartFile categoryIcon,
                                       @RequestParam("isDefault") Integer isDefault,
                                       @RequestParam("createTime") Date createTime) {
        JSONObject response = validationCheck(categoryOwner, categoryName, categoryIcon, isDefault);
        if(response.has("statue")) return ResponseEntity.ok(response.toString());
        //Save Image in S3
        String path = saveCategoryIcon(categoryOwner, categoryIcon, isDefault);
        if (categoryService.saveCategory(categoryName, path, categoryOwner, createTime)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("categoryNameError", "Category has existed");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping(
            path = "/categories/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<Object> update(@RequestParam("categoryId") String categoryId,
                                         @RequestParam("categoryOwner") String categoryOwner,
                                         @RequestParam("categoryName") String categoryName,
                                         @RequestParam(name = "categoryIcon", required = false) MultipartFile categoryIcon,
                                         @RequestParam("isDefault") Integer isDefault) {
        JSONObject response = validationCheck(categoryOwner, categoryName, categoryIcon, isDefault);
        if(response.has("statue")) return ResponseEntity.ok(response.toString());
        String path = saveCategoryIcon(categoryOwner, categoryIcon, isDefault);
        if (categoryService.updateCategory(Integer.valueOf(categoryId), categoryName, path)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("categoryNameError", "Category has existed");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/categories/delete")
    @ResponseBody
    public ResponseEntity<Object> delete(@RequestParam("ids") Integer[] ids,
                                         @RequestParam("categoryOwner") String categoryOwner) {
        JSONObject response = new JSONObject();
        if (ids.length < 1) {
            response.put("status", "failed");
            response.put("errorMsg", "Select at least 1 category");
            return ResponseEntity.ok(response.toString());
        }
        if (categoryService.deleteBatch(ids, categoryOwner)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("errorMsg", "DataBase Error");
        }
        return ResponseEntity.ok(response.toString());
    }

    public JSONObject validationCheck(String categoryOwner, String categoryName, MultipartFile categoryIcon, Integer isDefault){
        JSONObject response = new JSONObject();
        if (!StringUtils.hasText(categoryOwner)) {
            response.put("status", "failed");
            response.put("categoryOwnerError", "Login to add a new category");
            return response;
        }
        if (!StringUtils.hasText(categoryName)) {
            response.put("status", "failed");
            response.put("categoryNameError", "Name can not be null");
        }
        //check if the file is empty
        if (isDefault == -1 && categoryIcon.isEmpty()) {
            response.put("categoryIconError", "Category icon cannot be null");
            if (!response.has("status")) response.put("status", "failed");
        }
        //Check if the file is an image
        if (isDefault == -1 && !Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(categoryIcon.getContentType())) {
            response.put("categoryIconError", "Category icon can only be image");
            if (!response.has("status")) response.put("status", "failed");
        }
        return response;
    }

    public String saveCategoryIcon(String categoryOwner, MultipartFile categoryIcon, Integer isDefault){
        String path = "";
        if(isDefault == -1){
            String coverId = String.format("%s", IDGenerator.generateID());
            path = String.format("%s/%s/%s", categoryOwner, "category-icons", coverId);
            fileStoreService.uploadFile(path, categoryIcon);
        }
        else {
            path = defaultIconPath.get(isDefault);
        }
        return path;
    }
}
