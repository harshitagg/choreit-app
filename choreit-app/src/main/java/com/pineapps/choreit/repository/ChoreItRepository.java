package com.pineapps.choreit.repository;

import android.database.sqlite.SQLiteDatabase;

public abstract class ChoreItRepository {
    Repository masterRepository;

    public void updateMasterRepository(Repository repository) {
        this.masterRepository = repository;
    }

    abstract protected void onCreate(SQLiteDatabase database);
}
