package com.orange_yishenggong.wiki.resp;

public class UserLoginResp {
    private Long id;

    private String loginName;

    private String nickname;

    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserLoginResp{");
        sb.append("id=").append(id);
        sb.append(", loginName='").append(loginName).append('\'');
        sb.append(", nickname='").append(nickname).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}