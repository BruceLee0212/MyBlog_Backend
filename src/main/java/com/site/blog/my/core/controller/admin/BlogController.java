package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.entity.Blog;
import com.site.blog.my.core.entity.BlogCategory;
import com.site.blog.my.core.entity.BlogTag;
import com.site.blog.my.core.service.BlogService;
import com.site.blog.my.core.service.CategoryService;
import com.site.blog.my.core.service.FileStoreService;
import com.site.blog.my.core.service.TagService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.http.entity.ContentType.*;

@Controller
@CrossOrigin
public class BlogController {

    @Resource
    private BlogService blogService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private TagService tagService;
    @Resource
    private FileStoreService fileStoreService;
    private static final List<String> defaultCoverPath = new ArrayList<>(Arrays.asList("default-resources/blog-covers/1.jpg",
                                                                                       "default-resources/blog-covers/2.jpg",
                                                                                       "default-resources/blog-covers/3.jpg",
                                                                                       "default-resources/blog-covers/4.jpg",
                                                                                       "default-resources/blog-covers/5.jpg"));
    Pattern pattern = Pattern.compile("\\d+");
    @GetMapping("/blogs")
    public ResponseEntity<Object> blogPage(@RequestParam("blogOwner") String blogOwner) {
        List<Blog> blogList = blogService.getBlogs(blogOwner);
        JSONObject response = new JSONObject();
        response.put("blogList", blogList);
        return ResponseEntity.ok(response.toString());
    }

