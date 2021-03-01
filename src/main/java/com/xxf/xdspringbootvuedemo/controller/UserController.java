package com.xxf.xdspringbootvuedemo.controller;

import com.xxf.xdspringbootvuedemo.model.entity.User;
import com.xxf.xdspringbootvuedemo.model.request.LoginRequest;
import com.xxf.xdspringbootvuedemo.service.UserService;
import com.xxf.xdspringbootvuedemo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author:rooten
 * @Date:2021/2/7
 * @Description: 用户登录
 */
@RestController
@RequestMapping("api/v1/pri/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("register")
    public JsonData register(@RequestBody Map<String,String> userInfo) {

        int rows = userService.save(userInfo);
        return rows == 1? JsonData.buildSuccess():JsonData.buildError("注册失败，请重试");
    }

    @PostMapping("login")
    public JsonData login(@RequestBody LoginRequest loginRequest) {
        String token = userService.findByPhoneAndPwd(loginRequest.getPhone(), loginRequest.getPwd());

        return token == null ?JsonData.buildError("登录失败，账号密码错误"): JsonData.buildSuccess(token);
    }

    /**
     * 根据用户id查询用户信息
     * @param request
     * @return
     */
    @GetMapping("find_by_token")
    public JsonData findUserInfoByToken(HttpServletRequest request){

        Integer userId = (Integer) request.getAttribute("user_id");

        if(userId == null) {
            return JsonData.buildError("查询失败");
        }
        User user =  userService.findByUserId(userId);

        return JsonData.buildSuccess(user);
    }
}
