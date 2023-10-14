package com.site.blog.my.core.entity;

import java.util.Date;

public class ConfirmationToken {

    private Long tokenId;

    private String tokenContent;

    private Date createTime;

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenContent() {
        return tokenContent;
    }

    public void setTokenContent(String tokenContent) {
        this.tokenContent = tokenContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ConfirmationToken{" +
                "tokenId=" + tokenId +
                ", tokenContent='" + tokenContent + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