    @GetMapping("/new-blog")
    public ResponseEntity<Map<String, Object>> newBlogPage(@RequestParam("blogOwner") String blogOwner,
                                                           @RequestParam(name = "originalBlogCover", required = false) String originalBlogCover) {
        Map<String, Object> responseMap = new HashMap<>();
        List<BlogCategory> blogCategoryList = categoryService.getBlogCategories(blogOwner);
        List<BlogTag> blogTagList = tagService.getBlogTags(blogOwner);
        List<byte[]> defaultCovers = fileStoreService.downloadMultipleFiles(defaultCoverPath);
        byte[] blogCover = defaultCovers.get(0);
        if(originalBlogCover != null){
            if(originalBlogCover.split("/")[0].equals("default-resources")){
                Matcher matcher = pattern.matcher(originalBlogCover);
                if(matcher.find()){
                    int index = Integer.parseInt(matcher.group()) - 1;
                    blogCover = defaultCovers.get(index);
                    responseMap.put("blogCoverSource", index);
                }
            }
            else{
                blogCover =  fileStoreService.downloadFile(originalBlogCover);
                responseMap.put("blogCoverSource", 5);
            }
            responseMap.put("originalBlogCover", blogCover);
        }
        responseMap.put("blogCategoryList", blogCategoryList);
        responseMap.put("blogTagList", blogTagList);
        responseMap.put("defaultCovers",defaultCovers);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping(
            path = "/blogs/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
            )
    @ResponseBody
    public ResponseEntity<Map<String, Object>> save(@RequestParam("blogOwner") String blogOwner,
                                                    @RequestParam(name = "blogId", required = false) Long blogId,
                                                    @RequestParam(name = "blogTitle", required = false) String blogTitle,
                                                    @RequestParam(name = "blogTags", required = false) String blogTags,
                                                    @RequestParam(name = "blogUrl", required = false) String blogSubUrl,
                                                    @RequestParam(name = "blogCategoryId", required = false) String blogCategoryId,
                                                    @RequestParam(name = "blogCategoryName", required = false) String blogCategoryName,
                                                    @RequestParam(name = "blogContent", required = false) String blogContent,
                                                    @RequestParam(name = "blogCover", required = false) MultipartFile blogCover,
                                                    @RequestParam(name = "blogCoverSource", required = false) Integer blogCoverSource,
                                                    @RequestParam(name = "blogStatus", required = false) Byte blogStatus,
                                                    @RequestParam(name = "enableComment", required = false) Byte enableComment) {
        if(blogId == null){
            blogId = (long)IDGenerator.generateID();
        }

        Map<String, Object> responseMap = new HashMap<>();
        List<String> errorList = new ArrayList<>();
        if(blogStatus == null){
            errorList.add("Blog status can not be null");
            responseMap.put("status", "failed");
            responseMap.put("errorList", errorList);
            return ResponseEntity.ok(responseMap);
        }
        Blog blog = blogService.getBlogById(blogId);
        int updateFlag = 0;
        if(blog == null) {
            blog = new Blog();
        }
        else{
            updateFlag = 1;
        }
        if(blogStatus == 0){
            blog.setBlogId(blogId);
            blog.setBlogOwner(blogOwner);
            if (StringUtils.hasText(blogTitle)) blog.setBlogTitle(blogTitle);
            if (StringUtils.hasText(blogTags)) blog.setBlogTags(blogTags);
            if (StringUtils.hasText(blogSubUrl)) blog.setBlogSubUrl(blogSubUrl);
            if (!blogCategoryId.equals("undefined")) blog.setBlogCategoryId(Integer.parseInt(blogCategoryId));
            if (StringUtils.hasText(blogCategoryName)) blog.setBlogCategoryName(blogCategoryName);
            if (StringUtils.hasText(blogContent)) blog.setBlogContent(blogContent);
            if(blogCoverSource != null){
                if(blogCoverSource < 5 && blogCoverSource >= 0){
                    blog.setBlogCoverImage(defaultCoverPath.get(blogCoverSource));
                } else if (blogCoverSource == 6) {
                    if (blogCover != null) blog.setBlogCoverImage(saveImage(blogOwner, blogCover, errorList));
                }
            }

            if(errorList.size() > 0){
                responseMap.put("status", "failed");
                responseMap.put("errorList", errorList);
                return ResponseEntity.ok(responseMap);
            }
            blog.setBlogStatus(blogStatus);
            if(enableComment != null) blog.setEnableComment(enableComment);
        }
        else{
            // input validation check
            if (!StringUtils.hasText(blogTitle)) {
                errorList.add("Blog title can not be null");
                responseMap.put("status", "failed");
            }
            if (blogTitle.trim().length() > 150) {
                errorList.add("Blog title should be less than 150 words");
                if(!responseMap.containsKey("status")) responseMap.put("status", "failed");
            }
            if (!StringUtils.hasText(blogTags)) {
                errorList.add("Blog tags can not be null");
                if(!responseMap.containsKey("status")) responseMap.put("status", "failed");
            }
            if (blogTags.trim().length() > 150) {
                errorList.add("Tag title should be less than 150 words");
                if(!responseMap.containsKey("status")) responseMap.put("status", "failed");
            }
            if (blogSubUrl.trim().length() > 150) {
                errorList.add("Url should be less than 150");
                if(!responseMap.containsKey("status")) responseMap.put("status", "failed");
            }
            if (!StringUtils.hasText(blogContent)) {
                errorList.add("Blog content can not be null");
                if(!responseMap.containsKey("status")) responseMap.put("status", "failed");
            }
            if (blogTags.trim().length() > 100000) {
                errorList.add("Blog content should be less than 100,000 words");
                if(!responseMap.containsKey("status")) responseMap.put("status", "failed");
            }
            //check if the file is empty
            String path="";
            if(blogCoverSource == 6){
                if (blogCover == null || blogCover.isEmpty()) {
                    errorList.add("Blog cover cannot be null");
                    if(!responseMap.containsKey("status")) responseMap.put("status", "failed");
                }
                else {
                    path = saveImage(blogOwner, blogCover, errorList);
                }
            } else if (blogCoverSource >= 0 && blogCoverSource < 5) {
                path = defaultCoverPath.get(blogCoverSource);
            } else {
                path = blog.getBlogCoverImage();
            }
            if(responseMap.containsKey("status") && responseMap.get("status").equals("failed")){
                responseMap.put("errorList", errorList);
                return ResponseEntity.ok(responseMap);
            }
            //create the blog entity
            blog.setBlogOwner(blogOwner);
            blog.setBlogId(blogId);
            blog.setBlogTitle(blogTitle);
            blog.setBlogSubUrl(blogSubUrl);
            blog.setBlogCategoryId(Integer.parseInt(blogCategoryId));
            blog.setBlogCategoryName(blogCategoryName);
            blog.setBlogTags(blogTags);
            blog.setBlogContent(blogContent);
            blog.setBlogCoverImage(path);
            blog.setBlogStatus(blogStatus);
            blog.setEnableComment(enableComment);
        }
        String result = "";
        if(updateFlag == 0){
            result = blogService.saveBlog(blog);
        }
        else{
            result = blogService.updateBlog(blog);
        }

        if ("success".equals(result)) {
            responseMap.put("status", "succeeded");
        } else {
            if(!responseMap.containsKey("status")) responseMap.put("status", "failed");
            responseMap.put("database_error", result);
        }
        responseMap.put("errorList", errorList);
        return ResponseEntity.ok(responseMap);
    }

    public String saveImage(String blogOwner, MultipartFile blogCover, List<String> errorList){
        //Check if the file is an image
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(blogCover.getContentType())) {
            errorList.add( "blog cover can only be image");
            return "";
        }
        //Save Image in S3
        String coverId = String.format("%s",IDGenerator.generateID());
        String path = String.format("%s/%s/%s", blogOwner,"blog-covers", coverId);
        fileStoreService.uploadFile(path,blogCover);
        return path;
    }

    @PostMapping("/blogs/delete")
    @ResponseBody
    public ResponseEntity<Object> delete(@RequestParam("blogOwner") String blogOwner,
                                         @RequestParam("ids") Integer[] ids) {
        JSONObject response = new JSONObject();
        if (ids.length < 1) {
            response.put("status", "failed");
            response.put("errorMsg", "Selected at least 1 row");
            return ResponseEntity.ok(response.toString());
        }
        if (blogService.deleteBatch(blogOwner, ids)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("errorMsg", "Database Error");
        }
        return ResponseEntity.ok(response.toString());
    }

}
