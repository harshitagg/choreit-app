package com.pineapps.choreit.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Repository extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChoreIt.db";
    private final ChoreItRepository[] choreItRepositories;

    public Repository(Context context, ChoreItRepository... choreItRepositories) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.choreItRepositories = choreItRepositories;

        for (ChoreItRepository choreItRepository : choreItRepositories) {
            choreItRepository.updateMasterRepository(this);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (ChoreItRepository choreItRepository : choreItRepositories) {
            choreItRepository.onCreate(sqLiteDatabase);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
