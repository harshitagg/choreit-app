package com.pineapps.choreit;

import android.content.Context;
import com.pineapps.choreit.repository.ChoreRepository;
import com.pineapps.choreit.repository.Repository;
import com.pineapps.choreit.service.ChoreService;

public class ChoreItContext {
    private static ChoreItContext choreItContext;

    private Context context;

    private Repository repository;
    private ChoreRepository choreRepository;

    private ChoreService choreService;

    public static ChoreItContext getInstance() {
        if (choreItContext == null) {
            choreItContext = new ChoreItContext();
        }

        return choreItContext;
    }

    public ChoreItContext updateApplicationContext(Context context) {
        this.context = context;
        return this;
    }

    public Repository initRepository() {
        if (repository == null) {
            repository = new Repository(context, choreRepository());
        }

        return repository;
    }

    private ChoreService choreService() {
        initRepository();
        if (choreService == null) {
            choreService = new ChoreService(choreRepository());
        }

        return choreService;
    }

    private ChoreRepository choreRepository() {
        if (choreRepository == null) {
            choreRepository = new ChoreRepository();
        }

        return choreRepository;
    }
}
