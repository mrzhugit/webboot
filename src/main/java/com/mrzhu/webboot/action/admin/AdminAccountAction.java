package com.mrzhu.webboot.action.admin;

import com.mrzhu.webboot.dao.AdminUserDao;
import com.mrzhu.webboot.domain.User;
import com.mrzhu.webboot.dto.JsonResult;
import com.mrzhu.webboot.dto.TableQuery;
import com.mrzhu.webboot.util.ActionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by mrzhu on 8/29/15.
 */
@Controller
@RequestMapping("/admin/account")
public class AdminAccountAction {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminUserDao adminUserDao;

    /**
     * 用户登陆
     */
    @RequestMapping(path = "/list", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResult list(@RequestParam Map<String, String> param,User user) {
        TableQuery query = ActionUtil.parseTableQuery(param);
        log.debug("account list param:" + query.toString());
        JsonResult result = new JsonResult();
        List<User> users = adminUserDao.query(user, query);
        result.put("data",users);
        result.put("draw",query.getDraw());
        result.put("recordsTotal",100);
        result.put("recordsFiltered",50);
        result.setResult(true);
        return result;
    }
}
