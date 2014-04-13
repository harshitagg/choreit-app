package com.pineapps.choreit.util;

public class Session {
    private String password;
    private String repositoryName = "ChoreIt.db";

    public String password() {
        return password;
    }

    public String repositoryName() {
        return repositoryName;
    }

    public Session setPassword(String password) {
        this.password = password;
        return this;
    }

    public Session setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }
}
