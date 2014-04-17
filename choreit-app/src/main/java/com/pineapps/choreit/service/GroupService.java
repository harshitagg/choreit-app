package com.pineapps.choreit.service;

import com.pineapps.choreit.domain.Group;
import com.pineapps.choreit.domain.User;
import com.pineapps.choreit.repository.GroupRepository;

import java.util.List;

public class GroupService {
    private GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void addGroup(String name, List<User> users) {
        groupRepository.insert(new Group(name, users));
    }

    public List<Group> getAllGroups() {
        return groupRepository.getAll();
    }

    public Group getGroupById(String id) {
        return groupRepository.findByGroupId(id);
    }
}
