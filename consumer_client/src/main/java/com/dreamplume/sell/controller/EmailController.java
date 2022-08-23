package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.service.EmailServer;
import com.dreamplume.sell.service.UserService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Classname EmailController
 * @Description TODO
 * @Date 2022/5/8 12:34
 * @Created by 翊
 */
@RestController
@RequestMapping("/email")
@Slf4j
public class EmailController {

    @Resource
    EmailServer emailServer;

    @Resource
    UserService userService;

    // 发送用户验证码
    @PostMapping("/send/code")
    public ResultVO<Object> sendCode(@RequestParam("email") String recipientEmail) {
        return emailServer.sendEmail(recipientEmail);
    }

    // 推送消息给所有用户
    @PostMapping("/send/message")
    public ResultVO<Object> sendMessage(@RequestParam("pictureUrl") String pictureUrl) {
        log.info("pictureUrl={}",pictureUrl);
        ResultVO<Object> findAllUserEmailResultVo = userService.findAllUserEmail();
        if (!findAllUserEmailResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findAllUserEmailResultVo.getCode()));
        }
        return emailServer.sendMessage(JSON.parseArray(JSONArray.toJSONString(findAllUserEmailResultVo.getData()), String.class), pictureUrl);
    }
}
