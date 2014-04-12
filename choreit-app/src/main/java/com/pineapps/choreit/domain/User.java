package com.pineapps.choreit.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class User {
    private String emailId;
    private String name;
    private boolean isSynced;

    public User(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
        this.isSynced = false;
    }

    public User(String name, String emailId, boolean isSynced) {
        this.emailId = emailId;
        this.name = name;
        this.isSynced = isSynced;
    }

    public String emailId() {
        return emailId;
    }

    public String name() {
        return name;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void markAsSynced() {
        this.isSynced = true;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
