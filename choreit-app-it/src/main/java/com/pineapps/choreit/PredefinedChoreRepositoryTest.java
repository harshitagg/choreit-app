package com.pineapps.choreit;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.pineapps.choreit.domain.PredefinedChore;
import com.pineapps.choreit.repository.PredefinedChoreRepository;
import com.pineapps.choreit.repository.Repository;
import com.pineapps.choreit.util.Session;

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

public class PredefinedChoreRepositoryTest extends AndroidTestCase {
    private PredefinedChoreRepository repository;

    @Override
    protected void setUp() throws Exception {
        repository = new PredefinedChoreRepository();
        Session session = new Session().setPassword("password").setRepositoryName("choreit.db" + new Date().getTime());
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), session, repository);
        repository.deleteAllChores();
    }

    public void testShouldSavePredefinedChore() throws Exception {
        repository.insert(new PredefinedChore("id", "test", "description"));

        List<PredefinedChore> predefinedChores = repository.getAll();
        assertEquals(asList(new PredefinedChore("id", "test", "description")), predefinedChores);
    }

    public void testShouldFetchAllPredefinedChores() throws Exception {
        PredefinedChore predefinedChore1 = new PredefinedChore("id 1", "title 1", "description 1");
        PredefinedChore predefinedChore2 = new PredefinedChore("id 2", "title 2", "description 2");
        PredefinedChore predefinedChore3 = new PredefinedChore("id 3", "title 3", "description 3");
        PredefinedChore predefinedChore4 = new PredefinedChore("id 4", "title 4", "description 4");
        repository.insert(predefinedChore1);
        repository.insert(predefinedChore2);
        repository.insert(predefinedChore3);
        repository.insert(predefinedChore4);

        List<PredefinedChore> predefinedChores = repository.getAll();

        assertEquals(asList(predefinedChore1, predefinedChore2, predefinedChore3, predefinedChore4), predefinedChores);
    }
}
