package com.site.blog.my.core.controller.blog;

import com.site.blog.my.core.entity.*;
import com.site.blog.my.core.service.*;
import com.site.blog.my.core.util.IDGenerator;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
public class BlogPageController {
    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LinkService linkService;
    @Resource
    private ConfigService configService;
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private FileStoreService fileStoreService;
    @Resource CommentService commentService;
    @Resource EmailService emailService;

    @GetMapping("/blog-index")
    public ResponseEntity<Map<String, Object>> index(@RequestParam("author") String author){
        Map<String, Object> responseMap = new HashMap<>();
        BlogConfig blogConfig = configService.getConfigByNameAndValue("websiteUrl", author);
        String indexOwner = blogConfig.getConfigOwner();
        AdminUser adminUser = adminUserService.getUserDetailById(indexOwner);
        List<Blog> blogList = blogService.getBlogsWithoutContent(indexOwner);
        List<String> blogCovers = blogList.stream()
                .map(Blog::getBlogCoverImage)
                .collect(Collectors.toList());
        Map<String, byte[]> blogCoverList = new HashMap<>();
        for(String blogCover : blogCovers){
            blogCoverList.put(blogCover, fileStoreService.downloadFile(blogCover));
        }
        List<BlogTag> tagList = tagService.getBlogTags(indexOwner);
        List<BlogCategory> categoryList = categoryService.getBlogCategories(indexOwner);
        List<BlogLink> linkList = linkService.getBlogLinks(indexOwner);
        Map<String, String> siteConfig = configService.getSiteConfigs(indexOwner);
        Map<String, String> websiteConfig = configService.getWebsiteConfigs(indexOwner);
        responseMap.put("blogList", blogList);
        responseMap.put("blogCoverList", blogCoverList);
        responseMap.put("tagList", tagList);
        responseMap.put("categoryList", categoryList);
        responseMap.put("linkList", linkList);
        responseMap.put("footerConfig", configService.getFooterConfigs(indexOwner));
        responseMap.put("siteConfig", siteConfig);
        responseMap.put("websiteConfig", websiteConfig);
        if(siteConfig.get("websiteIcon") != null && siteConfig.get("websiteIcon").length() > 0){
            responseMap.put("siteIcon", fileStoreService.downloadFile(siteConfig.get("websiteIcon")));
        }
        responseMap.put("github", configService.getConfigByName(indexOwner, "github"));
        responseMap.put("authorName", adminUserService.getUserNameById(indexOwner));
        responseMap.put("authorProfilePic", fileStoreService.downloadFile(configService.getConfigByName(indexOwner, "profilePic")));
        responseMap.put("authorEmail", adminUser.getAccount());
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/blog-detail")
    public ResponseEntity<Map<String, Object>> detail(@RequestParam("author") String author,
                                                      @RequestParam("blogUrl") String blogUrl){
        Map<String, Object> responseMap = new HashMap<>();
        BlogConfig blogConfig = configService.getConfigByNameAndValue("websiteUrl", author);
        String indexOwner = blogConfig.getConfigOwner();
        Blog blog = blogService.getBlogDetailBySubUrl(indexOwner, blogUrl);
        blog.setBlogContent(blog.getBlogContent());
        List<BlogComment> commentList = commentService.getAllCommentForOneBlog(blog.getBlogId());
        String authorName = adminUserService.getUserNameById(indexOwner);
        String authorProfilePicPath = configService.getConfigByName(indexOwner, "profilePic");
        if(StringUtils.hasText(authorProfilePicPath)){
            byte[] authorProfilePic = fileStoreService.downloadFile(authorProfilePicPath);
            responseMap.put("authorProfilePic", authorProfilePic);
        }
        responseMap.put("websiteTitle", configService.getConfigByName(indexOwner, "websiteTitle"));
        String websiteIconPath = configService.getConfigByName(indexOwner, "websiteIcon");
        if(StringUtils.hasText(websiteIconPath)){
            responseMap.put("websiteIcon", fileStoreService.downloadFile(websiteIconPath));
        }
        responseMap.put("github", configService.getConfigByName(indexOwner, "github"));
        Map<String, byte[]> commentatorProfilePic = new HashMap<>();
        for(BlogComment comment : commentList){
            String email = comment.getEmail();
            if(email != null && !commentatorProfilePic.containsKey(email)){
                String profilePic = configService.getConfigByName(email,"profilePic");
                if(StringUtils.hasText(profilePic)) {
                    commentatorProfilePic.put(email, fileStoreService.downloadFile(profilePic));
                }
            }
        }
        Map<String, String> websiteConfig = configService.getWebsiteConfigs(indexOwner);
        responseMap.put("websiteConfig", websiteConfig);
        responseMap.put("footerConfig", configService.getFooterConfigs(indexOwner));
        responseMap.put("blog", blog);
        responseMap.put("commentList", commentList);
        responseMap.put("authorName", authorName);
        responseMap.put("commentatorProfilePic", commentatorProfilePic);
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/blog-link")
    public ResponseEntity<Map<String, Object>> links(@RequestParam("author") String author){
        Map<String, Object> responseMap = new HashMap<>();
        BlogConfig blogConfig = configService.getConfigByNameAndValue("websiteUrl", author);
        String indexOwner = blogConfig.getConfigOwner();;
        responseMap.put("websiteTitle", configService.getConfigByName(indexOwner, "websiteTitle"));
        String websiteIconPath = configService.getConfigByName(indexOwner, "websiteIcon");
        if(StringUtils.hasText(websiteIconPath)){
            responseMap.put("websiteIcon", fileStoreService.downloadFile(websiteIconPath));
        }
        responseMap.put("github", configService.getConfigByName(indexOwner, "github"));
        responseMap.put("authorEmail", indexOwner);
        responseMap.put("footerConfig", configService.getFooterConfigs(indexOwner));
        Map<Byte, List<BlogLink>> linkMap = linkService.getLinksByType(indexOwner);
        List<BlogLink> fLinkList = linkMap.get((byte)0);
        List<BlogLink> rLinkList = linkMap.get((byte)1);
        List<BlogLink> pLinkList = linkMap.get((byte)2);
        responseMap.put("fLinkList", fLinkList);
        responseMap.put("rLinkList", rLinkList);
        responseMap.put("pLinkList", pLinkList);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("view-increase")
    public ResponseEntity<Object> viewIncrease(@RequestParam("blogId") Long blogId){
        Blog blog = blogService.getBlogById(blogId);
        blog.setBlogViews(blog.getBlogViews() + 1);
        blogService.updateBlog(blog);
        JSONObject response = new JSONObject();
        response.put("message", "blog views increased");
        return ResponseEntity.ok(response);
    }

    @PostMapping("add-comment")
    public ResponseEntity<Object> addComment(@RequestParam("blogId") Long blogId,
                                             @RequestParam("cName") String cName,
                                             @RequestParam(name = "cEmail", required = false) String cEmail,
                                             @RequestParam("cContent") String cContent){
        JSONObject response = new JSONObject();
        if(blogId == null){
            response.put("status", "failed");
            response.put("blogError", "Blog doesn't exist");
        }
        if(cName == null || !StringUtils.hasText(cName.trim())){
            if(!response.has("status")) response.put("status", "failed");
            response.put("nameError", "Name can not be null");
        }
        if(cContent == null || !StringUtils.hasText(cContent.trim())){
            if(!response.has("status")) response.put("status", "failed");
            response.put("contentError", "Comment can not be null");
        }
        if(response.has("status")){
            return ResponseEntity.ok(response.toString());
        }
        BlogComment comment = new BlogComment();
        comment.setCommentId((long)IDGenerator.generateID());
        comment.setCommentOwner(blogService.getOwnerById(blogId));
        comment.setBlogId(blogId);
        comment.setCommentator(cName);
        comment.setEmail(cEmail);
        comment.setCommentBody(cContent);
        comment.setCommentStatus((byte)0);
        comment.setIsDeleted((byte)0);
        if(commentService.addComment(comment)){
            response.put("status", "succeeded");
        }
        else{
            response.put("status", "failed");
            response.put("dbError", "Database Error");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/send-email")
    public ResponseEntity<Object> sendEmail(@RequestParam("sender") String sender,
                                            @RequestParam("receiver") String receiver,
                                            @RequestParam("content") String content){
        JSONObject response = new JSONObject();
        if(!StringUtils.hasText(sender)){
            response.put("status", "failed");
            response.put("senderError", "Email can not be null");
        }
        if(!StringUtils.hasText(receiver)){
            if(!response.has("status")) response.put("status", "failed");
            response.put("receiverError", "System error, refresh and try again");
        }
        if(!StringUtils.hasText(content)){
            if(!response.has("status")) response.put("status", "failed");
            response.put("contentError", "Content can not be null");
        }
        if(response.has("status")) return ResponseEntity.ok(response.toString());
        String formattedContent = content.replace("\n", "<br>");
        emailService.sendEmail(sender, receiver, "You get a new message from your blog", buildEmail(adminUserService.getUserNameById(receiver), sender, formattedContent ));
        response.put("message", "Message has been sent to author");
        return ResponseEntity.ok(response.toString());
    }

    private String buildEmail(String authorName, String sender, String content) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">New Message</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi "+ authorName +",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> You got a new message from: "+ sender +"</p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><div style=\"width: 100%; word-wrap: break-word; font-size: 19px; line-height: 25px; color: #0b0c0c\">" + content + "</div></blockquote>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
