package com.pineapps.choreit.domain;

public enum FetchStatus implements Displayable {
    success("Update successful."), nothingFetched("Already up to date."), failure("Update failed. Please try again.");
    private String displayValue;

    private FetchStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String displayValue() {
        return displayValue;
    }
}
