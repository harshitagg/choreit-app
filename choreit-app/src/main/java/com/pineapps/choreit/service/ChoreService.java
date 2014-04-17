package com.pineapps.choreit.service;

import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.repository.ChoreRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChoreService {
    private ChoreRepository choreRepository;

    public ChoreService(ChoreRepository choreRepository) {
        this.choreRepository = choreRepository;
    }

    public void addChore(String name, String description, String dueDate, String groupId) {
        choreRepository.insert(new Chore(name, description, dueDate, groupId));
    }

    public List<Chore> getAllChores() {
        return choreRepository.getAll();
    }

    public List<Chore> getAllUndoneChoresSortedByDueDate() {
        List<Chore> choreList = choreRepository.getAllUndoneChores();
        Collections.sort(choreList, new Comparator<Chore>() {
            @Override
            public int compare(Chore lhs, Chore rhs) {
                if (lhs.dueDateAsLocalDate().isAfter(rhs.dueDateAsLocalDate())) {
                    return 1;
                }
                if (lhs.dueDateAsLocalDate().isBefore(rhs.dueDateAsLocalDate())) {
                    return -1;
                }
                return 0;
            }
        });

        return choreList;
    }

    public Chore getChoreById(String choreId) {
        return choreRepository.findByChoreId(choreId);
    }

    public void markChoreAsDone(Chore chore) {
        chore.markAsDone();
        choreRepository.update(chore);
    }
}
