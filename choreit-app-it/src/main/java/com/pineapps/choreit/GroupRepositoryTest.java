package com.pineapps.choreit;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.pineapps.choreit.domain.Group;
import com.pineapps.choreit.domain.User;
import com.pineapps.choreit.repository.GroupRepository;
import com.pineapps.choreit.repository.Repository;
import com.pineapps.choreit.util.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

public class GroupRepositoryTest extends AndroidTestCase {
    private GroupRepository groupRepository;

    @Override
    protected void setUp() throws Exception {
        groupRepository = new GroupRepository();
        Session session = new Session().setPassword("password").setRepositoryName("choreit.db" + new Date().getTime());
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), session, groupRepository);
        groupRepository.deleteAllGroups();
    }

    public void testShouldSaveGroup() throws Exception {
        User user1 = new User("name 1", "email-id 1", true);
        User user2 = new User("name 2", "email-id 2", false);
        Group group = new Group("group id", "group name", asList(user1, user2), false);
        groupRepository.insert(group);

        List<Group> groups = groupRepository.getAll();
        assertEquals(asList(group), groups);
    }

    public void testShouldFetchAllGroups() throws Exception {
        User user1 = new User("name 1", "email-id 1", true);
        User user2 = new User("name 2", "email-id 2", false);
        User user3 = new User("name 3", "email-id 3", true);
        User user4 = new User("name 4", "email-id 4", false);
        Group group1 = new Group("group id 1", "group name 1", asList(user1, user2), true);
        Group group2 = new Group("group id 2", "group name 2", asList(user1, user2, user3), false);
        Group group3 = new Group("group id 3", "group name 3", asList(user2, user3, user4), true);
        Group group4 = new Group("group id 4", "group name 4", asList(user4), false);
        groupRepository.insert(group1);
        groupRepository.insert(group2);
        groupRepository.insert(group3);
        groupRepository.insert(group4);

        List<Group> groups = groupRepository.getAll();

        assertEquals(asList(group1, group2, group3, group4), groups);
    }

    public void testShouldFindById() throws Exception {
        User user1 = new User("name 1", "email-id 1", true);
        User user2 = new User("name 2", "email-id 2", false);
        Group group1 = new Group("group id 1", "group name 1", asList(user1, user2), false);
        Group group2 = new Group("group id 2", "group name 2", asList(user1, user2), true);
        groupRepository.insert(group1);
        groupRepository.insert(group2);

        Group group = groupRepository.findByGroupId("group id 1");

        assertEquals(group1, group);
    }

    public void testShouldFetchAllUnSyncedUsers() throws Exception {
        User user1 = new User("name 1", "email-id 1", true);
        User user2 = new User("name 2", "email-id 2", false);
        User user3 = new User("name 3", "email-id 3", true);
        User user4 = new User("name 4", "email-id 4", false);
        Group group1 = new Group("group id 1", "group name 1", asList(user1, user2), true);
        Group group2 = new Group("group id 2", "group name 2", asList(user1, user2, user3), false);
        Group group3 = new Group("group id 3", "group name 3", asList(user2, user3, user4), true);
        Group group4 = new Group("group id 4", "group name 4", asList(user4), false);
        groupRepository.insert(group1);
        groupRepository.insert(group2);
        groupRepository.insert(group3);
        groupRepository.insert(group4);

        List<Group> pendingGroups = groupRepository.getPendingGroups();

        assertEquals(asList(group2, group4), pendingGroups);
    }

    public void testShouldMarkUsersAsSynced() throws Exception {
        User user1 = new User("name 1", "email-id 1", true);
        User user2 = new User("name 2", "email-id 2", false);
        User user3 = new User("name 3", "email-id 3", true);
        User user4 = new User("name 4", "email-id 4", false);
        Group group1 = new Group("group id 1", "group name 1", asList(user1, user2), false);
        Group group2 = new Group("group id 2", "group name 2", asList(user1, user2, user3), false);
        Group group3 = new Group("group id 3", "group name 3", asList(user2, user3, user4), false);
        Group group4 = new Group("group id 4", "group name 4", asList(user4), false);
        List<Group> groups = new ArrayList<Group>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        groups.add(group4);
        groupRepository.insert(group1);
        groupRepository.insert(group2);
        groupRepository.insert(group3);
        groupRepository.insert(group4);

        groupRepository.markGroupsAsSynced(groups);

        group1.markAsSynced();
        group2.markAsSynced();
        group3.markAsSynced();
        group4.markAsSynced();
        assertEquals(asList(group1, group2, group3, group4), groupRepository.getAll());
    }
}
