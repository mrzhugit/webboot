package com.mrzhu.webboot.shiro;

import com.mrzhu.webboot.dao.AdminUserDao;
import com.mrzhu.webboot.domain.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 后台权限验证类
 * Created by mrzhu on 8/23/15.
 */
@Component
public class AdminAuthenticatingRealm extends AuthorizingRealm {

    @Autowired
    private AdminUserDao adminUserDao;

    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    //验证用户
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String loginName = (String) token.getPrincipal(); // 得到用户名
        String password = new String((char[]) token.getCredentials()); // 得到密码

        User user = new User();
        user.setLoginName(loginName);
        user.setPassword(password);
        User loginUser = adminUserDao.login(user.getLoginName(), user.getPassword());
        if (loginUser == null || loginUser.getId() <= 0) {
            throw new IncorrectCredentialsException("用户名或密码不正确!");
        }

        // 如果身份认证验证成功,返回一个 AuthenticationInfo 实现;
        return new SimpleAuthenticationInfo(loginName, password, getName());
    }

    //载入角色和权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String loginName = (String) principals.getPrimaryPrincipal();
        User user = adminUserDao.getByLoginName(loginName);

        Set<String> permissionSet = adminUserDao.getPermissions(user.getId());
        Set<String> roleSet = adminUserDao.getRoles(user.getId());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roleSet);
        authorizationInfo.setStringPermissions(permissionSet);

        return authorizationInfo;
    }

}