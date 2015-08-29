package com.mrzhu.webboot.dao;

import com.mrzhu.webboot.domain.User;
import com.mrzhu.webboot.dto.TableQuery;

import java.util.List;
import java.util.Set;

/**
 * Created by mrzhu on 8/23/15.
 */
public interface AdminUserDao {
    /**
     * 用户登录
     * @param loginName 登录名
     * @param password 密码(明文)
     * @return 登录成功返回User,失败返回null
     */
    User login(String loginName, String password);

    /**
     * 根据登录名获取用户
     * @param loginName 登录名
     * @return user/null
     */
    User getByLoginName(String loginName);

    /**
     * 获取用户的角色(code)列表
     * @param userId
     * @return
     */
    Set<String> getRoles(Long userId);

    /**
     * 获取用户的权限(code)列表
     * @param userId
     * @return
     */
    Set<String> getPermissions(Long userId);

    /**
     * 分页查询user列表
     * @param user user属性条件
     * @param query 分页&排序条件
     * @return 分页结果
     */
    List<User> query(User user, TableQuery query);
}
