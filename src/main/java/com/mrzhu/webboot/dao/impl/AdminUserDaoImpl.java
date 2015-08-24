package com.mrzhu.webboot.dao.impl;

import com.mrzhu.webboot.dao.AdminUserDao;
import com.mrzhu.webboot.domain.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by mrzhu on 8/23/15.
 */
@Repository
public class AdminUserDaoImpl implements AdminUserDao {

    @Value("${password.key}")
    private String PASSWROD_KEY;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public User login(String loginName, String password) {
        String pass = encodePass(password);
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        sql.append("    u.id, ");
        sql.append("    u.login_name, ");
        sql.append("    u.display_name, ");
        sql.append("    u.password, ");
        sql.append("    u.last_login_time, ");
        sql.append("    u.create_time, ");
        sql.append("    u.update_time ");
        sql.append(" FROM admin_user u ");
        sql.append(" WHERE u.login_name = :loginName ");
        sql.append("        AND u.password = :password ");

        Map<String, Object> param = new HashMap<>();
        param.put("loginName", loginName);
        param.put("password", pass.toLowerCase());
        User user = null;
        List<User> userList = jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<User>(User.class));
        if (userList != null && userList.size() == 1) {
            user = userList.get(0);
        }
        return user;
    }

    @Override
    public User getByLoginName(String loginName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        sql.append("    u.id, ");
        sql.append("    u.login_name, ");
        sql.append("    u.display_name, ");
        sql.append("    u.password, ");
        sql.append("    u.last_login_time, ");
        sql.append("    u.create_time, ");
        sql.append("    u.update_time ");
        sql.append(" FROM admin_user u ");
        sql.append(" WHERE u.login_name = :loginName ");

        Map<String, Object> param = new HashMap<>();
        param.put("loginName", loginName);
        User user = null;
        List<User> userList = jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<>(User.class));
        if (userList != null && userList.size() == 1) {
            user = userList.get(0);
        }
        return user;
    }

    @Override
    public Set<String> getRoles(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT r.code ");
        sql.append(" FROM admin_user_role ur LEFT JOIN role r ON ur.role_id = r.id ");
        sql.append(" WHERE ur.admin_user_id = :userId ");

        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        List<String> codeList = jdbcTemplate.queryForList(sql.toString(), param, String.class);
        if(codeList!=null){
            return new HashSet<>(codeList);
        }else{
            return null;
        }
    }

    @Override
    public Set<String> getPermissions(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT p.code ");
        sql.append(" FROM permission p ");
        sql.append(" WHERE p.id IN (SELECT permission_id ");
        sql.append("               FROM admin_user_permission ");
        sql.append("               WHERE admin_user_id = :userId) ");
        sql.append("      OR p.id IN (SELECT permission_id ");
        sql.append("                  FROM role_permission ");
        sql.append("                  WHERE role_id IN (SELECT role_id ");
        sql.append("                                    FROM admin_user_role ");
        sql.append("                                    WHERE admin_user_id = :userId)) ");

        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        List<String> codeList = jdbcTemplate.queryForList(sql.toString(), param, String.class);
        if(codeList!=null){
            return new HashSet<>(codeList);
        }else{
            return null;
        }
    }

    /**
     * 明文加密
     *
     * @param text
     * @return
     */
    private String encodePass(String text) {
        return DigestUtils.md5Hex(text + PASSWROD_KEY);
    }
}
