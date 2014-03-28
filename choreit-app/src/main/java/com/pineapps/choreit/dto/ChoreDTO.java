package com.pineapps.choreit.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChoreDTO {
    private final String chore_id;
    private final String title;
    private final String description;
    private final String due_date;
    private final boolean is_done;

    public ChoreDTO(String chore_id, String title, String description, String due_date, boolean is_done) {
        this.chore_id = chore_id;
        this.title = title;
        this.description = description;
        this.due_date = due_date;
        this.is_done = is_done;
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
