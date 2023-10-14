package com.site.blog.my.core.entity;

import org.springframework.stereotype.Component;

@Component
public class AdminUser {
    private String email;

    private String password;

    private String fName;

    private String lName;

    private String pName;

    private Byte locked;

    public String getAccount() { return email; }

    public void setAccount(String email) {
        this.email = email;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName == null ? null : fName.trim();
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName == null ? null : lName.trim();
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    public Byte getLocked() {
        return locked;
    }

    public void setLocked(Byte locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", firstName=").append(fName);
        sb.append(", lastName=").append(lName);
        sb.append(", preferredName=").append(pName);
        sb.append(", locked=").append(locked);
        sb.append("]");
        return sb.toString();
    }
}