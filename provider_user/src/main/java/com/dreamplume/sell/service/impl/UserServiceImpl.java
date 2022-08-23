package com.dreamplume.sell.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dreamplume.sell.configure.PictureConfig;
import com.dreamplume.sell.entity.User;
import com.dreamplume.sell.enums.LogoutEnum;
import com.dreamplume.sell.enums.RoleEnum;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.form.*;
import com.dreamplume.sell.repository.UserDao;
import com.dreamplume.sell.service.EmailServer;
import com.dreamplume.sell.service.UserService;
import com.dreamplume.sell.service.WalletService;
import com.dreamplume.sell.util.PictureUnit;
import com.dreamplume.sell.util.generator.IDGenerator;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Date 2022/4/21 10:16
 * @Created by 翊
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    UserDao userDao;

    @Resource
    WalletService walletService;

    @Resource
    EmailServer emailServer;

    // 用户登录
    @Override
    public User logon(LogonForm logonForm) {
        log.info("logonForm={}",logonForm);
        /*
            根据用户的名称进行匹配，
            之后对登录的密码进行 md5 加密与数据库中的数据进行匹配，
            同时判断登录的用户的状态是否为未注销
         */
        User user = userDao.selectOne(new QueryWrapper<User>()
                .eq("user_name", logonForm.getUserName())
                .eq("password", DigestUtils.md5DigestAsHex(logonForm.getPassword().getBytes()))
                .eq("logout", LogoutEnum.NO_LOGOFF));
//        log.info("user={}",user);

        if (ObjectUtils.isEmpty(user)) {
            log.info("【用户登录】用户名或密码错误，logonForm={}",logonForm);
            throw new SellException(SellErrorCode.USER_LOGIN_FAIL);
        }
        return user;
    }

    /**
     * 忘记密码
     * @param form 表单
     */
    @Override
    public void forgetPassword(ForgetPasswordForm form) {
        log.info("【userService:forgetPassword, form={}】", form);
        // 首先验证邮箱
        ResultVO<Object> verificationCodeResultVo = emailServer.verificationCode(form.getEmail(), form.getCode());
        if (!verificationCodeResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(verificationCodeResultVo.getCode()));
        }
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("email", form.getEmail());
        // 根据邮箱获取用户 user 对象
        User user = userDao.selectOne(wrapper);
        log.info("【userService:forgetPassword, user={}】", user);
        if (ObjectUtils.isEmpty(user)) {
            log.error("【忘记密码】未查找到指定邮箱的用户，email={}", form.getEmail());
            throw new SellException(SellErrorCode.USER_NOT_FOUND);
        }

        user.setPassword(DigestUtils.md5DigestAsHex(form.getNewPassword().getBytes()));
        if (userDao.update(user, wrapper) <= 0) {
            log.error("【忘记密码】密码重置失败");
            throw new SellException(SellErrorCode.USER_UPDATE_PASSWORD_FAIL);
        }
    }

    /**
     * 忘记支付密码
     * @param form 表单
     */
    @Override
    public void forgetPayPassword(ForgetPasswordForm form) {
        // 首先验证邮箱
        ResultVO<Object> verificationCodeResultVo = emailServer.verificationCode(form.getEmail(), form.getCode());
        if (!verificationCodeResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(verificationCodeResultVo.getCode()));
        }
        User user = userDao.selectOne(new QueryWrapper<User>().eq("email", form.getEmail()));
        log.info("【忘记支付密码】user={}", user);
        if (ObjectUtils.isEmpty(user)) {
            log.error("【查找用户id】未查询到指定用户, email={}", form.getEmail());
            throw new SellException(SellErrorCode.USER_NOT_FOUND);
        }

        // 更新支付密码
        ResultVO<Object> updatePayPasswordResultVo = walletService.updatePayPasswordByUserId(user.getUserId(), form.getNewPassword());
        if (!updatePayPasswordResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(updatePayPasswordResultVo.getCode()));
        }
    }

    /**
     * 用户修改登录密码
     * @param updateUserPasswordForm 用户更新登录密码表
     */
    @Override
    public void updatePassword(UpdateUserPasswordForm updateUserPasswordForm) {
        // 首先验证邮箱
        ResultVO<Object> verificationCodeResultVo = emailServer.verificationCode(updateUserPasswordForm.getEmail(), updateUserPasswordForm.getCode());
        if (!verificationCodeResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(verificationCodeResultVo.getCode()));
        }
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_id", updateUserPasswordForm.getUserId());
        User user = userDao.selectOne(wrapper);
        // 验证用户是否存在
        if (ObjectUtils.isEmpty(user)) {
            log.error("【更改用户密码】用户不存在,userId={}", updateUserPasswordForm.getUserId());
            throw new SellException(SellErrorCode.USER_NOT_FOUND);

            // 验证用户原密码
        } else if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(updateUserPasswordForm.getOldPassword().getBytes()))) {
            log.error("【更改用户密码】用户原密码输入错误, userId={}, oldPassword={}", updateUserPasswordForm.getUserId(), updateUserPasswordForm.getOldPassword());
            throw new SellException(SellErrorCode.USER_UPDATE_OLD_PASSWORD_ERROR);
        }

        // 设置用户新密码
        user.setPassword(DigestUtils.md5DigestAsHex(updateUserPasswordForm.getNewPassword().getBytes()));
        int update = userDao.update(user, wrapper);
        if (update <= 0) {
            log.error("【更改用户】用户更新登录密码失败");
            throw new SellException(SellErrorCode.USER_UPDATE_PASSWORD_FAIL);
        }
    }

    /**
     * 用户注册
     * @param registerForm 注册信息表
     */
    @Override
    public void register(RegisterForm registerForm) {
        if (userDao.selectOne(new QueryWrapper<User>().eq("user_name", registerForm.getUserName())) != null) {
            log.info("【用户注册】注册用户名重复, userName={}", registerForm.getUserName());
            throw new SellException(SellErrorCode.USER_NAME_REPEAT);
        } else if (userDao.selectOne(new QueryWrapper<User>().eq("email", registerForm.getEmail())) != null) {
            log.info("【用户注册】注册邮箱已被注册，email={}", registerForm.getEmail());
            throw new SellException(SellErrorCode.USER_REGISTER_EMAIL_REPEAT);
        }

        // 邮箱验证
        log.info("【用户注册】邮箱验证");
        ResultVO<Object> verificationCodeResultVo = emailServer.verificationCode(registerForm.getEmail(), registerForm.getCode());
        if (!verificationCodeResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(verificationCodeResultVo.getCode()));
        }

        User newUser = new User();
        newUser.setUserName(registerForm.getUserName());
        // 对明文的密码进行 md5 加密
        String md5Password = DigestUtils.md5DigestAsHex(registerForm.getPassword().getBytes());
        newUser.setPassword(md5Password);
        // 使用 IDGenerator 唯一 ID 生成器生成用户 ID 编码
        newUser.setUserId(IDGenerator.getInstance().getId());
        newUser.setRoleId(RoleEnum.BUYER.getCode());
        newUser.setEmail(registerForm.getEmail());
        newUser.setHeadPortrait(PictureConfig.DEFAULT_HEAD_PORTRAIT);
        newUser.setPictureFile(PictureConfig.DEFAULT_PICTURE_FILE);

        log.info("【用户注册】newUser={}",newUser);
        ResultVO<Object> createResultVo = walletService.create(newUser.getUserId());
        if (!createResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(createResultVo.getCode()));
        }
        int insert = userDao.insert(newUser);
        if (insert == 0) {
            log.error("【用户注册】注册失败，newUser={}", newUser);
            throw new SellException(SellErrorCode.USER_REGISTER_FAIL);
        }
    }

    /**
     * 用户退出登录
     * @param userId 用户 ID
     */
    @Override
    public void logout(String userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_id", userId);
        User user = userDao.selectOne(wrapper);
        // 对 Redis 中用户的 JWT 令牌进行删除
        Boolean delete = stringRedisTemplate.delete(userId);
        // 判断 JWT 令牌是否删除成功删除为是否成功用户退出的依据
        if (ObjectUtils.isEmpty(delete) || !delete) {
            log.info("【用户退出】退出失败，userId={}", userId);
            throw new SellException(SellErrorCode.USER_LOGOUT_FAIL);
        }
    }

    /**
     * 用户注销
     * @param userId 用户ID
     */
    @Override
    @Transactional
    public void logoff(String userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_id", userId);
        User user = userDao.selectOne(wrapper);
        user.setLogout(LogoutEnum.LOGOFF.getCode());
        int update = userDao.update(user, wrapper);
        Boolean delete = stringRedisTemplate.delete(userId);
        if (update <= 0 || ObjectUtils.isEmpty(delete) || !delete) {
            log.error("【用户注销】注销失败，userId={}", userId);
            throw new SellException(SellErrorCode.USER_LOGOFF_FAIL);
        }
    }

    /**
     * 用户更改用户名
     * @param updateUserForm 用户更新表对象
     */
    @Override
    public void updateUserName(UpdateUserForm updateUserForm) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_id", updateUserForm.getUserId());
        User user = userDao.selectOne(wrapper);
        log.info("【更改用户名】,user={}",user);
        if ((userDao.selectOne(new QueryWrapper<User>().eq("user_name", updateUserForm.getUserName()))) != null) {
            log.error("【更新用户名】用户名重复，updateUserForm={}", updateUserForm);
            throw new SellException(SellErrorCode.USER_UPDATE_NAME_REPLICATION);
        }
        if (!user.getUserName().equals(updateUserForm.getUserName())) {
            user.setUserName(updateUserForm.getUserName());
        }
        log.info("【更改用户名】newUser={}", user);
        if (userDao.update(user, wrapper) <= 0) {
            log.error("【用户更新用户名】用户名更改失败, updateUSerForm={}", updateUserForm);
            throw new SellException(SellErrorCode.USER_NAME_UPDATE_FAIL);
        }
    }

    /**
     * 更新用户头像
     * @param userId 用户 id
     * @param newPictureFile 新用户头像文件
     */
    @Override
    @Transactional
    public void updateUserHeadPortrait(String userId, MultipartFile newPictureFile) {
        log.info("====================impl=================");
        log.info("userId = {}", userId);
        log.info("newPictureFile = {}", newPictureFile);
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_id", userId);
        User user = userDao.selectOne(wrapper);
        log.info("【更新用户头像】user={}", user);

        // 存储用户新头像
        Map<String, String> newImageInfo = PictureUnit.storage(newPictureFile);
        log.info("【更新用户头像】newImageInfo={}",newImageInfo);

        if (!user.getHeadPortrait().equals(PictureConfig.DEFAULT_HEAD_PORTRAIT)) {
            // 删除原先用户的头像图片文件 (默认头像则不删除)
            log.info("【更新用户头像】用户旧头像文件名称={}", user.getPictureFile());
            PictureUnit.deleteImage(user.getPictureFile());
        }

        // 更新用户的头像图片
        user.setHeadPortrait(newImageInfo.get(PictureConfig.PICTURE_URL));
        user.setPictureFile(newImageInfo.get(PictureConfig.PICTURE_FILE_NAME));
        if (userDao.update(user, wrapper) <= 0) {
            log.error("【更新用户头像】更新失败");
            throw new SellException(SellErrorCode.USER_UPDATE_HEAD_PORTRAIT_FAIL);
        }
    }

    // 返回商家用户
    public User findBusiness() {
        User business = userDao.selectOne(new QueryWrapper<User>().eq("role_id", RoleEnum.BUSINESS.getCode()));
        if (ObjectUtils.isEmpty(business)) {
            log.error("【查找商家】未查询到商家用户");
            throw new SellException(SellErrorCode.USER_NOT_FOUND);
        }
        return business;
    }

    @Override
    public List<String> findAllUserEmail() {
        return userDao.findAllUserEmail(RoleEnum.BUYER.getCode());
    }
}
