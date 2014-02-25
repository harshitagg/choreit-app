package com.pineapps.choreit.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.LocalDate;

import java.util.UUID;

public class Chore {
    private String id;
    private String title;
    private String description;
    private String dueDate;

    public Chore(String title, String description, String dueDate) {
        this.dueDate = dueDate;
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
    }

    public Chore(String id, String title, String description, String dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public String dueDate() {
        return dueDate;
    }

    public LocalDate dueDateAsLocalDate() {
        return LocalDate.parse(dueDate);
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
