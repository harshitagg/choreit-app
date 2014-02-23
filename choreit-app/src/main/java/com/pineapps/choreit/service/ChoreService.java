package com.pineapps.choreit.service;

import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.repository.ChoreRepository;

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
}
