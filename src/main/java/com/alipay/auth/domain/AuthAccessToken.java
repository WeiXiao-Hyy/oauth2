package com.alipay.auth.domain;

import java.util.Date;
import lombok.Builder;

@Builder
public class AuthAccessToken {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.access_token
     *
     * @mbg.generated
     */
    private String accessToken;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.user_id
     *
     * @mbg.generated
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.user_name
     *
     * @mbg.generated
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.client_id
     *
     * @mbg.generated
     */
    private String clientId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.expires_in
     *
     * @mbg.generated
     */
    private Long expiresIn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.grant_type
     *
     * @mbg.generated
     */
    private String grantType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.scope
     *
     * @mbg.generated
     */
    private String scope;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.create_user
     *
     * @mbg.generated
     */
    private Integer createUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.update_user
     *
     * @mbg.generated
     */
    private Integer updateUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_access_token.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.id
     *
     * @return the value of auth_access_token.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.id
     *
     * @param id the value for auth_access_token.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.access_token
     *
     * @return the value of auth_access_token.access_token
     *
     * @mbg.generated
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.access_token
     *
     * @param accessToken the value for auth_access_token.access_token
     *
     * @mbg.generated
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.user_id
     *
     * @return the value of auth_access_token.user_id
     *
     * @mbg.generated
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.user_id
     *
     * @param userId the value for auth_access_token.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.user_name
     *
     * @return the value of auth_access_token.user_name
     *
     * @mbg.generated
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.user_name
     *
     * @param userName the value for auth_access_token.user_name
     *
     * @mbg.generated
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.client_id
     *
     * @return the value of auth_access_token.client_id
     *
     * @mbg.generated
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.client_id
     *
     * @param clientId the value for auth_access_token.client_id
     *
     * @mbg.generated
     */
    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.expires_in
     *
     * @return the value of auth_access_token.expires_in
     *
     * @mbg.generated
     */
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.expires_in
     *
     * @param expiresIn the value for auth_access_token.expires_in
     *
     * @mbg.generated
     */
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.grant_type
     *
     * @return the value of auth_access_token.grant_type
     *
     * @mbg.generated
     */
    public String getGrantType() {
        return grantType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.grant_type
     *
     * @param grantType the value for auth_access_token.grant_type
     *
     * @mbg.generated
     */
    public void setGrantType(String grantType) {
        this.grantType = grantType == null ? null : grantType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.scope
     *
     * @return the value of auth_access_token.scope
     *
     * @mbg.generated
     */
    public String getScope() {
        return scope;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.scope
     *
     * @param scope the value for auth_access_token.scope
     *
     * @mbg.generated
     */
    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.create_user
     *
     * @return the value of auth_access_token.create_user
     *
     * @mbg.generated
     */
    public Integer getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.create_user
     *
     * @param createUser the value for auth_access_token.create_user
     *
     * @mbg.generated
     */
    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.create_time
     *
     * @return the value of auth_access_token.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.create_time
     *
     * @param createTime the value for auth_access_token.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.update_user
     *
     * @return the value of auth_access_token.update_user
     *
     * @mbg.generated
     */
    public Integer getUpdateUser() {
        return updateUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.update_user
     *
     * @param updateUser the value for auth_access_token.update_user
     *
     * @mbg.generated
     */
    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_access_token.update_time
     *
     * @return the value of auth_access_token.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_access_token.update_time
     *
     * @param updateTime the value for auth_access_token.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_access_token
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", accessToken=").append(accessToken);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", clientId=").append(clientId);
        sb.append(", expiresIn=").append(expiresIn);
        sb.append(", grantType=").append(grantType);
        sb.append(", scope=").append(scope);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}