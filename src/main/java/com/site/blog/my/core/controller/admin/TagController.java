package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.entity.BlogTag;
import com.site.blog.my.core.service.TagService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class TagController {
    @Resource
    private TagService tagService;

    @GetMapping("/tags")
    public ResponseEntity<List<BlogTag>> tagPage(@RequestParam("tagOwner") String tagOwner) {
        List<BlogTag> blogTagList = tagService.getBlogTags(tagOwner);
        return ResponseEntity.ok(blogTagList);
    }

    @PostMapping("/tags/save")
    @ResponseBody
    public ResponseEntity<Object> save(@RequestParam("tagOwner") String tagOwner,
                                       @RequestParam("tagName") String tagName,
                                       @RequestParam("createTime") Date createTime) {
        JSONObject response = new JSONObject();
        if (!StringUtils.hasText(tagOwner)) {
            response.put("status", "failed");
            response.put("tagOwnerError", "Login to add a new tag");
            return ResponseEntity.ok(response.toString());
        }
        if (!StringUtils.hasText(tagName)) {
            response.put("status", "failed");
            response.put("tagNameError", "Name can not be null");
            return ResponseEntity.ok(response.toString());
        }

        if (tagService.saveTag(tagName, tagOwner, createTime)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("tagNameError", "Tag has existed");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/tags/delete")
    @ResponseBody
    public ResponseEntity<Object> delete(@RequestParam("ids") Integer[] ids,
                                         @RequestParam("tagOwner") String tagOwner) {
        JSONObject response = new JSONObject();
        if (ids.length < 1) {
            response.put("status", "failed");
            response.put("errorMsg", "Select at least 1 tag");
            return ResponseEntity.ok(response.toString());
        }
        List<Integer> result = tagService.deleteBatch(ids, tagOwner);
        if(result.isEmpty()){
            response.put("status", "succeeded");
        } else if (result.get(0).equals(-1)) {
            response.put("status", "failed");
            response.put("errorMsg", "DataBase error");
        }
        else{
            response.put("status", "failed");
            response.put("errorMsg", "Delete failed");
            List<BlogTag> tagList = new ArrayList<>();
            for(int id : result){
                tagList.add(tagService.getTagById(id));
            }
            JSONArray jsonArray = new JSONArray(tagList);
            response.put("tags", jsonArray);
        }
        return ResponseEntity.ok(response.toString());
    }
}
