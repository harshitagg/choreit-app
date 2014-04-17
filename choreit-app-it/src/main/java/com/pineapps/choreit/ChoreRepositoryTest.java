package com.pineapps.choreit;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.repository.ChoreRepository;
import com.pineapps.choreit.repository.Repository;
import com.pineapps.choreit.util.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

public class ChoreRepositoryTest extends AndroidTestCase {
    private ChoreRepository choreRepository;

    @Override
    protected void setUp() throws Exception {
        choreRepository = new ChoreRepository();
        Session session = new Session().setPassword("password").setRepositoryName("choreit.db" + new Date().getTime());
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), session, choreRepository);
        choreRepository.deleteAllChores();
    }

    public void testShouldSaveChore() throws Exception {
        choreRepository.insert(new Chore("id", "test", "description", "2014-01-01", "groupId", false, false));

        List<Chore> chores = choreRepository.getAll();
        assertEquals(asList(new Chore("id", "test", "description", "2014-01-01", "groupId", false, false)), chores);
    }

    public void testShouldFetchAllChores() throws Exception {
        Chore chore1 = new Chore("id 1", "title 1", "description 1", "2014-01-01", "groupId 1", false, true);
        Chore chore2 = new Chore("id 2", "title 2", "description 2", "2014-01-01", "groupId 2", false, false);
        Chore chore3 = new Chore("id 3", "title 3", "description 3", "2014-01-01", "groupId 3", true, true);
        Chore chore4 = new Chore("id 4", "title 4", "description 4", "2014-01-01", "groupId 4", false, false);
        choreRepository.insert(chore1);
        choreRepository.insert(chore2);
        choreRepository.insert(chore3);
        choreRepository.insert(chore4);

        List<Chore> allChores = choreRepository.getAll();

        assertEquals(asList(chore1, chore2, chore3, chore4), allChores);
    }

    public void testShouldFindByChoreId() throws Exception {
        Chore chore1 = new Chore("id 1", "title 1", "description 1", "2014-01-01", "groupId 1", false, true);
        Chore chore2 = new Chore("id 2", "title 2", "description 2", "2014-01-01", "groupId 2", false, false);
        choreRepository.insert(chore1);
        choreRepository.insert(chore2);

        Chore chore = choreRepository.findByChoreId("id 1");

        assertEquals(chore1, chore);
    }

    public void testShouldUpdateChore() throws Exception {
        Chore chore = new Chore("id 1", "title 1", "description 1", "2014-01-01", "groupId 1", false, false);
        choreRepository.insert(chore);
        chore.markAsDone();

        choreRepository.update(chore);

        Chore updatedChore = choreRepository.findByChoreId("id 1");
        assertEquals(chore, updatedChore);
    }

    public void testShouldFetchAllUndoneChores() throws Exception {
        Chore chore1 = new Chore("id 1", "title 1", "description 1", "2014-01-01", "groupId 1", false, true);
        Chore chore2 = new Chore("id 2", "title 2", "description 2", "2014-01-01", "groupId 2", true, false);
        Chore chore3 = new Chore("id 3", "title 3", "description 3", "2014-01-01", "groupId 3", true, true);
        Chore chore4 = new Chore("id 4", "title 4", "description 4", "2014-01-01", "groupId 4", false, false);
        choreRepository.insert(chore1);
        choreRepository.insert(chore2);
        choreRepository.insert(chore3);
        choreRepository.insert(chore4);

        List<Chore> allUndoneChores = choreRepository.getAllUndoneChores();

        assertEquals(asList(chore1, chore4), allUndoneChores);
    }

    public void testShouldFetchAllUnSyncedChores() throws Exception {
        Chore chore1 = new Chore("id 1", "title 1", "description 1", "2014-01-01", "groupId 1", false, true);
        Chore chore2 = new Chore("id 2", "title 2", "description 2", "2014-01-01", "groupId 2", true, false);
        Chore chore3 = new Chore("id 3", "title 3", "description 3", "2014-01-01", "groupId 3", true, true);
        Chore chore4 = new Chore("id 4", "title 4", "description 4", "2014-01-01", "groupId 4", false, false);
        choreRepository.insert(chore1);
        choreRepository.insert(chore2);
        choreRepository.insert(chore3);
        choreRepository.insert(chore4);

        List<Chore> pendingChores = choreRepository.getPendingChores();

        assertEquals(asList(chore2, chore4), pendingChores);
    }

    public void testShouldMarkChoresAsSynced() throws Exception {
        Chore chore1 = new Chore("id 1", "title 1", "description 1", "2014-01-01", "groupId 1", false, false);
        Chore chore2 = new Chore("id 2", "title 2", "description 2", "2014-01-01", "groupId 2", true, false);
        Chore chore3 = new Chore("id 3", "title 3", "description 3", "2014-01-01", "groupId 3", true, false);
        Chore chore4 = new Chore("id 4", "title 4", "description 4", "2014-01-01", "groupId 4", false, false);
        List<Chore> chores = new ArrayList<Chore>();
        chores.add(chore1);
        chores.add(chore2);
        chores.add(chore3);
        chores.add(chore4);
        choreRepository.insert(chore1);
        choreRepository.insert(chore2);
        choreRepository.insert(chore3);
        choreRepository.insert(chore4);

        choreRepository.markChoresAsSynced(chores);

        chore1.markAsSynced();
        chore2.markAsSynced();
        chore3.markAsSynced();
        chore4.markAsSynced();
        assertEquals(asList(chore1, chore2, chore3, chore4), choreRepository.getAll());
    }
}
