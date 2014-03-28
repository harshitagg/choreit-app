package com.pineapps.choreit;

import android.content.Context;
import com.pineapps.choreit.domain.PredefinedChore;
import com.pineapps.choreit.repository.ChoreRepository;
import com.pineapps.choreit.repository.PredefinedChoreRepository;
import com.pineapps.choreit.repository.Repository;
import com.pineapps.choreit.service.ChoreService;
import com.pineapps.choreit.service.ChoreSyncService;
import com.pineapps.choreit.service.HTTPAgent;
import com.pineapps.choreit.service.PredefinedChoreService;
import com.pineapps.choreit.view.ChoreIconMap;

public class ChoreItContext {
    private static ChoreItContext choreItContext;

    private Context context;

    private Repository repository;
    private ChoreRepository choreRepository;
    private PredefinedChoreRepository predefinedChoreRepository;

    private ChoreService choreService;
    private PredefinedChoreService predefinedChoreService;
    private ChoreSyncService choreSyncService;

    private ChoreIconMap choreIconMap;
    private HTTPAgent httpAgent;

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

    public ChoreIconMap choreIconMap() {
        if (choreIconMap == null) {
            choreIconMap = new ChoreIconMap();
        }

        return choreIconMap;
    }

    public Repository initRepository() {
        if (repository == null) {
            repository = new Repository(context, choreRepository(), predefinedChoreRepository());
        }

        return repository;
    }

    public ChoreService choreService() {
        initRepository();
        if (choreService == null) {
            choreService = new ChoreService(choreRepository());
        }

        return choreService;
    }

    public PredefinedChoreService predefinedChoreService() {
        initRepository();
        if (predefinedChoreService == null) {
            predefinedChoreService = new PredefinedChoreService(predefinedChoreRepository());
        }

        return predefinedChoreService;
    }

    public ChoreSyncService choreSyncService() {
        initRepository();
        if (choreSyncService == null) {
            choreSyncService = new ChoreSyncService(choreRepository(), httpAgent());
        }
        return choreSyncService;
    }

    private ChoreRepository choreRepository() {
        if (choreRepository == null) {
            choreRepository = new ChoreRepository();
        }

        return choreRepository;
    }

    private PredefinedChoreRepository predefinedChoreRepository() {
        if (predefinedChoreRepository == null) {
            predefinedChoreRepository = new PredefinedChoreRepository();
        }

        return predefinedChoreRepository;
    }

    private HTTPAgent httpAgent() {
        initRepository();
        if (httpAgent == null) {
            httpAgent = new HTTPAgent();
        }
        return httpAgent;
    }

}
