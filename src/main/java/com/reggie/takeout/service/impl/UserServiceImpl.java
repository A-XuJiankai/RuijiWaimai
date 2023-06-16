package com.reggie.takeout.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.AddShortUrlRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dao.UserDao;
import com.reggie.takeout.dto.UserLoginReqDto;
import com.reggie.takeout.dto.UserLoginRspDto;
import com.reggie.takeout.dto.UserSendMsgReqDto;
import com.reggie.takeout.po.UserPo;
import com.reggie.takeout.service.UserService;
import com.reggie.takeout.utils.BusinessException;
import com.reggie.takeout.utils.SMSUtils;
import com.reggie.takeout.utils.ValidateCodeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @Author gett
 * @Date 2022/4/26  8:57
 * @Description 用户信息
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserPo> implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Value("${msg.accessKeyId}")
    private String accessKeyId;

    @Value("${msg.accessKeySecret}")
    private String accessKeySecret;


    /**
     * @Description 发送手机验证码
     * @Author gett
     */
    @Override
    public String sendMsg(UserSendMsgReqDto userSendMsgReqDto) throws Exception {

        logger.info("发送手机验证码入参{}", JSONObject.toJSONString(userSendMsgReqDto));

        try {
            String code=null;
            String phone = userSendMsgReqDto.getPhone();
            if (StringUtils.isNotBlank(phone)){
                code = ValidateCodeUtils.generateValidateCode(4).toString();
                logger.info("code{}",code);
                Client client = SMSUtils.createClient(accessKeyId, accessKeySecret);
                //AddShortUrlRequest addShortUrlRequest = new AddShortUrlRequest()
                //        .setResourceOwnerAccount("your_value")
                //        .setResourceOwnerId(1L)
                //        .setSourceUrl("your_value")
                //        .setShortUrlName("your_value");


                SendSmsRequest sendSmsRequest = new SendSmsRequest()
                			.setSignName("阿里云短信测试")
                			.setTemplateCode("SMS_154950909")
                			.setPhoneNumbers(phone)
                			.setTemplateParam("{\"code\":\""+code+"\"}");
                //	// 复制代码运行请自行打印 API 的返回值
                	client.sendSms(sendSmsRequest);
            }
            return code;

        }catch (Exception e){
            logger.error("发送手机验证码异常{}", e);
            throw new BusinessException("发送手机验证码异常");
        }

    }

    /**
     * @Description 登录
     * @Author gett
     */
    @Override
    public UserLoginRspDto login(UserLoginReqDto userLoginReqDto) throws Exception{
        logger.info("登录入参{}", JSONObject.toJSONString(userLoginReqDto));
        try {

            UserLoginRspDto userLoginRspDto = new UserLoginRspDto();

            LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserPo::getPhone,userLoginReqDto.getPhone());
            wrapper.eq(UserPo::getStatus,1);

            UserPo userPo = userDao.selectOne(wrapper);

            if (ObjectUtils.isEmpty(userPo)){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                userPo= new UserPo();
                userPo.setStatus(1);
                userPo.setPhone(userLoginReqDto.getPhone());
                userDao.insert(userPo);
            }
            BeanUtils.copyProperties(userPo,userLoginRspDto);

            return userLoginRspDto;

        }catch (Exception e){
            logger.error("登录异常{}", e);
            throw new BusinessException("登录异常");
        }

    }
}
