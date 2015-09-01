package com.mrzhu.webboot.dao.impl;

import com.mrzhu.webboot.dao.AdminUserDao;
import com.mrzhu.webboot.domain.User;
import com.mrzhu.webboot.dto.TableQuery;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by mrzhu on 8/23/15.
 */
@Repository
public class AdminUserDaoImpl implements AdminUserDao {

    private Logger log = LoggerFactory.getLogger(this.getClass());

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
        try {
            List<User> userList = jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<User>(User.class));
            if (userList != null && userList.size() == 1) {
                user = userList.get(0);
                updateLastLoginInfo(user); //更新最后登录时间
                addLoginLog(user); //记录最后登录时间
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return user;
    }

    private void addLoginLog(User user){
        String sql = " insert into admin_user_login_log (admin_user_id,login_time) values (:id,now()) ";
        jdbcTemplate.update(sql,new BeanPropertySqlParameterSource(user));
    }

    private void updateLastLoginInfo(User user){
        String sql = " update admin_user set last_login_time = now()  where id=:id ";
        jdbcTemplate.update(sql,new BeanPropertySqlParameterSource(user));
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
        if (codeList != null) {
            return new HashSet<>(codeList);
        } else {
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
        if (codeList != null) {
            return new HashSet<>(codeList);
        } else {
            return null;
        }
    }

    @Override
    public List<User> query(User user, TableQuery query) {
        Map<String, Object> param = new HashMap<>();

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        sql.append("    u.id, ");
        sql.append("    u.login_name loginName, ");
        sql.append("    u.display_name displayName, ");
        sql.append("    u.password, ");
        sql.append("    u.last_login_time lastLoginTime, ");
        sql.append("    u.create_time createTime, ");
        sql.append("    u.update_time updateTime ");
        sql.append(" FROM admin_user u ");
        sql.append(" WHERE 1 = 1 ");
        //处理查询条件
        if (user != null && StringUtils.isNoneBlank(user.getLoginName())) {
            sql.append(" and u.login_name like :loginName ");
            param.put("loginName", "%" + user.getLoginName() + "%");
        }

        if (user != null && StringUtils.isNoneBlank(user.getDisplayName())) {
            sql.append(" and u.display_name like :displayName ");
            param.put("displayName", "%" + user.getDisplayName() + "%");
        }

        if (query != null) {
            if (StringUtils.isNoneBlank(query.getOrderColumn()) && StringUtils.isNoneBlank(query.getOrderDir())) {
                sql.append(" order by " + query.getOrderColumn() + " " + query.getOrderDir());
            }

            //处理分页
            if (query.getStart() != null && query.getLength() != null) {
                sql.append(" limit " + query.getStart() + "," + query.getLength());
            }
        }

        List<User> userList = jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<>(User.class));
        return userList;
    }

    @Override
    public int queryCount(User user) {
        Map<String, Object> param = new HashMap<>();

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        sql.append("    count(u.id) ");
        sql.append(" FROM admin_user u ");
        sql.append(" WHERE 1 = 1 ");
        if (user != null && StringUtils.isNoneBlank(user.getLoginName())) {
            sql.append(" and u.login_name like :loginName ");
            param.put("loginName", "%" + user.getLoginName() + "%");
        }

        if (user != null && StringUtils.isNoneBlank(user.getDisplayName())) {
            sql.append(" and u.display_name like :displayName ");
            param.put("displayName", "%" + user.getDisplayName() + "%");
        }
        return jdbcTemplate.queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public int totalCount() {
        return jdbcTemplate.queryForObject(" select count(*) from admin_user ", new HashMap<String, Object>(), Integer.class);
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
