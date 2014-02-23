package com.pineapps.choreit.service;

import com.pineapps.choreit.domain.PredefinedChore;
import com.pineapps.choreit.repository.PredefinedChoreRepository;

import java.util.List;

public class PredefinedChoreService {
    private PredefinedChoreRepository predefinedChoreRepository;

    public PredefinedChoreService(PredefinedChoreRepository predefinedChoreRepository) {
        this.predefinedChoreRepository = predefinedChoreRepository;
    }

    public void addChore(String name, String description) {
        predefinedChoreRepository.insert(new PredefinedChore(name, description));
    }

    public List<PredefinedChore> getAllPredifinedChores() {
        return predefinedChoreRepository.getAll();
    }
}
