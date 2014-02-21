package com.pineapps.choreit.service;

import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.repository.ChoreRepository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class ChoreService {
    private ChoreRepository choreRepository;

    public ChoreService(ChoreRepository choreRepository) {
        this.choreRepository = choreRepository;
    }

    public void addChore(String name, String description) {
        choreRepository.insert(new Chore(name, description));
    }

    public List<Chore> getAllChores() {
        return choreRepository.getAll();
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
