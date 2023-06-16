package com.reggie.takeout.service;

import com.reggie.takeout.dto.UserLoginReqDto;
import com.reggie.takeout.dto.UserLoginRspDto;
import com.reggie.takeout.dto.UserSendMsgReqDto;

/**
 * @Author gett
 * @Date 2022/4/26  8:56
 * @Description 用户信息
 */

public interface UserService {

    /**
     * @Description 发送手机验证码
     * @Author gett
     */
    String sendMsg(UserSendMsgReqDto userSendMsgReqDto) throws Exception;

    /**
     * @Description 登录
     * @Author gett
     */
    UserLoginRspDto login(UserLoginReqDto userLoginReqDto) throws Exception;
}
