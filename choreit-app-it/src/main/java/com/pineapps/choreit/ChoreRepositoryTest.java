package com.pineapps.choreit;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.repository.ChoreRepository;
import com.pineapps.choreit.repository.Repository;

import java.util.List;

import static java.util.Arrays.asList;

public class ChoreRepositoryTest extends AndroidTestCase {
    private ChoreRepository choreRepository;

    @Override
    protected void setUp() throws Exception {
        choreRepository = new ChoreRepository();
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), choreRepository);
        choreRepository.deleteAllChores();
    }

    public void testShouldSaveChore() throws Exception {
        choreRepository.insert(new Chore("id", "test", "description", "2014-01-01"));

        List<Chore> chores = choreRepository.getAll();
        assertEquals(asList(new Chore("id", "test", "description", "2014-01-01")), chores);
    }

    public void testShouldFetchAllChores() throws Exception {
        Chore chore1 = new Chore("id 1", "title 1", "description 1", "2014-01-01");
        Chore chore2 = new Chore("id 2", "title 2", "description 2", "2014-01-01");
        Chore chore3 = new Chore("id 3", "title 3", "description 3", "2014-01-01");
        Chore chore4 = new Chore("id 4", "title 4", "description 4", "2014-01-01");
        choreRepository.insert(chore1);
        choreRepository.insert(chore2);
        choreRepository.insert(chore3);
        choreRepository.insert(chore4);

        List<Chore> allChores = choreRepository.getAll();

        assertEquals(asList(chore1, chore2, chore3, chore4), allChores);
    }
}
