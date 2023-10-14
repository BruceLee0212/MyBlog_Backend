package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.entity.BlogComment;
import com.site.blog.my.core.service.BlogService;
import com.site.blog.my.core.service.CommentService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
public class CommentController {
    @Resource
    private CommentService commentService;
    @Resource
    private BlogService blogService;

    @GetMapping("/comments")
    public ResponseEntity<Map<String, Object>> getAllComments(@RequestParam("commentOwner") String commentOwner) {
        Map<String, Object> responseMap = new HashMap<>();
        List<BlogComment> blogCommentList = commentService.getCommentList(commentOwner);
        long[] blogIds = blogCommentList.stream()
                .mapToLong(BlogComment::getBlogId)
                .distinct()
                .toArray();
        Map<Long, String> blogTitleList = blogService.getTitleByIds(blogIds);
        responseMap.put("blogCommentList", blogCommentList);
        responseMap.put("blogTitleList", blogTitleList);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping( "comments/batch-approve")
    public ResponseEntity<Object> batchApprove(@RequestParam("commentOwner") String commentOwner,
                                               @RequestParam("ids") Long[] ids) {
        JSONObject response = new JSONObject();
        if (ids.length < 1) {
            response.put("status", "failed");
            response.put("errorMsg", "Select at least 1 comment");
            return ResponseEntity.ok(response.toString());
        }
        if (commentService.checkDone(commentOwner, ids)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("errorMsg", "DataBase Error");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping( "comments/batch-delete")
    public ResponseEntity<Object> batchDelete(@RequestParam("commentOwner") String commentOwner,
                                               @RequestParam("ids") Long[] ids) {
        JSONObject response = new JSONObject();
        if (ids.length < 1) {
            response.put("status", "failed");
            response.put("errorMsg", "Select at least 1 comment");
            return ResponseEntity.ok(response.toString());
        }
        if (commentService.deleteBatch(commentOwner, ids)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("errorMsg", "DataBase Error");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/comments/reply")
    public ResponseEntity<Object> reply(@RequestParam("commentId") Long commentId,
                                        @RequestParam("replyBody") String replyBody){
        JSONObject response = new JSONObject();
        if (commentId == null) {
            response.put("status", "failed");
            response.put("errorMsg", "Comment Not Found");
            return ResponseEntity.ok(response.toString());
        }
        if(!StringUtils.hasText(replyBody)){
            response.put("status", "failed");
            response.put("errorMsg", "Comment can not be null");
            return ResponseEntity.ok(response.toString());
        }
        if (commentService.addReply(commentId, replyBody)) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
            response.put("errorMsg", "DataBase Error");
        }
        return ResponseEntity.ok(response.toString());
    }
}
