package com.reggie.takeout.controller;

import com.alibaba.fastjson.JSONObject;
import com.reggie.takeout.dto.UserLoginReqDto;
import com.reggie.takeout.dto.UserLoginRspDto;
import com.reggie.takeout.dto.UserSendMsgReqDto;
import com.reggie.takeout.service.UserService;
import com.reggie.takeout.utils.R;
import com.reggie.takeout.vo.User;
import com.reggie.takeout.vo.UserLoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @Author gett
 * @Date 2022/4/26  8:58
 * @Description  用户信息
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @Description 发送手机验证码
     * @Author gett
     */
    @RequestMapping(value = "/sendMsg",method = RequestMethod.POST)
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        logger.info("发送手机验证码入参{}", JSONObject.toJSONString(user));

        try {
            UserSendMsgReqDto userSendMsgReqDto = new UserSendMsgReqDto();
            BeanUtils.copyProperties(user,userSendMsgReqDto);

            String msg=userService.sendMsg(userSendMsgReqDto);

            //session.setAttribute(userSendMsgReqDto.getPhone(), msg);
            redisTemplate.opsForValue().set(userSendMsgReqDto.getPhone(),msg,5, TimeUnit.MINUTES);

            return R.success("发送手机验证码成功");

        }catch (Exception e) {
            logger.info("发送手机验证码异常{}", e);
            return R.error("发送手机验证码异常");
        }

    }


    /**
     * @Description 登录
     * @Author gett
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public R<UserLoginRspDto> login(@RequestBody UserLoginVo userLoginVo, HttpSession session){

        logger.info("登录入参{}", JSONObject.toJSONString(userLoginVo));

        try {

            UserLoginRspDto userLoginRspDto=null;
            String phone = userLoginVo.getPhone();

            String code = userLoginVo.getCode();

            //Object codeInSession = session.getAttribute(phone);
            Object codeInSession = redisTemplate.opsForValue().get(phone);


            if(codeInSession != null && codeInSession.equals(code)){
                UserLoginReqDto userLoginReqDto = new UserLoginReqDto();
                BeanUtils.copyProperties(userLoginVo,userLoginReqDto);
                userLoginRspDto=userService.login(userLoginReqDto);
                session.setAttribute("user",userLoginRspDto.getId());
                redisTemplate.delete(phone);
            }

            return R.success(userLoginRspDto);

        }catch (Exception e) {
            logger.info("登录异常{}", e);
            return R.error("登录异常");
        }

    }

    /**
     * @Description 退出
     * @Author gett
     */
    @RequestMapping(value = "/loginout", method = RequestMethod.POST)
    public R<String> loginout(HttpServletRequest request) {

        try {

            //①. 清理Session中的用户id
            request.getSession().removeAttribute("user");

            //②. 返回结果
            return R.success("退出成功");

        }catch (Exception e){
            logger.info("退出异常",e);
            return R.error("退出异常");
        }

    }


}
