package com.alipay.authserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.authserver.domain.AuthorizationResp;
import com.alipay.authserver.domain.User;
import com.alipay.authserver.service.LoginService;
import com.alipay.authserver.service.req.RequestTokenDTO;
import com.alipay.authserver.service.resp.LoginResp;
import com.alipay.authcommon.constants.Constants;
import com.alipay.authcommon.enums.ErrorCodeEnum;
import com.alipay.authcommon.enums.GrantTypeEnum;
import com.alipay.authcommon.err.BizException;
import com.alipay.authcommon.utils.EncryptUtils;
import com.alipay.authcommon.utils.JsonUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author hyy
 * @Description
 * @create 2024-04-06 10:02
 */

@Service("loginServiceImpl")
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Value("${own.oauth2.client-id}")
    private String clientId;

    @Value("${own.oauth2.scope}")
    private String scope;

    @Value("${own.oauth2.client-secret}")
    private String clientSecret;

    @Value("${own.oauth2.user-authorization-uri}")
    private String authorizationUri;

    @Value("${own.oauth2.access-token-uri}")
    private String accessTokenUri;

    @Value("${own.oauth2.resource.userInfoUri}")
    private String userInfoUri;

    @Override
    public LoginResp login(HttpServletRequest request) {
        //当前系统登录成功之后的回调URL
        String redirectUrl = request.getParameter("redirectUrl");

        //当前系统请求认证服务器成功之后返回的Authorization Code
        String code = request.getParameter("code");
        String resUri = "";

        HttpSession session = request.getSession();
        //当前请求路径
        String currentUrl = request.getRequestURL().toString();

        //code为空，则说明当前请求不是认证服务器的回调请求，则重定向URL到认证服务器登录
        if (StringUtils.isBlank(code)) {
            //如果存在回调URL，则将这个URL添加到Session，方便在用户登录后重定向到之前指定的URL
            if (StringUtils.isNotBlank(redirectUrl)) {
                session.setAttribute(Constants.SESSION_LOGIN_REDIRECT_URL, redirectUrl);
            }
            //生成随机的状态码，用于防止CSRF攻击
            String status = EncryptUtils.getRandomStr1(6);
            session.setAttribute(Constants.SESSION_AUTH_CODE_STATUS, status);
            //拼装请求Authorization Code的地址
            //http://127.0.0.1:7001/oauth2.0/authorize?client_id={0}&response_type=code&scope=super&&status={1}&redirect_uri={2}
            resUri = MessageFormat.format(authorizationUri, clientId, status, currentUrl);

            return LoginResp.builder()
                    .redirectUri(resUri)
                    .build();
        } else {
            //通过Authorization Code获取Access Token
            //http://127.0.0.1:7001/oauth2.0/token?client_id={1}&client_secret={2}&grant_type=authorization_code&code={3}&redirect_uri={4}
            AuthorizationResp resp = getAccessTokenByCode(accessTokenUri, clientId, clientSecret, code, currentUrl);

            //如果正常返回
            if (StringUtils.isNotBlank(resp.getAccess_token())) {
                log.info("Rest client get resp access_token = {}", resp.getAccess_token());

                //2.1 将Access Token存到session中
                session.setAttribute(Constants.SESSION_ACCESS_TOKEN, resp.getAccess_token());

                //2.2 再次查询用户基础信息，并将用户ID存到session中
                //http://127.0.0.1:7001/api/users/getInfo?access_token={1}
                User user = getUserInfoByAccessToken(userInfoUri, resp.getAccess_token());

                if (Objects.nonNull(user)) {
                    session.setAttribute(Constants.SESSION_USER, user);
                    log.info("Get user info by access_token, user={}", user);
                }
            }

            return LoginResp.builder()
                    .token(resp.getAccess_token())
                    .build();
        }
    }

    private AuthorizationResp getAccessTokenByCode(String accessTokenUri, String clientId, String clientSecret, String code, String redirectUrl) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestTokenDTO requestTokenDTO = RequestTokenDTO.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .redirectUri(redirectUrl)
                .grantType(GrantTypeEnum.AUTHORIZATION_CODE.getType())
                .build();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(requestTokenDTO));

        Request request = new Request.Builder()
                .url(accessTokenUri)
                .post(body)
                .build();

        AuthorizationResp resp;
        try (Response response = client.newCall(request).execute()) {
            resp = JsonUtils.fromJson(response.body().string(), AuthorizationResp.class);
            log.info("Get access_token by code resp={}", resp);
        } catch (IOException e) {
            throw new BizException(ErrorCodeEnum.UNKNOWN_ERROR.getError());
        }
        return resp;
    }

    private User getUserInfoByAccessToken(String uri, String token) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(uri + "?access_token=" + token)
                .build();

        User user;
        try (Response response = client.newCall(request).execute()) {
            user = JsonUtils.fromJson(response.body().string(), User.class);
        } catch (IOException e) {
            throw new BizException(ErrorCodeEnum.UNKNOWN_ERROR.getError());
        }
        return user;
    }
}