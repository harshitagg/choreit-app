package com.pineapps.choreit.domain;

import java.util.UUID;

public class Chore {
    private String id;
    private String title;
    private String description;

    public Chore(String title, String description) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
    }

    public Chore(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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
}
