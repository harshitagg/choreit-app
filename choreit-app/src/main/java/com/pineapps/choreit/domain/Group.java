package com.pineapps.choreit.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.UUID;

public class Group {
    private String id;
    private String name;
    private List<User> users;
    private boolean isSynced;

    public Group(String name, List<User> users) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.users = users;
        this.isSynced = false;
    }

    public Group(String id, String name, List<User> users, boolean isSynced) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.isSynced = isSynced;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<User> users() {
        return users;
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
