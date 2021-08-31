package com.orange_yishenggong.wiki.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserResetPasswordReq {
    private Long id;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserResetPasswordReq{");
        sb.append("id=").append(id);
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}