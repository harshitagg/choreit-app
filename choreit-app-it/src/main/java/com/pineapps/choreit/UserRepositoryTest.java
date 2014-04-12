package com.pineapps.choreit;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.pineapps.choreit.domain.User;
import com.pineapps.choreit.repository.Repository;
import com.pineapps.choreit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class UserRepositoryTest extends AndroidTestCase {
    private UserRepository userRepository;

    @Override
    protected void setUp() throws Exception {
        userRepository = new UserRepository();
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), userRepository);
        userRepository.deleteAllUsers();
    }

    public void testShouldSaveUser() throws Exception {
        userRepository.insert(new User("name", "email-id", false));

        List<User> users = userRepository.getAll();
        assertEquals(asList(new User("name", "email-id", false)), users);
    }

    public void testShouldFetchAllUsers() throws Exception {
        User user1 = new User("name 1", "email-id 1", true);
        User user2 = new User("name 2", "email-id 2", false);
        User user3 = new User("name 3", "email-id 3", true);
        User user4 = new User("name 4", "email-id 4", false);
        userRepository.insert(user1);
        userRepository.insert(user2);
        userRepository.insert(user3);
        userRepository.insert(user4);

        List<User> allUsers = userRepository.getAll();

        assertEquals(asList(user1, user2, user3, user4), allUsers);
    }

    public void testShouldFindByEmailId() throws Exception {
        User user1 = new User("name 1", "email-id 1", true);
        User user2 = new User("name 2", "email-id 2", false);
        userRepository.insert(user1);
        userRepository.insert(user2);

        User user = userRepository.findByEmailId("email-id 1");

        assertEquals(user1, user);
    }

    public void testShouldFetchAllUnSyncedUsers() throws Exception {
        User user1 = new User("name 1", "email-id 1", true);
        User user2 = new User("name 2", "email-id 2", false);
        User user3 = new User("name 3", "email-id 3", true);
        User user4 = new User("name 4", "email-id 4", false);
        userRepository.insert(user1);
        userRepository.insert(user2);
        userRepository.insert(user3);
        userRepository.insert(user4);

        List<User> pendingUsers = userRepository.getPendingUsers();

        assertEquals(asList(user2, user4), pendingUsers);
    }

    public void testShouldMarkUsersAsSynced() throws Exception {
        User user1 = new User("name 1", "email-id 1", true);
        User user2 = new User("name 2", "email-id 2", false);
        User user3 = new User("name 3", "email-id 3", true);
        User user4 = new User("name 4", "email-id 4", false);
        ArrayList<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        userRepository.insert(user1);
        userRepository.insert(user2);
        userRepository.insert(user3);
        userRepository.insert(user4);

        userRepository.markUsersAsSynced(users);

        user1.markAsSynced();
        user2.markAsSynced();
        user3.markAsSynced();
        user4.markAsSynced();
        assertEquals(asList(user1, user2, user3, user4), userRepository.getAll());
    }
}
