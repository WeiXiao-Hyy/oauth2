package com.alipay.auth.service.impl;

import com.alipay.auth.common.Constants;
import com.alipay.auth.common.err.BizException;
import com.alipay.auth.dao.mapper.AuthClientDetailsMapper;
import com.alipay.auth.domain.AuthClientDetails;
import com.alipay.auth.domain.User;
import com.alipay.auth.enums.ErrorCodeEnum;
import com.alipay.auth.enums.ExpireEnum;
import com.alipay.auth.enums.GrantTypeEnum;
import com.alipay.auth.service.AuthorizationService;

import com.alipay.auth.service.RedisService;
import com.alipay.auth.service.req.AuthClientAuthorizeReq;
import com.alipay.auth.service.req.AuthClientRegisterReq;
import com.alipay.auth.service.req.AuthClientTokenReq;
import com.alipay.auth.utils.DateUtils;
import com.alipay.auth.utils.EncryptUtils;
import com.alipay.auth.utils.SpringContextUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @Description
 * @create 2024-04-01 12:40
 */
@Service("authorizationServiceImpl")
public class AuthorizationServiceImpl implements AuthorizationService {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Autowired
    private AuthClientDetailsMapper authClientDetailsMapper;

    @Override
    public boolean register(AuthClientRegisterReq request) {
        //客户端的名称为回调地址不能为空
        if (!request.checkParam()) {
            return false;
        }
        //生成24位随机的clientId
        //TODO: 保证唯一性
        String clientId = EncryptUtils.getRandomStr1(20);

        //生成32位随机的clientSecret
        String clientSecret = EncryptUtils.getRandomStr1(32);

        //当前时间
        Date current = new Date();

        //获取当前用户
        HttpSession session = SpringContextUtils.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        //保存注册信息
        AuthClientDetails authClientDetails = AuthClientDetails.builder()
                .clientId(clientId)
                .clientName(request.getName())
                .clientSecret(clientSecret)
                .redirectUri(request.getRedirectUri())
                .description(request.getDescription())
                .createUser(user.getId())
                .createTime(current)
                .updateUser(user.getId())
                .updateTime(current)
                .build();
        int res = authClientDetailsMapper.insertSelective(authClientDetails);

        return res > 0;
    }

    @Override
    public String authorize(AuthClientAuthorizeReq req) {
        //从Session获取User
        User user = (User) SpringContextUtils.getSession().getAttribute(Constants.SESSION_USER);

        //客户端ID
        String clientId = req.getClientId();
        //权限范围
        String scope = req.getScope();
        //回调URL
        String redirectUri = req.getRedirectUri();
        //status,用于防止CSRF攻击(非必填)
        String status = req.getStatus();

        String authorizationCode = createAuthorizationCode(clientId, scope, user);

        String params = "?code=" + authorizationCode;
        if (StringUtils.isNoneBlank(status)) {
            params = params + "&status=" + status;
        }

        return redirectUri + params;
    }

    @Override
    public String token(AuthClientTokenReq request) {
        //校验授权方式
        if (!GrantTypeEnum.AUTHORIZATION_CODE.getType().equals(request.getGrantType())) {
            throw new BizException(ErrorCodeEnum.INVALID_GRANT.getError());
        }

        //TODO:校验请求的客户端密钥和注册的密钥是否匹配

        String code = request.getCode();

        //从Redis获取允许访问的用户权限范围
        String scope = redisService.get(code + ":scope");
        //从Redis获取对应的用户信息
        User user = redisService.get(code + ":user");

        //如果能够通过Authorization Code获取到对应的用户消息，则说明code有效
        if (StringUtils.isNoneBlank(scope) && Objects.nonNull(user)) {
            //过期时间
            Long expiresIn = DateUtils.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());

            //TODO: 生成AccessToken
        }

        //TODO: 待实现
        return "";
    }

    private String createAuthorizationCode(String clientId, String scope, User user) {
        //1. 拼装加密字符串(clientId+scope+当前精确到毫秒的时间戳)
        String str = clientId + scope + DateUtils.currentTimeMillis();

        //2. SHA1加密
        String encryptedStr = EncryptUtils.sha1Hex(str);

        //3.1 保存本次请求的授权范围
        redisService.setWithExpire(encryptedStr + ":user", user, ExpireEnum.AUTHORIZATION_CODE.getTime(),
                ExpireEnum.AUTHORIZATION_CODE.getTimeUnit());
        //3.2 保存本次请求的用户信息
        redisService.setWithExpire(encryptedStr + ":scope", scope, ExpireEnum.AUTHORIZATION_CODE.getTime(),
                ExpireEnum.AUTHORIZATION_CODE.getTimeUnit());

        return encryptedStr;
    }

    private String createAccessToken(User user, AuthClientDetails savedAuthClientDetails, String grantType,
                                     String scope, Long expiresIn) {
        Date current = new Date();

        //过期的时间戳
        Long expiresAt = DateUtils.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);

        //1. 拼接待加密字符串(username + clientId + 当前毫秒时间戳)
        String str = user.getUsername() + savedAuthClientDetails.getClientId() + DateUtils.currentDateStr();

        //2. SHA1加密
        String accessToken = "1." + EncryptUtils.sha1Hex(str) + "." + expiresIn + "." + expiresAt;

        //3. TODO: 保存AccessToken
        return "";
    }


}