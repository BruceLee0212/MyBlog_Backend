package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.entity.BlogConfig;
import com.site.blog.my.core.service.AdminUserService;
import com.site.blog.my.core.service.ConfigService;
import com.site.blog.my.core.service.FileStoreService;
import com.site.blog.my.core.util.IDGenerator;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;

@Controller
@CrossOrigin
public class ConfigurationController {

    @Resource
    private ConfigService configService;
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private FileStoreService fileStoreService;

    @GetMapping("/configurations")
    public ResponseEntity<Map<String, Object>> configPage(@RequestParam("configOwner") String configOwner) {
        Map<String, String> blogConfigMap = configService.getAllConfigs(configOwner);
        Map<String, Object> responseMap = new HashMap<>(blogConfigMap);
        if(!blogConfigMap.get("profilePic").equals("default")){
            responseMap.put("profilePicBase64", fileStoreService.downloadFile(blogConfigMap.get("profilePic")));
        }
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/configurations/site")
    @ResponseBody
    public ResponseEntity<Object> site(@RequestParam("configOwner") String configOwner,
                                       @RequestParam(value = "websiteIcon", required = false) MultipartFile websiteIcon,
                                       @RequestParam(value = "websiteTitle", required = false) String websiteTitle,
                                       @RequestParam(value = "websiteDescription", required = false) String websiteDescription) {
        JSONObject response = new JSONObject();
        if(!StringUtils.hasText(configOwner)){
            response.put("message", "Login to update");
            return ResponseEntity.ok(response.toString());
        }
        int updateResult = 0;
        if (websiteIcon != null) {
            if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                    IMAGE_BMP.getMimeType(),
                    IMAGE_GIF.getMimeType(),
                    IMAGE_JPEG.getMimeType()).contains(websiteIcon.getContentType())) {
                response.put("websiteIconError", "Website icon can only be image");
                if (!response.has("status")) response.put("status", "failed");
                return ResponseEntity.ok(response.toString());
            }
            String path = "";
            String profilePicId = String.format("%s", IDGenerator.generateID());
            path = String.format("%s/%s/%s", configOwner, "website-icon", profilePicId);
            fileStoreService.uploadFile(path, websiteIcon);
            updateResult += configService.updateConfig(configOwner, "websiteIcon", path);
        }
        if (StringUtils.hasText(websiteTitle)) {
            updateResult += configService.updateConfig(configOwner, "websiteTitle", websiteTitle);
        }
        if (StringUtils.hasText(websiteDescription)) {
            updateResult += configService.updateConfig(configOwner,"websiteDescription", websiteDescription);
        }
        if(updateResult > 0){
            response.put("message", "Update Successfully");
        }
        else{
            response.put("message", "Nothing changed");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/configurations/profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> profile(@RequestParam("configOwner") String configOwner,
                                          @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
                                          @RequestParam(value = "fName", required = false) String fName,
                                          @RequestParam(value = "lName", required = false) String lName,
                                          @RequestParam(value = "pName", required = false) String pName) {
        Map<String, Object> responseMap = new HashMap<>();
        if(!StringUtils.hasText(configOwner)){
            responseMap.put("message", "Login to update");
            return ResponseEntity.ok(responseMap);
        }
        int updateResult = 0;
        if (profilePic != null) {
            if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                    IMAGE_BMP.getMimeType(),
                    IMAGE_GIF.getMimeType(),
                    IMAGE_JPEG.getMimeType()).contains(profilePic.getContentType())) {
                responseMap.put("profilePicError", "Profile picture can only be image");
                if (!responseMap.containsKey("status")) responseMap.put("status", "failed");
                return ResponseEntity.ok(responseMap);
            }
            String path = "";
            String profilePicId = String.format("%s", IDGenerator.generateID());
            path = String.format("%s/%s/%s", configOwner, "profile-pics", profilePicId);
            fileStoreService.uploadFile(path, profilePic);
            updateResult += configService.updateConfig(configOwner, "profilePic", path);
        }
        if(adminUserService.updateName(configOwner, fName, lName, pName)) updateResult++;
        if(updateResult > 0){
            responseMap.put("message", "Update Successfully");
        }
        else{
            responseMap.put("message", "Nothing changed");
        }
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/configurations/website")
    @ResponseBody
    public ResponseEntity<Object> website(@RequestParam("configOwner") String configOwner,
                                         @RequestParam(value = "github", required = false) String github,
                                         @RequestParam(value = "facebook", required = false) String facebook,
                                         @RequestParam(value = "x", required = false) String x,
                                         @RequestParam(value = "instagram", required = false) String instagram) {
        JSONObject response = new JSONObject();
        if(!StringUtils.hasText(configOwner)){
            response.put("message", "Login to update");
            return ResponseEntity.ok(response.toString());
        }
        int updateResult = 0;
        if(StringUtils.hasText(github)) {
            updateResult += configService.updateConfig(configOwner, "github", github);
        }
        if(StringUtils.hasText(facebook)) {
            updateResult += configService.updateConfig(configOwner, "facebook", facebook);
        }
        if(StringUtils.hasText(x)) {
            updateResult += configService.updateConfig(configOwner, "x", x);
        }
        if(StringUtils.hasText(instagram)) {
            updateResult += configService.updateConfig(configOwner, "instagram", instagram);
        }
        if(updateResult > 0){
            response.put("message", "Update Successfully");
        }
        else{
            response.put("message", "Nothing changed");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/configurations/footer")
    @ResponseBody
    public ResponseEntity<Object> footer(@RequestParam("configOwner") String configOwner,
                                         @RequestParam(value = "footerAbout", required = false) String footerAbout,
                                         @RequestParam(value = "footerCopyRight", required = false) String footerCopyRight,
                                         @RequestParam(value = "footerPoweredBy", required = false) String footerPoweredBy,
                                         @RequestParam(value = "footerPoweredByUrl", required = false) String footerPoweredByUrl) {
        JSONObject response = new JSONObject();
        if(!StringUtils.hasText(configOwner)){
            response.put("message", "Login to update");
            return ResponseEntity.ok(response.toString());
        }
        int updateResult = 0;
        if (StringUtils.hasText(footerAbout)) {
            updateResult += configService.updateConfig(configOwner, "footerAbout", footerAbout);
        }
        if (StringUtils.hasText(footerAbout)) {
            updateResult += configService.updateConfig(configOwner, "footerCopyRight", footerCopyRight);
        }
        if (StringUtils.hasText(footerAbout)) {
            updateResult += configService.updateConfig(configOwner, "footerPoweredBy", footerPoweredBy);
        }
        if (StringUtils.hasText(footerAbout)) {
            updateResult += configService.updateConfig(configOwner, "footerPoweredByUrl", footerPoweredByUrl);
        }
        if(updateResult > 0){
            response.put("message", "Update Successfully");
        }
        else{
            response.put("message", "Nothing changed");
        }
        return ResponseEntity.ok(response.toString());
    }

}
