package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.entity.Role;
import com.dreamplume.sell.entity.User;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.form.*;
import com.dreamplume.sell.service.RoleService;
import com.dreamplume.sell.service.UserService;
import com.dreamplume.sell.util.JWTUnit;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import com.dreamplume.sell.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2022/4/21 11:25
 * @Created by 翊
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @Resource
    RoleService roleService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;  // key, value 都是字符串

   // 用户登录
    @PostMapping("/logon")
    public ResultVO<Object> logon(@RequestBody @Valid LogonForm logonForm) {
        ResultVO<Object> logon = userService.logon(logonForm);
        if (logon.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            // json Object 对象不能直接使用 toString() 转成字符串，而是使用 Json.toJSONString() 方法
            String objStr = JSON.toJSONString(logon.getData());
            User user = JSONObject.parseObject(objStr, User.class);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setToken(JWTUnit.createJwt(user));
            ResultVO<Object> findOneResultVo = roleService.findOne(user.getRoleId());
            if (!findOneResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
                return ResultVOUtil.error(SellErrorCode.get(findOneResultVo.getCode()));
            }
            userVO.setRole(JSON.parseObject(JSON.toJSONString(findOneResultVo.getData()), Role.class).getRoleName());
            log.info("【用户登录】userVO={}", userVO);
            // 将用户的 userId userToken 作为键值对存储到 Redis 中，过期时间设置为 24 小时
            stringRedisTemplate.opsForValue().set(userVO.getUserId(), userVO.getToken(),24, TimeUnit.HOURS);
            log.info("redis token={}",stringRedisTemplate.opsForValue().get(userVO.getUserId()));
            return ResultVOUtil.success(JSONObject.toJSON(userVO));
        } else {
            return logon;
        }
    }

    // 忘记密码
    @PutMapping("/forget/password")
    public ResultVO<Object> forgetPassword(@Valid @RequestBody ForgetPasswordForm from) {
        return userService.forgetPassword(from);
    }

   // 买家注册
    @PostMapping("/register")
    public ResultVO<Object> register(@RequestBody @Valid RegisterForm registerForm) {
        log.info("远程调用：用户注册");
        log.info("client:registerForm={}",registerForm);
        return userService.register(registerForm);
    }

    // 用户注销
    @PutMapping("/logoff")
    public ResultVO<Object> logoff(@RequestParam("userId") String userId) {
        return userService.logoff(userId);
    }

    // 用户登录退出
    @PutMapping("/logout")
    public ResultVO<Object> logout(@RequestParam("userId") String userId) {
        return userService.logout(userId);
    }

    // 用户更新用户名
    @PutMapping("/update/username")
    public ResultVO<Object> updateUserInfo(@RequestBody @Valid UpdateUserForm updateUserForm) {
        log.info("【更改用户名】,updateUserForm={}", updateUserForm);
        return userService.updateUserName(updateUserForm);
}

    // 用户更新登录密码
    @PutMapping("/update/password")
    public ResultVO<Object> updateUserPassword(@RequestBody UpdateUserPasswordForm updateUserPasswordForm) {
        log.info("updateUserPasswordForm={}",updateUserPasswordForm);
        return userService.updatePassword(updateUserPasswordForm);
    }

   // 用户更新头像图片
    @PostMapping("/picture/update")
    public ResultVO<Object> updateUserHeadPortrait(@RequestParam("userId") String userId,
                                                   @RequestParam("picture") MultipartFile newPictureFile) {
        log.info("userId = {}", userId);
        log.info("newPictureFile = {}", newPictureFile);
        return userService.updateUserHeadPortrait(userId, newPictureFile);
    }

    // 忘记支付密码
    @PutMapping("/forget/pay/password")
    public ResultVO<Object> forgetPayPassword(@RequestBody ForgetPasswordForm form) {
        log.info("【忘记支付密码】form={}", form);
        return userService.forgetPayPassword(form);
    }
}
