package com.xxf.xdspringbootvuedemo.service.impl;

import com.xxf.xdspringbootvuedemo.model.entity.User;
import com.xxf.xdspringbootvuedemo.model.request.LoginRequest;
import com.xxf.xdspringbootvuedemo.service.UserService;
import com.xxf.xdspringbootvuedemo.mapper.UserMapper;
import com.xxf.xdspringbootvuedemo.utils.CommonUtils;
import com.xxf.xdspringbootvuedemo.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @Author:rooten
 * @Date:2021/2/7
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserId(Integer userId) {
        User user = userMapper.findByUserId(userId);
        return user;
    }

    @Override
    public int save(Map<String, String> userInfo) {
        User user = parseToUser(userInfo);
        if(user != null) {
            return userMapper.save(user);
        } else {
            return -1;
        }
    }

    @Override
    public String findByPhoneAndPwd(String phone, String pwd) {
        User user = userMapper.findByPhoneAndPwd(phone, CommonUtils.MD5(pwd));

        if(user == null) {
            return null;

        } else {
            String token = JWTUtils.geneJsonWebToken(user);
            return token;
        }
    }

    /**
     * 解析 user 对象
     * @param userInfo
     * @return
     */
    private User parseToUser(Map<String, String> userInfo) {

        if(userInfo.containsKey("phone")&&userInfo.containsKey("name")&&userInfo.containsKey("pwd")){
            User user = new User();
            user.setName(userInfo.get("name"));
            user.setHeadImg(getRandomImg());
            user.setCreateTime(new Date());
            user.setPhone(userInfo.get("phone"));
            String pwd = userInfo.get("pwd");
            // MD5加密
            user.setPwd(CommonUtils.MD5(pwd));
            return user;
        }else {
            return null;
        }
    }

    /**
     * 放在CDN上的随机头像图片数组
     * @return
     */
    private static final String[] headImg = {
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/12.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/11.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/13.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/14.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/15.jpeg"
    };

    private String getRandomImg() {
        int size = headImg.length;
        Random random = new Random();
        int index = random.nextInt(size);
        return headImg[index];
    }
}