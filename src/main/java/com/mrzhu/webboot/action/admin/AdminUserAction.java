package com.mrzhu.webboot.action.admin;

import com.mrzhu.webboot.dto.JsonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by mrzhu on 8/25/15.
 */
@Controller
@RequestMapping("/admin/user")
public class AdminUserAction {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecurityManager securityManager;

    /**
     * 用户登陆
     */
    @RequestMapping(path = "/login", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResult login(String loginName, String password, Boolean rememberMe) {
        JsonResult result = new JsonResult();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
        if (rememberMe != null && rememberMe) {
            token.setRememberMe(true);
        }
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            subject.logout();
            log.info("登录失败");
            result.setResult(false);
            return result;
        }

        if (subject.isAuthenticated()) {
            result.setResult(true);
        } else {
            result.setResult(false);
        }

        return result;
    }

}
