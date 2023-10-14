package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.entity.BlogLink;
import com.site.blog.my.core.service.LinkService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class LinkController {

    @Resource
    private LinkService linkService;

    /**
     * get all links
     */
    @GetMapping("/links")
    public ResponseEntity<List<BlogLink>> tagPage(@RequestParam("linkOwner") String linkOwner) {
        List<BlogLink> blogLinkList = linkService.getBlogLinks(linkOwner);
        return ResponseEntity.ok(blogLinkList);
    }

    /**
     * add new link
     */
    @PostMapping ( "/links/save")
    @ResponseBody
    public ResponseEntity<Object> save(@RequestParam("linkOwner") String linkOwner,
                                       @RequestParam("linkName") String linkName,
                                       @RequestParam("linkType") String linkType,
                                       @RequestParam("linkUrl") String linkUrl,
                                       @RequestParam("linkDescription") String linkDescription,
                                       @RequestParam("linkRank") String linkRank,
                                       @RequestParam("createTime") Date createTime) {
        JSONObject response = new JSONObject();
        if (!StringUtils.hasText(linkOwner)) {
            response.put("status", "failed");
            response.put("linkOwnerError", "Login to add a new link");
            return ResponseEntity.ok(response.toString());
        }
        if(!StringUtils.hasText(linkType)){
            response.put("status", "failed");
            response.put("linkTypeError", "Choose a type from drop down list");
        }
        if(!StringUtils.hasText(linkRank) || Integer.valueOf(linkRank) < 0){
            if(!response.has("status")) response.put("status", "failed");
            response.put("linkRankError", "Set the correct displayed order of the link");
        }
        if(!StringUtils.hasText(linkName)){
            if(!response.has("status")) response.put("status", "failed");
            response.put("linkNameError", "Link name can not be null");
        }
        if(!StringUtils.hasText(linkUrl)){
            if(!response.has("status")) response.put("status", "failed");
            response.put("linkUrlError", "Link url can not be null");
        }
        if(!StringUtils.hasText(linkDescription)){
            if(!response.has("status")) response.put("status", "failed");
            response.put("linkDescriptionError", "Link description can not be null");
        }
        if(response.has("status")) return ResponseEntity.ok(response.toString());
        if (linkService.saveLink(linkOwner, Integer.valueOf(linkType), linkName, linkUrl, linkDescription, Integer.valueOf(linkRank), createTime)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("linkDatabaseError", "DataBase Error");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/links/update")
    @ResponseBody
    public ResponseEntity<Object> update(@RequestParam("linkId") String linkId,
                                         @RequestParam("linkOwner") String linkOwner,
                                         @RequestParam("linkType") String linkType,
                                         @RequestParam("linkName") String linkName,
                                         @RequestParam("linkUrl") String linkUrl,
                                         @RequestParam("linkRank") String linkRank,
                                         @RequestParam("linkDescription") String linkDescription) {
        JSONObject response = new JSONObject();
        if (!StringUtils.hasText(linkOwner)) {
            response.put("status", "failed");
            response.put("linkOwnerError", "Login to update the link");
            return ResponseEntity.ok(response.toString());
        }
        if(!StringUtils.hasText(linkType)){
            response.put("status", "failed");
            response.put("linkTypeError", "Choose a type from drop down list");
        }
        if(!StringUtils.hasText(linkRank) || Integer.valueOf(linkRank) < 0){
            if(!response.has("status")) response.put("status", "failed");
            response.put("linkRankError", "Set the correct displayed order of the link");
        }
        if(!StringUtils.hasText(linkName)){
            if(!response.has("status")) response.put("status", "failed");
            response.put("linkNameError", "Link name can not be null");
        }
        if(!StringUtils.hasText(linkUrl)){
            if(!response.has("status")) response.put("status", "failed");
            response.put("linkUrlError", "Link url can not be null");
        }
        if(!StringUtils.hasText(linkDescription)){
            if(!response.has("status")) response.put("status", "failed");
            response.put("linkDescriptionError", "Link description can not be null");
        }
        if(response.has("status")) return ResponseEntity.ok(response.toString());
        BlogLink tempLink = linkService.selectById(Integer.valueOf(linkId));
        tempLink.setLinkType(Byte.valueOf(linkType));
        tempLink.setLinkRank(Integer.valueOf(linkRank));
        tempLink.setLinkName(linkName);
        tempLink.setLinkUrl(linkUrl);
        tempLink.setLinkDescription(linkDescription);
        if(linkService.updateLink(tempLink)){
            response.put("status", "succeeded");
        }
        else{
            response.put("status", "failed");
            response.put("linkDatabaseError", "DataBase Error");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping( "/links/delete")
    @ResponseBody
    public ResponseEntity<Object> delete(@RequestParam("ids") Integer[] ids,
                                         @RequestParam("linkOwner") String linkOwner) {
        JSONObject response = new JSONObject();
        if (ids.length < 1) {
            response.put("status", "failed");
            response.put("errorMsg", "Select at least 1 link");
            return ResponseEntity.ok(response.toString());
        }
        if (linkService.deleteBatch(ids, linkOwner)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("errorMsg", "DataBase Error");
        }
        return ResponseEntity.ok(response.toString());
    }
}
