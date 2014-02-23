package com.pineapps.choreit.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.pineapps.choreit.repository.PredefinedChoreRepository.PREDEFINED_CHORE_TABLE_NAME;

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
            if (choreItRepository.getClass() == PredefinedChoreRepository.class) {
                createPredefinedChores(sqLiteDatabase);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    private void createPredefinedChores(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + PREDEFINED_CHORE_TABLE_NAME + " values ('1','Vacuuming','Clean carpet')");
        db.execSQL("INSERT INTO " + PREDEFINED_CHORE_TABLE_NAME + " values ('2','Dish washing','Wash all utensils')");
        db.execSQL("INSERT INTO " + PREDEFINED_CHORE_TABLE_NAME + " values ('3','Laundry','Put clothes for washing')");
    }
}
