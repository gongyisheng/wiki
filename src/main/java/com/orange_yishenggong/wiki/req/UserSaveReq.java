package com.orange_yishenggong.wiki.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserSaveReq {
    private Long id;

    @NotNull(message = "[用户名]不得为空")
    @NotEmpty(message = "[用户名]不得为空")
    private String loginName;

    @NotNull(message = "[昵称]不得为空")
    @NotEmpty(message = "[昵称]不得为空")
    private String nickname;

    @NotNull(message = "[密码]不得为空")
    @NotEmpty(message = "[密码]不得为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "[密码]至少包含数字和英文字母，长度6~32位")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", loginName=").append(loginName);
        sb.append(", nickname=").append(nickname);
        sb.append(", password=").append(password);
        sb.append("]");
        return sb.toString();
    }
}