package com.site.blog.my.core.controller;

import com.site.blog.my.core.entity.AdminUser;
import com.site.blog.my.core.entity.BlogConfig;
import com.site.blog.my.core.entity.ConfirmationToken;
import com.site.blog.my.core.service.*;
import com.site.blog.my.core.util.IDGenerator;
import com.site.blog.my.core.util.MD5Util;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

@Controller
@CrossOrigin
public class LoginController {

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private EmailService emailService;
    @Resource
    private TokenService tokenService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private ConfigService configService;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestBody String json) {
        JSONObject loginData = new JSONObject(json);
        String email = loginData.getString("email");
        String password = loginData.getString("password");
        String redirectUrl = "";
        JSONObject response = new JSONObject();
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            response.put("error", "Email or password can not be null");
        }
        else {
            AdminUser adminUser = adminUserService.login(email, password);
            if (adminUser != null) {
                if(adminUser.getLocked() == 0){
                    redirectUrl = "/home";
                }
                else {
                    response.put("error", "Email not confirmed yet");
                    redirectUrl = "account-activate";
                }
            } else {
                response.put("error", "Email or password incorrect");
            }
        }
        response.put("redirect", redirectUrl);
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Object> signUp(@RequestParam("firstName") String fName,
                                         @RequestParam("lastName") String lName,
                                         @RequestParam("preferredName") String pName,
                                         @RequestParam("email") String email,
                                         @RequestParam("password") String password,
                                         @RequestParam("tokenId") Long tokenId,
                                         @RequestParam("verificationCode") String verificationCode) {
        JSONObject response = new JSONObject();
        ConfirmationToken token = tokenService.getToken(tokenId);
        Date current = new Date();
        long Difference = Math.abs(current.getTime() - token.getCreateTime().getTime()) / (60 * 1000);
        if(Difference > 15){
            response.put("status", "failed");
            response.put("error", "Verification code expired");
            return ResponseEntity.ok(response.toString());
        }
        if(!token.getTokenContent().equals(verificationCode)){
            response.put("status", "failed");
            response.put("error", "Verification code not match");
            return ResponseEntity.ok(response.toString());
        }

        AdminUser adminUser = new AdminUser();
        adminUser.setAccount(email);
        adminUser.setFName(fName);
        adminUser.setLName(lName);
        adminUser.setPName(pName);
        adminUser.setPassword(MD5Util.MD5Encode(password, "UTF-8"));
        adminUser.setLocked((byte) 0);
        String result = adminUserService.register(adminUser);
        if (result.equals("success")) {
            categoryService.saveCategory("Default", "default-resources/category-icons/default-icon.jpg", email, new Date());
            BlogConfig blogConfig = new BlogConfig();
            blogConfig.setConfigId(IDGenerator.generateID());
            blogConfig.setConfigOwner(email);
            blogConfig.setConfigName("websiteUrl");
            blogConfig.setConfigValue(pName.length() != 0 ? pName.toLowerCase() : fName.toLowerCase() + lName.toLowerCase());
            blogConfig.setCreateTime(new Date());
            configService.insert(blogConfig);
            response.put("status", "succeeded");
            response.put("redirect", "/home");

        } else {
            response.put("status", "failed");
            response.put("error", result);
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/change-password")
    public ResponseEntity<Object> passwordUpdate(@RequestParam("email") String email,
                                                 @RequestParam("password") String password,
                                                 @RequestParam("tokenId") Long tokenId,
                                                 @RequestParam("verificationCode") String verificationCode){
        JSONObject response = new JSONObject();
        ConfirmationToken token = tokenService.getToken(tokenId);
        Date current = new Date();
        long Difference = Math.abs(current.getTime() - token.getCreateTime().getTime()) / (60 * 1000);
        if(Difference > 15){
            response.put("status", "failed");
            response.put("error", "Verification code expired");
            return ResponseEntity.ok(response.toString());
        }
        if(!token.getTokenContent().equals(verificationCode)){
            response.put("status", "failed");
            response.put("error", "Verification code not match");
            return ResponseEntity.ok(response.toString());
        }
        if(!StringUtils.hasText(email)){
            response.put("status", "failed");
            response.put("errorEmail", "Email can not be null");
        }
        if(!StringUtils.hasText(password)){
            if(!response.has("status"))response.put("status", "failed");
            response.put("errorPwd", "Password can not be null");
        }
        if(response.has("status")){
            return ResponseEntity.ok(response.toString());
        }
        AdminUser adminUser = adminUserService.getUserDetailById(email);
        adminUser.setPassword(MD5Util.MD5Encode(password, "UTF-8"));
        adminUser.setLocked((byte) 0);
        if (adminUserService.updateUser(adminUser) > 0) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/account-activate")
    public ResponseEntity<Object> activate(@RequestParam("email") String email,
                                                 @RequestParam("tokenId") Long tokenId,
                                                 @RequestParam("verificationCode") String verificationCode){
        JSONObject response = new JSONObject();
        ConfirmationToken token = tokenService.getToken(tokenId);
        Date current = new Date();
        long Difference = Math.abs(current.getTime() - token.getCreateTime().getTime()) / (60 * 1000);
        if(Difference > 15){
            response.put("status", "failed");
            response.put("error", "Verification code expired");
            return ResponseEntity.ok(response.toString());
        }
        if(!token.getTokenContent().equals(verificationCode)){
            response.put("status", "failed");
            response.put("error", "Verification code not match");
            return ResponseEntity.ok(response.toString());
        }
        if(!StringUtils.hasText(email)){
            response.put("status", "failed");
            response.put("errorEmail", "Email can not be null");
        }
        if(response.has("status")){
            return ResponseEntity.ok(response.toString());
        }
        AdminUser adminUser = adminUserService.getUserDetailById(email);
        adminUser.setLocked((byte) 0);
        if (adminUserService.updateUser(adminUser) > 0) {
            response.put("status", "succeeded");
        } else {
            response.put("status", "failed");
        }
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/confirmation")
    public ResponseEntity<Object> emailConfirmation(@RequestParam("email") String email){
        JSONObject response = new JSONObject();
        if(!StringUtils.hasText(email)){
            response.put("status", "failed");
            response.put("errorEmail", "Email can not be null");
            return ResponseEntity.ok(response.toString());
        }
        String code = String.format("%s", generateVerificationCode());
        ConfirmationToken token = new ConfirmationToken();
        token.setTokenId((long)IDGenerator.generateID());
        token.setTokenContent(code);
        token.setCreateTime(new Date());
        tokenService.saveToken(token);
        emailService.sendTokenEmail(email, buildEmail(code));
        response.put("message", "Verification code has been sent to your email");
        response.put("tokenId", token.getTokenId());
        return ResponseEntity.ok(response.toString());
    }

    private int generateVerificationCode(){
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    private String buildEmail(String code) {
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi Blogger,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Here is your verification code: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + code + "</p></blockquote>\n Code will expire in 15 minutes. <p>See you soon</p>" +
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
