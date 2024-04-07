package com.alipay.authserver.service.impl;

import com.alipay.authcommon.constants.Constants;
import com.alipay.authcommon.enums.ErrorCodeEnum;
import com.alipay.authcommon.enums.ExpireEnum;
import com.alipay.authcommon.enums.GrantTypeEnum;
import com.alipay.authcommon.err.BizException;
import com.alipay.authcommon.utils.DateUtils;
import com.alipay.authcommon.utils.EncryptUtils;
import com.alipay.authcommon.utils.SpringContextUtils;
import com.alipay.authserver.dao.mapper.AuthAccessTokenMapper;
import com.alipay.authserver.dao.mapper.AuthClientDetailsMapper;
import com.alipay.authserver.dao.mapper.AuthClientUserMapper;
import com.alipay.authserver.dao.mapper.AuthRefreshTokenMapper;
import com.alipay.authserver.dao.mapper.UserMapper;
import com.alipay.authserver.domain.AuthAccessToken;
import com.alipay.authserver.domain.AuthClientDetails;
import com.alipay.authserver.domain.AuthClientUser;
import com.alipay.authserver.domain.AuthRefreshToken;
import com.alipay.authserver.domain.User;
import com.alipay.authserver.service.AuthorizationService;
import com.alipay.authserver.service.RedisService;
import com.alipay.authserver.service.req.AuthClientAgreeReq;
import com.alipay.authserver.service.req.AuthClientAuthorizeReq;
import com.alipay.authserver.service.req.AuthClientRefreshTokenReq;
import com.alipay.authserver.service.req.AuthClientRegisterReq;
import com.alipay.authserver.service.req.AuthClientTokenReq;
import com.alipay.authserver.service.res.AuthClientAgreeResp;
import com.alipay.authserver.service.res.AuthClientRefreshTokenResp;
import com.alipay.authserver.service.res.AuthClientTokenResp;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
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

    @Autowired
    private AuthAccessTokenMapper authAccessTokenMapper;

    @Autowired
    private AuthRefreshTokenMapper authRefreshTokenMapper;

    @Autowired
    private AuthClientUserMapper authClientUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(AuthClientRegisterReq request) {

        //客户端的名称为回调地址不能为空
        if (!request.checkParam()) {
            throw new BizException(ErrorCodeEnum.INVALID_REQUEST.getError());
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
    public AuthClientAgreeResp agree(AuthClientAgreeReq req) {
        User user = (User) SpringContextUtils.getSession().getAttribute(Constants.SESSION_USER);

        AuthClientUser authClientUser = AuthClientUser.builder()
                .authClientId(req.getClientId())
                .userId(user.getId())
                .authScopeId(req.getScope())
                .build();

        // 保存授权信息
        int cnt = authClientUserMapper.insert(authClientUser);

        return AuthClientAgreeResp.builder()
                .success(cnt > 0)
                .build();
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
    public AuthClientTokenResp token(AuthClientTokenReq request) {
        //校验授权方式
        if (!GrantTypeEnum.AUTHORIZATION_CODE.getType().equals(request.getGrantType())) {
            throw new BizException(ErrorCodeEnum.INVALID_GRANT.getError());
        }

        AuthClientDetails savedAuthClientDetails = authClientDetailsMapper.selectByClientId(request.getClientId());

        //校验client_secret
        if (Objects.isNull(savedAuthClientDetails) || !savedAuthClientDetails.getClientSecret().equals(request.getClientSecret())) {
            throw new BizException(ErrorCodeEnum.INVALID_CLIENT.getError());
        }

        //校验回调URL
        if (!savedAuthClientDetails.getRedirectUri().equals(request.getRedirectUri())) {
            throw new BizException(ErrorCodeEnum.REDIRECT_URI_MISMATCH.getError());
        }

        String code = request.getCode();

        //从Redis获取允许访问的用户权限范围
        String scope = redisService.get(code + ":scope");
        //从Redis获取对应的用户信息
        User user = redisService.get(code + ":user");

        //如果能够通过Authorization Code获取到对应的用户消息，则说明code有效
        if (StringUtils.isBlank(scope) || Objects.isNull(user)) {
            throw new BizException(ErrorCodeEnum.INVALID_GRANT.getError());
        }

        //过期时间
        Long expiresIn = DateUtils.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());

        //生成AccessToken
        String accessTokenStr = createAccessToken(user, savedAuthClientDetails, request.getGrantType(), scope, expiresIn);

        //查询插入到数据库的Access Token
        AuthAccessToken accessToken = authAccessTokenMapper.selectByAccessToken(accessTokenStr);

        //生成Refresh Token
        String refreshTokenStr = createRefreshToken(user, accessToken);

        //构造返回结果
        return AuthClientTokenResp.builder()
                .accessToken(accessTokenStr)
                .expiresIn(expiresIn)
                .refreshToken(refreshTokenStr)
                .scope(scope)
                .build();
    }

    @Override
    public AuthClientRefreshTokenResp refreshToken(AuthClientRefreshTokenReq request) {

        AuthRefreshToken authRefreshToken = authRefreshTokenMapper.selectByRefreshToken(request.getRefreshToken());
        if (Objects.isNull(authRefreshToken)) {
            throw new BizException(ErrorCodeEnum.INVALID_GRANT.getError());
        }

        Long saveExpiresAt = authRefreshToken.getExpiresIn();

        //过期日期
        LocalDateTime expiresDateTime = DateUtils.ofEpochSecond(saveExpiresAt, null);

        //当前日期
        LocalDateTime nowDateTime = DateUtils.now();

        //如果refresh_token已经失效,则需要重新生成
        if (expiresDateTime.isBefore(nowDateTime)) {
            throw new BizException(ErrorCodeEnum.EXPIRED_TOKEN.getError());
        }

        //获取存储的Access Token
        AuthAccessToken authAccessToken = authAccessTokenMapper.selectByPrimaryKey(authRefreshToken.getTokenId());
        if (Objects.isNull(authAccessToken)) {
            throw new BizException(ErrorCodeEnum.UNKNOWN_ERROR.getError());
        }

        //获取对应的客户端信息
        AuthClientDetails saveClientDetails = authClientDetailsMapper.selectByPrimaryKey(authAccessToken.getClientId());
        if (Objects.isNull(saveClientDetails)) {
            throw new BizException(ErrorCodeEnum.UNKNOWN_ERROR.getError());
        }

        //获取对应的用户信息
        User user = userMapper.selectByPrimaryKey(authAccessToken.getUserId());
        if (Objects.isNull(user)) {
            throw new BizException(ErrorCodeEnum.UNKNOWN_ERROR.getError());
        }

        //新的过期时间
        Long expiresIn = DateUtils.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());

        //新的access_token
        String newAccessTokenStr = createAccessToken(user, saveClientDetails, authAccessToken.getGrantType(),
                authAccessToken.getScope(), expiresIn);

        //返回结果
        return AuthClientRefreshTokenResp.builder()
                .accessToken(newAccessTokenStr)
                .refreshToken(request.getRefreshToken())
                .expiresIn(expiresIn)
                .scope(authAccessToken.getScope())
                .build();
    }

    @Override
    public AuthAccessToken selectByAccessToken(String accessToken) {
        return authAccessTokenMapper.selectByAccessToken(accessToken);
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

        //3. 保存Access Token
        AuthAccessToken savedAccessToken = authAccessTokenMapper.selectByUserIdClientIdScope(user.getId(),
                savedAuthClientDetails.getId(), scope);

        //如果存在userId+clientId+scope的记录，则更新，否则直接插入
        if (Objects.nonNull(savedAccessToken)) {
            savedAccessToken.setAccessToken(accessToken);
            savedAccessToken.setExpiresIn(expiresIn);
            savedAccessToken.setUpdateUser(user.getId());
            savedAccessToken.setUpdateTime(current);
            authAccessTokenMapper.updateByPrimaryKeySelective(savedAccessToken);
        } else {
            AuthAccessToken newToken = AuthAccessToken.builder()
                    .accessToken(accessToken)
                    .userId(user.getId())
                    .userName(user.getUsername())
                    //AuthClientDetails的外键id
                    .clientId(savedAuthClientDetails.getId())
                    .expiresIn(expiresAt)
                    .scope(scope)
                    .grantType(grantType)
                    .createUser(user.getId())
                    .createTime(current)
                    .updateUser(user.getId())
                    .updateTime(current)
                    .build();
            authAccessTokenMapper.insertSelective(newToken);
        }

        return accessToken;
    }

    private String createRefreshToken(User user, AuthAccessToken accessToken) {

        Date current = new Date();
        //过期时间
        Long expiresIn = DateUtils.dayToSecond(ExpireEnum.REFRESH_TOKEN.getTime());
        //过期的时间戳
        Long expiresAt = DateUtils.nextDaysSecond(ExpireEnum.REFRESH_TOKEN.getTime(), null);

        //1. 拼装待加密字符串（username + accessToken + 当前精确到毫秒的时间戳）
        String str = user.getUsername() + accessToken.getAccessToken() + DateUtils.currentTimeMillis();

        //2. SHA1加密
        String refreshTokenStr = "2." + EncryptUtils.sha1Hex(str) + "." + expiresIn + "." + expiresAt;

        //3. 保存Refresh Token
        AuthRefreshToken savedRefreshToken = authRefreshTokenMapper.selectByTokenId(accessToken.getId());

        //如果存在tokenId匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (savedRefreshToken != null) {
            savedRefreshToken.setRefreshToken(refreshTokenStr);
            savedRefreshToken.setExpiresIn(expiresAt);
            savedRefreshToken.setUpdateUser(user.getId());
            savedRefreshToken.setUpdateTime(current);
            authRefreshTokenMapper.updateByPrimaryKeySelective(savedRefreshToken);
        } else {
            AuthRefreshToken refreshToken = AuthRefreshToken.builder()
                    .tokenId(accessToken.getId())
                    .refreshToken(refreshTokenStr)
                    .expiresIn(expiresAt)
                    .createUser(user.getId())
                    .updateUser(user.getId())
                    .createTime(current)
                    .updateTime(current)
                    .build();
            authRefreshTokenMapper.insertSelective(refreshToken);
        }

        //4. 返回Refresh Token
        return refreshTokenStr;
    }


}